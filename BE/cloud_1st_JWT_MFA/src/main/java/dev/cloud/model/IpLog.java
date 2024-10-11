package dev.cloud.model;


import jakarta.persistence.*;

import java.util.Date;


@Entity
public class IpLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;

    private Date LoginDate;
}
