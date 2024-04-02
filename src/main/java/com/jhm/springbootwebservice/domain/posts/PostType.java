package com.jhm.springbootwebservice.domain.posts;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostType {
    NOTICE("notice", "공지사항"),
    FREE("free", "자유게시판"),
    BOARD1("board1", "게시판1"),
    BOARD2("board2", "게시판2"),
    BOARD3("board3", "게시판3");

    private final String key;
    private final String title;
}
