"use client";

import { useRouter } from "next/navigation";

export default function SpecialPage() {
  const router = useRouter();
  return (
    <>
      <div className="flex justify-center items-center h-20">
        <h1 className="text-4xl">이곳은 인증된 사용자만 볼 수 있는 특별한 페이지입니다!</h1>
      </div>
      <div>
        <button className="border-black w-1/4 h-1/4" onClick={() => router.push('/')}>to home</button>
      </div>
    </>
  );
}
