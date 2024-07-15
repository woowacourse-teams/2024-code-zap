package codezap.template.domain;

import jakarta.persistence.*;

import codezap.global.domain.BaseTimeEntity;
import codezap.member.domain.Member;
import lombok.Getter;

@Entity
@Table(name = "template")
@Getter
public class Template extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String title;
}
