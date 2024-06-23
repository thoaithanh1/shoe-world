package root.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import root.app.filter.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailService();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(auth ->
                auth
                        .requestMatchers("/api/v1/account/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/brand/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/category/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/color/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/product/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/product/filter").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/product-detail/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/role/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/size/**").permitAll()
                        .requestMatchers("/api/v1/user/**").permitAll()
                        .requestMatchers("/api/v1/cart/**").permitAll()
                        .requestMatchers("/api/v1/user/page").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/favorite/count/**").permitAll()
                        .requestMatchers("/api/v1/user/is-authenticated").permitAll()
                        .requestMatchers("/api/v1/account/profile").authenticated()
                        .requestMatchers(HttpMethod.GET,"/api/v1/payment/vnpay/response").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/payment-method").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/review/**").permitAll()
                        .anyRequest().authenticated()
        )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
