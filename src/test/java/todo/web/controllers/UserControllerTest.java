package todo.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import todo.authenticationjwt.JwtAuthenticationEntryPoint;
import todo.authenticationjwt.JwtAuthenticationProvider;
import todo.authenticationjwt.JwtAuthenticationTokenFilter;
import todo.authenticationjwt.JwtTokenService;
import todo.config.YAMLToDoConfig;
import todo.service.api.UserService;
import todo.service.dto.UserDto;
import todo.utils.UserSupplier;

import java.nio.charset.Charset;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @MockBean
    private UserService userService;
    @InjectMocks
    UserController userController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @MockBean
    private YAMLToDoConfig yamlToDoConfig;

    @MockBean
    private JwtTokenService jwtTokenService;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    private JwtAuthenticationTokenFilter authenticationTokenFilterBean = Mockito.mock(JwtAuthenticationTokenFilter.class);

    @Test
    public void getUserById() throws Exception {
        final UserDto user = UserSupplier.supplyUserDtoForInsertWithId();
        Mockito.doReturn(Optional.of(user))
                .when(userService).getUserById(user.getId());
        mockMvc.perform(MockMvcRequestBuilders.get("/users/user/{id}", user.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"firstName\":\"Fotache\",\"lastName\":\"Delia\",\"alias\":\"deliuta\"}"))
        ;
    }

    @Test
    public void getUserByAlias() throws Exception {
        Mockito.doReturn(Optional.of(UserSupplier.supplyUserDtoForInsertWithId()))
                .when(userService).getUserByAlias("deliuta");
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{alias}", "deliuta"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"firstName\":\"Fotache\",\"lastName\":\"Delia\",\"alias\":\"deliuta\"}"))
        ;
    }

    @Test
    public void getUserByName() throws Exception {
        Mockito.doReturn(Optional.of(UserSupplier.supplyUserDtoListForInsertWithId()))
                .when(userService).getUsersByName("Fotache", "Delia");
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{firstName}/{lastName}", "Fotache", "Delia"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"firstName\":\"Fotache\",\"lastName\":\"Delia\",\"alias\":\"deliuta\"}]"))
        ;
    }

    @Test
    public void findByAliasAndPassword() throws Exception {
        Mockito.doReturn(Optional.of(UserSupplier.supplyUserDtoForInsertWithId()))
                .when(userService).findByAliasAndPassword("deliuta", "deliuta");
        mockMvc.perform(MockMvcRequestBuilders.get("/users/authenticate/{alias}/{password}", "deliuta", "deliuta"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"firstName\":\"Fotache\",\"lastName\":\"Delia\",\"alias\":\"deliuta\"}"))
        ;
    }

    @Test
    public void getAllUsers() throws Exception {
        Mockito.doReturn(UserSupplier.supplyUserDtoPageForInsertWithId())
                .when(userService).getAllUsers();
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"content\":[{\"firstName\":\"Fotache\",\"lastName\":\"Delia\",\"alias\":\"deliuta\"}]}"))
        ;
    }

    // OBS: For insert, update, delete test methods to work it needs UserDto class to override equals and hashCode
    @Test
    public void addUser() throws Exception {
        final UserDto userWithId = UserSupplier.supplyUserDtoForInsertWithId();
        Mockito.doReturn(Optional.of(userWithId)).when(userService).insertUser(userWithId);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .contentType(APPLICATION_JSON_UTF8).content(asJsonString(userWithId)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"" + userWithId.getId() + "\",\"firstName\":\"Fotache\",\"lastName\":\"Delia\",\"alias\":\"deliuta\",\"password\":\"deliuta\"}"))
        ;
    }

    @Test
    public void updateUser() throws Exception {
        final UserDto userWithId = UserSupplier.supplyUserDtoForInsertWithId();
        Mockito.doReturn(Optional.of(userWithId)).when(userService).updateUser(userWithId.getId(), userWithId);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/users/{id}",userWithId.getId())
                        .contentType(APPLICATION_JSON_UTF8).content(asJsonString(userWithId)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"" + userWithId.getId() + "\",\"firstName\":\"Fotache\",\"lastName\":\"Delia\",\"alias\":\"deliuta\",\"password\":\"deliuta\"}"))
        ;
    }

    @Test
    public void deleteProject() throws Exception {
        final UserDto userWithId = UserSupplier.supplyUserDtoForInsertWithId();
        Mockito.doReturn(Optional.of(userWithId)).when(userService).updateUser(userWithId.getId(), userWithId);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/{id}",userWithId.getId())
                        .contentType(APPLICATION_JSON_UTF8).content(asJsonString(userWithId)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent())
        ;
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
