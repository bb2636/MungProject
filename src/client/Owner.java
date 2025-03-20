package client;

public class Owner {
  private String name;
  private Dog dog;

  public Owner(String name, int age, String breed, String dogName) {
    this.name = name;
    this.dog = new Dog(dogName, age, breed);
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

  @Override
  public String toString() {
    return "👤 보호자 " + name + " (크기: " + getBreed() + ", " + getAge() + "세)";
  }
}
