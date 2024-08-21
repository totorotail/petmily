package com.concord.petmily.domain.comment.controller;

import com.concord.petmily.domain.comment.dto.CommentDto;
import com.concord.petmily.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * 댓글 관련 컨트롤러
 *
 * - 댓글 등록
 * - 댓글 수정
 * - 댓글 삭제
 */

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 새로운 댓글 등록
     * @param postId 댓글이 작성될 게시물
     * @param dto 등록할 댓글의 정보
     * @param userDetails 현재 인증된 사용자의 세부 정보
     * @return HTTP 201 상태를 반환
     */
    @PostMapping("/{postId}/comment")
    public ResponseEntity<String> createComment(@PathVariable Long postId,
                                                @RequestBody CommentDto dto,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        commentService.createComment(username, postId, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body("댓글 등록 성공");
    }

    /**
     * 댓글 수정
     * @param commentId 수정될 댓글의 id
     * @param dto 등록할 댓글의 정보
     * @param userDetails 현재 인증된 사용자의 세부 정보
     * @return HTTP 200 상태를 반환
     */
    @PutMapping("/comment/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable Long commentId,
                                                @RequestBody CommentDto dto,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        commentService.updateComment(username, commentId, dto);

        return ResponseEntity.status(HttpStatus.OK).body("댓글 수정 성공");
    }

    /**
     * 댓글 삭제
     * @param commentId 삭제될 댓글의 id
     * @param userDetails 현재 인증된 사용자의 세부 정보
     * @return HTTP 204 상태를 반환
     */
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        commentService.deleteComment(username, commentId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}