package com.jhm.springbootwebservice.service.comments;

import com.jhm.springbootwebservice.web.dto.CommentRequestDto;

public interface CommentsService {

    public Long commentSave(Long userId, Long id, CommentRequestDto dto);
}
