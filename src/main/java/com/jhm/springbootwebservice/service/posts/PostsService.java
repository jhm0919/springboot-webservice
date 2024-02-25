package com.jhm.springbootwebservice.service.posts;

import com.jhm.springbootwebservice.web.dto.response.PostsListResponseDto;
import com.jhm.springbootwebservice.web.dto.response.PostsResponseDto;
import com.jhm.springbootwebservice.web.dto.request.PostsSaveRequestDto;
import com.jhm.springbootwebservice.web.dto.request.PostsUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostsService {

    Long save(Long id, PostsSaveRequestDto requestDto);

    Long update(Long id, PostsUpdateRequestDto requestDto);

    int updateView(Long id);

    PostsResponseDto findById(Long id);

    Page<PostsListResponseDto> findAll(Pageable pageable);

    Page<PostsListResponseDto> postsSearch(String searchKeyword, Pageable pageable);

    void delete(Long id);

}
