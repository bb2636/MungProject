package client;

import java.util.Scanner;

public class OwnerView {
  private Owner owner;
  private Scanner scanner;

  public OwnerView(Scanner scanner) {
    this.scanner = scanner;
  }

  public void startOwnerMode() {
    System.out.println("=== 보호자 모드 ===");
    System.out.print("보호자 이름 입력: ");
    String ownerName = scanner.nextLine();
    System.out.print("견종 크기 입력: ");
    String breed = scanner.nextLine();
    System.out.print("강아지 나이 입력: ");
    int age = scanner.nextInt();
    scanner.nextLine();  // 개행 문자 처리

    owner = new Owner(ownerName, age, breed);

    while (true) {
      System.out.println("\n=== 보호자 모드 ===");
      System.out.println("1. 훈련사명 리스트(방 탐색)");
      System.out.println("2. 훈련 기록 조회 (/history)");
      System.out.println("3. 내정보 확인");
      System.out.println("4. 나가기");
      System.out.print("✏️ 선택: ");
      int choice = scanner.nextInt();
      scanner.nextLine(); // 개행 문자 처리

      switch (choice) {
        case 1:
          enterTrainingRoom();
          break;
        case 2:
          viewTrainingHistory();
          break;
        case 3:
          //내정보 확인
          System.out.println(owner.getInfo());
          break;
        case 4:
          System.out.println("나가기");
          return; // 보호자 모드 종료
        default:
          System.out.println("잘못된 선택입니다. 다시 입력해주세요.");
      }
    }
  }

  private void enterTrainingRoom() {
    System.out.println("접속할 방 ID를 선택하세요.");
    System.out.print("✏️ 입력: ");
    int roomId = scanner.nextInt();
    scanner.nextLine();

    while (true) {
      System.out.println("훈련 명령어 입력 (/sit, /stay, /fetch, /exit)");
      System.out.print("✏️ 입력: ");
      String command = scanner.nextLine();
      if (command.equals("/exit")) {
        System.out.println("훈련 방을 나갑니다.");
        break;
      }
      // owner.executeCommand(command);  // 훈련 명령 실행
    }
  }

  private void viewTrainingHistory() {
    System.out.println("훈련 기록을 조회합니다.");
    // owner.viewTrainingHistory();
  }

  // 📌 🔥 **메인 메서드 추가** → OwnerView를 단독 실행 가능하게 변경
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    OwnerView ownerView = new OwnerView(scanner);
    ownerView.startOwnerMode();
    scanner.close();
  }
}
