package com.jhm.springbootwebservice.service.comment;

import com.jhm.springbootwebservice.domain.comment.CommentRepository;
import com.jhm.springbootwebservice.domain.comment.Comment;
import com.jhm.springbootwebservice.domain.post.Post;
import com.jhm.springbootwebservice.domain.post.PostRepository;
import com.jhm.springbootwebservice.domain.user.User;
import com.jhm.springbootwebservice.domain.user.UserRepository;
import com.jhm.springbootwebservice.web.dto.request.CommentRequestDto;
import com.jhm.springbootwebservice.web.dto.response.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public Long save(Long userId, Long postId, CommentRequestDto dto) {

        User user = userRepository.findById(userId).orElseThrow(() ->
            new IllegalArgumentException("댓글 쓰기 실패: 해당 사용자가 존재하지 않습니다."));

        Post post = postRepository.findById(postId).orElseThrow(() ->
            new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다."));

        log.info("댓글이면 null 대댓글이면 1 ={}", dto.getHasParent());
        if (dto.getHasParent() == null) { // 첫 댓글인 경우
            log.info("첫 댓글인 경우 호출");
            saveComment(dto, user, post);
        } else { // 자식댓글인 경우
            log.info("대댓글인 경우 호출");
            saveReply(dto, user, post);
        }
        return 1L;
    }

    private void saveReply(CommentRequestDto dto, User user, Post post) {
        Comment parentComment = commentRepository.findById(dto.getId()).orElseThrow(
                () -> new IllegalArgumentException("부모 댓글이 존재하지 않습니다."));

        Long groupNo = parentComment.getGroupNo();
        int depthNo = parentComment.getDepthNo();
        int level = parentComment.getLevel();
        int maxDepth = 0;

        if (parentComment.getParent() == null) { // 부모가 없는 댓글의 답글
            log.info("부모가 없는 댓글의 답글");
            int result = commentRepository.findMaxDepthNoByGroupNoAndLevel(groupNo, level + 1);
            maxDepth = result + 1;
        } else {
            log.info("부모가 있는 댓글의 답글");
            Long parentId = parentComment.getId(); // 부모 아이디
            int result = commentRepository.findMaxDepthNoByGroupNoAndParentIdAndLevel(groupNo, parentId, level + 1);
            if (result == 0) { // 레벨이 + 1된 첫 댓글은 depthNo를
                maxDepth = depthNo + 1;
            } else { // 첫댓글이 아닌 경우 max값
                maxDepth = result + 1;
                log.info("동일 level의 max값={}", result);
            }
            log.info("같은 레벨이면서 같은 부모를 가진 depthNo의 최댓값={}", result);

        }

        Comment comment = Comment.builder()
            .comment(dto.getComment())
            .groupNo(groupNo)
            .depthNo(maxDepth)
            .level(level + 1)
            .parent(parentComment)
            .post(post)
            .user(user)
            .build();

        // 같은 groupNo들 중에서 추가된 댓글의 depthNo보다 큰 depthNo들 + 1 처리하기
        commentRepository.incrementDepthNoByGroupNo(groupNo, maxDepth);

        commentRepository.save(comment);
    }

    private void saveComment(CommentRequestDto dto, User user, Post post) {

        dto.setUser(user);
        dto.setPost(post);

        Comment comment = dto.toEntity();

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

    public List<CommentResponseDto> getSortedCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findCommentsByPostIdSorted(postId);
        return comments.stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }
}
