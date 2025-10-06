package jp.co.eaz.todo_handson_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.eaz.todo_handson_api.model.UserEntity;

@Repository
public interface UserRepository  extends JpaRepository<UserEntity, Integer> {

    List<UserEntity> findByUserName(String userName);

}
