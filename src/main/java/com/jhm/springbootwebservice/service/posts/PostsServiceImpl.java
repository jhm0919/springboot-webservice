package com.jhm.springbootwebservice.service.posts;

import com.jhm.springbootwebservice.domain.posts.PostType;
import com.jhm.springbootwebservice.domain.posts.Posts;
import com.jhm.springbootwebservice.domain.posts.PostsRepository;
import com.jhm.springbootwebservice.domain.user.User;
import com.jhm.springbootwebservice.domain.user.UserRepository;
import com.jhm.springbootwebservice.web.dto.request.UserSearchDto;
import com.jhm.springbootwebservice.web.dto.response.PostsListResponseDto;
import com.jhm.springbootwebservice.web.dto.response.PostsResponseDto;
import com.jhm.springbootwebservice.web.dto.request.PostsSaveRequestDto;
import com.jhm.springbootwebservice.web.dto.request.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostsServiceImpl implements PostsService{

    private final PostsRepository postsRepository;
    private final UserRepository userRepository;
    private String tempLocation = "C:\\springboot-webservice\\src\\main\\resources\\static\\temp\\";
    private String localLocation = "C:\\springboot-webservice\\src\\main\\resources\\static\\files\\";

    @Override
    @Transactional
    public Long save(Long userId, PostsSaveRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
            new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        String content = moveImg(requestDto.getContent());// content를 넘겨서 이미지 경로 이동과 내용물 변경
        
        requestDto.setUser(user);
        requestDto.setContent(content);

        Posts posts = requestDto.toEntity();

        return postsRepository.save(posts).getId();
    }

    private String moveImg(String content) {

        Pattern nonValidPattern = Pattern
                .compile("(?i)< *[IMG][^\\>]*[src] *= *[\"\']{0,1}([^\"\'\\ >]*)");
        Matcher matcher = nonValidPattern.matcher(content);
        String img;
        while (matcher.find()) {
            img = matcher.group(1);
            log.info("asdf={}", img);
            img = img.replace("/temp", ""); // 이미지 이름만 추출
            content = content.replace("/temp", "/files"); // content에 있는 temp를 files로 교체
            File file = new File(tempLocation + img);
            file.renameTo(new File(localLocation + img));// 실제 저장 경로로 이동
        }
        return content;
    }

    @Override
    @Transactional
    public Long update(Long postId, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. postId=" + postId));

        String content = moveImg(requestDto.getContent());

        posts.update(requestDto.getTitle(), content, requestDto.getPostType()); // 게시글 수정

        // todo: 수정시 넘어온 이미지 제외 지우기?

        return postId;
    }

    @Override
    @Transactional
    public void updateView(Long id) {
        Posts posts = postsRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        posts.increaseView(); // 조회수 증가
//        postsRepository.save(posts);
    }

    @Override
    @Transactional(readOnly = true)
    public PostsResponseDto findById(Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        return new PostsResponseDto(posts);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostsListResponseDto> findAll(PostType postType, UserSearchDto userSearchDto, int page, int myPost, Long userId) {
        PageRequest pageRequest = createPageRequest(page);
        return postsRepository.findPageDynamicQuery(postType, userSearchDto, pageRequest, myPost, userId);
    }

    private PageRequest createPageRequest(int page) {
        return PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        
        String content = posts.getContent();

        deleteImg(content); // 이미지 삭제
        
        postsRepository.delete(posts);
    }

    private void deleteImg(String content) {
        Pattern nonValidPattern = Pattern
                .compile("(?i)< *[IMG][^\\>]*[src] *= *[\"\']{0,1}([^\"\'\\ >]*)");
        Matcher matcher = nonValidPattern.matcher(content);
        String img;
        while (matcher.find()) {
            img = matcher.group(1);
            img = img.replace("/files", ""); // 이미지 이름만 추출
            File file = new File(localLocation + img);
            file.delete();
        }
    }
}
