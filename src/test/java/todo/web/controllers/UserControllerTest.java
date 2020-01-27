package todo.web.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;
import todo.authenticationjwt.JwtAuthenticationEntryPoint;
import todo.authenticationjwt.JwtAuthenticationProvider;
import todo.authenticationjwt.JwtAuthenticationTokenFilter;
import todo.authenticationjwt.JwtTokenService;
import todo.config.YAMLToDoConfig;
import todo.service.api.UserService;
import todo.service.dto.UserDto;
import todo.utils.UserSupplier;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @MockBean
    private UserService userService;

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
                .when(userService).getUsersByName("Fotache","Delia");
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{firstName}/{lastName}", "Fotache","Delia"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"firstName\":\"Fotache\",\"lastName\":\"Delia\",\"alias\":\"deliuta\"}]"))
        ;
    }

    @Test
    public void findByAliasAndPassword() throws Exception {
        Mockito.doReturn(Optional.of(UserSupplier.supplyUserDtoForInsertWithId()))
                .when(userService).findByAliasAndPassword("deliuta","deliuta");
        mockMvc.perform(MockMvcRequestBuilders.get("/users/authenticate/{alias}/{password}", "deliuta","deliuta"))
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

}
