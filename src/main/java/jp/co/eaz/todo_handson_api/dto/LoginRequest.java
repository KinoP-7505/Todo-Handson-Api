package jp.co.eaz.todo_handson_api.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

/**
 * ログインリクエストDTO
 */
@Data
public class LoginRequest {
    @Pattern(regexp = "^[a-zA-Z0-9\\-_.@]+$", message = "使用できない文字が含まれています。")
    private String userName;

    @Pattern(regexp = "^[!-\\~]+$", message = "使用できない文字が含まれています。")
    @Size(min = 4, max = 16, message = "パスワードは4～16文字で入力してください")
    private String password;
}
