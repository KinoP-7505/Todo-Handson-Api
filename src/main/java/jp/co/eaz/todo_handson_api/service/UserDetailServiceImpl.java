package jp.co.eaz.todo_handson_api.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jp.co.eaz.todo_handson_api.model.UserEntity;
import jp.co.eaz.todo_handson_api.repository.UserRepository;

/**
 * ユーザ情報取得サービス UserDetailsService実装クラス
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * UserDetailsService継承メソッド
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity userEntity = null;
        List<UserEntity> users = userRepository.findByUserName(userName);

        if (users.size() > 0) {
            userEntity = users.get(0);

            System.out.println("loadUserByUsername");
            System.out.println(userEntity);
        } else {
          // ユーザ名が無い場合
          System.out.println("UsernameNotFoundException");
          throw new UsernameNotFoundException("ログインエラー（ユーザー名）");
        }
        
        // Userを作成
        System.out.println("Userを作成");
        User user = new User(userEntity.getUserName(), // ユーザ名
                "{noop}" + userEntity.getPassword(), // パスワード
//                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
              Collections.singleton(new SimpleGrantedAuthority("USER")));

        System.out.println("loadUserByUsername user");
        System.out.println(user);
        return user;
    }

}
