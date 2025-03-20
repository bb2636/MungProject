package admin;

public class TrainingProgram {
  String programName;
  Admin admin;

  public TrainingProgram(String programName, String trainerName, String specialtyDogBreeds) {
    this.programName = programName;
    this.admin = new Admin(trainerName,specialtyDogBreeds);
  }

  @Override
  public String toString() {
    return "새로 등록된 프로그램[" +
        "프로그램명 : '" + programName +
        ", 훈련사정보 : " + admin.toString() +
        ']';
  }
}
