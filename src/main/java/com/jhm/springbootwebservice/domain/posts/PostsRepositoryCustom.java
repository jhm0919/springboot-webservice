package com.jhm.springbootwebservice.domain.posts;

import com.jhm.springbootwebservice.web.dto.request.UserSearchDto;
import com.jhm.springbootwebservice.web.dto.response.PostsListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


@Repository
public interface PostsRepositoryCustom {
    Page<PostsListResponseDto> findPageDynamicQuery(PostType postType, UserSearchDto searchDto, Pageable pageable, int myPost, Long userId);
}
