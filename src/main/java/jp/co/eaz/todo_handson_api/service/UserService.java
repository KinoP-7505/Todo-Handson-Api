package jp.co.eaz.todo_handson_api.service;

import jp.co.eaz.todo_handson_api.model.UserEntity;

public interface UserService {
    
    /**
     * ユーザ情報を１件返す。無い場合はnullを返す。
     * @return
     */
    public UserEntity getUserByUserId(String userId);
    public UserEntity getUserByUserName(String userName);
    public UserEntity getUserInfo();
    public Integer getUserId();

}
