package com.jhm.springbootwebservice.service.recommend;

import com.jhm.springbootwebservice.domain.comments.Comment;
import com.jhm.springbootwebservice.domain.comments.CommentRepository;
import com.jhm.springbootwebservice.domain.comments.recommend.*;
import com.jhm.springbootwebservice.domain.posts.Posts;
import com.jhm.springbootwebservice.domain.posts.PostsRepository;
import com.jhm.springbootwebservice.domain.user.User;
import com.jhm.springbootwebservice.domain.user.UserRepository;
import com.jhm.springbootwebservice.web.dto.request.RecommendRequestDto;
import com.jhm.springbootwebservice.web.dto.response.RecommendResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentsRecommendService {

    private final CommentsRecommendUpRepository commentsRecommendUpRepository;
    private final CommentsRecommendDownRepository commentsRecommendDownRepository;
    private final CommentRepository commentRepository;
    private final PostsRepository postsRepository;
    private final UserRepository userRepository;

//    @Transactional
//    public RecommendResponseDto findById(RecommendRequestDto requestDto) {
//        Posts post = postsRepository.findById(requestDto.getPostId()).orElseThrow(() ->
//                new IllegalArgumentException("해당 게시물이 없습니다."));
//        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(() ->
//                new IllegalArgumentException("해당 사용자가 없습니다."));
//        Comment comment = commentRepository.findByPostsIdAndId(requestDto.getPostId(), requestDto.getCommentId());
//
//        CommentsRecommendPK commentsRecommendPK = CommentsRecommendPK.builder()
//                .postId(post.getId())
//                .userId(user.getId())
//                .commentId(comment.getId())
//                .build();
//
//        boolean isRecommend = commentsRecommendUpRepository.findById(commentsRecommendPK).isPresent();
//
//        return new RecommendResponseDto(isRecommend, comment.getRecommendUp(),comment.getRecommendDown());
//    }

    @Transactional
    public void postDelete(Long postId) { // 지워지는 게시글 id인 추천들 삭제
        List<CommentsRecommendUp> postsRecommendUp = commentsRecommendUpRepository.findByPostsId(postId);
        List<CommentsRecommendDown> postsRecommendDown = commentsRecommendDownRepository.findByPostsId(postId);
        commentsRecommendUpRepository.deleteAll(postsRecommendUp);
        commentsRecommendDownRepository.deleteAll(postsRecommendDown);
    }

    @Transactional
    public void commentDelete(Long commentId) {
        List<CommentsRecommendUp> commentsRecommendUp = commentsRecommendUpRepository.findByCommentId(commentId);
        List<CommentsRecommendDown> commentsRecommendDown = commentsRecommendDownRepository.findByCommentId(commentId);
        commentsRecommendUpRepository.deleteAll(commentsRecommendUp);
        commentsRecommendDownRepository.deleteAll(commentsRecommendDown);
    }

    @Transactional
    public RecommendResponseDto recommend(RecommendRequestDto requestDto) {
        Posts post = postsRepository.findById(requestDto.getPostId()).orElseThrow(() ->
            new IllegalArgumentException("해당 게시물이 없습니다."));
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(() ->
            new IllegalArgumentException("해당 사용자가 없습니다."));
        Comment comment = getComment(requestDto);

        CommentsRecommendPK commentRecommendPK = getCommentRecommendPK(post, user, comment);

        Optional<CommentsRecommendUp> recommendUpOptional = commentsRecommendUpRepository.findById(commentRecommendPK);

        if (recommendUpOptional.isPresent()) { // 추천 한 경우
            recommendCancelLogic(comment, commentRecommendPK);
        } else { // 추천 안한 경우
            recommendLogic(post, user, comment, commentRecommendPK);
        }
        commentRepository.save(comment);
        return new RecommendResponseDto(comment.getRecommendUp(), comment.getRecommendDown());
    }

    private void recommendLogic(Posts post, User user, Comment comment, CommentsRecommendPK commentRecommendPK) {
        CommentsRecommendUp recommend = CommentsRecommendUp.builder()
                .id(commentRecommendPK)
                .posts(post)
                .comment(comment)
                .user(user)
                .build();
        commentsRecommendUpRepository.save(recommend);
        comment.recommendUp(); // 추천 증가
    }

    private void recommendCancelLogic(Comment comment, CommentsRecommendPK commentRecommendPK) {
        commentsRecommendUpRepository.deleteById(commentRecommendPK);
        comment.recommendDown(); // 추천 감소
    }

    @Transactional
    public RecommendResponseDto disRecommend(RecommendRequestDto requestDto) {
        Posts post = postsRepository.findById(requestDto.getPostId()).orElseThrow(() ->
            new IllegalArgumentException("해당 게시물이 없습니다."));
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(() ->
            new IllegalArgumentException("해당 사용자가 없습니다."));
        Comment comment = getComment(requestDto);

        CommentsRecommendPK commentRecommendPK = getCommentRecommendPK(post, user, comment);

        Optional<CommentsRecommendDown> recommendDownOptional = commentsRecommendDownRepository.findById(commentRecommendPK);

        if (recommendDownOptional.isPresent()) {
            disRecommendCancelLogic(comment, commentRecommendPK);
        } else {
            disRecommendLogic(post, user, comment, commentRecommendPK);
        }
        commentRepository.save(comment);
        return new RecommendResponseDto(comment.getRecommendUp(), comment.getRecommendDown());
    }

    private void disRecommendLogic(Posts post, User user, Comment comment, CommentsRecommendPK commentRecommendPK) {
        CommentsRecommendDown recommend = CommentsRecommendDown.builder()
            .id(commentRecommendPK)
            .posts(post)
            .user(user)
            .comment(comment)
            .build();
        commentsRecommendDownRepository.save(recommend);
        comment.disRecommendUp(); // 추천 증가
    }

    private void disRecommendCancelLogic(Comment comment, CommentsRecommendPK commentRecommendPK) {
        commentsRecommendDownRepository.deleteById(commentRecommendPK);
        comment.disRecommendDown(); // 비추천 감소
    }

    private Comment getComment(RecommendRequestDto requestDto) {
        return commentRepository.findByPostsIdAndId(requestDto.getPostId(), requestDto.getCommentId());
    }

    private static CommentsRecommendPK getCommentRecommendPK(Posts post, User user, Comment comment) {
        CommentsRecommendPK commentsRecommendPK = CommentsRecommendPK.builder()
            .postId(post.getId())
            .userId(user.getId())
            .commentId(comment.getId())
            .build();
        return commentsRecommendPK;
    }

}
