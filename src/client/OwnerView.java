package client;

import server.TrainerServer;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.*;
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
  static Connection conn = makeConnection();

  private static Connection makeConnection() {
    String url = "jdbc:mysql://localhost:3306/mung?serverTimezone=Asia/Seoul";
    Connection con = null;

    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      System.out.println("데이터베이스 연결 중...");
      con = DriverManager.getConnection(url, "root", "root");
      System.out.println("데이터베이스 연결 성공");
    } catch (ClassNotFoundException e) {
      System.out.println("JDBC 드라이버를 찾지 못했습니다.");
      System.out.println(e.getMessage());
    } catch (SQLException e) {
      System.out.println("데이터베이스 연결 실패");
    }
    return con;
  }
  public OwnerView(Scanner scanner) {
    this.scanner = scanner;
    try {
      this.socket = new Socket("127.0.0.1", 3306);
      this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
      this.in = new Scanner(new InputStreamReader(socket.getInputStream(), "UTF-8"));
      System.out.println("✅ 서버에 연결되었습니다.");
      new Thread(this::listenForMessages).start();
    } catch (IOException e) {
      System.out.println("❌ 서버에 연결할 수 없습니다. 서버가 실행 중인지 확인하세요.");
      System.exit(1);
    }
  }

  // 서버에서 메시지를 수신하는 쓰레드
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

    System.out.print("강아지 이름 입력: ");
    String dogName = scanner.nextLine();

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

    Owner owner = new Owner(ownerName, age, breed, dogName);
    TrainerServer.getOwnerMap().put(ownerName, owner);
    ownerMap.put(ownerName, owner);

    // 데이터베이스에 보호자 정보 저장
    saveOwnerToDatabase(owner);

    // 서버에 보호자 정보 등록 요청
    out.println("/register " + ownerName + " " + age + " " + breed + " " + dogName);

    System.out.println("\n=== 보호자 모드 ===");
    while (true) {
      System.out.println("1. 훈련사명 리스트(방 탐색)");
      System.out.println("2. 내정보 확인");
      System.out.println("3. 훈련 기록 조회");
      System.out.println("4. 종료");
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
          getTrainingHistory();
          break;
        case 4:
          System.out.println("뒤로 가기");
          return;
        default:
          System.out.println("잘못된 선택입니다. 다시 입력해주세요.");
      }
    }
  }

  private void saveOwnerToDatabase(Owner owner) {
    String ownerQuery = "INSERT INTO owner (name) VALUES (?)";
    String dogQuery = "INSERT INTO dog (dogName, age, breed, ownerId) VALUES (?, ?, ?, ?)";

    try {
      // 1. 보호자 정보 삽입
      try (PreparedStatement ownerPstmt = conn.prepareStatement(ownerQuery, Statement.RETURN_GENERATED_KEYS)) {
        ownerPstmt.setString(1, owner.getName());

        int ownerRowsAffected = ownerPstmt.executeUpdate();
        if (ownerRowsAffected > 0) {
          ResultSet rs = ownerPstmt.getGeneratedKeys();
          if (rs.next()) {
            int generatedId = rs.getInt(1); // 첫 번째 열의 값이 자동 생성된 ID
            System.out.println("보호자 정보가 데이터베이스에 저장되었습니다. 생성된 ID: " + generatedId);

            // 2. 개 정보 삽입
            try (PreparedStatement dogPstmt = conn.prepareStatement(dogQuery)) {
              dogPstmt.setString(1, owner.getDogName());
              dogPstmt.setInt(2, owner.getAge()); // 개의 나이
              dogPstmt.setString(3, owner.getBreed());
              dogPstmt.setInt(4, generatedId); // 보호자의 ID

              int dogRowsAffected = dogPstmt.executeUpdate();
              if (dogRowsAffected > 0) {
                System.out.println("개 정보가 데이터베이스에 저장되었습니다.");
              } else {
                System.out.println("개 정보를 저장하는 데 실패했습니다.");
              }
            }
          }
        } else {
          System.out.println("보호자 정보를 저장하는 데 실패했습니다.");
        }
      }
    } catch (SQLException e) {
      System.out.println("데이터베이스 오류: " + e.getMessage());
    }
  }


  public Map<String, Owner> getOwnerMap() {
    return new HashMap<>(ownerMap); // ownerMap의 복사본을 반환
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

  // 특정 방 입장 후 채팅 및 훈련 명령어
  private void enterTrainingRoom(int roomId) {
    out.println("/joinRoom " + roomId);
    System.out.println("🏠 보호자 " + ownerName + "님이 방 ID " + roomId + "에 입장하였습니다.");
    inRoom = true;
    System.out.print("훈련 명령어 입력 (/sit, /stay, /fetch, /exit): ");
    while (inRoom) {
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

  // 훈련 기록 조회
  private void getTrainingHistory() {
    System.out.println("📜 훈련 기록을 조회 중...");
    out.println("/getHistory");

    try {
      while (in.hasNextLine()) {
        String message = in.nextLine();
        if (message.startsWith("📜")) {
          System.out.println(message);
        } else if (message.startsWith("❌")) { // 기록이 없을 경우
          System.out.println(message);
          break;
        }
      }
    } catch (Exception e) {
      System.out.println("❌ 훈련 기록을 가져오는 중 오류 발생: " + e.getMessage());
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

  public void shutdown() {
    closeConnection(); // socket 닫고 서버에 /exit 보내기
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    OwnerView ownerView = new OwnerView(scanner);
    ownerView.startOwnerMode();
    scanner.close();
  }
}