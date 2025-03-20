package admin;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AdminView {
  private Admin admin;
  private Scanner in;

  public AdminView(Scanner in) {
    this.in = in;
  }

public void startAdminView() {
  String adminView =
      """
          ===관리자 모드===
          1.보호자 목록 조회
          2.훈련사 등록
          3.훈련 프로그램 추가
          4.나가기
          """;
  while(true) {
    System.out.println(adminView);
    System.out.print("✏️ 선택: ");
    int choice = in.nextInt();
    in.nextLine();// 버퍼 비우기
    switch (choice) {
      case 1:
        //보호자 목록 조회
        //owner.getInfo()
        break;
      case 2:
        registerTrainer(in);
        break;
      case 3:
        //프로그램 등록
        break;
      case 4:
        System.out.println("나가기");
        return;
      default:
        System.out.println("잘못된 선택입니다. 다시 입력해주세요");
      }
    }
  }
  private static void registerTrainer(Scanner in){//새로운 훈련사 등록
    Map<String, Admin> adminMap = new HashMap<>();
    while (true) {

      System.out.println("새로 등록될 훈련사의 이름을 입력하세요");
      System.out.print("입력 : ");
      String newTrainerName = in.nextLine(); // 훈련사 이름 입력

      System.out.println("훈련사의 전문 견종을 입력하세요(소/중/대)");
      System.out.print("입력 : ");
      String newTrainerSpecDog = in.nextLine(); // 전문 견종 입력

      Admin newTrainer = new Admin(newTrainerName, newTrainerSpecDog);
      adminMap.put(newTrainerName, newTrainer);
      System.out.println("훈련사 등록이 완료되었습니다.");
      System.out.println(newTrainer);
      break;
    }
  }

    public static void main (String[]args){
    Scanner in = new Scanner(System.in);
    AdminView adminView = new AdminView(in);
    adminView.startAdminView();
    in.close();
  }
}