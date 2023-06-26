package org.zerock.b52.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.zerock.b52.security.CustomOAuth2UserService;
import org.zerock.b52.security.handler.CustmOAuthSuccessHandler;
import org.zerock.b52.security.handler.CustomAccessDeniedHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Configuration
@Log4j2
@EnableMethodSecurity
@RequiredArgsConstructor
public class CustomSecurityConfig {

	private final DataSource dataSource;

	private final CustomOAuth2UserService customOAuth2UserService;

	@Bean
	public PasswordEncoder passwordEncoder(){

		return new BCryptPasswordEncoder();
	}

	  @Bean
  public PersistentTokenRepository persistentTokenRepository() {
      JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
      repo.setDataSource(dataSource);
      return repo;
  }

	
	@Bean
	public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception{

		log.info("filter chain------------------------------");

		// login 경로 로그인 페이지 띄우기(시큐리티가 제공하는 기본 로그인 폼)

		// http.formLogin(Customizer.withDefaults());

		http.formLogin(config -> {
			config.loginPage("/member/signin");
		});

		http.exceptionHandling(
			config -> config.accessDeniedHandler(new CustomAccessDeniedHandler())
		);

		http.rememberMe(config -> {
			config.tokenRepository(persistentTokenRepository());
			config.tokenValiditySeconds(60*60*24*7);
		}); 

		http.csrf(config -> {
			config.disable();
		});

		http.oauth2Login(config -> {
			config.loginPage("/member/signin");
			config.successHandler(new CustmOAuthSuccessHandler());
		});



		return http.build();
	}



}
