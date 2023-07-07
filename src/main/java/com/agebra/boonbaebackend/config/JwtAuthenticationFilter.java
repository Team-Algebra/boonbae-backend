package com.agebra.boonbaebackend.config;

import com.agebra.boonbaebackend.exception.JwtException;
import com.agebra.boonbaebackend.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

//    @Override
//    protected void doFilterInternal(
//      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
//    ) throws ServletException, IOException {
//
//        final String authHeader;
//        authHeader = request.getHeader("Authorization");
//
//        final String jwt;
//        final String userId; //로그인아이디
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        jwt = authHeader.substring(7); //Bearer 제외
//        userId = jwtService.extractUsername(jwt);
//
//        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userId);
//
//            if (jwtService.isTokenValid(jwt, userDetails)) {
//                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                  userDetails,
//                  null, //우리는 credentials가 없는 사용자 사용
//                  userDetails.getAuthorities()
//                );
//
//                authToken.setDetails(
//                  new WebAuthenticationDetailsSource().buildDetails(request)
//                );
//
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//        }
//        filterChain.doFilter(request, response);
//    }

    @Override
    protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {

        final Authentication authentication = getAuthentication(request);
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }



    private Authentication getAuthentication (HttpServletRequest request) {
        final String authHeader;
        authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        String jwt = authHeader.substring(7); //Bearer 제외
        String userId = jwtService.extractUsername(jwt);
        UsernamePasswordAuthenticationToken authToken;

        if (userId != null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userId);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                authToken = new UsernamePasswordAuthenticationToken(
                  userDetails,
                  null,
                  userDetails.getAuthorities()
                );

                return authToken;
            }
        }

        throw new JwtException("유효하지 않은 토큰");
    }

}
