package codezap.member.domain;

import jakarta.persistence.*;

import codezap.global.domain.BaseTimeEntity;
import lombok.Getter;

@Entity
@Table(name = "member")
@Getter
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;
}
