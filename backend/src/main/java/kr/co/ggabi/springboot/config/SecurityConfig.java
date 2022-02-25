package kr.co.ggabi.springboot.config;


import kr.co.ggabi.springboot.jwt.JwtAccessDeniedHandler;
import kr.co.ggabi.springboot.jwt.JwtAuthenticationEntryPoint;
import kr.co.ggabi.springboot.jwt.JwtSecurityConfig;
import kr.co.ggabi.springboot.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;


    @Override
    public void configure(WebSecurity web){
        web.ignoring()
                .antMatchers("/h2-console/**", "/favicon.ico");
    }

    @Value("${admin.accessIp}")
    String ip;

    @Override
    protected void configure(HttpSecurity http) throws Exception{

        http
                .csrf().disable()

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                .and()
                .sessionManagement()
                .sessionCreationPolicy((SessionCreationPolicy.STATELESS))

                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/board/**").permitAll()
                .antMatchers("/api/board").permitAll()
                .antMatchers("/api/danger").access("hasIpAddress('192.168.4.211')")
                .antMatchers("/api/admin", "/api/admin/**").permitAll()
                .antMatchers("/api/address/personal").permitAll()
                .antMatchers("/api/address/personal/**").permitAll()
                .antMatchers("/api/admin/user").permitAll()
                .antMatchers("/api/admin/user/**").permitAll()
                .antMatchers("/api/address/fixed").permitAll()
                .antMatchers("/api/user","/api/user/**").permitAll()
                .antMatchers("/api/**").hasAnyRole("ADMIN", "USER")


                .and()
                .apply(new JwtSecurityConfig(tokenProvider));
    }
}
