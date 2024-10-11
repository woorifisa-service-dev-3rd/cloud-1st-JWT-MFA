package dev.cloud.service;

import dev.cloud.dto.EmailAuthResponseDto;
import dev.cloud.dto.MemberResponseDto;
import dev.cloud.model.Member;
import dev.cloud.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberResponseDto findMemberInfoById(Long userId) {
        return memberRepository.findById(userId).
                map(MemberResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
    }

    public MemberResponseDto findMemberInfoByEmail(String email) {
        return memberRepository.findByEmail(email)
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
    }

    public Member signUp(Member newMember) {
        // 사용자 존재 여부 확인
        if (memberRepository.existsByEmail(newMember.getEmail())){
            log.info("{} 이미 존재하는 이메일");
            return  Member.builder().name("이미 존재하는 이메일").build();
        }
        return memberRepository.save(newMember);
    }

    public boolean signin(Member entity) {
        Optional<Member> findedUser = memberRepository.findByEmail(entity.getEmail());
        if (findedUser.isPresent()) {
            if (findedUser.get().getPw().equals(entity.getPw())) {
                return true;
            }
        }
        return false;
    }
}
