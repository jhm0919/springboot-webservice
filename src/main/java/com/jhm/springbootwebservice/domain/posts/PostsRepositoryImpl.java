package com.jhm.springbootwebservice.domain.posts;

import com.jhm.springbootwebservice.domain.comments.QComment;
import com.jhm.springbootwebservice.web.dto.request.UserSearchDto;
import com.jhm.springbootwebservice.web.dto.response.PostsListResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class PostsRepositoryImpl implements PostsRepositoryCustom {

    private final EntityManager em;

    QPosts posts = QPosts.posts;

    @Override

    public Page<PostsListResponseDto> findPageDynamicQuery(PostType postType, UserSearchDto searchDto, Pageable pageable, int myPost, Long userId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        List<PostsListResponseDto> content = queryFactory
                .select(Projections.constructor(PostsListResponseDto.class, posts))
                .from(posts)
                .where(postTypeEq(postType), userEq(myPost, userId), searchDtoEq(searchDto))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(posts.id.desc())
                .fetch();

        Long total = queryFactory
                .select(posts.count())
                .from(posts)
                .where(postTypeEq(postType), userEq(myPost, userId), searchDtoEq(searchDto))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression userEq(int myPost, Long userId) {
        return myPost != 0 ? posts.user.id.eq(userId) : null;
    }

    private BooleanExpression postTypeEq(PostType postTypeCond) {
        return postTypeCond != null ? posts.postType.eq(postTypeCond) : null;
    }

    private BooleanExpression searchDtoEq(UserSearchDto searchDtoCond) {
        if (searchDtoCond.getSearchKeyword() != "" && searchDtoCond.getSearchType() != null) {
            if (searchDtoCond.getSearchType().equals("title")) {
                return posts.title.contains(searchDtoCond.getSearchKeyword());
            } else if (searchDtoCond.getSearchType().equals("content")) {
                return posts.pureContent.contains(searchDtoCond.getSearchKeyword());
            } else if (searchDtoCond.getSearchType().equals("author")) {
                return posts.author.contains(searchDtoCond.getSearchKeyword());
            } else {
                return posts.title.contains(searchDtoCond.getSearchKeyword())
                        .or(posts.pureContent.contains(searchDtoCond.getSearchKeyword()));
            }
        }
        return null;
    }
}
