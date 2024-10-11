"use client";
import { useRouter } from "next/navigation";
import { useState } from "react";

export default function Login() {
  const [email, setEmail] = useState("");
  const [pw, setPw] = useState("");
  const router = useRouter();

  const handleLogin = async (e) => {
    e.preventDefault();
    // TODO: API를 호출해 로그인 처리
    e.preventDefault(); // 폼의 기본 제출 동작 방지

    const userData = { email, pw };

    try {
      const response = await fetch('http://localhost:8080/api/auth/signin', {
        method: 'post',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData),
      });

      if (response.ok) {
        const data = await response.json();
        // 성공 처리 (예: 사용자 리다이렉션)
        console.log('로긴 성공:', data);
        router.push(`/verify-email?email=${encodeURIComponent(email)}`);
      } else {
        // 에러 처리
        console.error('로긴 실패');
      }
    } catch (error) {
      console.error('에러 발생:', error);
    }

    console.log("Login:", email, pw);
  };

  return (
    <div className="flex justify-center items-center h-screen">
      <form onSubmit={handleLogin} className="flex flex-col">
        <h1 className="text-2xl mb-4">로그인</h1>
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
        <button type="submit" className="bg-green-500 text-white py-2">로그인</button>
      </form>
    </div>
  );
}