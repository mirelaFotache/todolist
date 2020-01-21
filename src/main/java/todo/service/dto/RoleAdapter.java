package todo.service.dto;


import todo.repository.models.Project;
import todo.repository.models.Role;

import java.util.HashSet;
import java.util.Set;

public class RoleAdapter {

    public static RoleDto toDto(Role role) {
        RoleDto dto = new RoleDto();
        dto.setId(role.getId());
        dto.setName(role.getName());
        return dto;
    }

    public static Role fromDto(RoleDto dto) {
        Role role = new Role();
        role.setId(dto.getId());
        role.setName(dto.getName());
        return role;
    }

    public static Set<RoleDto> toDtoSet(Set<Role> roles) {
        Set<RoleDto> dtos = new HashSet<>();
        if (roles!=null && !roles.isEmpty()) {
            for (Role o : roles) {
                dtos.add(toDto(o));
            }
        }
        return dtos;
    }

    public static Set<Role> fromDtoSet(Set<RoleDto> projectsDto) {
        Set<Role> roles = new HashSet<>();
        if (projectsDto != null && !projectsDto.isEmpty()) {
            for (RoleDto o : projectsDto) {
                roles.add(fromDto(o));
            }
        }
        return roles;
    }
}
