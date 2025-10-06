package jp.co.eaz.todo_handson_api.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jp.co.eaz.todo_handson_api.dto.Todo;
import jp.co.eaz.todo_handson_api.model.TodoEntity;
import jp.co.eaz.todo_handson_api.model.UserEntity;
import jp.co.eaz.todo_handson_api.repository.TodoRepository;
import jp.co.eaz.todo_handson_api.repository.UserRepository;

@Service
public class TodoServiceImpl implements TodoService {

//    // Todoリスト
//    private List<Todo> todoList;
//
//    // 初期TodoList作成
//    public TodoServiceImpl() {
//        todoList = new ArrayList<Todo>();
//        Todo sample = new Todo(1, "sample todo");
//        todoList.add(sample);
//    }

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<TodoEntity> getTodoList() {
        // todoを全件返す
        return todoRepository.findAll(Sort.by(Sort.Direction.ASC, "todoId"));
    }

    @Override
    public void addTodo(Todo todo) {
        // User
        UserEntity user = userRepository.findById(1).get();

//        Todo addTodo = new Todo(todo.getTodoText(), user.get());
        TodoEntity addTodo = new TodoEntity();
        addTodo.setTodoText(todo.getTodoText());
        addTodo.setUserId(user.getUserId());
        addTodo.setCreatedAt(getCurrentDate());

        todoRepository.save(addTodo);
    }

    @Override
    public void updateTodo(Todo todo) {
        // todoIdに紐づくレコードを取得
        TodoEntity entity = todoRepository.findById(todo.getTodoId()).get();
        
        entity.setTodoText(todo.getTodoText());
        entity.setUpdatedAt(getCurrentDate());
        // 更新
        todoRepository.save(entity);
    }

    @Override
    public void updateCompleteAt(Todo todo) {
        // todoIdに紐づくレコードを取得
        TodoEntity entity = todoRepository.findById(todo.getTodoId()).get();

        // 完了日がNullの場合
        if (Objects.isNull(entity.getCompleateAt())) {
            // レコードの完了日を更新
            entity.setCompleateAt(getCurrentDate());
        } else {
            // 上記以外は完了日をnull更新
            entity.setCompleateAt(null);
        }
        // 更新
        todoRepository.save(entity);

    }

    // Timestamp型の現在日時を返却
    private Timestamp getCurrentDate() {
        long currentTimeMillis = System.currentTimeMillis();
        return new Timestamp(currentTimeMillis);
    }

//    @Override
//    public String updateTodo(Todo todo) {
//        // TODO 自動生成されたメソッド・スタブ
//        return "success";
//    }
//
//    @Override
//    public String completeTodo(Long id) {
//        // TODO 自動生成されたメソッド・スタブ
//        return "success";
//    }
//
//    @Override
//    public String unCompleteTodo(Long id) {
//        // TODO 自動生成されたメソッド・スタブ
//        return "success";
//    }

}
