"use client";
import { useState } from "react";

export default function SignUp() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const handleSignUp = async (e) => {
        e.preventDefault();
        // TODO: API를 호출해 회원가입 처리
        console.log("Sign Up:", email, password);
    };

    return (
        <div className="flex justify-center items-center h-screen">
            <form onSubmit={handleSignUp} className="flex flex-col">
                <h1 className="text-2xl mb-4">회원가입</h1>
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
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="비밀번호"
                    required
                    className="mb-4"
                />
                <button type="submit" className="bg-blue-500 text-white py-2">회원가입</button>
            </form>
        </div>
    );
}