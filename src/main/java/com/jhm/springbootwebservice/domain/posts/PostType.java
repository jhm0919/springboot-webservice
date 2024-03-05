package com.jhm.springbootwebservice.domain.posts;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostType {
    NOTICE("notice", "공지사항"),
    FREE("free", "자유게시판");

    private final String key;
    private final String title;
}
