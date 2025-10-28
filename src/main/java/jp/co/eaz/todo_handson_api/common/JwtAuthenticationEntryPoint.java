package jp.co.eaz.todo_handson_api.common;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * 認証スキームを開始します。
     * ExceptionTranslationFilter` は、このメソッドを呼び出す前に、 要求されたターゲットURLを
     * `AbstractAuthenticationProcessingFilter.SPRING_SECURITY_SAVED_REQUEST_KEY`
     * という名前の `HttpSession`
     * 属性に格納します。
     * 実装クラスは、認証プロセスを開始するために、必要に応じて `ServletResponse` の ヘッダーを変更する必要があります。
     * 
     * @param request       `AuthenticationException` を引き起こしたリクエスト
     * @param response      ユーザーエージェント（ブラウザなど）が認証を開始できるようにするためのレスポンス
     * @param authException 呼び出しの原因となった認証例外
     */
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        // フィルタでリクエスト属性に設定したエラー情報を取得する
        final Object exception = request.getAttribute("exception");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        String errMsg = "";

        if (exception instanceof ExpiredJwtException) {
            // トークン期限切れ
            errMsg = "{\"error\": \"Token Expired\", \"message\": \"Access token is expired and requires a refresh or re-login.\"}";
        } else if (authException != null) {
            // Spring Securityの一般的な認証エラーの場合
            errMsg = "{\"error\": \"Unauthorized\", \"message\": \"" + authException.getMessage() + "\"}";
        } else {
            // その他の認証エラー（例：トークンがない、無効な署名など）
            errMsg = "{\"error\": \"Unauthorized\", \"message\": \"Authentication Failed.\"}";
        }

        // レスポンスにエラーメッセージを設定
        response.getWriter().write(errMsg);
    }

}
