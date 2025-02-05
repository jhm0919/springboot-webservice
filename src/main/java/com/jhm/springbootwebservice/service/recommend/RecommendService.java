package com.jhm.springbootwebservice.service.recommend;

import com.jhm.springbootwebservice.domain.comments.Comment;
import com.jhm.springbootwebservice.domain.comments.CommentRepository;
import com.jhm.springbootwebservice.domain.posts.Posts;
import com.jhm.springbootwebservice.domain.posts.PostsRepository;
import com.jhm.springbootwebservice.domain.recommend.Recommend;
import com.jhm.springbootwebservice.domain.recommend.RecommendRepository;
import com.jhm.springbootwebservice.domain.user.User;
import com.jhm.springbootwebservice.domain.user.UserRepository;
import com.jhm.springbootwebservice.web.dto.request.RecommendRequestDto;
import com.jhm.springbootwebservice.web.dto.response.RecommendResponseDtoV2;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class RecommendService {

    private final RecommendRepository recommendRepository;
    private final PostsRepository postsRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> recommend(RecommendRequestDto dto) {

        // postId, userId, commentId, recommendType
        Long commentId = dto.getCommentId();
        Long postId = dto.getPostId();
        Long userId = dto.getUserId();
        Long recommendType = dto.getRecommendType();

        if (commentId == null) {
            // 게시물 추천 or 비추천
            Posts post = postsRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다."));
            User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));

            // 추천 중복 확인 (user, postId, commentId 조합으로)
            if (recommendRepository.existsByUserIdAndPostsIdAndCommentIdIsNull(userId, postId)) {
                return ResponseEntity.ok(Map.of("message", "이미 추천/비추천 한 게시물입니다."));
            }

            Recommend recommend = Recommend.builder()
                    .posts(post)
                    .user(user)
                    .comment(null)
                    .recommendType(recommendType)
                    .build();

            recommendRepository.save(recommend);

            if (recommendType == 0) {
                return ResponseEntity.ok(Map.of("message", "게시글을 추천 하였습니다."));
            } else {
                return ResponseEntity.ok(Map.of("message", "게시글을 비추천 하였습니다."));
            }
        } else {
            // 댓글 추천 or 비추천
            Posts post = postsRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다."));
            User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
            Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));

            // 추천 중복 확인 (userId, postId, commentId 조합으로)
            if (recommendRepository.existsByUserIdAndPostsIdAndCommentId(userId, postId, commentId)) {
                return ResponseEntity.ok(Map.of("message", "이미 추천/비추천 한 게시물입니다."));
            }

            Recommend recommend = Recommend.builder()
                    .posts(post)
                    .user(user)
                    .comment(comment)
                    .recommendType(recommendType)
                    .build();

            recommendRepository.save(recommend);

            if (recommendType == 0) {
                return ResponseEntity.ok(Map.of("message", "댓글을 추천 하였습니다."));
            } else {
                return ResponseEntity.ok(Map.of("message", "댓글을 비추천 하였습니다."));
            }
        }

    }

//    // 추천, 비추천 수 조회 메서드
//    public RecommendResponseDtoV2 findById(Long userId, Long postId, Long commentId) {
//        // 게시글 추천, 비추천, 댓글 추천, 비추천 조회 후 리턴
////        recommendRepository.existsByUserIdAndPostsIdAndCommentId(userId, postId, commentId);
//    }

}
