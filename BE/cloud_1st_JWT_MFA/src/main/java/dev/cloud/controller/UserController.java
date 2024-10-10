package dev.cloud.controller;


import dev.cloud.model.User;
import dev.cloud.record.UserDTO;
import dev.cloud.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDTO userDTO) {
        // 회원가입 요청 변환 및 저장
        User newUser = userService.signUp(userDTO.toEntity());
        // 기본 응답 반환
        return ResponseEntity.ok(UserDTO.basicResponse(newUser));
    }
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody UserDTO userDTO) {
        // 회원가입 요청 변환 및 저장
        if(userService.signin(userDTO.toEntity())){
            return ResponseEntity.ok("login successfully");
        }
        // 기본 응답 반환
        return ResponseEntity.ok("login failed");
    }
}
