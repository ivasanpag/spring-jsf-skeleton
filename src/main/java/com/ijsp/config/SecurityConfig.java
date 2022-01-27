package com.ijsp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Spring Security config.
 * @author ijsp
 * @since 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final String[] RESOURCES = new String[]{
            "/css/**", "/icons/**", "/img/**", "/js/**", "/scss/**", "/vendor/**", "/javax.faces.resource/**"
    };
    /**
     * Spring Security configuration for HTTP requests
     *
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // CSRF not needed as JSF 2.2 is implicitly protected against CSRF. use ¿csrf().disable()?
        http
               //  .cors(withDefaults())
                .authorizeRequests(authorize -> authorize
                        .expressionHandler(webSecurityExpressionHandler())
                        .antMatchers("/faces/login.xhtml").permitAll()
                        .antMatchers(RESOURCES).permitAll()
                        .antMatchers("/faces/dashboard.xhtml").hasRole("USER")
                        .antMatchers("/faces/setting.xhtml").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/faces/login.xhtml")
                        .loginProcessingUrl("/faces/login.xhtml")
                        .permitAll()
                        .defaultSuccessUrl("/faces/dashboard.xhtml", true)
                        .failureUrl("/faces/login.xhtml")
                        .usernameParameter("username")
                        .passwordParameter("password")
                )
                .logout(logout -> logout
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .logoutSuccessUrl("/faces/login.xhtml")
                )
                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionConcurrency((sessionConcurrency) ->
                                sessionConcurrency
                                        .maximumSessions(1) // use configRepository to retrieve -1 or 1
                                        .maxSessionsPreventsLogin(true)
                        )
                )
                .headers(headers -> headers
                        .frameOptions().sameOrigin()
                        .contentTypeOptions().and()
                        .httpStrictTransportSecurity().includeSubDomains(true).maxAgeInSeconds(31536000).and()
                        .xssProtection().block(true).and()
                        .cacheControl()
                );


    }

    /**
     * Password encoder password encoder.
     *
     * @return the password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("SHA-256", new MessageDigestPasswordEncoder("SHA-256"));

        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("bcrypt", encoders);
        passwordEncoder.setDefaultPasswordEncoderForMatches(new MessageDigestPasswordEncoder("SHA-256"));

        return passwordEncoder;
    }

    @Autowired
    //TESTING
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.inMemoryAuthentication().withUser("john.doe")
                .password(passwordEncoder().encode("1234")).roles("USER").and()
                .withUser("jane.doe").password(passwordEncoder().encode("5678")).roles("ADMIN");
    }

    // Spring Web Expressions
    @Bean
    public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }

    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return roleHierarchy;
    }

    @Bean
    public RoleHierarchyVoter roleVoter() {
        return new RoleHierarchyVoter(roleHierarchy());
    }

    @Bean
    public AffirmativeBased accessDecisionManager() {
        return new AffirmativeBased(Collections.singletonList(roleVoter()));
    }

    @Bean
    public DefaultWebSecurityExpressionHandler expressionHandler(){
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy());
        expressionHandler.setDefaultRolePrefix("");
        return expressionHandler;
    }

    @Bean(name = "mvcHandlerMappingIntrospector")
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }

}
