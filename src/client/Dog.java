package client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Dog implements Serializable {
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

  public void train(String message) {
    trainingHistory.add(message);
  }
  public List<String> getTrainingHistory() {
    return trainingHistory;
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