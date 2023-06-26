package org.zerock.b52.security.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.zerock.b52.dto.MemberDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class CustmOAuthSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

				// 인증, 인가가 다 통과됐을 때 동작하는 메소드
				log.info("--------------------------------");
				log.info("--------------------------------");
				log.info("--------------------------------");
				log.info(authentication.getPrincipal());		// memberDTO

				MemberDTO dto = (MemberDTO)authentication.getPrincipal();

				log.info(dto);	// pw: 공백문자

				log.info("--------------------------------");
				log.info("--------------------------------");


				if(dto.getPw() == null || dto.getPw().equals("")){
					response.sendRedirect("/member/modify");
					return;
				}

				response.sendRedirect("/member/mypage");

	}
	
}
