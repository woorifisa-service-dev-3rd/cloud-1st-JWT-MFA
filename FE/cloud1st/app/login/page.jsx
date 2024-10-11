"use client";
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import Modal from "../../components/Modal";

export default function Login() {
  const [memberIp, setMemberIp] = useState("");
  const [email, setEmail] = useState("");
  const [pw, setPw] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [modalMessage, setModalMessage] = useState("");
  const router = useRouter();
  useEffect(() => {
    // https://jsonip.com을 사용하여 IP 주소 가져오기
    fetch("https://jsonip.com")
      .then(response => response.json())
      .then(data => setMemberIp(data.ip))
      .catch(error => console.error("IP 주소를 가져오는 중 오류 발생:", error));
  }, []);
  const handleLogin = async (e) => {
    e.preventDefault();
    const userData = { email, pw, memberIp };

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
        if (data.success) {
          // 로그인 성공 후 이메일 인증 페이지로 이동
          router.push(`/verify-email?email=${email}`);
        } else {
          setModalMessage("로그인 실패: 이메일 또는 비밀번호를 확인해주세요.");
          setShowModal(true);
        }
      } else {
        setModalMessage("로그인 실패: 이메일 또는 비밀번호를 확인해주세요.");
        setShowModal(true);
      }
    } catch (error) {
      setModalMessage("에러 발생: 서버 오류가 발생했습니다. 다시 시도해주세요.");
      setShowModal(true);
    }
  };

  const handleModalClose = () => {
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
        <Modal
          message={modalMessage}
          onClose={handleModalClose}
        />
      )}
    </div>
  );
}