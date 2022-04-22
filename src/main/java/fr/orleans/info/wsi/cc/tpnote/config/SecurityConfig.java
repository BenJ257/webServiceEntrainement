package fr.orleans.info.wsi.cc.tpnote.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/quizz/utilisateur").permitAll()
                .antMatchers(HttpMethod.GET, "/api/quizz/utilisateur/{idUtilisateur}").hasAnyRole("PROFESSEUR", "ETUDIANT")
                .antMatchers(HttpMethod.POST, "/api/quizz/question").hasRole("PROFESSEUR")
                .antMatchers(HttpMethod.GET, "/api/quizz/question/{idQuestion}").hasAnyRole("PROFESSEUR", "ETUDIANT")
                .antMatchers(HttpMethod.PUT, "/api/quizz/question/{idQuestion}/vote").hasRole("ETUDIANT")
                .antMatchers(HttpMethod.GET, "/api/quizz/question/{idQuestion}/vote").hasRole("PROFESSEUR")
                .and().httpBasic()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
