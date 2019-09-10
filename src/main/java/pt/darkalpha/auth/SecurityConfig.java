package pt.darkalpha.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private String roleUser = "USER";
	private String roleAdmin = "ADMIN";
 
    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
          .withUser("user").password(passwordEncoder().encode("password")).roles(roleUser)
          .and()
          .withUser("admin").password(passwordEncoder().encode("pass")).roles(roleUser, roleAdmin);
    }
 
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
          .csrf().disable()
          .authorizeRequests()
          .antMatchers("/admin/**").hasRole(roleAdmin)
          .antMatchers("/api/auth/**").permitAll()
          .antMatchers("/api/**").hasRole(roleUser)
          .antMatchers("/**").permitAll()
          .anyRequest().authenticated()
          .and()
          .formLogin()
          .loginPage("/login")
          .loginProcessingUrl("/api/auth/login")
          .defaultSuccessUrl("/", true)
          //.failureUrl("/login?error=true")
          //.failureHandler(authenticationFailureHandler())
          .and()
          .logout()
          .logoutUrl("/api/auth/logout")
          .deleteCookies("JSESSIONID");
    }
     
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}