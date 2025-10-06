package jp.co.eaz.todo_handson_api.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String userName;
    private String password;
}
