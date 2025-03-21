package client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Owner implements Serializable {
  private String name;
  private Dog dog;
  private Map<String, List<String>> trainingRecords;

  public Owner(String name, int age, String breed, String dogName) {
    this.name = name;
    this.dog = new Dog(dogName, age, breed);
    trainingRecords = new HashMap<>();
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
  // ✅ 훈련 기록 저장
  public void addTrainingRecord(String command, String message) {
    trainingRecords.putIfAbsent(command, new ArrayList<>());
    trainingRecords.get(command).add(message);
  }

  // ✅ 훈련 기록을 문자열로 반환
  public String getTrainingHistoryString() {
    if (trainingRecords.isEmpty()) return "❌ 아직 훈련 기록이 없습니다.";
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<String, List<String>> entry : trainingRecords.entrySet()) {
      sb.append("▶ ").append(entry.getKey()).append(" 훈련 기록:\n");
      for (String record : entry.getValue()) {
        sb.append("   ").append(record).append("\n");
      }
    }
    return sb.toString();
  }
  @Override
  public String toString() {
    return "👤 보호자 " + name + " (크기: " + getBreed() + ", " + getAge() + "세)";
  }
}
