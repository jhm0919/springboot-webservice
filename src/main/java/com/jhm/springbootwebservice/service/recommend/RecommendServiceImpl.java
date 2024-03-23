package com.jhm.springbootwebservice.service.recommend;

import com.jhm.springbootwebservice.domain.posts.Posts;
import com.jhm.springbootwebservice.domain.posts.PostsRepository;
import com.jhm.springbootwebservice.domain.recommend.Recommend;
import com.jhm.springbootwebservice.domain.recommend.RecommendPK;
import com.jhm.springbootwebservice.domain.recommend.RecommendRepository;
import com.jhm.springbootwebservice.domain.user.User;
import com.jhm.springbootwebservice.domain.user.UserRepository;
import com.jhm.springbootwebservice.web.dto.request.RecommendRequestDto;
import com.jhm.springbootwebservice.web.dto.response.RecommendResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class RecommendServiceImpl implements RecommendService{

    private final RecommendRepository recommendRepository;
    private final PostsRepository postsRepository;
    private final UserRepository userRepository;


    @Override
    public RecommendResponseDto findById(RecommendRequestDto requestDto) {
        Posts post = postsRepository.findById(requestDto.getPostId()).orElseThrow(() ->
            new IllegalArgumentException("해당 게시물이 없습니다."));

        RecommendPK recommendPK = RecommendPK.builder()
            .postId(requestDto.getPostId())
            .userId(requestDto.getUserId())
            .build();
        boolean isRecommend = recommendRepository.findById(recommendPK).isPresent();

        return new RecommendResponseDto(isRecommend, post.getRecommend());
    }

    @Override
    @Transactional
    public RecommendResponseDto recommend(RecommendRequestDto requestDto) {
        Posts post = postsRepository.findById(requestDto.getPostId()).orElseThrow(() ->
            new IllegalArgumentException("해당 게시물이 없습니다."));
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(() ->
            new IllegalArgumentException("해당 사용자가 없습니다."));

        RecommendPK recommendPK = RecommendPK.builder()
            .postId(post.getId())
            .userId(user.getId())
            .build();

        if (recommendRepository.findById(recommendPK).isPresent()) { // 추천이 눌려있다면
            return new RecommendResponseDto(false, post.getRecommend());
        }

        Recommend recommend = Recommend.builder()
            .id(recommendPK)
            .posts(post)
            .user(user)
            .build();

        recommendRepository.save(recommend);
        post.recommendCount();
        postsRepository.save(post);

        return new RecommendResponseDto(true, post.getRecommend());
    }

    @Override
    public RecommendResponseDto disRecommend(RecommendRequestDto requestDto) {
        Posts post = postsRepository.findById(requestDto.getPostId()).orElseThrow(() ->
            new IllegalArgumentException("해당 게시물이 없습니다."));
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(() ->
            new IllegalArgumentException("해당 사용자가 없습니다."));

        RecommendPK recommendPK = RecommendPK.builder()
            .postId(post.getId())
            .userId(user.getId())
            .build();

        if (recommendRepository.findById(recommendPK).isEmpty()) { // 추천이 눌려있지 않으면
            return new RecommendResponseDto(false, post.getRecommend());
        }
        recommendRepository.deleteById(recommendPK);
        post.disRecommend();
        postsRepository.save(post);

        return new RecommendResponseDto(true, post.getRecommend());
    }
}
