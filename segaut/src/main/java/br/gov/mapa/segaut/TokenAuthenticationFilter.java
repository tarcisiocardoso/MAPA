package br.gov.mapa.segaut;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import br.gov.mapa.segaut.model.CustemUserDetail;
import br.gov.mapa.segaut.service.SecurityService;


public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    @Autowired
    SecurityService service;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    	Authentication aut = SecurityContextHolder.getContext().getAuthentication();
    	System.out.println(">>>>>TokenAuthenticationFilter<<<<<"+aut.getName() );
        try {
        	if( aut.getName().split(":").length > 1) {
	            CustemUserDetail ud = service.buscaUserDetail(aut);
	            if( ud != null ) {
//		            System.out.println("---->"+ aut.getName()+ "<<<<"+ aut.getAuthorities().size() );
	            	
	            	UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());
	            	
	            	SecurityContextHolder.getContext().setAuthentication(authentication);
	            }
        	}
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }
        filterChain.doFilter(request, response);
    }

    

	private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}