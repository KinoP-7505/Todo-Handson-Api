package jp.co.eaz.todo_handson_api.service;

import java.util.List;

import jp.co.eaz.todo_handson_api.dto.Todo;
import jp.co.eaz.todo_handson_api.model.TodoEntity;

public interface TodoService {

    /**
     * Todoリストを取得
     * 
     * @return
     */
    public List<TodoEntity> getTodoList();

    /**
     * Todoをリストに追加
     */
    public void addTodo(Todo todo);

    /**
     * Todoを更新
     */
    public void updateTodo(Todo todo);

    /**
     * Todoをリストに追加
     */
    public void updateCompleteAt(Todo todo);

    /**
     * Todoを完了
     */
//    public String completeTodo(Long id);

    /**
     * Todoを完了から未完了に変更
     */
//    public String unCompleteTodo(Long id);
}
