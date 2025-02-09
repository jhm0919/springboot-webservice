package com.jhm.springbootwebservice.service.recommend;

import com.jhm.springbootwebservice.domain.comment.Comment;
import com.jhm.springbootwebservice.domain.comment.CommentRepository;
import com.jhm.springbootwebservice.domain.post.Post;
import com.jhm.springbootwebservice.domain.post.PostRepository;
import com.jhm.springbootwebservice.domain.recommend.CommentRecommend;
import com.jhm.springbootwebservice.domain.recommend.CommentRecommendRepository;
import com.jhm.springbootwebservice.domain.recommend.PostRecommend;
import com.jhm.springbootwebservice.domain.recommend.PostRecommendRepository;
import com.jhm.springbootwebservice.domain.user.User;
import com.jhm.springbootwebservice.domain.user.UserRepository;
import com.jhm.springbootwebservice.web.dto.request.RecommendRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Service
public class RecommendService {

    private final PostRecommendRepository postRecommendRepository;
    private final CommentRecommendRepository commentRecommendRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<String> recommend(RecommendRequestDto dto) {

        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
        Post post = postRepository.findById(dto.getPostId()).orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다."));

        if (dto.getCommentId() == null) { // 게시물 추천 or 비추천
            return postRecommend(dto, post, user);
        } else { // 댓글 추천 or 비추천
            return commentRecommend(dto, post, user);
        }

    }

    private ResponseEntity<String> postRecommend(RecommendRequestDto dto, Post post, User user) {

        PostRecommend recommend = postRecommendRepository.findByUserIdAndPostId(dto.getUserId(), dto.getPostId());
        if (recommend != null) { // 이미 추천 테이블에 있다면
            if (dto.getRecommendType() == recommend.getRecommendType()) { // 이 경우 추천or비추천 취소
                return postRecommendCancelLogic(dto, post ,recommend); // 취소
            } else { // 이 경우 추천or비추천을 취소하는게 아닌 다른걸 누름
                return ResponseEntity.badRequest().body("이미 추천/비추천을 누른 게시글 입니다. 취소 후 다시시도하세요.");
            }
        } else { // 추천이 없으면 (추천)
            return postRecommendLogic(dto, user, post);
        }
    }

    private ResponseEntity<String> postRecommendLogic(RecommendRequestDto dto, User user, Post post) {
        log.info("추천 로직 실행");
        PostRecommend recommend = PostRecommend.builder()
                .post(post)
                .user(user)
                .recommendType(dto.getRecommendType())
                .build();

        postRecommendRepository.save(recommend);
        if (dto.getRecommendType() == 0) { // 추천
            post.recommendUp();
            return ResponseEntity.ok("추천하였습니다.");
        } else { // 비추천
            post.disRecommendUp();
            return ResponseEntity.ok("비추천하였습니다.");
        }
    }

    private ResponseEntity<String> postRecommendCancelLogic(RecommendRequestDto dto, Post post , PostRecommend recommend) {
        log.info("추천 취소 로직 실행");
        postRecommendRepository.delete(recommend);
        if (dto.getRecommendType() == 0) {// 추천 취소
            post.recommendDown();
            return ResponseEntity.ok("추천이 취소되었습니다.");
        } else { // 비추천 취소
            post.disRecommendDown();
            return ResponseEntity.ok("비추천이 취소되었습니다.");
        }
    }

    private ResponseEntity<String> commentRecommend(RecommendRequestDto dto, Post post, User user) {
        Comment comment = commentRepository.findById(dto.getCommentId()).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));

        CommentRecommend recommend = commentRecommendRepository.findByUserIdAndPostIdAndCommentId(dto.getUserId(), dto.getPostId(), dto.getCommentId());
        if (recommend != null) { // 이미 추천 테이블에 있다면
            if (dto.getRecommendType() == recommend.getRecommendType()) { // 이 경우 추천or비추천 취소
                return commentRecommendCancelLogic(dto, comment ,recommend); // 취소
            } else { // 이 경우 추천or비추천을 취소하는게 아닌 다른걸 누름
                return ResponseEntity.badRequest().body("이미 추천/비추천을 누른 댓글 입니다. 취소 후 다시시도하세요.");
            }
        } else { // 추천이 없으면 (추천)
            return commentRecommendLogic(dto, user, post, comment);
        }

    }

    private ResponseEntity<String> commentRecommendLogic(RecommendRequestDto dto, User user, Post post, Comment comment) {

        CommentRecommend recommend = CommentRecommend.builder()
                .user(user)
                .post(post)
                .comment(comment)
                .recommendType(dto.getRecommendType())
                .build();

        commentRecommendRepository.save(recommend);
        if (dto.getRecommendType() == 0) {
            comment.recommendUp();
            return ResponseEntity.ok("댓글을 추천 하였습니다.");
        } else {
            comment.disRecommendUp();
            return ResponseEntity.ok("댓글을 비추천 하였습니다.");
        }

    }

    private ResponseEntity<String> commentRecommendCancelLogic(RecommendRequestDto dto, Comment comment, CommentRecommend recommend) {
        commentRecommendRepository.delete(recommend);
        if (dto.getRecommendType() == 0) { // 추천 취소
            comment.recommendDown();
            return ResponseEntity.ok("댓글 추천이 취소되었습니다.");
        } else { // 비추천 취소
            comment.disRecommendDown();
            return ResponseEntity.ok("댓글 비추천이 취소되었습니다.");
        }
    }


}
