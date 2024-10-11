package dev.cloud.repository;

import dev.cloud.model.IpLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IpLogRepository extends JpaRepository<IpLog, Long> {
    List<IpLog> findByMemberIdAndLoginDateAfter(Long memberId, LocalDateTime oneWeekAgo);
}
