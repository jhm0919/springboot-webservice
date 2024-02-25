package com.jhm.springbootwebservice.service.comments;

import com.jhm.springbootwebservice.domain.comments.Comment;
import com.jhm.springbootwebservice.domain.comments.CommentRepository;
import com.jhm.springbootwebservice.domain.posts.Posts;
import com.jhm.springbootwebservice.domain.posts.PostsRepository;
import com.jhm.springbootwebservice.domain.user.User;
import com.jhm.springbootwebservice.domain.user.UserRepository;
import com.jhm.springbootwebservice.web.dto.CommentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentsServiceImpl implements CommentsService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostsRepository postsRepository;

    @Override
    public Long commentSave(Long userId, Long id, CommentRequestDto dto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
            new IllegalArgumentException("댓글 쓰기 실패: 해당 사용자가 존재하지 않습니다."));

        Posts posts = postsRepository.findById(id).orElseThrow(() ->
            new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다."));

        dto.setUser(user);
        dto.setPosts(posts);

        Comment comment = dto.toEntity();
        commentRepository.save(comment);

        return comment.getId();
    }
}
