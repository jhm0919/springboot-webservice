package com.jhm.springbootwebservice;

import com.jhm.springbootwebservice.domain.post.Post;
import com.jhm.springbootwebservice.domain.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
public class TestDataInit {

    private final PostRepository postRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        postRepository.save(com.jhm.springbootwebservice.domain.post.Post.builder()
                .title("글1")
                .content("가나다라마바사")
                .author("김부각")
                .build());

        postRepository.save(Post.builder()
                .title("글2")
                .content("zxcbzxcv")
                .author("맛김치")
                .build());

        postRepository.save(com.jhm.springbootwebservice.domain.post.Post.builder()
                .title("글3")
                .content("qwetyhgbz")
                .author("쫄면")
                .build());

        postRepository.save(com.jhm.springbootwebservice.domain.post.Post.builder()
                .title("글4")
                .content("asdfasdfa")
                .author("춤추는네오")
                .build());

        postRepository.save(com.jhm.springbootwebservice.domain.post.Post.builder()
                .title("글5")
                .content("fasdfas")
                .author("배개에 파묻힌 프로도")
                .build());
    }
}
