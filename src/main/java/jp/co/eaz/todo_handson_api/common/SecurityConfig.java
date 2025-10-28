package jp.co.eaz.todo_handson_api.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthFilter = jwtAuthenticationFilter;
    }

    /**
     * AuthenticationManagerをBeanとして公開する。
     * このメソッドを追加することで、AuthenticationManagerがDI可能になる
     */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        // Spring Securityの設定から AuthenticationManager のインスタンスを取得して返す
        return authenticationConfiguration.getAuthenticationManager();
    }

    // アクセス修飾子なし
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // CORSを有効
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 1.CSRFを無効化
                .csrf(csrf -> csrf.disable())
                // 2. 認証リクエストの設定 (新しいラムダ形式)
                .authorizeHttpRequests(auth -> auth
                        // （プリフライトリクエスト（PPTIONS））認証不要
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // （ログイン）認証不要
                        .requestMatchers("/login").permitAll()
                        // それ以外のリクエストは要認証
                        .anyRequest().authenticated())
                // セッションをSTATELESSに設定
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 認証エラー時のエントリーポイント
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                // JWT認証フィルターを追加
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

    // CORSの設定
    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
        // クロスドメインのリクエストに対してX-AUTH-TOKENヘッダーでトークンを返すように設定しています。
//        corsConfiguration.addExposedHeader("X-AUTH-TOKEN");
//        corsConfiguration.addAllowedOrigin("http://127.0.0.1:8080");
        corsConfiguration.addAllowedOrigin("http://localhost:5173");

        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**", corsConfiguration);
        return corsSource;
    }

}
