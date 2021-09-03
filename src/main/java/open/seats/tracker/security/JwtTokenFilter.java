package open.seats.tracker.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.log4j.Log4j2;
import open.seats.tracker.model.User;

@Log4j2
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String jwt = parseJwt(request);
			if (jwt != null) {
				String username = jwtUtils.getUserNameFromJwtToken(jwt);

				if (StringUtils.isNotBlank(username)) { // if blank, later authentication will fail in chain anyway
					User user = userDetailsService.loadUserByUserNameAndVerified(username);
					
					if(request.getHeader("userId")==null || user.getUserId()!=Integer.parseInt(request.getHeader("userId"))) {
						log.error("jwt token verification error; userId mismatch: userid from db is:{}", user.getUserId());
						throw new Exception();
					}
					
					UserDetails userDetails = UserDetailsImpl.build(user); 

					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(authentication);
				} else {
					log.error("failed to fetch username from jwt token");
				}
			} 
		} catch (Exception e) {
			logger.error("failed to set user authentication", e);
		}

		filterChain.doFilter(request, response);
	}

	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.isNoneBlank(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7);
		} else {
			log.error("auth header is:{}", headerAuth);
		}

		return null;
	}

}
