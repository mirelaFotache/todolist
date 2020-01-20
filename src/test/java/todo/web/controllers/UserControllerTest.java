package todo.web.controllers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import todo.repository.ProjectRepository;
import todo.repository.UserRepository;
import todo.repository.models.Project;
import todo.repository.models.User;
import todo.service.dto.UserDto;
import todo.utils.RestResponsePage;
import todo.utils.UserSupplier;

import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase
public class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProjectRepository projectRepository = Mockito.mock(ProjectRepository.class);

    @Autowired
    private UserRepository userRepository = Mockito.mock(UserRepository.class);

    private static boolean setUpIsDone = false;


    @Before
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
        Set<Project> projects = new HashSet<>();
        Project project = new Project();
        project.setLabel("project");
        projectRepository.save(project);
        projects.add(project);
        defaultUser.setProjects(projects);
        userRepository.save(defaultUser);
    }

    @Test
    public void getUserByAlias() {
       /* ResponseEntity<String> responseEntity = restTemplate.withBasicAuth("adminUser", "s3cret").getForEntity(
                "http://localhost:8888/bookstore/users/name?searchBy={name}", String.class, "mira");*/
        ResponseEntity<UserDto> responseEntity = restTemplate.getForEntity(
                "http://localhost:" + port + "/todolist/users/{alias}", UserDto.class, "mifo");

        Assert.assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        UserDto user = responseEntity.getBody();
        Assert.assertNotNull(user);
        Assert.assertEquals("mifo", user.getAlias());
    }

    @Test
    public void getUserByName() {
        final ResponseEntity<List<UserDto>> responseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/todolist/users/Fotache/Mirela",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UserDto>>() {
                });
        List<UserDto> users = responseEntity.getBody();
        Assert.assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        Assert.assertNotNull(users);
        Assert.assertEquals("mifo", users.get(0).getAlias());
    }

    @Test
    public void getAllUsers() {
        final ResponseEntity<RestResponsePage<UserDto>> responseEntity = restTemplate.exchange(
                "http://localhost:"+port+"/todolist/users/",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<RestResponsePage<UserDto>>() {
                });
        RestResponsePage<UserDto> body = responseEntity.getBody();

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertNotNull(body);
        Assert.assertTrue(body.getContent().size()>0);
        Assert.assertEquals("mifo",body.getContent().get(0).getAlias());
    }

    @Test
    public void insertUser() {
        UserDto userToBePersisted = UserSupplier.supplyUserDto2ForInsert();
        ResponseEntity<UserDto> responseEntity =
                restTemplate.postForEntity("http://localhost:" + port + "/todolist/users", userToBePersisted, UserDto.class);

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        UserDto user = responseEntity.getBody();
        Assert.assertNotNull(user);
        Assert.assertEquals("ralu", user.getAlias());
    }

    @Test
    public void updateUser() {
        UserDto userToBePersisted = UserSupplier.supplyUserDtoForUpdate();
        ResponseEntity<UserDto> userToBeUpdated =
                restTemplate.postForEntity("http://localhost:" + port + "/todolist/users", userToBePersisted, UserDto.class);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDto> requestEntity = new HttpEntity<UserDto>(userToBeUpdated.getBody(), requestHeaders);
        ResponseEntity<UserDto> responseEntity =
                restTemplate.exchange("http://localhost:" + port + "/todolist/users/"+ userToBeUpdated.getBody().getId(), HttpMethod.PUT, requestEntity, UserDto.class);
        UserDto user = responseEntity.getBody();

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertNotNull(user);
        Assert.assertEquals("mari", user.getAlias());
    }

    @Test
    public void deleteUser() {
        UserDto userToBeDeleted = UserSupplier.supplyUserDtoForDelete();
        ResponseEntity<UserDto> persistedUser =
                restTemplate.postForEntity("http://localhost:" + port + "/todolist/users", userToBeDeleted, UserDto.class);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDto> requestEntity = new HttpEntity<UserDto>(persistedUser.getBody(), requestHeaders);
        ResponseEntity<Boolean> responseEntity =
                restTemplate.exchange("http://localhost:" + port + "/todolist/users/"+persistedUser.getBody().getId(), HttpMethod.DELETE, requestEntity, Boolean.class);

        Assert.assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }
}
