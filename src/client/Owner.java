package client;

public class Owner {
  public String name;
  Dog dog;
  public Owner(String name, int age, String breed) {
    this.name = name;
    this.dog = new Dog(name, age, breed);
  }

  public String getInfo() {
    return "👤 보호자 정보: " + name + "\n" + dog.getInfo();
  }
}
