package admin;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AdminView {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    Map<String, Admin> adminMap = new HashMap<>();
    String adminView =
        """
            ===관리자 모드===
            1.보호자 목록 조회
            2.훈련사 등록
            3.훈련 프로그램 추가
            4.나가기
            """;
    while (true) {
      System.out.println(adminView);
      System.out.print("✏️ 선택: ");
      int choice = in.nextInt();
      switch (choice) {
        case 1:
          //보호자 목록 조회
        case 2:
          //Admin a1 = new newTriner(trainerName, specialtyDogBreeds);
          System.out.println("새로 등록될 훈련사의 이름을 입력하세요");
          String newTrainerName = in.nextLine();
          System.out.println("훈련사의 전문 견종을 입력하세요(소/중/대)");
          String newTrainerSpecDog = in.nextLine();
          Admin newTrainer = new Admin(newTrainerName,newTrainerSpecDog);
          adminMap.put(newTrainerName, newTrainer);
          System.out.println("훈련사 등록이 완료되었습니다.");
          System.out.println(newTrainer);
          break;
        case 3:
          //프로그램 등록
        case 4:
          System.out.println("나가기");
          return;
        default:
          System.out.println("잘못된 선택입니다. 다시 입력해주세요");
      }
    }
  }
}
