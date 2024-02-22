package com.jhm.springbootwebservice.service.posts;

import com.jhm.springbootwebservice.domain.posts.Posts;
import com.jhm.springbootwebservice.domain.posts.PostsRepository;
import com.jhm.springbootwebservice.web.dto.PostsListResponseDto;
import com.jhm.springbootwebservice.web.dto.PostsResponseDto;
import com.jhm.springbootwebservice.web.dto.PostsSaveRequestDto;
import com.jhm.springbootwebservice.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class PostsServiceImpl implements PostsService{

    private final PostsRepository postsRepository;

    @Override
    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Override
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }


//    @Override
//    @Transactional(readOnly = true) // 트랜잭션 범위는 유지, 조회만 가능하여 속도 개선
//    public List<PostsListResponseDto> findAllDesc() {
//        return postsRepository.findAllDesc().stream()
//            .map(PostsListResponseDto::new)
////                .map(posts -> new PostsListResponseDto(posts))
//                .collect(Collectors.toList());
//        // postsRepository 결과로 넘어온 Posts의 Stream을 map을 통해
//        // PostsListResponseDto 변환 -> List로 반환하는 메소드
//    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostsListResponseDto> findAll(Pageable pageable) {
        int page = pageable.getPageNumber() - 1; // page 위치에 있는 값은 0부터 시작한다.
        int pageLimit = 3; // 한페이지에 보여줄 글 개수

        // 한 페이지당 3개식 글을 보여주고 정렬 기준은 ID기준으로 내림차순
        Page<Posts> postsPages = postsRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Direction.DESC, "id")));

        // 목록 : id, title, content, author
        Page<PostsListResponseDto> postsListResponseDto = postsPages.map(
                postPage -> new PostsListResponseDto(postPage));
        return postsListResponseDto;
    }

//    @Transactional
//    public Boolean getNextCheck(Pageable pageable) {
//        Page<Posts> posts = postsRepository.findAll(pageable);
//        return posts.hasNext();
//    }

    @Override
    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        postsRepository.delete(posts);
    }
}
