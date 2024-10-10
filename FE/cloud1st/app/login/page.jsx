"use client";
import { useState } from "react";
import { useRouter } from "next/navigation";
import Modal from "../../components/Modal"; // 모달 컴포넌트 임포트

export default function Login() {
  const [email, setEmail] = useState("");
  const [pw, setPw] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [modalMessage, setModalMessage] = useState("");
  const router = useRouter();

  const handleLogin = async (e) => {
    e.preventDefault();
    const userData = { email, pw };

    try {
      const response = await fetch("http://localhost:8080/api/auth/signin", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(userData),
      });

      if (response.ok) {
        const data = await response.json();
        if (data.emailSent) {
          setModalMessage("로그인 성공! 이메일 인증 페이지로 이동합니다.");
          setShowModal(true);

          // 모달이 닫힌 후 이메일 인증 페이지로 이동
          setTimeout(() => {
            setShowModal(false);
            router.push(`/verify-email?email=${email}`);
          }, 2000);
        }
      } else {
        setModalMessage("로그인 실패. 다시 시도해주세요.");
        setShowModal(true);
      }
    } catch (error) {
      setModalMessage("에러 발생: 서버와 통신할 수 없습니다.");
      setShowModal(true);
    }
  };

  const closeModal = () => {
    setShowModal(false);
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
        <button type="submit" className="bg-green-500 text-white py-2">
          로그인
        </button>
      </form>
      {showModal && (
        <Modal message={modalMessage} onClose={closeModal} />
      )}
    </div>
  );
}
