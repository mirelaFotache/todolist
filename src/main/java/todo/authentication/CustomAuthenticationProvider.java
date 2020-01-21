package todo.authentication;

import org.springframework.beans.InvalidPropertyException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import todo.service.api.UserService;
import todo.service.dto.RoleDto;
import todo.service.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private UserService userService;
    private MessageSource messageSource;

    public CustomAuthenticationProvider(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws InvalidPropertyException {
        String alias = authentication.getName();
        String password = authentication.getCredentials().toString();

        // Use the credentials and authenticate against the third-party system
        UserDto user = userService.findByAliasAndPassword(alias, password).orElseThrow(() ->
                new BadCredentialsException(messageSource.getMessage("error.authentication.failed", null, LocaleContextHolder.getLocale())));

        if (user != null) {
            if (user.getAlias().equals(alias) && user.getPassword().equals(password)) {
                if (user.getActive()) {
                    List<GrantedAuthority> persistedRoles = new ArrayList<>();
                    if (user.getRoles() != null && user.getRoles().size() > 0) {
                        for (RoleDto role : user.getRoles())
                            persistedRoles.add(new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase()));
                    }
                    return new UsernamePasswordAuthenticationToken(alias, password, persistedRoles);
                } else {
                    throw new
                            BadCredentialsException(messageSource.getMessage("error.user.not.active", null, LocaleContextHolder.getLocale()));
                }
            } else {
                throw new
                        BadCredentialsException(messageSource.getMessage("error.authentication.failed", null, LocaleContextHolder.getLocale()));
            }
        }
        throw new
                BadCredentialsException(messageSource.getMessage("error.authentication.failed", null, LocaleContextHolder.getLocale()));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
