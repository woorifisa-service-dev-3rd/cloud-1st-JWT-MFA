package dev.cloud.controller;

import dev.cloud.dto.UserResponseDto;
import dev.cloud.service.UserService;
import dev.cloud.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    // 내정보 가져오기
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> findUserInfoById() {
        return ResponseEntity.ok(userService.findUserInfoById(SecurityUtil.getCurrentUserId()));
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserResponseDto> findUserInfoByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findUserInfoByEmail(email));
    }
}
