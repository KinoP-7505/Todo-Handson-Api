package jp.co.eaz.todo_handson_api.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jp.co.eaz.todo_handson_api.dto.Todo;
import jp.co.eaz.todo_handson_api.model.TodoEntity;
import jp.co.eaz.todo_handson_api.repository.TodoRepository;
import jp.co.eaz.todo_handson_api.repository.TodoSpecification;
import jp.co.eaz.todo_handson_api.repository.UserRepository;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Override
    public List<TodoEntity> getTodoList() {
        // ログインユーザのIDはuserSeviceから取得可能
        Integer userId = userService.getUserId();

        // ユーザIDはある前提
        // 抽出条件の構築
        List<TodoEntity> list;
        Specification<TodoEntity> spec = TodoSpecification.hasUserId(userId)
                .and(TodoSpecification.conditionalCompleteAt(false));
        // ソート条件
        Sort sort = Sort.by(Sort.Direction.ASC, "todoId");
        // JPAクエリ実行（条件、ソートを入力）
        list = todoRepository.findAll(spec, sort);

        return list;
    }

    @Override
    public void addTodo(Todo todo) {
        // ログインユーザのIDはuserSeviceから取得可能
        Integer userId = userService.getUserId();

        TodoEntity addTodo = new TodoEntity();
        addTodo.setTodoText(todo.getTodoText());
        addTodo.setUserId(userId);
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
    public void completeTodoList(Integer[] ids) {
        Timestamp currentDate = getCurrentDate();
        // ログインユーザのIDはuserSeviceから取得可能
        Integer userId = userService.getUserId();

        // idsから１つづつ処理
        for (Integer todoId : ids) {
            // idに紐づくレコードを取得
            TodoEntity entity = todoRepository.findByTodoIdAndUserId(todoId, userId);
            // レコードの完了日を更新
            entity.setCompleateAt(currentDate);
            // 更新
            todoRepository.save(entity);
        }
    }

    // Timestamp型の現在日時を返却
    private Timestamp getCurrentDate() {
        long currentTimeMillis = System.currentTimeMillis();
        return new Timestamp(currentTimeMillis);
    }

    @Override
    public void updateCompleteAt(Todo todo) {
        // TODO 自動生成されたメソッド・スタブ

    }

}
