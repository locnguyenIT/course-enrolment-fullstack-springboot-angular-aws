package com.example.demo.result;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Embeddable
public class ResultID implements Serializable {

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_id")
    private Integer courseId;
}
