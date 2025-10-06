package jp.co.eaz.todo_handson_api.model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Data
@Table(name = "todos")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class TodoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Integer todoId;
    
    @Column(name = "todo_text")
    private String todoText;

    // @ManyToOne:userIdは使用するタイミングでDBから取得を行う
    @JoinColumn(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;
    
    @Column(name = "compleate_at")
    private Timestamp compleateAt;
}
