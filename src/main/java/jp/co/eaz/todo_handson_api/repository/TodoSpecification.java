package jp.co.eaz.todo_handson_api.repository;

import org.springframework.data.jpa.domain.Specification;

import jp.co.eaz.todo_handson_api.model.TodoEntity;

public class TodoSpecification {

    /**
     * 条件：userIdで抽出
     * @param userUd ユーザID
     * @return Specification<TodoEntity>
     */
    public static Specification<TodoEntity> hasUserId(Integer userId) {
        // 引数が無効の場合は実行しない
        if (userId == null || userId == 0) {
            return null;
        }
        
        return (root, query, builder) -> {
            return builder.equal(root.get("userId"), userId);
        };
    };
    
    /**
     * 条件：完了日の入力あり/なし
     * @return Specification<TodoEntity>
     */
    public static Specification<TodoEntity> conditionalCompleteAt(boolean done) {
        // 完了日ありの場合の抽出
        if (done) {
            return (root, query, builder) -> {
                return builder.isNotNull(root.get("compleateAt"));
            };
        }
        // 完了日なしの抽出
        return (root, query, builder) -> {
            return builder.isNull(root.get("compleateAt"));
        };
    };
}
