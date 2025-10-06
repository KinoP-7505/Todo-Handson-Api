package jp.co.eaz.todo_handson_api.common;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {
    private final String JWT_SECRET = "LILY+IS+SOCUTE+THANK+YOU+FOR+YOUR+HARD+WORK+ON+THIS+SEASON+ANIME+BROADCAST";
    private final Integer EXPIRATION_TIME  = 30 * 1000;
    
    private Key jwtSecretKey;
    
    public JwtTokenProvider() {
        byte[] jwtSecretBytes = Decoders.BASE64.decode(JWT_SECRET);
        this.jwtSecretKey = Keys.hmacShaKeyFor(jwtSecretBytes);
    }
    
    // 署名付きJWTトークンを作成
    public String generateToken(String userName) {
        Date nowDate = new Date();
        Date expiryDate = new Date(nowDate.getTime() + EXPIRATION_TIME);
        
        return Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(nowDate)
                .setExpiration(expiryDate)
                .signWith(jwtSecretKey)
                .compact();
    }
    
    // JWTユーザー名を取得
    public String getUsernameFromToken(String token) {
        String userName = Jwts.parserBuilder()
                .setSigningKey(jwtSecretKey) 
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return userName;
    }
    
    // JWTの検証
    public boolean validateToken(String authToken) {
        boolean isVail = false;
        
        try {
            // JWT parseBuilderを使用して作成
            Jwts.parserBuilder()
            .setSigningKey(jwtSecretKey) // 保持している SecretKey を使用
            .build()
            .parseClaimsJws(authToken);
            
            isVail = true;
        } catch (Exception e) {
            // トークンが無効な場合の例外処理
            isVail = false; // とりあえずfalseを返す
        }
        return isVail;
    }

}
