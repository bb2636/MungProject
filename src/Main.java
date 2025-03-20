import client.Dog;
import client.Owner;
import server.Server;

import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
//    Server server = new Server();

    System.out.println("=== 멍훈련소 시스템 ===");
    System.out.println("1. 보호자로 로그인");
    System.out.println("2. 종료");
    System.out.print("✏️ 선택: ");
    int userType = scanner.nextInt();
    scanner.nextLine();

    if (userType == 1) {
      System.out.println("=== 보호자 모드 ===");
      System.out.print("보호자 이름 입력: ");
      String ownerName = scanner.nextLine();
      System.out.print("견종 크기 입력: ");
      String breed = scanner.nextLine();
      System.out.print("강아지 나이 입력: ");
      int age = scanner.nextInt();
      scanner.nextLine();
      Owner owner = new Owner(ownerName, age,breed);
//      owner.connectServer();

      while (true) {
        System.out.println("\n=== 보호자 모드 ===");
        System.out.println("1. 훈련사명 리스트(방 탐색)");
        System.out.println("2. 훈련 기록 조회 (/history)");
        System.out.println("3. 내정보 확인");
        System.out.println("4. 나가기");
        System.out.print("✏️ 선택: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
          case 1:
//            List<Room> rooms = owner.listRooms();
//            if (rooms.isEmpty()) {
//              System.out.println("현재 접속 가능한 방이 없습니다.");
//              break;
//            }
            System.out.println("접속할 방 ID를 선택하세요.");
            System.out.print("✏️ 입력: ");
            int roomId = scanner.nextInt();
            scanner.nextLine();
//            owner.joinRoom(roomId);
            while (true) {
              System.out.println("훈련 명령어 입력 (/sit, /stay, /fetch, /exit)");
              System.out.print("✏️ 입력: ");
              String command = scanner.nextLine();
              if (command.equals("/exit")) {
//                owner.leaveRoom();
                break;
              }
//              owner.executeCommand(command);
            }
            break;
          case 2:
//            owner.viewTrainingHistory();
            break;
          case 3:
            System.out.println(owner.getInfo());
            break;
          case 4:
            System.out.println("나가기");
            return;
          default:
            System.out.println("잘못된 선택입니다. 다시 입력해주세요.");
        }
      }
    } else {
      System.out.println("프로그램을 종료합니다.");
    }
  }
}
