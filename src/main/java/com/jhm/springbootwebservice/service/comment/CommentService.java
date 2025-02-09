package com.jhm.springbootwebservice.service.comment;

import com.jhm.springbootwebservice.web.dto.request.CommentRequestDto;

public interface CommentService {

    Long save(Long userId, Long id, CommentRequestDto dto);

    Long update(Long postId, Long id, CommentRequestDto dto);

    Long delete(Long postId, Long id);
}
