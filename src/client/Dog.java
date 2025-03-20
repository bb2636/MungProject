package client;

public class Dog{
  public String name;
  private final int age;
  private final String breed;

  public Dog(String name, int age, String breed) {
    this.name = name;
    this.age = age;
    this.breed = breed;
  }

  public String getInfo() {
    return "강아지 이름: "+ name + ", 강아지 나이: " + age + ", 강아지 크기: " + breed;
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