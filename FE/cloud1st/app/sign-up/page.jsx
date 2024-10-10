"use client";
import { useState } from "react";
import { useRouter } from "next/navigation";
import Modal from "../../components/Modal";

export default function SignUp() {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [pw, setPw] = useState("");
    const [showModal, setShowModal] = useState(false);
    const router = useRouter();

    const handleSignUp = async (e) => {
        e.preventDefault();
        const userData = { name, pw, email };

        try {
            const response = await fetch("http://localhost:8080/api/auth/signup", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(userData),
            });

            if (response.ok) {
                setShowModal(true);
            } else {
                console.error("회원가입 실패");
            }
        } catch (error) {
            console.error("에러 발생:", error);
        }
    };

    const handleModalClose = () => {
        setShowModal(false);
        router.push("/"); // 메인 홈 페이지로 이동
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
                <button type="submit" className="bg-blue-500 text-white py-2">
                    회원가입
                </button>
            </form>
            {showModal && (
                <Modal
                    message="회원가입이 완료되었습니다."
                    onClose={handleModalClose}
                />
            )}
        </div>
    );
}
