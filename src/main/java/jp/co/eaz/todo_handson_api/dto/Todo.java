package jp.co.eaz.todo_handson_api.dto;

import java.sql.Timestamp;
import java.util.Objects;

import jp.co.eaz.todo_handson_api.model.TodoEntity;
import lombok.Data;

/**
 * TODO DTO
 */
@Data
public class Todo {
    
    private Integer todoId;
    private String todoText;
    private Integer userId;
    // JSONでは日付を数値で扱う、TodoEntity（Postgre）はTIMESTAMP型で扱う
    private Long createdAt;
    private Long updatedAt;
    private Long compleateAt;
    
    
    public void setExchangeTodo(TodoEntity todoEnt) {
        this.todoId = todoEnt.getTodoId();
        this.todoText = todoEnt.getTodoText();
        this.userId = todoEnt.getUserId();
        
        this.createdAt = getTimeLong(todoEnt.getCreatedAt());
        this.updatedAt = getTimeLong(todoEnt.getUpdatedAt());
        this.compleateAt = getTimeLong(todoEnt.getCompleateAt());
    }
    
    private Long getTimeLong(Timestamp dt) {
        // nullの場合は-1を返却
        if (Objects.isNull(dt)) {
            return (long) -1;
        }
        // 日付をLong型に変換
        return dt.getTime();
    }


}
