package com.jhm.springbootwebservice.service.comments;

import com.jhm.springbootwebservice.domain.comments.Comment;
import com.jhm.springbootwebservice.domain.comments.CommentRepository;
import com.jhm.springbootwebservice.domain.posts.Posts;
import com.jhm.springbootwebservice.domain.posts.PostsRepository;
import com.jhm.springbootwebservice.domain.user.User;
import com.jhm.springbootwebservice.domain.user.UserRepository;
import com.jhm.springbootwebservice.web.dto.request.CommentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentsServiceImpl implements CommentsService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostsRepository postsRepository;

    @Override
    public Long save(Long userId, Long postId, CommentRequestDto dto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
            new IllegalArgumentException("댓글 쓰기 실패: 해당 사용자가 존재하지 않습니다."));

        Posts post = postsRepository.findById(postId).orElseThrow(() ->
            new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다."));

        dto.setUser(user);
        dto.setPosts(post);
        post.commentSizeUp(); // 해당 게시글 댓글 수 증가

        Comment comment = dto.toEntity();
        commentRepository.save(comment);

        return comment.getId();
    }

    @Override
    public Long update(Long postId, Long commentId, CommentRequestDto dto) {
        Comment comment = commentRepository.findByPostsIdAndId(postId, commentId);

        comment.update(dto.getComment());

        return commentId;
    }

    @Override
    public Long delete(Long postId, Long commentId) {
        Posts post = postsRepository.findById(postId).orElseThrow(() ->
                new IllegalArgumentException("댓글 삭제 실패: 해당 게시글이 존재하지 않습니다."));
        Comment comment = commentRepository.findByPostsIdAndId(postId, commentId);

        post.commentSizeDown(); // 해당 게시글 댓글 수 감소
        commentRepository.delete(comment);

        return commentId;
    }
}
