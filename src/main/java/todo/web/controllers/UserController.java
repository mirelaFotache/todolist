package todo.web.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import todo.service.api.UserService;
import todo.service.dto.UserDto;
import todo.service.exceptions.InvalidParameterException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    public static final String EXCEPTION_EMPTY_OBJECT_RETURNED = "exception.empty.object.returned";
    private UserService userService;

    public UserController(){}
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") String id) {
        return ResponseEntity.ok(userService.getUserById(id).orElseThrow(RuntimeException::new));
    }

    @GetMapping(value = "/{firstName}/{lastName}")
    public ResponseEntity<List<UserDto>> getUserByName(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName) {
        return ResponseEntity.ok(userService.getUsersByName(firstName, lastName).orElseThrow(RuntimeException::new));
    }

    @GetMapping(value = "/{alias}")
    public ResponseEntity<UserDto> getUserByAlias(@PathVariable("alias") String alias) {
        final UserDto body = userService.getUserByAlias(alias).orElseThrow(RuntimeException::new);
        return ResponseEntity.ok(body);
    }

    @GetMapping(value = "/authenticate/{alias}/{password}")
    public ResponseEntity<UserDto> findByAliasAndPassword(@PathVariable("alias") String alias,@PathVariable("password") String password) {
        return ResponseEntity.ok(userService.findByAliasAndPassword(alias,password).orElseThrow(RuntimeException::new));
    }

    @GetMapping
    public ResponseEntity<Page<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody @Valid UserDto userDto) {
        final UserDto user = userService.insertUser(userDto).orElseThrow(RuntimeException::new);
        return ResponseEntity.ok(user);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String id, @RequestBody @Valid UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto).orElseThrow(RuntimeException::new));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
