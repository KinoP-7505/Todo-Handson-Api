package jp.co.eaz.todo_handson_api.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetailService {
    
    public UserDetails loadUserByUsername(String userName);

}
