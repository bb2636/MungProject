# 🐾 멍! 훈련소 - Dog Training Chat System

> 보호자와 훈련사가 실시간으로 소통하며 강아지를 훈련할 수 있는  
> **콘솔 기반 멀티룸 채팅 & 훈련 시스템**입니다.

---

## 📌 프로젝트 소개

이 프로젝트는 Java 네트워크 프로그래밍을 기반으로  
**보호자(Client)** 와 **훈련사(Server)** 가 실시간으로:

- 💬 채팅
- 🐶 훈련 명령 실행 (`/sit`, `/fetch` 등)
- 📜 훈련 기록 저장 및 조회

등의 기능을 사용할 수 있는 **1:N 채팅 시스템**입니다.

### ✅ 주요 특징

- 🔁 **멀티룸 구조**: 실시간 방 생성, 입장, 퇴장 가능  
- 🐕‍🦺 **강아지 정보 연동**  
- 📂 **훈련 기록 저장 및 출력**  
- ⚙️ **스레드풀 기반 다중 클라이언트 처리**

---

## 💻 실행 방법

### 🔸 서버 실행
```bash
cd server
javac TrainerServer.java
java server.TrainerServer


📦mung-project/
├─ 📁 client/
│  ├─ OwnerView.java         # 보호자 UI + 명령 처리
│  ├─ Owner.java             # 보호자 클래스
│  ├─ Dog.java               # 강아지 정보
├─ 📁 server/
│  ├─ TrainerServer.java     # 메인 서버 + ThreadPool
│  ├─ ClientHandler.java     # 클라이언트 통신 처리
│  ├─ ChatRoom.java          # 방 관리 클래스
├─ 📁 admin/
│  ├─ Admin.java             # 훈련사 객체
│  ├─ TrainingProgram.java   # 훈련 프로그램
│  ├─ AdminView.java         # 훈련사 UI
├─ README.md

🧩 주요 기능
기능	설명
/register	보호자 등록
/saveAdmin	훈련사 + 프로그램 등록 및 방 생성
/listRooms	현재 참여 가능한 방 목록 조회
/joinRoom [ID]	방 입장
/sit, /stay, /fetch	강아지 훈련 명령 실행
/getHistory	훈련 기록 조회
/leaveRoom, /exit	퇴장 및 종료

 사용 기술
☕ Java 21

🌐 Java Socket (TCP 기반)

🧵 Multi-threading + Thread Pool (ExecutorService)

📡 UTF-8 문자 인코딩 통신

🧱 MVC 유사 구조 분리
