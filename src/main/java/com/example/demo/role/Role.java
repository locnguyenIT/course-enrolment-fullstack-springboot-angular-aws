package com.example.demo.role;

import com.example.demo.user.User;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@Table(name = "role", uniqueConstraints = {
                        @UniqueConstraint(name = "name_unique",columnNames = "name")
})
public class Role {

    @Id
    @SequenceGenerator(name = "role_sequence",sequenceName = "role_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "role_sequence")
    @Column(name = "id",nullable = false)
    private Integer id;

    @Column(name = "name",nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumRole name;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "role")
    private List<User> user = new ArrayList<>();

    public Role(EnumRole name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EnumRole getName() {
        return name;
    }

    public void setName(EnumRole name) {
        this.name = name;
    }
}
