"use client";
import { useState } from "react";

export default function SignUp() {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [pw, setPw] = useState("");

    const handleSignUp = async (e) => {
        e.preventDefault();
        e.preventDefault(); // 폼의 기본 제출 동작 방지

        const userData = { name, pw, email };

        try {
            const response = await fetch('http://localhost:8080/api/users/signup', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(userData),
            });

            if (response.ok) {
                const data = await response.json();
                // 성공 처리 (예: 사용자 리다이렉션)
                console.log('회원가입 성공:', data);
            } else {
                // 에러 처리
                console.error('회원가입 실패');
            }
        } catch (error) {
            console.error('에러 발생:', error);
        }
        console.log("Sign Up:", name, email, pw);
    };


    return (
        <div className="flex justify-center items-center h-screen">
            <form onSubmit={handleSignUp} className="flex flex-col">
                <h1 className="text-2xl mb-4">회원가입</h1>
                <input
                    type="name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    placeholder="이름"
                    required
                    className="mb-2"
                />
                <input
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="이메일"
                    required
                    className="mb-2"
                />
                <input
                    type="password"
                    value={pw}
                    onChange={(e) => setPw(e.target.value)}
                    placeholder="비밀번호"
                    required
                    className="mb-4"
                />
                <button type="submit" className="bg-blue-500 text-white py-2">회원가입</button>
            </form>
        </div>
    );
}