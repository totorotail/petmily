package com.concord.petmily.pet.service;

import com.concord.petmily.pet.dto.PetDto;
import com.concord.petmily.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *  반려동물 비즈니스 로직
 *
 */
@Service
@RequiredArgsConstructor
public class PetService {

  private final PetRepository petRepository;

  /**
   * 반려동물 등록
   */
  public void createPet(Long userId,PetDto.Create request){

  }

  /**
   * 반려동물 조회
   */
  public void userPetList(){

  }

  /**
   * 반려동물 정보 수정
   */
  public void modifierPet(){

  }

  /**
   * 반려동물 삭제 처리
   */

  public void deletePet(){

  }

}
