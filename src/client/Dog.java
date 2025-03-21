package client;

import java.util.ArrayList;
import java.util.List;

public class Dog{
  public String name;
  private final int age;
  private final String breed;
  private List<String> trainingHistory;

  public Dog(String name, int age, String breed) {
    this.name = name;
    this.age = age;
    this.breed = breed;
    trainingHistory = new ArrayList<>();
  }

  public String getInfo() {
    return "강아지 이름: "+ name + ", 강아지 나이: " + age + ", 강아지 크기: " + breed;
  }

  public void train(String command) {
    String message;
    switch (command) {
      case "/sit":
        message = "🐾 " + name + "이(가) 앉으면서 애교를 부립니다.";
        break;
      case "/stay":
        message = "🐾 " + name + "이(가) 밥 먹기를 기다립니다.";
        break;
      case "/fetch":
        message = "🐾 " + name + "이(가) 공을 신나게 가져옵니다.";
        break;
      default:
        message = "❌ 잘못된 훈련 명령어입니다.";
    }
    trainingHistory.add(message);
  }

  // ✅ 훈련 기록을 문자열로 반환
  public String getTrainingHistoryString() {
    return trainingHistory.isEmpty() ? "❌ 아직 훈련 기록이 없습니다." : String.join("\n", trainingHistory);
  }



  public int getAge() {
    return age;
  }
  public String getBreed() {
    return breed;
  }
  public String getName() {
    return name;
  }
}