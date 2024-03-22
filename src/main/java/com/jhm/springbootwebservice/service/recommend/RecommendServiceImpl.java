package com.jhm.springbootwebservice.service.recommend;

import com.jhm.springbootwebservice.domain.posts.Posts;
import com.jhm.springbootwebservice.domain.posts.PostsRepository;
import com.jhm.springbootwebservice.domain.recommend.Recommend;
import com.jhm.springbootwebservice.domain.recommend.RecommendPK;
import com.jhm.springbootwebservice.domain.recommend.RecommendRepository;
import com.jhm.springbootwebservice.domain.user.User;
import com.jhm.springbootwebservice.domain.user.UserRepository;
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
    @Transactional
    public RecommendResponseDto recommend(Long postId, Long userId) {
        Posts post = postsRepository.findById(postId).orElseThrow(() ->
            new IllegalArgumentException("해당 게시물이 없습니다."));
        User user = userRepository.findById(userId).orElseThrow(() ->
            new IllegalArgumentException("해당 사용자가 없습니다."));

        RecommendPK recommendPK = new RecommendPK(postId, userId);
        boolean isRecommend;

        if (recommendRepository.findById(recommendPK).isPresent()) {
            recommendRepository.deleteById(recommendPK);
            post.disRecommend();
            postsRepository.save(post);
            return new RecommendResponseDto(false, post.getRecommend());
        }

        recommendRepository.save(new Recommend(recommendPK, post, user));
        post.recommendCount();
        postsRepository.save(post);
        return new RecommendResponseDto(true, post.getRecommend());
    }

    @Override
    @Transactional
    public Long cancel(Long id) {
        return null;
    }
}
