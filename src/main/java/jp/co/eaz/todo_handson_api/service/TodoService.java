package jp.co.eaz.todo_handson_api.service;

import java.util.List;

import jp.co.eaz.todo_handson_api.dto.Todo;
import jp.co.eaz.todo_handson_api.model.TodoEntity;

public interface TodoService {

    /**
     * Todoリストを取得（未完了）
     * 
     * @return
     */
    public List<TodoEntity> getTodoList(String operation);

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
     * todoIdに紐づくTodoを完了／未完了に設定
     * @param operation 完了／未完了操作
     * @param ids 操作対象TodoId
     */
    public void completeTodoList(String operation,Integer[] ids);

    /**
     * Todoを削除
     * @param todo 削除Todo
     */
    public void  deleteTodo(Todo todo);
}
