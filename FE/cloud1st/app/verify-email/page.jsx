"use client";
import { useState } from "react";
import { useSearchParams } from "next/navigation";
import { useRouter } from "next/navigation";

export default function VerifyEmail() {
  const searchParams = useSearchParams();
  const email = searchParams.get("email");
  const [authCode, setAuthCode] = useState("");
  const [message, setMessage] = useState("");
  const router = useRouter();
  const handleVerify = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch('http://localhost:8080/api/auth/smtp', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          email: email,
          authCode: authCode
        }),
      });

      if (response.ok) {
        const data = await response.json();
        // 쿠키에 액세스 토큰과 리프레시 토큰 저장
        document.cookie = `accessToken=${data.accessToken}; path=/; secure; samesite=strict`;
        document.cookie = `refreshToken=${data.refreshToken}; path=/; secure; samesite=strict`;

        setMessage("이메일 인증이 완료되었습니다.");
        router.push('/special-page');
      } else {
        setMessage("인증 실패: 인증 코드를 확인해주세요.");
      }
    } catch (error) {
      console.error("에러 발생:", error);
      setMessage("서버 오류가 발생했습니다. 다시 시도해주세요.");
    }
  };

  return (
    <div className="flex justify-center items-center h-screen">
      <form onSubmit={handleVerify} className="flex flex-col">
        <h1 className="text-2xl mb-4">이메일 인증</h1>
        <p className="mb-4">이메일: {email}</p>
        <input
          type="text"
          value={authCode}
          onChange={(e) => setAuthCode(e.target.value)}
          placeholder="인증 코드 입력"
          required
          className="mb-4"
        />
        <button type="submit" className="bg-blue-500 text-white py-2">
          인증하기
        </button>
        {message && <p className="mt-4">{message}</p>}
      </form>
    </div>
  );
}