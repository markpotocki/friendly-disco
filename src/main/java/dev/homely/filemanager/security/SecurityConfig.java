package dev.homely.filemanager.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import reactor.core.publisher.Mono;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf().disable() // TODO: remove csrf disable
                .authorizeExchange()
                    .pathMatchers("/api/files/admin/**").hasRole("ADMIN")
                    .pathMatchers("/login", "/index.html", "/").permitAll()
                    .pathMatchers("/api/quotas").permitAll()
                    .pathMatchers("/api/file/test").permitAll() // TODO: Remove this line
                    .anyExchange().authenticated() // TODO: switch back to authenticated
                .and()
                .httpBasic()
                    .securityContextRepository(new WebSessionServerSecurityContextRepository())
                .and()
                .build();
    }

    private Mono<AuthorizationDecision> currentUserMatchesPath(Mono<Authentication> authentication, AuthorizationContext context) {
        return authentication
                .map( a -> context.getVariables().get("userId").equals(a.getName()))
                .map(AuthorizationDecision::new);
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        return new MapReactiveUserDetailsService(user);
    }

    @Bean
    public ReactiveUserDetailsService userDetailsService() {

    }
}
