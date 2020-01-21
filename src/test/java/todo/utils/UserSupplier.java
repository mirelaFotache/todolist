package todo.utils;

import todo.repository.models.User;
import todo.service.dto.UserDto;

import java.util.UUID;

public class UserSupplier {

    public static User supplyUserForInsert() {
        User user = new User();
        user.setAlias("mifo");
        user.setFirstName("Fotache");
        user.setLastname("Mirela");
        return user;
    }
    public static User supplyUserForInsertWithId() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setAlias("mifo");
        user.setFirstName("Fotache");
        user.setLastname("Mirela");
        return user;
    }
    public static UserDto supplyUserDtoForInsert() {
        UserDto user = new UserDto();
        user.setAlias("mifo");
        user.setFirstName("Fotache");
        user.setLastName("Mirela");
        return user;
    }

    public static User supplyUserForInsert2() {
        User user = new User();
        user.setAlias("delia");
        user.setFirstName("Fotache");
        user.setLastname("Delia");
        return user;
    }

    public static UserDto supplyUserDto2ForInsert() {
        UserDto user = new UserDto();
        user.setAlias("ralu");
        user.setFirstName("Cristian");
        user.setLastName("Raluca");
        return user;
    }
    public static UserDto supplyUserDtoForInsertWithId() {
        UserDto user = new UserDto();
        user.setId(UUID.randomUUID().toString());
        user.setAlias("deliuta");
        user.setFirstName("Fotache");
        user.setLastName("Delia");
        return user;
    }

    public static UserDto supplyUserDtoForUpdate() {
        UserDto user = new UserDto();
        user.setAlias("mari");
        user.setFirstName("Enescu");
        user.setLastName("Maria");
        return user;
    }

    public static UserDto supplyUserDtoForDelete() {
        UserDto user = new UserDto();
        user.setAlias("robi");
        user.setFirstName("Marcu");
        user.setLastName("Robert");
        return user;
    }
}
