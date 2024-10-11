package dev.cloud.service;

import dev.cloud.model.IpLog;
import dev.cloud.model.Member;
import dev.cloud.repository.IpLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class IpLogService {

    private final IpLogRepository ipLogRepository;


    public boolean checkAndSave(Member member, String memberIp) {
        // 최근 1주일 내의 로그인 기록 가져오기
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
        List<IpLog> recentIpLogs = ipLogRepository.findByMemberIdAndLoginDateAfter(member.getId(), oneWeekAgo);
        log.info("{}",recentIpLogs);


        // 로그인 시도했으니 저장
        ipLogRepository.saveAndFlush(IpLog.builder().member(member).ipAddress(memberIp).loginDate(LocalDateTime.now()).build());

        // 최근 기록 중에 현재 IP가 있는지 확인
        boolean isIpRecognized = recentIpLogs.stream()
                .anyMatch(log -> log.getIpAddress() != null && log.getIpAddress().equals(memberIp));

        if (isIpRecognized) {
            // IP가 최근 기록에 있는 경우: 로그인 허용
            log.info("{} 냐?", isIpRecognized);
            return true;
        } else {
            return false;
        }
    }
}
