package client;

public class Owner {
  private String name;
  private Dog dog;

  public Owner(String name, int age, String breed) {
    this.name = name;
    this.dog = new Dog(name, age, breed);
  }

  // 보호자 정보 조회
  public String getInfo() {
    return "👤 보호자: " + name + "\n" + dog.getInfo();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Dog getDog() {
    return dog;
  }

  public void setDog(Dog dog) {
    this.dog = dog;
  }

  // ✅ 강아지 정보도 가져올 수 있도록 추가
  public int getAge() {
    return dog.getAge();
  }

  public String getBreed() {
    return dog.getBreed();
  }

  @Override
  public String toString() {
    return "👤 보호자 " + name + " (크기: " + getBreed() + ", " + getAge() + "세)";
  }
}
