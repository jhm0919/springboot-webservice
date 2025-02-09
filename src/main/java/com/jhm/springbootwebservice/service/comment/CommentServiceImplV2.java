package com.jhm.springbootwebservice.service.comment;

import com.jhm.springbootwebservice.domain.comment.Comment;
import com.jhm.springbootwebservice.domain.comment.CommentRepository;
import com.jhm.springbootwebservice.domain.comment.CommentRepositoryV2;
import com.jhm.springbootwebservice.domain.comment.CommentV2;
import com.jhm.springbootwebservice.domain.post.Post;
import com.jhm.springbootwebservice.domain.post.PostRepository;
import com.jhm.springbootwebservice.domain.user.User;
import com.jhm.springbootwebservice.domain.user.UserRepository;
import com.jhm.springbootwebservice.web.dto.request.CommentRequestDto;
import com.jhm.springbootwebservice.web.dto.request.CommentRequestDtoV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentServiceImplV2 {

    private final CommentRepositoryV2 commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public Long save(Long userId, Long postId, CommentRequestDtoV2 dto) {

        User user = userRepository.findById(userId).orElseThrow(() ->
            new IllegalArgumentException("댓글 쓰기 실패: 해당 사용자가 존재하지 않습니다."));

        Post post = postRepository.findById(postId).orElseThrow(() ->
            new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다."));


        if (dto.getParentId() == null) { // 첫 댓글인 경우
            saveComment(dto, user, post);
        } else { // 자식댓글인 경우
            CommentV2 parentComment = commentRepository.findById(dto.getParentId()) // 부모 댓글 정보
                .orElseThrow(() -> new IllegalArgumentException("부모 댓글이 존재하지 않습니다."));

            int depthNo = parentComment.getDepthNo();
            int level = parentComment.getLevel();
            int maxLevel = 0;

            List<CommentV2> siblings = commentRepository.findByGroupNoAndDepthNo(parentComment.getGroupNo(), depthNo + 1);
            for (CommentV2 sibling : siblings) {
                maxLevel = Math.max(maxLevel, sibling.getLevel());
            }

            // 대댓글
            CommentV2 comment = CommentV2.builder()
                .groupNo(dto.getParentId())
                .depthNo(parentComment.getDepthNo() + 1)
                .level(maxLevel + 1)
                .parent(parentComment)
                .build();


        }
        return 1L;
    }

    private void saveComment(CommentRequestDtoV2 dto, User user, Post post) {
        dto.setUser(user);
        dto.setPost(post);

        CommentV2 comment = dto.toEntity();

        comment.updateAuthor(user);
        comment.updatePost(post);

        // 1. 먼저 저장해서 ID 자동 생성
        commentRepository.save(comment);

        // 2. 방금 생성된 ID를 groupNo로 업데이트
        comment.updateGroupNo(comment.getId());
        commentRepository.save(comment);
    }

    public Long update(Long postId, Long id, CommentRequestDto dto) {
        return null;
    }

    public Long delete(Long postId, Long id) {
        return null;
    }
}
