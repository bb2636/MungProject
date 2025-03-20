package client;

public class Dog{
  public String name;
  private final int age;
  private final String breed;

  public Dog(String name, int age, String breed) {
    this.age = age;
    this.breed = breed;
  }

  public String getInfo() {
    return "강아지 나이: " + age + ", 강아지 크기: " + breed;
  }
}