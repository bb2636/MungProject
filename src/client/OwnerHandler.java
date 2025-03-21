package client;

import java.util.Map;

public class OwnerHandler {
  private static final Map<String, Owner> ownerMap = TrainerServer.getOwnerMap();

  // ✅ 보호자 정보 등록
  public static void registerOwner(String clientName, int age, String breed, String dogName) {
    ownerMap.put(clientName, new Owner(clientName, age, breed,dogName));
    System.out.println("✅ 보호자 등록: " + clientName);
  }

  // ✅ 보호자 정보 가져오기
  public static String getClientInfo(String clientName) {
    Owner owner = ownerMap.get(clientName);
    if (owner != null) {
      return owner.getName();
    }
    return clientName;
  }

  // ✅ 보호자 삭제 (방을 나갈 때)
  public static void removeOwner(String clientName) {
    ownerMap.remove(clientName);
    System.out.println("🚪 보호자 " + clientName + " 정보 삭제 완료");
  }

  // ✅ 훈련 명령어 실행 및 저장
  public static String trainDog(String clientName, String command) {
    Owner owner = ownerMap.get(clientName);
    if (owner == null) {
      return "❌ 보호자를 찾을 수 없습니다.";
    }

    String response;
    switch (command) {
      case "/sit":
        response = "🐶 " + owner.getName() + "의 강아지가 '앉기' 훈련을 완수했습니다!";
        break;
      case "/stay":
        response = "🐶 " + owner.getName() + "의 강아지가 '기다리기' 훈련을 완수했습니다!";
        break;
      case "/fetch":
        response = "🐶 " + owner.getName() + "의 강아지가 '공 가져오기' 훈련을 완수했습니다!";
        break;
      default:
        return "❌ 유효하지 않은 훈련 명령어입니다.";
    }

    owner.getDog().train(command); // 🐶 훈련 상태 저장
    return response;
  }
}
