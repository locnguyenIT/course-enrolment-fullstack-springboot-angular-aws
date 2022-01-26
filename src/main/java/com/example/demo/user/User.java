package com.example.demo.user;

import com.example.demo.enrolment.Enrolment;
import com.example.demo.result.Result;
import com.example.demo.role.Role;
import com.example.demo.resetpassword.token.Token;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@Table(name = "user",uniqueConstraints = {
        @UniqueConstraint(name = "user_name_unique",columnNames = "name"),
        @UniqueConstraint(name = "user_email_unique",columnNames = "email"),
})
public class User {

    @Id
    @SequenceGenerator(name = "user_sequence",sequenceName = "user_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_sequence")
    @Column(name = "id",nullable = false)
    private Integer id;

    @NotEmpty(message = "Name should not be empty")
    @Column(name = "name",nullable = false)
    private String name;

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email invalid")
    @Column(name = "email",nullable = false)
    private String email; //Username

    @NotEmpty(message = "Password should not be empty")
    @Size(min = 10,message = "Password should be at least 10 characters")
    @Column(name = "password",columnDefinition = "TEXT",nullable = false)
    private String password;

    @Column(name = "imageURL",columnDefinition = "TEXT",nullable = false)
    private String imageURL;

    @Column(name = "address",columnDefinition = "TEXT")
    private String address;

    @Column(name = "register_at",columnDefinition = "DATETIME",nullable = false)
    private LocalDateTime register_at;

    @Column(name = "enable",nullable = false)
    private boolean enable;


    @ManyToOne
    @JoinColumn(name = "role_id",
                referencedColumnName = "id",
                nullable = false,
                foreignKey = @ForeignKey(name = "user_role_fk"))
    private Role role;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Enrolment> listEnrolment = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Result> listResult = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Token> listToken = new ArrayList<>();

    public User(String name, String email, String password, String imageURL,
                String address, LocalDateTime register_at, boolean enable, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.imageURL = imageURL;
        this.address = address;
        this.register_at = register_at;
        this.enable = enable;
        this.role = role;
    }

    //Constructor for Registration
    public User(String name, String email, String password,String imageURL, LocalDateTime register_at, boolean enable, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.imageURL = imageURL;
        this.register_at = register_at;
        this.enable = enable;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getRegister_at() {
        return register_at;
    }

    public void setRegister_at(LocalDateTime register_at) {
        this.register_at = register_at;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
