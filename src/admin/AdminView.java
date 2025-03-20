package admin;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AdminView {
  private Admin admin;
  private Scanner in;
  private static Map<String, Admin> adminMap;

  public AdminView(Scanner in) {
    this.in = in;
    this.adminMap = new HashMap<>();
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
        addTrainingProgram(in, adminMap);
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
  public static void addTrainingProgram(Scanner in, Map<String, Admin> adminMap){
    Map<String, TrainingProgram> trainingProgramMap = new HashMap<>();
    while (true) {
      System.out.println("새로 등록될 프로그램 이름을 입력하세요");
      System.out.print("입력 : ");
      String newProgramName = in.nextLine();

      System.out.println("훈련사 이름을 입력하세요");
      System.out.print("입력 : ");
      String trainerName = in.nextLine();
      Admin trainer = adminMap.get(trainerName); // 훈련사 이름으로 adminMap에서 훈련사 정보를 찾기
      if (trainer == null) {
        System.out.println("해당 이름의 훈련사가 존재하지 않습니다. 다시 입력해주세요.");
        continue; // 훈련사가 없으면 다시 입력
      }
      // 전문 견종 정보 가져오기
      String specialtyDogBreeds = trainer.getSpecialtyDogBreeds(); // Admin 클래스에 이 메서드가 있어야 함

      // TrainingProgram 객체 생성
      TrainingProgram newProgram = new TrainingProgram(newProgramName, trainerName, specialtyDogBreeds);
      trainingProgramMap.put(newProgramName, newProgram);
      System.out.println("프로그램 등록이 완료되었습니다");
      System.out.println(newProgram);
      break; // 프로그램 등록 후 루프 종료
    }
  }

    public static void main (String[]args){
    Scanner in = new Scanner(System.in);
    AdminView adminView = new AdminView(in);
    adminView.startAdminView();
    in.close();
  }
}