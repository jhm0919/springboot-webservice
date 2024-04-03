package com.jhm.springbootwebservice.service.recommend;

import com.jhm.springbootwebservice.domain.posts.Posts;
import com.jhm.springbootwebservice.domain.posts.PostsRepository;
import com.jhm.springbootwebservice.domain.posts.recommend.*;
import com.jhm.springbootwebservice.domain.user.User;
import com.jhm.springbootwebservice.domain.user.UserRepository;
import com.jhm.springbootwebservice.web.dto.request.RecommendRequestDto;
import com.jhm.springbootwebservice.web.dto.response.RecommendResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class PostsRecommendService {

    private final PostsRecommendUpRepository postsRecommendUpRepository;
    private final PostsRecommendDownRepository postsRecommendDownRepository;
    private final PostsRepository postsRepository;
    private final UserRepository userRepository;

    @Transactional
    public RecommendResponseDto findById(RecommendRequestDto requestDto) {
        Posts post = postsRepository.findById(requestDto.getPostId()).orElseThrow(() ->
            new IllegalArgumentException("해당 게시물이 없습니다."));

        PostsRecommendPK postsRecommendPK = PostsRecommendPK.builder()
            .postId(requestDto.getPostId())
            .userId(requestDto.getUserId())
            .build();
        boolean isRecommend = postsRecommendUpRepository.findById(postsRecommendPK).isPresent();

        return new RecommendResponseDto(isRecommend, post.getRecommendUp(),post.getRecommendDown());
    }

    @Transactional
    public void delete(Long id) {
        List<PostsRecommendUp> postsRecommendUp = postsRecommendUpRepository.findByPostsId(id);
        List<PostsRecommendDown> postsRecommendDown = postsRecommendDownRepository.findByPostsId(id);
        postsRecommendUpRepository.deleteAll(postsRecommendUp);
        postsRecommendDownRepository.deleteAll(postsRecommendDown);
    }

    @Transactional
    public RecommendResponseDto recommend(RecommendRequestDto requestDto) {
        Posts post = postsRepository.findById(requestDto.getPostId()).orElseThrow(() ->
                new IllegalArgumentException("해당 게시물이 없습니다."));
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 없습니다."));

        PostsRecommendPK postsRecommendPK = PostsRecommendPK.builder()
                .postId(post.getId())
                .userId(user.getId())
                .build();

        Optional<PostsRecommendUp> recommendUpOptional = postsRecommendUpRepository.findById(postsRecommendPK);

        if (recommendUpOptional.isPresent()) { // 추천 한 경우
            postsRecommendUpRepository.deleteById(postsRecommendPK);
            post.recommendDown(); // 추천 감소
            postsRepository.save(post);
            return new RecommendResponseDto(false, post.getRecommendUp(), post.getRecommendDown());
        } else { // 추천 안한 경우
            PostsRecommendUp recommend = PostsRecommendUp.builder()
                    .id(postsRecommendPK)
                    .posts(post)
                    .user(user)
                    .build();
            postsRecommendUpRepository.save(recommend);
            post.recommendUp(); // 추천 증가
            postsRepository.save(post);
            return new RecommendResponseDto(true, post.getRecommendUp(), post.getRecommendDown());
        }
    }

    @Transactional
    public RecommendResponseDto disRecommend(RecommendRequestDto requestDto) {
        Posts post = postsRepository.findById(requestDto.getPostId()).orElseThrow(() ->
                new IllegalArgumentException("해당 게시물이 없습니다."));
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 없습니다."));

        PostsRecommendPK postsRecommendPK = PostsRecommendPK.builder()
                .postId(post.getId())
                .userId(user.getId())
                .build();

        Optional<PostsRecommendDown> recommendDownOptional = postsRecommendDownRepository.findById(postsRecommendPK);

        if (recommendDownOptional.isPresent()) {
            postsRecommendDownRepository.deleteById(postsRecommendPK);
            post.disRecommendDown(); // 비추천 감소
            postsRepository.save(post);
            return new RecommendResponseDto(false, post.getRecommendUp(), post.getRecommendDown());
        } else {
            PostsRecommendDown recommend = PostsRecommendDown.builder()
                    .id(postsRecommendPK)
                    .posts(post)
                    .user(user)
                    .build();
            postsRecommendDownRepository.save(recommend);
            post.disRecommendUp(); // 추천 증가
            postsRepository.save(post);
            return new RecommendResponseDto(true, post.getRecommendUp(), post.getRecommendDown());
        }
    }

}
