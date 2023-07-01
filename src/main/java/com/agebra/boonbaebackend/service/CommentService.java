package com.agebra.boonbaebackend.service;

import com.agebra.boonbaebackend.domain.Comment;
import com.agebra.boonbaebackend.domain.CommentLike;
import com.agebra.boonbaebackend.domain.RecyclingInfo;
import com.agebra.boonbaebackend.domain.Users;
import com.agebra.boonbaebackend.dto.CommentDTO;
import com.agebra.boonbaebackend.exception.ForbiddenException;
import com.agebra.boonbaebackend.exception.NotFoundException;
import com.agebra.boonbaebackend.repository.CommentLikeRepository;
import com.agebra.boonbaebackend.repository.CommentRepository;
import com.agebra.boonbaebackend.repository.RecyclingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class CommentService {

    private final RecyclingRepository recyclingRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Transactional(readOnly = true)
    public CommentDTO.CommentListResponse getAllCommentsByRecyclingPk(Users user, Long recyclingPk) {

        // 게시물 존재 여부 확인
        recyclingRepository.findById(recyclingPk)
                .orElseThrow(() -> new NotFoundException("RecyclingInfo not found with PK: " + recyclingPk));

        List<Comment> comments = commentRepository.findByInfo_Pk(recyclingPk);

        List<CommentDTO.CommentResponse> commentResponses = comments.stream().map(comment -> {
            int likeCnt = commentLikeRepository.countByComment(comment);
            boolean like = commentLikeRepository.existsByUserAndComment(user, comment);

            return CommentDTO.CommentResponse.builder()
                    .commentPk(comment.getPk())
                    .username(comment.getUser().getUsername())
                    .content(comment.getContent())
                    .createAt(comment.getCreateAt())
                    .likeCnt(likeCnt)
                    .like(like)
                    .build();
        }).toList();

        return new CommentDTO.CommentListResponse(commentResponses.size(), commentResponses);
    }


    @Transactional
    public void createComment(Users user, Long recyclingPk, CommentDTO.CommentRequest commentRequest) {
        RecyclingInfo recyclingInfo = recyclingRepository.findById(recyclingPk)
                .orElseThrow(() -> new NotFoundException("RecyclingInfo not found with PK: " + recyclingPk));

        commentRepository.save(Comment.builder().content(commentRequest.getContent()).user(user).info(recyclingInfo).build());
    }

    @Transactional
    public void deleteComment(Users user, Long commentPk) {
        Comment comment = commentRepository.findById(commentPk)
                .orElseThrow(() -> new NotFoundException("Comment not found with PK: " + commentPk));

        if (!comment.getUser().getPk().equals(user.getPk())) {
            throw new ForbiddenException("Only the owner can delete the comment");
        }

        commentRepository.delete(comment);
    }

    @Transactional
    public void addLikeToComment(Users user, Long commentPk) {
        Comment comment = commentRepository.findById(commentPk)
                .orElseThrow(() -> new NotFoundException("Comment not found with PK: " + commentPk));

        // 이미 좋아요를 눌렀는지 확인
        boolean hasLiked = commentLikeRepository.existsByUserAndComment(user, comment);

        if (!hasLiked) {
            commentLikeRepository.save(CommentLike.builder().user(user).comment(comment).build());
        }
    }


    @Transactional
    public void removeLikeFromComment(Users user, Long commentPk) {
        Comment comment = commentRepository.findById(commentPk)
                .orElseThrow(() -> new NotFoundException("Comment not found with PK: " + commentPk));

        CommentLike like = commentLikeRepository.findByUserAndComment(user, comment)
                .orElseThrow(() -> new NotFoundException("Like not found for the given user and comment"));

        commentLikeRepository.delete(like);
    }

    @Transactional
    public void addReportToComment(Long commentPk) {
        Comment comment = commentRepository.findById(commentPk)
                .orElseThrow(() -> new NotFoundException("Comment not found with PK: " + commentPk));

        comment.setReportCnt(comment.getReportCnt() + 1);
        commentRepository.save(comment);
    }
}
