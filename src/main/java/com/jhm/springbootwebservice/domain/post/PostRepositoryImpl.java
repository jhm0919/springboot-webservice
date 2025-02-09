package com.jhm.springbootwebservice.domain.post;

import com.jhm.springbootwebservice.web.dto.request.UserSearchDto;
import com.jhm.springbootwebservice.web.dto.response.PostListResponseDto;
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
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final EntityManager em;

    QPost post = QPost.post;

    @Override

    public Page<PostListResponseDto> findPageDynamicQuery(PostType postType, UserSearchDto searchDto, Pageable pageable, int myPost, Long userId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        List<PostListResponseDto> content = queryFactory
                .select(Projections.constructor(PostListResponseDto.class, post))
                .from(post)
                .where(postTypeEq(postType), userEq(myPost, userId), searchDtoEq(searchDto))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.id.desc())
                .fetch();

        Long total = queryFactory
                .select(post.count())
                .from(post)
                .where(postTypeEq(postType), userEq(myPost, userId), searchDtoEq(searchDto))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression userEq(int myPost, Long userId) {
        return myPost != 0 ? post.user.id.eq(userId) : null;
    }

    private BooleanExpression postTypeEq(PostType postTypeCond) {
        return postTypeCond != null ? post.postType.eq(postTypeCond) : null;
    }

    private BooleanExpression searchDtoEq(UserSearchDto searchDtoCond) {
        if (searchDtoCond.getSearchKeyword() != "" && searchDtoCond.getSearchType() != null) {
            if (searchDtoCond.getSearchType().equals("title")) {
                return post.title.contains(searchDtoCond.getSearchKeyword());
            } else if (searchDtoCond.getSearchType().equals("content")) {
                return post.pureContent.contains(searchDtoCond.getSearchKeyword());
            } else if (searchDtoCond.getSearchType().equals("author")) {
                return post.author.contains(searchDtoCond.getSearchKeyword());
            } else {
                return post.title.contains(searchDtoCond.getSearchKeyword())
                        .or(post.pureContent.contains(searchDtoCond.getSearchKeyword()));
            }
        }
        return null;
    }
}
