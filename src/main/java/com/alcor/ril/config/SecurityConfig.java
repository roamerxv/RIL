package com.alcor.ril.config;

import com.alcor.ril.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created with admin.
 * User: duduba - 邓良玉
 * Date: 2017/12/4
 * Time: 00:34
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean
    UserDetailsService customUserService() {
        return new MyUserDetailService();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        
        http
                .authorizeRequests()
                    .antMatchers("/", "/index", "/dashboard/index").permitAll()
//                    .antMatchers("/user/registration").permitAll()
//                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
                    .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                        @Override
                        public <T extends FilterSecurityInterceptor> T postProcess(T fsi) {
                            fsi.setSecurityMetadataSource(mySecurityMetadataSource());
                            fsi.setAccessDecisionManager(myAccessDecisionManager());
                            return fsi;
                        }
                    })
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/signIn.json")
                    .usernameParameter("name")
                    .passwordParameter("passwd")
                    .defaultSuccessUrl("/index")
                    .successHandler(authenticationSuccessHandler())
                    .failureUrl("/login?error")
                    .permitAll()
                    .and()
                .logout()
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutUrl("/logout")
//                    .logoutSuccessHandler(logoutSuccessHandler())
                    .logoutSuccessUrl("/login?logout")
                    .permitAll()
                    .and()
                .exceptionHandling()
                    .accessDeniedHandler(accessDeniedHandler())
                    .and()
                .addFilterAt(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(customUserService())
                .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
//        super.configure(web);
        //解决静态资源被拦截的问题
        web
                .ignoring()
                .antMatchers("/assets/**", "/css/**", "/js/**", "/images/**", "/webjars/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    @Bean
    CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
//        filter.setAuthenticationFailureHandler(new FailureHandler());
        filter.setFilterProcessesUrl("/signIn.json");

        //这句很关键，重用WebSecurityConfigurerAdapter配置的AuthenticationManager，
        // 不然要自己组装AuthenticationManager
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler() {
        return new LoggingAccessDeniedHandler();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler("/index");
    }
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler("/login");
    }

    @Bean
    public FilterInvocationSecurityMetadataSource mySecurityMetadataSource() {
        return new MyFilterInvocationSecurityMetadataSource();
    }

    @Bean
    public AccessDecisionManager myAccessDecisionManager() {
        return new MyAccessDecisionManager();
    }

}
