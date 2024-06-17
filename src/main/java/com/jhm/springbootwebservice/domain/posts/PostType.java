package com.jhm.springbootwebservice.domain.posts;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostType {
    NOTICE("NOTICE", "공지사항"),
    FREE("FREE", "자유게시판"),
    BOARD1("BOARD1", "게시판1"),
    BOARD2("BOARD2", "게시판2"),
    BOARD3("BOARD3", "게시판3");

    private final String key;
    private final String title;
}
