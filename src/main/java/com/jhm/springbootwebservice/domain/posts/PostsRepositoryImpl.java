package com.jhm.springbootwebservice.domain.posts;

import com.jhm.springbootwebservice.config.auth.dto.SessionUser;
import com.jhm.springbootwebservice.web.dto.request.UserSearchDto;
import com.jhm.springbootwebservice.web.dto.response.PostsListResponseDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class PostsRepositoryImpl implements PostsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QPosts posts = QPosts.posts;

    @Override
    public Page<PostsListResponseDto> findPageDynamicQuery(PostType postType, UserSearchDto searchDto, Pageable pageable, int myPost, String username) {
        List<PostsListResponseDto> content = queryFactory
                .select(Projections.constructor(PostsListResponseDto.class, posts))
                .from(posts)
                .where(postTypeEq(postType), userEq(myPost, username), searchDtoEq(searchDto))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(posts.id.desc())
                .fetch();

        Long total = queryFactory
                .select(posts.count())
                .from(posts)
                .where(postTypeEq(postType), userEq(myPost, username), searchDtoEq(searchDto))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression userEq(int myPost, String username) {
        return myPost != 0 ? posts.author.eq(username) : null;
    }

    private BooleanExpression postTypeEq(PostType postTypeCond) {
        return postTypeCond != null ? posts.postType.eq(postTypeCond) : null;
    }

    private BooleanExpression searchDtoEq(UserSearchDto searchDtoCond) {
        if (searchDtoCond.getSearchKeyword() != "" && searchDtoCond.getSearchType() != null) {
            if (searchDtoCond.getSearchType().equals("title")) {
                log.info("제목 검색");
                return posts.title.contains(searchDtoCond.getSearchKeyword());
            } else if (searchDtoCond.getSearchType().equals("content")) {
                log.info("내용 검색");
                return posts.content.contains(searchDtoCond.getSearchKeyword());
            } else if (searchDtoCond.getSearchType().equals("author")) {
                log.info("작성자 검색");
                return posts.author.contains(searchDtoCond.getSearchKeyword());
            } else {
                log.info("제목+내용 검색");
                return posts.title.contains(searchDtoCond.getSearchKeyword())
                        .or(posts.content.contains(searchDtoCond.getSearchKeyword()));
            }
        }
        return null;
    }
}
