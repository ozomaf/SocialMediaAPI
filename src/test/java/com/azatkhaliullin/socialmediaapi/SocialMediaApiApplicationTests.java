package com.azatkhaliullin.socialmediaapi;

import com.azatkhaliullin.socialmediaapi.dto.AuthData;
import com.azatkhaliullin.socialmediaapi.dto.PostData;
import com.azatkhaliullin.socialmediaapi.dto.Relationship;
import com.azatkhaliullin.socialmediaapi.dto.User;
import com.azatkhaliullin.socialmediaapi.repository.RelationshipRepository;
import com.azatkhaliullin.socialmediaapi.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SocialMediaApiApplicationTests {

    private static final String username = "test";
    private static final String password = "test";
    private static final String email = "test@test.com";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RelationshipRepository relationshipRepository;

    private static String userAuthToken;
    private static String friendAuthToken;
    private static User user;
    private static User friend;


    @Test
    @Order(0)
    void contextLoads() {
    }

    @Test
    @Order(10)
    public void signUpTest() throws java.lang.Exception {
        AuthData authData = new AuthData();
        authData.setUsername(username);
        authData.setPassword(password);
        authData.setEmail(email);
        MvcResult mvcResult = mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(authData)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andReturn();
        userAuthToken = mvcResult.getResponse().getContentAsString();
        Optional<User> optionalUser = userRepo.findByUsername(username);
        Assertions.assertTrue(optionalUser.isPresent());
        user = optionalUser.get();
    }

    @Test
    @Order(20)
    public void signUpTestSameUsername() throws java.lang.Exception {
        AuthData authData = new AuthData();
        authData.setUsername(username);
        authData.setPassword(password);
        authData.setEmail("test1@test.com");
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authData)))
                .andDo(print())
                .andExpect(status().is(409))
                .andReturn();
    }

    @Test
    @Order(30)
    public void signUpTestSameEmail() throws java.lang.Exception {
        AuthData authData = new AuthData();
        authData.setUsername("test1");
        authData.setPassword(password);
        authData.setEmail(email);
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authData)))
                .andDo(print())
                .andExpect(status().is(409))
                .andReturn();
    }

    @Test
    @Order(40)
    public void createPostUsingSignUpTokenTest() throws java.lang.Exception {
        PostData postDataIn = new PostData();
        postDataIn.setTitle("Test title");
        postDataIn.setText("Test text");
        MvcResult mvcResult = mockMvc.perform(
                        post("/api/post")
                                .header("Authorization", "Bearer " + userAuthToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(postDataIn)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        PostData postDataOut = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PostData.class);
        Assertions.assertEquals("Test title", postDataOut.getTitle());
        Assertions.assertEquals("Test text", postDataOut.getText());
        Assertions.assertNotNull(postDataOut.getId());
    }

    @Test
    @Order(50)
    public void createEmptyPost() throws java.lang.Exception {
        PostData postData = new PostData();
        mockMvc.perform(post("/api/post")
                        .header("Authorization", "Bearer " + userAuthToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postData)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Post must not be null or empty"));
    }

    @Test
    @Order(60)
    public void createPostUsingWrongAuthToken() throws java.lang.Exception {
        String wrongAuthToken = userAuthToken.substring(0, userAuthToken.length() - 1) + (userAuthToken.charAt(0) + 1);
        PostData postData = new PostData();
        postData.setTitle("Test title");
        mockMvc.perform(post("/api/post")
                        .header("Authorization", "Bearer " + wrongAuthToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postData)))
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    @Order(70)
    public void failedLoginTest() throws java.lang.Exception {
        AuthData wrongAuthData = new AuthData();
        wrongAuthData.setUsername(username);
        wrongAuthData.setPassword("wrong" + password);
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongAuthData)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    @Order(80)
    public void successLoginTest() throws java.lang.Exception {
        AuthData wrongAuthData = new AuthData();
        wrongAuthData.setUsername(username);
        wrongAuthData.setPassword(password);
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongAuthData)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andReturn();
        userAuthToken = mvcResult.getResponse().getContentAsString();
    }

    @Test
    @Order(90)
    public void subscribe() throws java.lang.Exception {
        AuthData authData = new AuthData();
        authData.setUsername("friend");
        authData.setPassword("friend");
        authData.setEmail("friend@test.com");
        friendAuthToken = mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authData)))
                .andReturn()
                .getResponse()
                .getContentAsString();
        Optional<User> optionalFriend = userRepo.findByUsername("friend");
        Assertions.assertTrue(optionalFriend.isPresent());
        friend = optionalFriend.get();

        mockMvc.perform(post("/api/user/subscribe/{targetUserId}", friend.getId())
                        .header("Authorization", "Bearer " + userAuthToken))
                .andDo(print())
                .andExpect(status().isOk());

        Optional<Relationship> optionalRelationship = relationshipRepository.findByWhoAndToWhom(user, friend);

        Assertions.assertTrue(optionalRelationship.isPresent());
        Assertions.assertEquals(user, optionalRelationship.get().getWho());
        Assertions.assertEquals(friend, optionalRelationship.get().getToWhom());
        Assertions.assertEquals(Relationship.Status.SUBSCRIBER, optionalRelationship.get().getStatus());
        Assertions.assertFalse(optionalRelationship.get().isRequestDone());
    }

    @Test
    @Order(100)
    public void submitFriendRequest() throws java.lang.Exception {

        mockMvc.perform(put("/api/user/subscribe/{targetUserId}", user.getId())
                        .param("isAccepted", "true")
                        .header("Authorization", "Bearer " + friendAuthToken))
                .andDo(print())
                .andExpect(status().isOk());

        Optional<Relationship> optionalRelationship = relationshipRepository.findByWhoAndToWhom(user, friend);
        Assertions.assertTrue(optionalRelationship.isPresent());
        Assertions.assertEquals(user, optionalRelationship.get().getWho());
        Assertions.assertEquals(friend, optionalRelationship.get().getToWhom());
        Assertions.assertEquals(Relationship.Status.FRIEND, optionalRelationship.get().getStatus());
        Assertions.assertTrue(optionalRelationship.get().isRequestDone());
    }

    @Test
    @Order(110)
    public void getAllPostOfSubscribersWhileFriend() throws java.lang.Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/post/subscriptions")
                        .header("Authorization", "Bearer " + friendAuthToken)
                        .param("pageNumber", "0")
                        .param("pageSize", "5"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        List<PostData> posts = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });
        Assertions.assertFalse(posts.isEmpty());
        PostData postData = posts.get(0);
        Assertions.assertEquals("Test title", postData.getTitle());
        Assertions.assertEquals("Test text", postData.getText());
    }

    @Test
    @Order(120)
    public void unsubscribe() throws java.lang.Exception {
        mockMvc.perform(delete("/api/user/subscribe/{targetUserId}", user.getId())
                        .header("Authorization", "Bearer " + friendAuthToken))
                .andDo(print())
                .andExpect(status().isOk());
        Optional<Relationship> optionalRelationship = relationshipRepository.findByWhoAndToWhom(user, friend);
        Assertions.assertTrue(optionalRelationship.isPresent());
        Assertions.assertEquals(user, optionalRelationship.get().getWho());
        Assertions.assertEquals(friend, optionalRelationship.get().getToWhom());
        Assertions.assertEquals(Relationship.Status.SUBSCRIBER, optionalRelationship.get().getStatus());
        Assertions.assertTrue(optionalRelationship.get().isRequestDone());
    }

    @Test
    @Order(130)
    public void getAllPostOfSubscribersAfterFriend() throws java.lang.Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/post/subscriptions")
                        .header("Authorization", "Bearer " + friendAuthToken)
                        .param("pageNumber", "0")
                        .param("pageSize", "5"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        List<PostData> posts = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });
        Assertions.assertTrue(posts.isEmpty());
    }

}
