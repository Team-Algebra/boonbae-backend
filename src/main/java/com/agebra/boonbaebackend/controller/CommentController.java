package com.agebra.boonbaebackend.controller;
import com.agebra.boonbaebackend.domain.Users;
import com.agebra.boonbaebackend.dto.CommentDTO;
import com.agebra.boonbaebackend.service.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "CommentController", description = "분리배출 정보 게시글 댓글 서비스 관련 Controller 입니다.")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/")
public class CommentController {
    private final CommentService commentService;

    // 해당 리사이클 정보에 대한 모든 댓글 가져오기 (Any)
    @GetMapping("/recycling/{recycling_pk}/comments")
    public ResponseEntity<CommentDTO.CommentListResponse> getAllCommentsByRecyclingPk(@AuthenticationPrincipal Users user, @PathVariable("recycling_pk") Long recyclingPk) {
        return ResponseEntity.ok(commentService.getAllCommentsByRecyclingPk(user, recyclingPk));
    }

    // 댓글 작성 (Only User)
    @PostMapping("/recycling/{recycling_pk}/comments")
    public ResponseEntity<CommentDTO.CommentListResponse> createComment(@AuthenticationPrincipal Users user,
                                                       @PathVariable("recycling_pk") Long recyclingPk,
                                                       @RequestBody CommentDTO.CommentRequest commentRequest) {
        commentService.createComment(user, recyclingPk, commentRequest);
        return ResponseEntity.ok(commentService.getAllCommentsByRecyclingPk(user, recyclingPk));
    }

    // 댓글 삭제 (Only User)
    @DeleteMapping("/comments/{comment_pk}")
    public ResponseEntity deleteComment(@AuthenticationPrincipal Users user, @PathVariable("comment_pk") Long commentPk) {
        commentService.deleteComment(user, commentPk);
        return ResponseEntity.ok().build();
    }

    // 댓글에 좋아요 추가 (Only User)
    @PostMapping("/comments/{comment_pk}/likes")
    public ResponseEntity addLikeToComment(@AuthenticationPrincipal Users user, @PathVariable("comment_pk") Long commentPk) {
        commentService.addLikeToComment(user, commentPk);
        return ResponseEntity.ok().build();
    }

    // 댓글의 좋아요 제거 (Only User)
    @DeleteMapping("/comments/{comment_pk}/likes")
    public ResponseEntity removeLikeFromComment(@AuthenticationPrincipal Users user, @PathVariable("comment_pk") Long commentPk) {
        commentService.removeLikeFromComment(user, commentPk);
        return ResponseEntity.ok().build();
    }

    // 댓글에 신고 추가 (Any)
    @PostMapping("/comments/{comment_pk}/reports")
    public ResponseEntity addReportToComment(@PathVariable("comment_pk") Long commentPk) {
        commentService.addReportToComment(commentPk);
        return ResponseEntity.ok().build();
    }
}
