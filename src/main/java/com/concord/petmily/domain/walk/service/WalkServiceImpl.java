package com.concord.petmily.domain.walk.service;

import com.concord.petmily.common.exception.ErrorCode;
import com.concord.petmily.domain.pet.entity.Pet;
import com.concord.petmily.domain.pet.exception.PetException;
import com.concord.petmily.domain.pet.repository.PetRepository;
import com.concord.petmily.domain.user.entity.User;
import com.concord.petmily.domain.user.exception.UserNotFoundException;
import com.concord.petmily.domain.user.repository.UserRepository;
import com.concord.petmily.domain.walk.dto.*;
import com.concord.petmily.domain.walk.entity.*;
import com.concord.petmily.domain.walk.exception.WalkAccessDeniedException;
import com.concord.petmily.domain.walk.exception.WalkException;
import com.concord.petmily.domain.walk.exception.WalkNotFoundException;
import com.concord.petmily.domain.walk.repository.WalkActivityRepository;
import com.concord.petmily.domain.walk.repository.WalkParticipantRepository;
import com.concord.petmily.domain.walk.repository.WalkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 산책 관련 서비스
 * <p>
 * - 산책 위치 기록
 * - 산책 전체 정보 조회
 * - 산책 상세 정보 조회
 * - 산책 목표 설정
 * - 산책 목표 조회
 * - 산책 목표 선택
 */
@Service
@RequiredArgsConstructor
public class WalkServiceImpl implements WalkService {

    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final WalkRepository walkRepository;
    private final WalkParticipantRepository walkParticipantRepository;
    private final WalkActivityRepository walkActivityRepository;

    // 회원 정보 조회
    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    // 회원 산책 정보 조회
    private Walk getWalk(Long walkId) {
        return walkRepository.findById(walkId)
                .orElseThrow(() -> new WalkNotFoundException(ErrorCode.WALK_NOT_FOUND));
    }

    /**
     * 산책 시작 및 산책 정보 기록
     */
    @Override
    @Transactional
    public WalkDto startWalk(String username, List<Long> petIds, LocalDateTime startTime) {
        User user = getUser(username);

        // 회원 산책 중 여부 확인
        if (user.isWalking) {
            throw new WalkException(ErrorCode.WALK_ALREADY_IN_PROGRESS);
        }

        // TODO 리팩토링 : 깔끔하게 정리
        Walk walk = new Walk();
        walk.setUser(user);
        walk.setStartTime(startTime);
        walk.setWalkStatus(WalkStatus.IN_PROGRESS);
        walkRepository.save(walk);

        List<WalkParticipant> walkParticipants = new ArrayList<>();

        for (int i = 0; i < petIds.size(); i++) {
            Pet pet = petRepository.findById(petIds.get(i))
                    .orElseThrow(() -> new PetException(ErrorCode.PET_NOT_FOUND));

            // 주인 확인
            if (pet.getUser().getId() != user.getId()) {
                throw new PetException(ErrorCode.PET_OWNER_MISMATCH);
            }

            // TODO null 금지
            WalkParticipant walkParticipant = new WalkParticipant();
            walkParticipant.setWalk(walk);
            walkParticipant.setPet(pet);

            WalkParticipantId walkParticipantId = new WalkParticipantId();
            walkParticipantId.setWalkId(walk.getId());
            walkParticipantId.setPetId(pet.getId());
            walkParticipant.setId(walkParticipantId);

            walkParticipants.add(walkParticipant);
        }
        walkParticipantRepository.saveAll(walkParticipants);

        updateWalkingStatus(user, true);

        // Walk Entity를 WalkDto로 변환
        return WalkDto.fromEntity(walk);
    }

    /**
     * 산책 종료 및 산책 정보 기록
     */
    @Override
    @Transactional
    public WalkDto endWalk(Long walkId, String username, WalkDto walkDto) {

        Walk walk = getWalk(walkId);

        User user = getUser(username);

        // 산책 소유자 확인
        if (!walk.getUser().getUsername().equals(username)) {
            throw new WalkAccessDeniedException(ErrorCode.WALK_ACCESS_DENIED);
        }

        // 종료된 산책 여부 확인
        if (walk.getWalkStatus() == WalkStatus.TERMINATED) {
            throw new WalkException(ErrorCode.WALK_ALREADY_TERMINATED);
        }

        // 총 시간 계산
        long calculatedDuration = calculateDuration(walk.getStartTime(), walkDto.getEndTime());

        walk.setEndTime(walkDto.getEndTime());
        walk.setDistance(walkDto.getDistance());
        walk.setDuration(calculatedDuration);
        walk.setWalkStatus(WalkStatus.TERMINATED);
        walkRepository.save(walk);

        updateWalkingStatus(user, false);

        // Walk Entity를 WalkDto로 변환
        return WalkDto.fromEntity(walk);
    }

    // 산책 상태 갱신 메서드
    private void updateWalkingStatus(User user, boolean isWalking) {
        user.setIsWalking(isWalking);
        userRepository.save(user);
    }

    // 산책 시간 계산 메서드
    private long calculateDuration(LocalDateTime startTime, LocalDateTime endTime) {
        return Duration.between(startTime, endTime).toMinutes();
    }

    /**
     * 산책 활동 기록
     */
    @Override
    public WalkActivityDto logWalkActivity(Long walkId, String username, WalkActivityDto walkActivityDto) {

        Walk walk = getWalk(walkId);

        User user = getUser(username);

        WalkParticipant walkParticipant = walkParticipantRepository.findByWalkIdAndPetId(walkId, walkActivityDto.getPetId())
                .orElseThrow(() -> new WalkNotFoundException(ErrorCode.PET_NOT_IN_THIS_WALK));

        Pet pet = walkParticipant.getPet();

        // 산책 소유자 확인
        if (!walk.getUser().getUsername().equals(username)) {
            throw new WalkAccessDeniedException(ErrorCode.WALK_ACCESS_DENIED);
        }
        // 종료된 산책 여부 확인
        if (walk.getWalkStatus() == WalkStatus.TERMINATED) {
            throw new WalkException(ErrorCode.WALK_ALREADY_TERMINATED);
        }
        // 회원의 반려동물인지 확인
        validatePetOwnership(user, pet);

        WalkActivity walkActivity = new WalkActivity();
        walkActivity.setWalkParticipant(walkParticipant);
        walkActivity.setActivityType(walkActivityDto.getActivity());
        walkActivity.setLatitude(walkActivityDto.getLatitude());
        walkActivity.setLongitude(walkActivityDto.getLongitude());
        walkActivity.setTimestamp(walkActivityDto.getTimestamp());
        walkActivityRepository.save(walkActivity);

        return walkActivityDto;
    }

    // 회원의 반려동물인지 유효성 검사
    private void validatePetOwnership(User user, Pet pet) {
        if (pet.getUser().getId() != user.getId()) {
            throw new PetException(ErrorCode.PET_OWNER_MISMATCH);
        }
    }

    /**
     * 특정 산책 기록 상세 조회
     */
    @Override
    public WalkDetailDto getWalkDetail(Long walkId) {
        Walk walk = walkRepository.findById(walkId)
                .orElseThrow(() -> new WalkNotFoundException(ErrorCode.WALK_NOT_FOUND));

        // 산책에 참여한 반려동물 아이디 리스트
        List<Long> petIds = walkParticipantRepository.findByWalkId(walk.getId())
                .stream()
                .map(walkingPet -> walkingPet.getPet().getId())
                .collect(Collectors.toList());

        // 해당 산책에 활동 리스트
        List<WalkActivity> activities = walkActivityRepository.findByWalkId(walk.getId());
        List<WalkActivityDto> activityDtos = activities.stream()
                .map(WalkActivityDto::fromEntity)
                .collect(Collectors.toList());

        return WalkDetailDto.fromEntity(walk, petIds, activityDtos);
    }

    /**
     * 회원의 모든 반려동물의 특정 기간 산책 기록 조회
     */
    @Override
    public Page<WalkWithPetsDto> getUserPetsWalks(String username, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        User user = getUser(username);
        Page<Walk> walkPage;
        if (startDate != null && endDate != null) {
            walkPage = walkRepository.findByUserIdAndWalkDateBetween(user.getId(), startDate, endDate, pageable);
        } else {
            walkPage = walkRepository.findByUserId(user.getId(), pageable);
        }

        return walkPage.map(this::convertToWalkWithPetsDto);
    }

    private WalkStatisticsDto convertToWalkStatisticsDto(Pet pet) {
        List<WalkParticipant> walkParticipants = walkParticipantRepository.findByIdPetId(pet.getId());
        List<Walk> petWalks = walkParticipants.stream()
                .map(WalkParticipant::getWalk)
                .collect(Collectors.toList());

        double totalDistance = petWalks.stream().mapToDouble(Walk::getDistance).sum();
        long totalDuration = petWalks.stream().mapToLong(Walk::getDuration).sum();
        LocalDate lastWalkDate = petWalks.stream()
                .map(Walk::getWalkDate)
                .max(LocalDate::compareTo)
                .orElse(null);

        return WalkStatisticsDto.builder()
                .petId(pet.getId())
                .totalWalks(petWalks.size())
                .totalDistanceKm(totalDistance)
                .totalDurationMinutes(totalDuration)
                .build();
    }

    // Walk 엔티티를 WalkWithPetsDto로 변환
    private WalkWithPetsDto convertToWalkWithPetsDto(Walk walk) {
        List<Long> petIds = walkParticipantRepository.findByWalkId(walk.getId())
                .stream()
                .map(walkingPet -> walkingPet.getPet().getId())
                .collect(Collectors.toList());

        return WalkWithPetsDto.fromEntity(walk, petIds);
    }

    /**
     * 회원의 모든 반려동물별 전체 산책 통계 조회
     */
    @Override
    public Page<WalkStatisticsDto> getUserPetsWalkStatistics(String username, Pageable pageable) {
        User user = getUser(username);

        Page<Pet> petsPage = petRepository.findByUserId(user.getId(), pageable);

        return petsPage.map(pet -> {
            List<WalkParticipant> walkParticipants = walkParticipantRepository.findByIdPetId(pet.getId());
            List<Walk> petWalks = walkParticipants.stream()
                    .map(WalkParticipant::getWalk)
                    .collect(Collectors.toList());
            return createWalkStatisticsDto(pet.getId(), petWalks);
        });
    }

    // 산책 통계 메서드
    private WalkStatisticsDto createWalkStatisticsDto(Long petId, List<Walk> walks) {
        int totalWalks = walks.size();
        double totalDistance = walks.stream().mapToDouble(Walk::getDistance).sum();
        long totalDuration = walks.stream()
                .mapToLong(walk -> Duration.between(walk.getStartTime(), walk.getEndTime()).toMinutes())
                .sum();

        double avgDistance = totalWalks > 0 ? totalDistance / totalWalks : 0;
        long avgDuration = totalWalks > 0 ? totalDuration / totalWalks : 0;

        return WalkStatisticsDto.builder()
                .petId(petId)
                .totalWalks(totalWalks)
                .totalDistanceKm(totalDistance)
                .totalDurationMinutes(totalDuration)
                .averageDistanceKm(avgDistance)
                .averageDurationMinutes(avgDuration)
                .build();
    }


    /**
     * 모든 반려동물에 대한 종합적인 산책 통계 조회
     */
//    @Override
//    @Transactional(readOnly = true)
//    public WalkStatisticsDto getUserPetsOverallWalkStatistics(String username) {
//        User user = getUser(username);
//        List<Pet> pets = petRepository.findByUser(user);
//
//        List<Walk> allWalks = walkRepository.findAllByUserPets(pets);
//
//        int totalWalks = allWalks.size();
//        double totalDistance = allWalks.stream().mapToDouble(Walk::getDistance).sum();
//        long totalDuration = allWalks.stream()
//                .mapToLong(walk -> Duration.between(walk.getStartTime(), walk.getEndTime()).toMinutes())
//                .sum();
//
//        return WalkStatisticsDto.builder()
//                .totalWalks(totalWalks)
//                .totalDistanceKm(totalDistance)
//                .totalDurationMinutes(totalDuration)
//                .averageDistanceKm(totalWalks > 0 ? totalDistance / totalWalks : 0)
//                .averageDurationMinutes(totalWalks > 0 ? (double) totalDuration / totalWalks : 0)
//                .build();
//    }

    /**
     * 반려동물의 전체 산책 기록 조회
     */
//    @Override
//    public List<WalkDto> getPetWalks(Long petId, LocalDate startDate, LocalDate endDate) {
//        List<Walk> walks = walkRepository.findByPetIdAndDateBetween(petId, startDate, endDate);
//
//        List<WalkSummaryDto> walkSummaries = walks.stream()
//                .map(this::convertToSummary)
//                .collect(Collectors.toList());
//
//        WalkStatisticsDto statistics = calculateStatistics(walks);

//        return null;
//    }

//    private WalkSummaryDto convertToSummary(Walk walk) {
//        // Walk 엔티티를 WalkSummaryDto로 변환하는 로직
//    }
//
//    private WalkStatisticsDto calculateStatistics(List<Walk> walks) {
//        // 주어진 산책 목록에 대한 통계를 계산하는 로직
//    }


    /**
     * 반려동물의 특정 산책 정보 조회
     */
//    @Override
//    public WalkDto getPetWalk(Pet pet) {
//
//        return null;
//    }

//    /**
//     * 회원의 전체 반려동물 산책 기록 일별 조회
//     */
//    @Override
//    public Map<LocalDate, List<WalkStatisticsDto>> getUserDailyWalks(Long userId) {
//        return null;
//    }

}
