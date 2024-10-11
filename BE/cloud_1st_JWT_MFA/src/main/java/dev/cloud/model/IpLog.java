package dev.cloud.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ip_log") // 테이블 명시
public class IpLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "member_id", nullable = false) // 외래 키 명시적으로 지정
    private Member member;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime loginDate; // 필드 이름 수정

    private String ipAddress;

}
