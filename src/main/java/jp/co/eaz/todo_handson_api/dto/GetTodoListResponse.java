package jp.co.eaz.todo_handson_api.dto;

import java.util.ArrayList;
import java.util.List;

import jp.co.eaz.todo_handson_api.model.TodoEntity;
import lombok.Data;

/**
 * TodoリストレスポンスDTO
 */
@Data
public class GetTodoListResponse {

    private List<Todo> todoList;
    private String message;

    // TodoEntityをTodo型に変換
    public void setExchangeTodoList(List<TodoEntity> todoListEnt) {
        List<Todo> workList = new ArrayList<Todo>();
        for (TodoEntity todoEnt : todoListEnt) {
            Todo workTodo = new Todo();
            workTodo.setExchangeTodo(todoEnt);
            workList.add(workTodo);
        }
        todoList = workList;
    }

}
