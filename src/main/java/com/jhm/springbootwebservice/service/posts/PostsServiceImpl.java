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
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostsServiceImpl implements PostsService{

    private final PostsRepository postsRepository;
    private final UserRepository userRepository;

    @Value("${imageUrl.tempLocation}")
    private String tempLocation;

    @Value("${imageUrl.localLocation}")
    private String localLocation;

    private static Matcher getMatcher(String content) {
        Pattern pattern  = Pattern
                .compile("(?i)<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>");
        Matcher matcher = pattern .matcher(content); // 이미지 태그만 추출함
        return matcher;
    }

    private static String getPureContent(String content) {
        Document document = Jsoup.parse(content);
        Element body = document.body();
        String pureContent = body.text();
        return pureContent;
    }

    @Override
    @Transactional
    public Long save(Long userId, PostsSaveRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
            new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        requestDto.setUser(user);
        Posts post = requestDto.toEntity();

        Long postId = postsRepository.save(post).getId();// 일단 저장

        makeDir(postId); // 해당 게시글 이미지 저장할 폴더 생성

        String content = getSaveContent(requestDto.getContent(), postId); // content를 넘겨서 이미지 경로 이동과 내용물 변경
        String pureContent = getPureContent(content); // 작성한 글만 추출

        post.update(content, pureContent); // 작업된 content와 순수 글 업데이트

        return postId;
    }

    private void makeDir(Long postId) {
        File folder = new File(localLocation + postId); // 이미지 저장 경로

        if (!folder.exists()) { // 해당 폴더가 없으면
            boolean created = folder.mkdirs(); // 폴더 생성
            if (!created) {
                throw new IllegalStateException("폴더를 생성할 수 없습니다.");
            }
        }
    }

    private String getSaveContent(String content, Long postId) {

        Matcher matcher = getMatcher(content);

        while (matcher.find()) {
            String img = matcher.group(1).replace("/temp/", "");
            content = moveImgAndGetContent(content, postId, img);
        }

        return content;
    }

    @Override
    @Transactional
    public Long update(Long postId, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. postId=" + postId));

        String content = getUpdateContent(requestDto.getContent(), postId); // update할 content get
        String pureContent = getPureContent(content); // content에서 사진 제외
        posts.update(requestDto.getTitle(), content, pureContent, requestDto.getPostType()); // 게시글 수정

        return postId;
    }

    private String getUpdateContent(String content, Long postId) {

        Matcher matcher = getMatcher(content);

        List<String> useImgList = new ArrayList<>(); // 사용할 이미지들

        while (matcher.find()) {
            String img = matcher.group(1).replace("/temp/", "");

            String[] splitSrc = img.split("/");
            String imgName = splitSrc[splitSrc.length - 1]; // 파일명 추출
            useImgList.add(imgName);

            content = moveImgAndGetContent(content, postId, img);
        }

        updateImages(postId, useImgList);

        return content;
    }

    private void updateImages(Long postId, List<String> useImgList) {
        File folder = new File(localLocation + postId);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!useImgList.contains(file.getName())) { // 사용할 이미지에 없는 파일은 삭제
                    file.delete();
                }
            }
        }
    }

    private String moveImgAndGetContent(String content, Long postId, String img) {
        content = content.replace("/temp", "/files/" + postId); // content에 있는 /temp를 /files/postId로 교체
        File file = new File(tempLocation + img);
        file.renameTo(new File(localLocation + postId + "\\" + img));// 실제 저장 경로로 이동
        return content;
    }

    @Override
    @Transactional
    public void updateView(Long id) {
        Posts posts = postsRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        posts.increaseView(); // 조회수 증가
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
    public void delete(Long id) throws IOException {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        deleteImg(id); // 이미지 저장소 삭제
        postsRepository.delete(posts);
    }

    private void deleteImg(Long id) throws IOException {
        File file = new File(localLocation + id);
        FileUtils.cleanDirectory(file);
        file.delete();
    }
}
