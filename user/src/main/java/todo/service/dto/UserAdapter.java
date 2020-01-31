package todo.service.dto;

import todo.repository.models.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserAdapter {

    public static UserDto toDto(User user) {
        UserDto dto = new UserDto();
        if (user != null) {
            if (user.getId() != null)
                dto.setId(user.getId().toString());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastname());
            dto.setAlias(user.getAlias());
            dto.setPassword(user.getPassword());
            dto.setActive(user.getActive());
/*            if (user.getProjects() != null)
                dto.setProjects(ProjectAdapter.toDtoSet(user.getProjects()));*/
            if(user.getRoles()!=null){
                dto.setRoles(RoleAdapter.toDtoSet(user.getRoles()));
            }
        }
        return dto;
    }

    public static User fromDto(UserDto dto) {
        User user = new User();
        if (dto != null) {
            user.setFirstName(dto.getFirstName());
            user.setLastname(dto.getLastName());
            user.setAlias(dto.getAlias());
            user.setPassword(dto.getPassword());
            if (dto.getActive() != null)
                user.setActive(dto.getActive());
            //user.setProjects(ProjectAdapter.fromDtoSet(dto.getProjects()));
            user.setRoles(RoleAdapter.fromDtoSet(dto.getRoles()));
        }
        return user;
    }

    public static void fromDtoToUser(UserDto dto, User user) {
        if (dto != null) {
            user.setFirstName(dto.getFirstName());
            user.setLastname(dto.getLastName());
            user.setAlias(dto.getAlias());
        }
    }

    public static Set<User> fromDtoSet(List<UserDto> dtos) {
        Set<User> users = new HashSet<>();
        if (dtos != null && !dtos.isEmpty()) {
            dtos.forEach(u -> users.add(UserAdapter.fromDto(u)));
        }
        return users;
    }

    public static List<UserDto> toDtoSet(List<User> users) {
        List<UserDto> dtos = new ArrayList<>();
        if (users != null && !users.isEmpty()) {
            users.forEach(u -> dtos.add(UserAdapter.toDto(u)));
        }
        return dtos;
    }

}
