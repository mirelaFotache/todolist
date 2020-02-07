package todo.web.controllers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import todo.UserApplication;
import todo.repository.UserRepository;
import todo.repository.models.User;
import todo.service.dto.UserDto;
import todo.utils.RestResponsePage;
import todo.utils.UserSupplier;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase
@ContextConfiguration(classes= UserApplication.class)
public class UserIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository = mock(UserRepository.class);

    private static boolean setUpIsDone = false;

    //Token is generated with auth server and expires in the year 2400
    private static final String TOKEN = "eyJhbGciOiJSUzUxMiJ9.eyJhdXRoIjpbeyJhdXRob3JpdHkiOiJST0xFX0FETUlOIn1dLCJzdWIiOiJhZG1pbiIsImlhdCI6MTU4MDEyODc4MSwiZXhwIjoxMzYwMDk5NDQwMH0.wwzmLffh0Nh3lp5AO4K43uxvXaUtkh92z_d2cEY0io_N_t34Uiosd4JWEANiQMqAHBQmRjRrbzZeIJIL_cVU8wB72bu_ybtj0-HkL5qVf3N_JNzlo_WOJHReQPJegCm7Be3Sk5kb8VNUDsD7rk562gz7M_AQcf3iPjuDgb-M2KE";

    /*@Before
    public void setUp() {
        if (setUpIsDone) {
            return;
        }
        // do the setup
        setUpIsDone = true;

        User defaultUser = new User();
        defaultUser.setAlias("mifo");
        defaultUser.setFirstName("Fotache");
        defaultUser.setLastname("Mirela");
*//*        Set<Project> projects = new HashSet<>();
        Project project = new Project();
        project.setLabel("project");
        projects.add(project);
        defaultUser.setProjects(projects);*//*
        userRepository.save(defaultUser);

        final String token = "my.mocked.token";
        final Class<Object> type = Object.class;
    }

    @Test
    public void getUserByAlias() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setBearerAuth(TOKEN);

        HttpEntity<Object> getRequest = new HttpEntity<>(null, requestHeaders);
        ResponseEntity<UserDto> responseEntity = restTemplate
                .exchange(
                        "http://localhost:" + port + "/todolist/users/{alias}",
                        HttpMethod.GET,
                        getRequest,
                        UserDto.class,
                        "mifo");

        Assert.assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        UserDto user = responseEntity.getBody();
        Assert.assertNotNull(user);
        Assert.assertEquals("mifo", user.getAlias());
    }

    @Test
    public void getUserById() {
        UserDto userToBePersisted = UserSupplier.supplyUserDtoForInsertWithId();

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setBearerAuth(TOKEN);

        //Insert entity
        HttpEntity<UserDto> request = new HttpEntity<>(userToBePersisted, requestHeaders);
        ResponseEntity<UserDto> responseEntityInsert =
                restTemplate
                        .postForEntity("http://localhost:" + port + "/todolist/users", request, UserDto.class);
        Assert.assertEquals(HttpStatus.OK, responseEntityInsert.getStatusCode());

        //Get entity
        HttpEntity<Object> getRequest = new HttpEntity<>(null, requestHeaders);
        ResponseEntity<UserDto> responseEntity = restTemplate
                .exchange(
                        "http://localhost:" + port + "/todolist/users/user/{id}",
                        HttpMethod.GET,
                        getRequest,
                        UserDto.class,
                        responseEntityInsert.getBody().getId());

        Assert.assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        UserDto user = responseEntity.getBody();
        Assert.assertNotNull(user);
        Assert.assertEquals(responseEntityInsert.getBody().getId(), user.getId());
    }

    @Test
    public void getUserByName() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setBearerAuth(TOKEN);
        HttpEntity<Object> request = new HttpEntity<>(null, requestHeaders);

        final ResponseEntity<List<UserDto>> responseEntity = restTemplate
                .exchange(
                        "http://localhost:" + port + "/todolist/users/Fotache/Mirela",
                        HttpMethod.GET,
                        request,
                        new ParameterizedTypeReference<List<UserDto>>() {
                        });
        List<UserDto> users = responseEntity.getBody();
        Assert.assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        Assert.assertNotNull(users);
        Assert.assertEquals("mifo", users.get(0).getAlias());
    }

    @Test
    public void getAllUsers() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setBearerAuth(TOKEN);
        HttpEntity<Object> request = new HttpEntity<>(null, requestHeaders);

        final ResponseEntity<RestResponsePage<UserDto>> responseEntity = restTemplate
                .exchange(
                        "http://localhost:" + port + "/todolist/users/",
                        HttpMethod.GET,
                        request,
                        new ParameterizedTypeReference<RestResponsePage<UserDto>>() {
                        });
        RestResponsePage<UserDto> body = responseEntity.getBody();

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertNotNull(body);
        Assert.assertTrue(body.getContent().size() > 0);
        Assert.assertEquals("mifo", body.getContent().get(0).getAlias());
    }


    @Test
    public void insertUser() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setBearerAuth(TOKEN);

        UserDto userToBePersisted = UserSupplier.supplyUserDto2ForInsert();
*//*        Set<ProjectDto> projects = new HashSet<>();
        ProjectDto project = new ProjectDto();
        project.setLabel("project");
        projects.add(project);
        userToBePersisted.setProjects(projects);*//*

        //Insert user
        HttpEntity<UserDto> request = new HttpEntity<>(userToBePersisted, requestHeaders);
        ResponseEntity<UserDto> responseEntity =
                restTemplate
                        .postForEntity("http://localhost:" + port + "/todolist/users", request, UserDto.class);

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        UserDto user = responseEntity.getBody();
        Assert.assertNotNull(user);
        Assert.assertEquals("ralu", user.getAlias());
    }

    @Test
    public void updateUser() {
        UserDto userToBePersisted = UserSupplier.supplyUserDtoForUpdate();

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setBearerAuth(TOKEN);

        //Insert entity
        HttpEntity<UserDto> request = new HttpEntity<>(userToBePersisted, requestHeaders);
        ResponseEntity<UserDto> userToBeUpdated =
                restTemplate
                        .postForEntity("http://localhost:" + port + "/todolist/users", request, UserDto.class);

        //Update entity
        HttpEntity<UserDto> requestEntity = new HttpEntity<>(userToBeUpdated.getBody(), requestHeaders);
        ResponseEntity<UserDto> responseEntity =
                restTemplate
                        .exchange("http://localhost:" + port + "/todolist/users/" + userToBeUpdated.getBody().getId(),
                                HttpMethod.PUT, requestEntity, UserDto.class);
        UserDto user = responseEntity.getBody();

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertNotNull(user);
        Assert.assertEquals("mari", user.getAlias());
    }

    @Test
    public void deleteUser() {
        UserDto userToBeDeleted = UserSupplier.supplyUserDtoForDelete();

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setBearerAuth(TOKEN);

        //Insert user
        HttpEntity<UserDto> request = new HttpEntity<>(userToBeDeleted, requestHeaders);
        ResponseEntity<UserDto> persistedUser =
                restTemplate
                        .postForEntity("http://localhost:" + port + "/todolist/users", request, UserDto.class);

        //Delete user
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDto> requestEntity = new HttpEntity<UserDto>(persistedUser.getBody(), requestHeaders);
        ResponseEntity<Boolean> responseEntity =
                restTemplate
                        .exchange("http://localhost:" + port + "/todolist/users/" + persistedUser.getBody().getId(), HttpMethod.DELETE, requestEntity, Boolean.class);

        Assert.assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }*/
}
