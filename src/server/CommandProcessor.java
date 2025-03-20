package server;

// Owner 가 명령어 입력. 입력값을 반환
public class CommandProcessor {
  public static String processCommand(String command) {
    switch (command.toLowerCase()) {
      case "sit":
        return processSit();
      case "stay":
        return processStay();
      case "fetch":
        return processFetch();
      case "roll":
        return processRoll();
      default:
        return "알 수 없는 명령어입니다.";
    }
  }

  public static String processSit() {
    return "강아지가 앉았습니다.";
  }

  public static String processStay() {
    return "강아지가 기다립니다.";
  }

  public static String processFetch() {
    return "강아지가 물건을 가져왔습니다.";
  }

  public static String processRoll() {
    return "강아지가 구릅니다.";
  }
}