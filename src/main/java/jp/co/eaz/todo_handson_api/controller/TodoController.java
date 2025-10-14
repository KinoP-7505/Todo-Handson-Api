package jp.co.eaz.todo_handson_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jp.co.eaz.todo_handson_api.common.JwtTokenProvider;
import jp.co.eaz.todo_handson_api.dto.AuthResponse;
import jp.co.eaz.todo_handson_api.dto.GetTodoListResponse;
import jp.co.eaz.todo_handson_api.dto.LoginRequest;
import jp.co.eaz.todo_handson_api.dto.TodoRequest;
import jp.co.eaz.todo_handson_api.model.UserEntity;
import jp.co.eaz.todo_handson_api.service.TodoService;
import jp.co.eaz.todo_handson_api.service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:5173", methods = { RequestMethod.GET, RequestMethod.POST,
        RequestMethod.OPTIONS }) // Reactアプリのオリジン（CORS対応）
public class TodoController {

    @Autowired
    private TodoService todoService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    

    private final String BRANK = "";

    /**
     * ログイン認証（JWTトークン発行）
     * 
     * @param loginRequest
     * @return JWTトークン
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        System.out.println(loginRequest);

//        リクエスト検証
        if (loginRequest.getUserName().equals(BRANK) || loginRequest.getPassword().equals(BRANK)) {
            AuthResponse res = new AuthResponse();
            res.setJwtToken("リクエスト情報がブランクです");
            System.out.println(res);
            return ResponseEntity.badRequest().body(res);
        }

        // 1. Spring Securityの認証プロセスを実行
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
            System.out.print(authentication);
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "ユーザー名またはパスワードが間違っています");
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "ユーザーが存在しません");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "認証中にエラーが発生しました");
        }

        // 2. 認証に成功したら、ユーザーの詳細をロード
        System.out.println("2. 認証に成功したら、ユーザーの詳細をロード");
        String token = "";
        if (authentication.isAuthenticated()) {
            token = jwtTokenProvider.generateToken(loginRequest.getUserName());
            System.out.println(token);

        } else {
            throw new RuntimeException("ログイン失敗");
        }

        AuthResponse res = new AuthResponse();
        res.setJwtToken(token);
        res.setExpiresIn(30000);
        // ユーザ情報をセット（認証済みなので必ず取得する）
        UserEntity user = userService.getUserByUserName(loginRequest.getUserName());
        res.setUserId(user.getUserId());
        res.setUserViewName(user.getViewName());

        // 4. トークンをクライアントに返す
        return ResponseEntity.ok(res);
    }

    // Todoリスト取得API
    @PreAuthorize("hasRole('USER')") 
    @GetMapping("/getTodoList")
    public ResponseEntity<GetTodoListResponse> getTodoList() throws Exception {
        GetTodoListResponse res = new GetTodoListResponse();
        System.out.println("TodoController getTodoList()");

        try {
            res.setExchangeTodoList(todoService.getTodoList());
        } catch (Exception e) {
            // 例外
            System.out.println(e);
            res.setMessage("getTodoList エラー");
            return ResponseEntity.badRequest().body(res);
        }
        return ResponseEntity.ok(res);

    }

    // Todoリスト登録API
    @PreAuthorize("hasRole('USER')") 
    @PostMapping("/addTodo")
    public void addTodo(@RequestBody TodoRequest todoReq) {

        // 必須チェック

        // todo登録
        todoService.addTodo(todoReq.getTodo());
        // 登録したリストを返却
//        GetTodoListResponse res = new GetTodoListResponse();
//        res.setExchangeTodoList(todoService.getTodoList());
        
    }

    // Todoリスト更新API
    @PreAuthorize("hasRole('USER')") 
    @PostMapping("/updateTodo")
    public GetTodoListResponse updateTodo(@RequestBody TodoRequest reqTodo) {
        // todo更新
        todoService.updateTodo(reqTodo.getTodo());

        // 更新したリストを返却
        GetTodoListResponse res = new GetTodoListResponse();
        res.setExchangeTodoList(todoService.getTodoList());
        return res;
    }
    
    @PreAuthorize("hasRole('USER')") 
    @PostMapping("/sendCompleteList")
    public ResponseEntity<GetTodoListResponse> sendCompleteList(@RequestBody TodoRequest reqTodo) {
        // 完了日更新
        todoService.completeTodoList(reqTodo.getTodoIdList());

        GetTodoListResponse res = new GetTodoListResponse();
        return ResponseEntity.ok(res);
    }


    @PostMapping("/updateCompleteAt")
    public GetTodoListResponse updateCompleteAt(@RequestBody TodoRequest reqTodo) {

        todoService.updateCompleteAt(reqTodo.getTodo());
        // 更新したリストを返却
        GetTodoListResponse res = new GetTodoListResponse();
        return res;
    }

    // Todoリスト削除API
//    @PostMapping("deleteTodo")

}
