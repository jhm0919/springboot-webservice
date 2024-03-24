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

import java.util.List;


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

        return new RecommendResponseDto(isRecommend, post.getRecommendUp(),post.getRecommendDown());
    }

    @Override
    public void delete(Long id) {
        List<Recommend> recommend = recommendRepository.findByPostsId(id);
        recommendRepository.deleteAll(recommend);
    }

    @Override
    @Transactional
    public RecommendResponseDto recommend(RecommendRequestDto requestDto) {
        return getRecommendResponseDto(requestDto, true);
    }

    @Override
    public RecommendResponseDto disRecommend(RecommendRequestDto requestDto) {
        return getRecommendResponseDto(requestDto, false);
    }

    private RecommendResponseDto getRecommendResponseDto(RecommendRequestDto requestDto, boolean tOrF) {
        Posts post = postsRepository.findById(requestDto.getPostId()).orElseThrow(() ->
            new IllegalArgumentException("해당 게시물이 없습니다."));
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(() ->
            new IllegalArgumentException("해당 사용자가 없습니다."));

        RecommendPK recommendPK = RecommendPK.builder()
            .postId(post.getId())
            .userId(user.getId())
            .build();

        Recommend recommend = Recommend.builder()
            .id(recommendPK)
            .posts(post)
            .user(user)
            .build();

        if (recommendRepository.findById(recommendPK).isPresent()) { // 추천,비추천이 눌려있다면

            recommendRepository.delete(recommend);

            if (tOrF) {
                post.recommendDown(); // 추천 감소
            } else {
                post.disRecommendDown(); // 비추천 감소
            }

            postsRepository.save(post);

            return new RecommendResponseDto(false, post.getRecommendUp(), post.getRecommendDown());
        }
        // 추천이 안눌려 있다면

        recommendRepository.save(recommend);

        if (tOrF) {
            post.recommendUp(); // 추천 증가
        } else {
            post.disRecommendUp(); // 비추천 증가
        }

        postsRepository.save(post);

        return new RecommendResponseDto(true, post.getRecommendUp(), post.getRecommendDown());
    }
}
