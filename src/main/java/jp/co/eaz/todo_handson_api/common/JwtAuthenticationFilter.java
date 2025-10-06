package jp.co.eaz.todo_handson_api.common;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWTヘッダーを検証するための認証フィルター
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    @Autowired
    private UserDetailsService userDetailsService;

//    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
//        this.jwtTokenProvider = jwtTokenProvider;
//        this.userDetailsService = userDetailsService;
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
            HttpServletResponse response, 
            FilterChain filterChain)
            throws ServletException, IOException {
        
        try {
          String jwt = getJwtFromRequest(request);
          
          if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt) ) {
              // JWTユーザ名を取得
              String userName = jwtTokenProvider.getUsernameFromToken(jwt);
              UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
              
              // リクエスト認証
              UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                      userDetails, 
                      null, 
                      userDetails.getAuthorities()
              );
              auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
              
              SecurityContextHolder.getContext().setAuthentication(auth);
          }
        } catch (Exception e) {
         // 認証失敗
        }
        filterChain.doFilter(request, response);
    }

    /**
     * httpリクエストからトークンを取得する
     * 
     * @param request
     * @return
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String token = null;
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }
        return token;
    }

}
