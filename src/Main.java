import admin.AdminView;
import client.OwnerView;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    while (true) {
      System.out.println("\n=== 멍훈련소 시스템 ===");
      System.out.println("1. 보호자로 로그인");
      System.out.println("2. 관리자로 로그인");
      System.out.println("3. 종료");
      System.out.print("✏️ 선택: ");

      int choice = scanner.nextInt();
      scanner.nextLine(); // 개행 문자 처리
      OwnerView ownerView = new OwnerView(scanner);
      switch (choice) {
        case 1:
          ownerView.startOwnerMode();

          break;
        case 2:
          AdminView adminView = new AdminView(scanner);
          adminView.startAdminView();
          break;
        case 3:
          System.out.println("🐾 멍훈련소 시스템을 종료합니다.");
          ownerView.shutdown(); // 🔥 종료 직전에만 서버 연결 종료!
          scanner.close();
          System.exit(0);
        default:
          System.out.println("❌ 잘못된 선택입니다. 다시 입력해주세요.");
      }
    }
  }
}
