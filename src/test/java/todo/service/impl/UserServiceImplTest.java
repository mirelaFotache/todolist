package todo.service.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import todo.repository.ProjectRepository;
import todo.repository.UserRepository;
import todo.repository.models.Project;
import todo.repository.models.User;
import todo.service.dto.UserAdapter;
import todo.service.dto.UserDto;
import todo.service.exceptions.InvalidParameterException;
import todo.utils.ProjectSupplier;
import todo.utils.UserSupplier;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {UserRepository.class, ProjectRepository.class})
public class UserServiceImplTest {

    @Autowired
    private ProjectRepository projectRepository = Mockito.mock(ProjectRepository.class);

    private UserServiceImpl userService = new UserServiceImpl(Mockito.mock(UserRepository.class));
    private UserRepository userRepository = userService.getUserRepository();
    private List<User> users = new ArrayList<>();

    @Test
    public void getUserByAlias() {
        final User suppliedUser = UserSupplier.supplyUserForInsert();
        users.add(suppliedUser);
        Mockito.doReturn(suppliedUser).when(userRepository).getUserByAlias("mifo");

        Optional<UserDto> foundUser = userService.getUserByAlias("mira");
        Assert.assertNotNull(suppliedUser);
        Assert.assertEquals("mifo", suppliedUser.getAlias());
    }

    @Test
    public void getUsersByName() {
        final User suppliedUser = UserSupplier.supplyUserForInsert();
        List<User> users = new ArrayList<>();
        users.add(suppliedUser);
        Mockito.doReturn(users).when(userRepository).getUsersByName("Fotache", "Mirela");

        Optional<List<UserDto>> foundUsers = userService.getUsersByName("Fotache", "Mirela");
        Assert.assertNotNull(users);
        UserDto foundUser = foundUsers.get().get(0);
        Assert.assertNotNull(foundUser);
        Assert.assertEquals("Fotache", foundUser.getFirstName());
        Assert.assertEquals("Mirela", foundUser.getLastName());
    }

    @Test
    public void getAllUsers() {
        final User suppliedUser = UserSupplier.supplyUserForInsert();
        List<User> users = new ArrayList<>();
        users.add(suppliedUser);
        Mockito.doReturn(users).when(userRepository).findAll();

        Page<UserDto> foundUsers = userService.getAllUsers();
        Assert.assertNotNull(users);
        UserDto foundUser = foundUsers.stream().findAny().get();
        Assert.assertNotNull(foundUser);
        Assert.assertEquals("Fotache", foundUser.getFirstName());
        Assert.assertEquals("Mirela", foundUser.getLastName());
    }

    @Test
    public void insertUser() {
        User suppliedUser = UserSupplier.supplyUserForInsert();
        UserDto suppliedUserDto = UserSupplier.supplyUserDtoForInsert();
        Mockito.doReturn(null).when(userRepository).getUserByAlias(suppliedUserDto.getAlias());
        when(userRepository.save(any(User.class))).thenReturn(suppliedUser);

        Optional<UserDto> persistedUserDTO = userService.insertUser(suppliedUserDto);
        Assert.assertNotNull(persistedUserDTO);
        assertEquals("Fotache", persistedUserDTO.get().getFirstName());
    }

    @Test(expected = InvalidParameterException.class)
    public void insertUserDuplicateAlias() {
        User suppliedUser = UserSupplier.supplyUserForInsert();
        UserDto suppliedUserDto = UserSupplier.supplyUserDtoForInsert();
        Mockito.doReturn(suppliedUser).when(userRepository).getUserByAlias(suppliedUserDto.getAlias());
        when(userRepository.save(any(User.class))).thenReturn(suppliedUser);

        Optional<UserDto> persistedUserDTO = userService.insertUser(suppliedUserDto);
    }

    @Test
    public void updateUser() {
        User suppliedUser = UserSupplier.supplyUserForInsert();
        UUID id = UUID.randomUUID();
        suppliedUser.setId(id);
        Optional<User> user = Optional.ofNullable(suppliedUser);
        Mockito.doReturn(user).when(userRepository).findById(id);
        UserDto suppliedUserDto = UserSupplier.supplyUserDtoForInsert();
        Mockito.doReturn(null).when(userRepository).getUserByAlias(suppliedUserDto.getAlias());
        when(userRepository.save(any(User.class))).thenReturn(suppliedUser);

        Optional<UserDto> persistedUserDTO = userService.updateUser(id.toString(), suppliedUserDto);
        Assert.assertNotNull(persistedUserDTO);
        assertEquals("Fotache", persistedUserDTO.get().getFirstName());
    }

    @Test(expected = InvalidParameterException.class)
    public void updateUserIdInvalid() {
        User suppliedUser = UserSupplier.supplyUserForInsert();
        UUID id = UUID.randomUUID();
        suppliedUser.setId(id);
        Mockito.doReturn(Optional.empty()).when(userRepository).findById(id);
        UserDto suppliedUserDto = UserSupplier.supplyUserDtoForInsert();
        when(userRepository.save(any(User.class))).thenReturn(suppliedUser);

        userService.updateUser(id.toString(), suppliedUserDto);
    }

    @Test(expected = InvalidParameterException.class)
    public void updateUserDuplicateAlias() {
        User suppliedUser = UserSupplier.supplyUserForInsert();
        UUID id = UUID.randomUUID();
        suppliedUser.setId(id);
        Mockito.doReturn(Optional.ofNullable(suppliedUser)).when(userRepository).findById(id);
        UserDto suppliedUserDto = UserSupplier.supplyUserDtoForInsert();
        Mockito.doReturn(suppliedUser).when(userRepository).getUserByAlias(suppliedUserDto.getAlias());
        when(userRepository.save(any(User.class))).thenReturn(suppliedUser);

        userService.updateUser(id.toString(), suppliedUserDto);
    }

    @Test(expected = InvalidParameterException.class)
    public void updateUserIdNull() {
        UserDto suppliedUserDto = UserSupplier.supplyUserDtoForInsert();
        Optional<UserDto> user = userService.updateUser(null, suppliedUserDto);
    }

    @Test
    public void deleteUser() {
        User suppliedUser = UserSupplier.supplyUserForInsert();
        UUID id = UUID.randomUUID();
        Optional<User> user = Optional.ofNullable(suppliedUser);
        Mockito.doReturn(user).when(userRepository).findById(id);
        boolean done = userService.deleteUser(id.toString());
        assertEquals(true, done);
    }

    @Test
    public void deleteUserException() {
        boolean done = userService.deleteUser(null);
        assertFalse(done);
    }

}
