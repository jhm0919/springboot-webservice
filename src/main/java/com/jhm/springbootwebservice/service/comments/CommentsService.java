package com.jhm.springbootwebservice.service.comments;

import com.jhm.springbootwebservice.web.dto.request.CommentRequestDto;

public interface CommentsService {

    Long save(Long userId, Long id, CommentRequestDto dto);

    Long update(Long postsId, Long id, CommentRequestDto dto);

    Long delete(Long postsId, Long id);
}
