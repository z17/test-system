package test_system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import test_system.service.CustomUserDetailService;

@EnableJpaRepositories("test_system.repository")
@Configuration
@EnableWebSecurity
public class Config extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailService customUserDetailService;

    @Autowired
    public Config(final CustomUserDetailService customUserDetailService) {
        this.customUserDetailService = customUserDetailService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .and()
                .formLogin().loginPage("/login").permitAll()
                .and()
                .logout().logoutUrl("/logout")
                .and().userDetailsService(customUserDetailService);
    }

    @Bean
    public BCryptPasswordEncoder bcryptEncoder() {
        return new BCryptPasswordEncoder();
    }
}
