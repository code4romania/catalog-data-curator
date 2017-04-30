package ro.code4.curator.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import ro.code4.curator.authentication.SimpleUserRepositoryAuthenticationProvider;
import ro.code4.curator.entity.User;
import ro.code4.curator.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SimpleUserRepositoryAuthenticationProvider authenticationProvider;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/home", "/js/*", "/css/*")
                .permitAll()

                .anyRequest().authenticated()
                .and()

            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()

            .logout()
                .permitAll()
                .logoutSuccessUrl("/home")
        ;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }


}