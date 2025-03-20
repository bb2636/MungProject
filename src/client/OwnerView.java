package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class OwnerView {
  private Scanner scanner;
  private PrintWriter out;
  private Scanner in;
  private Socket socket;
  private boolean inRoom = false;
  private Map<String, Owner> ownerMap = new HashMap<>();
  private String ownerName;

  public OwnerView(Scanner scanner) {
    this.scanner = scanner;
    try {
      this.socket = new Socket("localhost", 5000);
      this.out = new PrintWriter(socket.getOutputStream(), true);
      this.in = new Scanner(socket.getInputStream());
      System.out.println("✅ 서버에 연결되었습니다.");
      new Thread(this::listenForMessages).start();
    } catch (IOException e) {
      System.out.println("❌ 서버에 연결할 수 없습니다. 서버가 실행 중인지 확인하세요.");
      System.exit(1);
    }
  }

  // ✅ 서버에서 메시지를 수신하는 쓰레드
  private void listenForMessages() {
    while (in.hasNextLine()) {
      String message = in.nextLine();
      if (message.startsWith("📢") || message.startsWith("💬")) {
        System.out.println(message); // 방 내 공지 및 채팅 메시지
      } else {
        System.out.println("[서버] " + message);
      }
    }
  }

  public void startOwnerMode() {
    System.out.print("보호자 이름 입력: ");
    ownerName = scanner.nextLine();

    System.out.print("견종 크기 입력: ");
    String breed = scanner.nextLine();

    System.out.print("강아지 나이 입력: ");
    int age;
    while (true) {
      try {
        age = Integer.parseInt(scanner.nextLine());
        break;
      } catch (NumberFormatException e) {
        System.out.println("나이는 숫자로 입력해야 합니다. 다시 입력해주세요.");
      }
    }

    Owner owner = new Owner(ownerName, age, breed);
    ownerMap.put(ownerName, owner);

    System.out.println("\n=== 보호자 모드 ===");
    while (true) {
      System.out.println("1. 훈련사명 리스트(방 탐색)");
      System.out.println("2. 내정보 확인");
      System.out.println("3. 종료");
      System.out.print("✏️ 선택: ");

      int choice;
      try {
        choice = Integer.parseInt(scanner.nextLine());
      } catch (NumberFormatException e) {
        System.out.println("잘못된 입력입니다. 숫자를 입력해주세요.");
        continue;
      }

      switch (choice) {
        case 1:
          listTrainingRooms();
          break;
        case 2:
          displayOwnerInfo();
          break;
        case 3:
          System.out.println("❌ 클라이언트 종료");
          closeConnection();
          return;
        default:
          System.out.println("잘못된 선택입니다. 다시 입력해주세요.");
      }
    }
  }

  private void displayOwnerInfo() {
    Owner owner = ownerMap.get(ownerName);
    if (owner != null) {
      System.out.println(owner.getInfo());
    } else {
      System.out.println("❌ 보호자 정보를 찾을 수 없습니다.");
    }
  }

  private void listTrainingRooms() {
    out.println("/listRooms");
    System.out.println("📜 현재 방 목록을 불러옵니다...");
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.print("접속할 방 ID를 입력하세요: ");
    int roomId;
    try {
      roomId = Integer.parseInt(scanner.nextLine());
    } catch (NumberFormatException e) {
      System.out.println("잘못된 입력입니다. 숫자를 입력해주세요.");
      return;
    }
    enterTrainingRoom(roomId);
  }

  private void enterTrainingRoom(int roomId) {
    out.println("/joinRoom " + roomId + " " + ownerName);
    System.out.println("🏠 보호자 " + ownerName + "님이 방 ID " + roomId + "에 입장하였습니다.");
    inRoom = true;

    while (inRoom) {
      System.out.print("훈련 명령어 입력 (/exit): ");
      String command = scanner.nextLine();
      if (command.equals("/exit")) {
        System.out.println("🏠 방을 나갑니다.");
        out.println("/leaveRoom");
        inRoom = false;
        return;
      }
      out.println(command);
    }
  }

  private void closeConnection() {
    try {
      out.println("/exit");
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    OwnerView ownerView = new OwnerView(scanner);
    ownerView.startOwnerMode();
    scanner.close();
  }
}
