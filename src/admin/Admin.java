package admin;

public class Admin {
  private String trainerName;
  private String specialtyDogBreeds; //전문 견종 - 소 중 대 중 1

  public Admin(String trainerName, String specialtyDogBreeds) {
    this.trainerName = trainerName;
    this.specialtyDogBreeds = specialtyDogBreeds;
  }
  public String getSpecialtyDogBreeds() {
    return specialtyDogBreeds;
  }

  @Override
  public String toString() {
    return "New Trainer [" +
        "훈련사명 : " + trainerName +
        ", 전문견종 : '" + specialtyDogBreeds +
        ']';
  }
}
