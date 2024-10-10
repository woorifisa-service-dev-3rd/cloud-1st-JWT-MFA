import Link from "next/link";

export default function Home() {
  return (
    <div className="flex flex-col items-center justify-center h-screen">
      <h1 className="text-3xl mb-6">환영합니다!</h1>
      <Link href="/sign-up">
        <div className="bg-blue-500 text-white px-4 py-2 mb-4">회원가입</div>
      </Link>
      <Link href="/login">
        <div className="bg-green-500 text-white px-4 py-2">로그인</div>
      </Link>
    </div>
  );
}