package jp.co.eaz.todo_handson_api.dto;

import lombok.Data;

@Data
public class TodoRequest {
    
    private String operation;
    private Todo todo;
    private Integer[] todoIdList;

}
