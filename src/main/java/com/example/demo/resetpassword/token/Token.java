package com.example.demo.resetpassword.token;

import com.example.demo.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Token {

    @Id
    @SequenceGenerator(name = "token_sequence",sequenceName = "token_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "token_sequence")
    @Column(name = "id",nullable = false)
    private Integer id;

    @Column(name = "token",columnDefinition = "TEXT",nullable = false)
    private String token;

    @Column(name = "create_at",columnDefinition = "DATETIME",nullable = false)
    private LocalDateTime create_at;

    @Column(name = "expired_at",columnDefinition = "DATETIME",nullable = false)
    private LocalDateTime expired_at;

    @Column(name = "confirmed_at",columnDefinition = "DATETIME")
    private LocalDateTime confirmed_at;

    @ManyToOne
    @JoinColumn(name = "user_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "token_user_fk"))
    private User user;

    public Token(String token, LocalDateTime create_at, LocalDateTime expired_at, User user) {
        this.token = token;
        this.create_at = create_at;
        this.expired_at = expired_at;
        this.user = user;
    }
}
