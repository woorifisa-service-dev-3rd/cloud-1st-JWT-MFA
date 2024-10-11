package dev.cloud.model;


import jakarta.persistence.*;


@Entity
public class IpLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;
}
