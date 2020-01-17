package todo.web.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import todo.service.api.UserService;
import todo.service.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/{firstName}/{lastName}")
    public ResponseEntity<List<UserDto>> getUserByName(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName) {
        return ResponseEntity.ok(userService.getUsersByName(firstName, lastName).get());
    }

    @GetMapping(value = "/{alias}")
    public ResponseEntity<UserDto> getUserByAlias(@PathVariable("alias") String alias) {
        return ResponseEntity.ok(userService.getUserByAlias(alias).get());
    }

    @GetMapping
    public ResponseEntity<Page<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping
    public ResponseEntity<UserDto> addUser(@RequestBody @Valid UserDto userDto) {
        return ResponseEntity.ok(userService.insertUser(userDto).get());
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String id, @RequestBody @Valid UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto).get());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteProject(@PathVariable("id") String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
