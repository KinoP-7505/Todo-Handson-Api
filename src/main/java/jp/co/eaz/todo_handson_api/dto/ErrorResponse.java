package jp.co.eaz.todo_handson_api.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 共通エラーレスポンスDTO
 */
@Data
public class ErrorResponse {
    private List<String> messages;
    private String code;
    
    // 初期化
    public ErrorResponse() {
        this.messages = new ArrayList<String>();
    }
    
    // メッセージをmessages配列に追加する
    public void addMessage(String message) {
        if (messages == null) {
            this.messages = new ArrayList<String>();
        } else {
            
        }
        this.messages.add(message);
    }
    
    
}
