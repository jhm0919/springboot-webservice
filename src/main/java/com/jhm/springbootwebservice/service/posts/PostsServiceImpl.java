package com.jhm.springbootwebservice.service.posts;

import com.jhm.springbootwebservice.domain.posts.Posts;
import com.jhm.springbootwebservice.domain.posts.PostsRepository;
import com.jhm.springbootwebservice.domain.user.User;
import com.jhm.springbootwebservice.domain.user.UserRepository;
import com.jhm.springbootwebservice.web.dto.PostsListResponseDto;
import com.jhm.springbootwebservice.web.dto.PostsResponseDto;
import com.jhm.springbootwebservice.web.dto.PostsSaveRequestDto;
import com.jhm.springbootwebservice.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostsServiceImpl implements PostsService{

    private final PostsRepository postsRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Long save(Long id, PostsSaveRequestDto requestDto) {
        User user = userRepository.findById(id).orElseThrow(() ->
            new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        requestDto.setUser(user);
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Override
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostsListResponseDto> findAll(Pageable pageable) {
        Page<Posts> postsPages = postsRepository.findAll(pageable);
        Page<PostsListResponseDto> postsListResponseDto = postsPages.map(PostsListResponseDto::new);
        return postsListResponseDto;
    }

    @Transactional
    public Page<PostsListResponseDto> postsSearch(String searchKeyword, Pageable pageable) {
        Page<Posts> posts = postsRepository.findByTitleContaining(searchKeyword, pageable);
        Page<PostsListResponseDto> postsListResponseDto = posts.map(PostsListResponseDto::new);
        return postsListResponseDto;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        postsRepository.delete(posts);
    }
}
