package client;

import java.util.HashMap;
import java.util.Map;

public class Owner {
  private String name;
  private Dog dog;
  private Map<String, String> trainingStatus;

  public Owner(String name, int age, String breed, String dogName) {
    this.name = name;
    this.dog = new Dog(dogName, age, breed);
    trainingStatus = new HashMap<>();
  }

  // 보호자 정보 조회
  public String getInfo() {
    return "👤 보호자: " + name + "\n" + dog.getInfo();
  }

  public String getName() {
    return name;
  }

  public int getAge() {
    return dog.getAge();
  }

  public String getBreed() {
    return dog.getBreed();
  }
  public String getName2(){
    return dog.getName();
  }
  public Dog getDog() {
    return dog;
  }
  // ✅ 강아지 훈련 상태 저장
  public void train(String command) {
    String message = switch (command) {
      case "/sit" -> "🐾 강아지가 '앉기' 훈련을 완료했습니다.";
      case "/stay" -> "🐾 강아지가 '기다리기' 훈련을 완료했습니다.";
      case "/fetch" -> "🐾 강아지가 '공 가져오기' 훈련을 완료했습니다.";
      default -> "❌ 잘못된 훈련 명령어입니다.";
    };
    trainingStatus.put(command, message);
  }

  // ✅ 훈련 상태 확인
  public String getTrainingStatus() {
    return trainingStatus.isEmpty() ? "❌ 아직 훈련 기록이 없습니다." : String.join("\n", trainingStatus.values());
  }

  @Override
  public String toString() {
    return "👤 보호자 " + name + " (크기: " + getBreed() + ", " + getAge() + "세)";
  }
}
