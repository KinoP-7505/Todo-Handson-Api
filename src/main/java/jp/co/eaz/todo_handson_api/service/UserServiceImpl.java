package jp.co.eaz.todo_handson_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import jp.co.eaz.todo_handson_api.model.UserEntity;
import jp.co.eaz.todo_handson_api.repository.UserRepository;

/**
 * Userテーブルのリポジトリクラス
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserEntity getUserByUserId(String userId) {
        List<UserEntity> users = userRepository.findByUserId(Integer.parseInt(userId));
        if (users.size() == 0) {
            return null;
        }
        return users.get(0);
    }

    @Override
    public UserEntity getUserByUserName(String userName) {
        List<UserEntity> users = userRepository.findByUserName(userName);
        if (users.size() == 0) {
            return null;
        }
        return users.get(0);
    }

    @Override
    public UserEntity getUserInfo() {
        // 1. SecurityContextHolderから認証オブジェクトを取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            // 2. principal（認証主体）からユーザー詳細情報を取得
            Object principal = authentication.getPrincipal();

            // 3. 取得したprincipalをUserDetails（またはカスタムのUserオブジェクト）にキャスト
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                String username = userDetails.getUsername();
                // ユーザー名をキーとして、UserServiceなどからDBの全ユーザー情報を取得

                // JWT検証時にカスタムのUserエンティティをprincipalに格納している場合
                if (principal instanceof UserEntity) {
                    UserEntity user = (UserEntity) principal;
                    return user; // ユーザーエンティティ全体を返す
                } else {
                    UserEntity user = userRepository.findByUserName(username).get(0);
                    return user;
                }

            } else if (principal instanceof String) {
                // principalがユーザー名（文字列）の場合の処理
                String username = (String) principal;
                // UserServiceなどを使ってDBからユーザー情報を取得
            }
        }
        // 認証情報が見つからない場合や、匿名ユーザーの場合
        return null;
    }

    @Override
    public Integer getUserId() {
        UserEntity user = getUserInfo();
        if (user == null) {
            // ユーザ取得できない場合、システムエラーを発生
            throw new IllegalArgumentException("AuthenticationからユーザIDの取得ができませんでした");
        }
        return user.getUserId();
    }

}
