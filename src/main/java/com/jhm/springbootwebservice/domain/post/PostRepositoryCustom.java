package com.jhm.springbootwebservice.domain.post;

import com.jhm.springbootwebservice.web.dto.request.UserSearchDto;
import com.jhm.springbootwebservice.web.dto.response.PostListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


@Repository
public interface PostRepositoryCustom {
    Page<PostListResponseDto> findPageDynamicQuery(PostType postType, UserSearchDto searchDto, Pageable pageable, int myPost, Long userId);
}
