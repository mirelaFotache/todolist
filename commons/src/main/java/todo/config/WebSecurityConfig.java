package todo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import todo.authenticationjwt.JwtAuthenticationEntryPoint;
import todo.authenticationjwt.JwtAuthenticationProvider;
import todo.authenticationjwt.JwtAuthenticationTokenFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) {
        authenticationManagerBuilder.authenticationProvider(jwtAuthenticationProvider);
    }

    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() {
        return new JwtAuthenticationTokenFilter();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/welcome","/eureka/**","/actuator/**","/hystrix/**","/hystrix.stream/**", "/webjars/**","/proxy.stream","/hystrix/monitor")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/users/**", "/project/**", "/task/**", "/taskitems/**", "/language/**", "/contactdetails/**")
                .hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.POST, "/users/**", "/project/**", "/task/**", "/taskitems/**", "/language/**", "/contactdetails/**")
                .hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/users/**", "/project/**", "/task/**", "/taskitems/**", "/language/**", "/contactdetails/**")
                .hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/users/**", "/project/**", "/task/**", "/taskitems/**", "/language/**", "/contactdetails/**")
                .hasAnyRole("ADMIN")

                .antMatchers(HttpMethod.POST, "/task/**", "/taskitems/**", "/language/**", "/contactdetails/**")
                .hasAnyRole("USER")
                .antMatchers(HttpMethod.PUT, "/task/**", "/taskitems/**", "/language/**", "/contactdetails/**")
                .hasAnyRole("USER")
                .antMatchers(HttpMethod.DELETE, "/task/**", "/taskitems/**", "/language/**", "/contactdetails/**")
                .hasAnyRole("USER")
                .anyRequest().authenticated();

        httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
        httpSecurity.headers().cacheControl();
    }

}
