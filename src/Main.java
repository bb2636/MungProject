import client.Dog;
import client.Owner;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    Owner owner = new Owner();

    System.out.println("=== 멍훈련소 시스템 ===");
    System.out.println("1. 보호자로 로그인");
    System.out.println("2. 훈련사로 로그인");
    System.out.println("3. 종료");
    System.out.print("✏️ 선택: ");
    int userType = scanner.nextInt();
    scanner.nextLine();

    if (userType == 1) {
      System.out.println("=== 보호자 모드 ===");
      System.out.print("보호자 이름 입력: ");
      String name = scanner.nextLine();
      System.out.print("견종 크기 입력: ");
      String breed = scanner.nextLine();
      System.out.print("강아지 나이 입력: ");
      int age = scanner.nextInt();
      scanner.nextLine();
//      Dog dog = new Dog(name, age, breed);
//      Owner owner = new Owner(name, dog, server);
//      owner.connectServer();

      while (true) {
        System.out.println("\n=== 보호자 모드 ===");
        System.out.println("1. 훈련사명 리스트(방 탐색)");
        System.out.println("2. 훈련 실행 (/sit, /stay, /fetch)");
        System.out.println("3. 훈련 기록 조회 (/history)");
        System.out.println("4. 훈련사와 채팅 (/chat)");
        System.out.println("5. 나가기");
        System.out.print("✏️ 선택: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
          case 1:
//            owner.listRooms();
            break;
          case 2:
            System.out.print("훈련 명령어 입력 (/sit, /stay, /fetch): ");
            String command = scanner.nextLine();
//            owner.executeCommand(command);
            break;
          case 3:
//            owner.viewTrainingHistory();
            break;
          case 4:
            System.out.print("채팅 입력: ");
            String message = scanner.nextLine();
//            owner.sendMessage(message);
            break;
          case 5:
            System.out.println("나가기");
            return;
          default:
            System.out.println("잘못된 선택입니다. 다시 입력해주세요.");
        }
      }
    } else if (userType == 2) {
      System.out.println("훈련사 모드는 아직 구현되지 않았습니다.");
    } else {
      System.out.println("프로그램을 종료합니다.");
    }
  }
}
