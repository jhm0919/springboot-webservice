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

//    @Override
    @Transactional
    public RecommendResponseDto findById(RecommendRequestDto requestDto) {
        Posts post = postsRepository.findById(requestDto.getPostId()).orElseThrow(() ->
                new IllegalArgumentException("해당 게시물이 없습니다."));
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 없습니다."));
        Comment comment = commentRepository.findByPostsIdAndId(requestDto.getPostId(), requestDto.getCommentId());

        CommentsRecommendPK commentsRecommendPK = CommentsRecommendPK.builder()
                .postId(post.getId())
                .userId(user.getId())
                .commentId(comment.getId())
                .build();

        boolean isRecommend = commentsRecommendUpRepository.findById(commentsRecommendPK).isPresent();

        return new RecommendResponseDto(isRecommend, comment.getRecommendUp(),comment.getRecommendDown());
    }

//    @Override
    @Transactional
    public void delete(Long id) {
        List<CommentsRecommendUp> postsRecommendUp = commentsRecommendUpRepository.findByPostsId(id);
        List<CommentsRecommendDown> postsRecommendDown = commentsRecommendDownRepository.findByPostsId(id);
        commentsRecommendUpRepository.deleteAll(postsRecommendUp);
        commentsRecommendDownRepository.deleteAll(postsRecommendDown);
    }

//    @Override
    @Transactional
    public RecommendResponseDto recommend(RecommendRequestDto requestDto) {
        Posts post = postsRepository.findById(requestDto.getPostId()).orElseThrow(() ->
                new IllegalArgumentException("해당 게시물이 없습니다."));
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 없습니다."));
        Comment comment = commentRepository.findByPostsIdAndId(requestDto.getPostId(), requestDto.getCommentId());

        CommentsRecommendPK commentsRecommendPK = CommentsRecommendPK.builder()
                .postId(post.getId())
                .userId(user.getId())
                .commentId(comment.getId())
                .build();

        Optional<CommentsRecommendUp> recommendUpOptional = commentsRecommendUpRepository.findById(commentsRecommendPK);

        if (recommendUpOptional.isPresent()) { // 추천 한 경우
            commentsRecommendUpRepository.deleteById(commentsRecommendPK);
            comment.recommendDown(); // 추천 감소
            commentRepository.save(comment);
            return new RecommendResponseDto(false, comment.getRecommendUp(), comment.getRecommendDown());
        } else { // 추천 안한 경우
            CommentsRecommendUp recommend = CommentsRecommendUp.builder()
                    .id(commentsRecommendPK)
                    .posts(post)
                    .comment(comment)
                    .user(user)
                    .build();
            commentsRecommendUpRepository.save(recommend);
            comment.recommendUp(); // 추천 증가
            commentRepository.save(comment);
            return new RecommendResponseDto(true, comment.getRecommendUp(), comment.getRecommendDown());
        }
    }

//    @Override
    @Transactional
    public RecommendResponseDto disRecommend(RecommendRequestDto requestDto) {
        Posts post = postsRepository.findById(requestDto.getPostId()).orElseThrow(() ->
                new IllegalArgumentException("해당 게시물이 없습니다."));
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 없습니다."));
        Comment comment = commentRepository.findByPostsIdAndId(requestDto.getPostId(), requestDto.getCommentId());

        CommentsRecommendPK commentsRecommendPK = CommentsRecommendPK.builder()
                .postId(post.getId())
                .userId(user.getId())
                .commentId(comment.getId())
                .build();

        Optional<CommentsRecommendDown> recommendDownOptional = commentsRecommendDownRepository.findById(commentsRecommendPK);

        if (recommendDownOptional.isPresent()) {
            commentsRecommendDownRepository.deleteById(commentsRecommendPK);
            comment.disRecommendDown(); // 비추천 감소
            commentRepository.save(comment);
            return new RecommendResponseDto(false, comment.getRecommendUp(), comment.getRecommendDown());
        } else {
            CommentsRecommendDown recommend = CommentsRecommendDown.builder()
                    .id(commentsRecommendPK)
                    .posts(post)
                    .user(user)
                    .comment(comment)
                    .build();
            commentsRecommendDownRepository.save(recommend);
            comment.disRecommendUp(); // 추천 증가
            commentRepository.save(comment);
            return new RecommendResponseDto(true, comment.getRecommendUp(), comment.getRecommendDown());
        }
    }
}
