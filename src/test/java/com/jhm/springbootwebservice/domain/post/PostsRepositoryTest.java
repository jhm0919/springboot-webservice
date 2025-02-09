package com.jhm.springbootwebservice.domain.post;

import static org.assertj.core.api.Assertions.assertThat;

//@ExtendWith(SpringExtension.class)
//@SpringBootTest(properties = {
//    "spring.profiles.active=mysql"
//})
class PostsRepositoryTest {

//    @Autowired
//    PostsRepository postsRepository;

//    @AfterEach
//    public void cleanup() {
//        postsRepository.deleteAll();
//    }

//    @Test
//    public void 게시글저장_불러오기() {
//        //given
//        String title = "테스트 게시글";
//        String content = "테스트 본문";
//
//        postsRepository.save(Posts.builder()
//                .title(title)
//                .content(content)
//                .author("jhm0919@naver.com")
//                .build());
//
//        //when
//        List<Posts> postsList = postsRepository.findAll();
//
//        //then
//        Posts posts = postsList.get(0);
//        assertThat(posts.getTitle()).isEqualTo(title);
//        assertThat(posts.getContent()).isEqualTo(content);
//    }
//
//    @Test
//    public void BaseTimeEntity_등록() {
//        //given
//        LocalDateTime now = LocalDateTime.of(2024, 1, 16, 0, 0, 0);
//        postsRepository.save(Posts.builder()
//                .title("title")
//                .content("content")
//                .author("author")
//                .build());
//
//        //when
//        List<Posts> postsList = postsRepository.findAll();
//
//        //then
//        Posts posts = postsList.get(0);
//
//        System.out.println(">>>>>>>> createDate=" + posts.getCreatedDate() +
//            ", modifiedDate=" + posts.getModifiedDate());

//        assertThat(posts.getCreatedDate()).isAfter(now);
//        assertThat(posts.getModifiedDate()).isAfter(now);
//    }

//    @Transactional
//    @Test
//    void 데이터_조회() {
//        // given
//        Optional<Posts> result = postsRepository.findById(1L);
//        // when
//        Posts post = result.get();
//        // then
//        System.out.println(post);
//    }
}