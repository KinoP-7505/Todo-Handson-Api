package jp.co.eaz.todo_handson_api.dto;

import lombok.Data;

/**
 * 認証レスポンスクラス
 */
@Data
public class AuthResponse {
    
    private String jwtToken;
    private Integer expiresIn;
    private String userViewName; // 表示ユーザ名
    private Integer userId;

}
