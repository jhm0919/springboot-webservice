package com.jhm.springbootwebservice.service.posts;

import com.jhm.springbootwebservice.domain.user.User;
import com.jhm.springbootwebservice.web.dto.PostsListResponseDto;
import com.jhm.springbootwebservice.web.dto.PostsResponseDto;
import com.jhm.springbootwebservice.web.dto.PostsSaveRequestDto;
import com.jhm.springbootwebservice.web.dto.PostsUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostsService {

    Long save(Long id, PostsSaveRequestDto requestDto);

    Long update(Long id, PostsUpdateRequestDto requestDto);

    PostsResponseDto findById(Long id);

    Page<PostsListResponseDto> findAll(Pageable pageable);

    void delete(Long id);

}
