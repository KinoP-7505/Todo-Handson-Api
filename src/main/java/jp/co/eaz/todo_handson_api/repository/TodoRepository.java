package jp.co.eaz.todo_handson_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import jp.co.eaz.todo_handson_api.model.TodoEntity;

/**
 * JPA Todoリポジトリインターフェース
 * JpaRepository: 標準JPAインターフェース
 * JpaSpecificationExecutor: JPAクエリ作成インターフェース
 */
@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, Integer>, JpaSpecificationExecutor<TodoEntity> {
    
    public List<TodoEntity> findByUserId(Integer userId);
    public TodoEntity findByTodoIdAndUserId(Integer todoId, Integer userId);

}
