package todo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import todo.authentication.CustomAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig
        extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider authProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/users/**", "/project/**", "/task/**", "/taskitems/**", "/language/**", "/contactdetails/**")
                .hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.POST, "/users/**", "/project/**", "/task/**", "/taskitems/**", "/language/**", "/contactdetails/**")
                .hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.PUT,  "/users/**", "/project/**", "/task/**", "/taskitems/**", "/language/**", "/contactdetails/**")
                .hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/users/**", "/project/**", "/task/**", "/taskitems/**", "/language/**", "/contactdetails/**")
                .hasAnyRole("ADMIN")

                .antMatchers(HttpMethod.POST, "/task/**", "/taskitems/**", "/language/**", "/contactdetails/**")
                .hasAnyRole( "USER")
                .antMatchers(HttpMethod.PUT,  "/task/**", "/taskitems/**", "/language/**", "/contactdetails/**")
                .hasAnyRole( "USER")
                .antMatchers(HttpMethod.DELETE, "/task/**", "/taskitems/**", "/language/**", "/contactdetails/**")
                .hasAnyRole("USER")
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                ;
    }

/*    @Bean
    public UserDetailsService userDetailsService() {

        User.UserBuilder users = User.withDefaultPasswordEncoder();
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(users.username("user").password("user").roles("USER").build());
        manager.createUser(users.username("admin").password("admin").roles("USER", "ADMIN").build());
        return manager;
    }*/

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
