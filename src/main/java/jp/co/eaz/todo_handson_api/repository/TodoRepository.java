package jp.co.eaz.todo_handson_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.eaz.todo_handson_api.model.TodoEntity;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, Integer> {
    

}
