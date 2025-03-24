package server;

import admin.Admin;
import admin.TrainingProgram;
import client.Owner;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler extends Thread {
  private final Socket socket;
  private PrintWriter out;
  private Scanner in;
  private ChatRoom currentRoom;
  private String clientName;
  private String programName;

  public ClientHandler(Socket socket) {
    this.socket = socket;
  }

  @Override
  public void run() {
    try {
      in = new Scanner(socket.getInputStream());
      out = new PrintWriter(socket.getOutputStream(), true);

      while (in.hasNextLine()) {
        String input = in.nextLine().trim();

        if (input.startsWith("/register ")) {
          handleRegister(input);
        } else if (input.startsWith("/saveAdmin ")) {
          handleSaveAdmin(input);
        } else if (input.startsWith("/removeAdmin ")) {
          handleRemoveAdmin(input);
        } else if (input.equals("/listRooms")) {
          sendRoomList();
        } else if (input.startsWith("/joinRoom ")) {
          joinRoom(Integer.parseInt(input.split(" ")[1]));
        } else if (input.equals("/leaveRoom")) {
          leaveRoom();
        } else if (input.equals("/exit")) {
          leaveRoom();
          socket.close();
          return;
        } else if (input.equals("/getHistory")) {
          getTrainingHistory();
        } else if (input.startsWith("/sit") || input.startsWith("/stay") || input.startsWith("/fetch")) {
          trainDog(input);
        } else {
          if (currentRoom != null) {
            currentRoom.broadcast("💬 " + getClientInfo() + ": " + input);
          } else {
            out.println("❌ 먼저 방에 입장하세요.");
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void handleRegister(String input) {
    String[] parts = input.split(" ", 5);
    if (parts.length < 5) return;
    clientName = parts[1];
    int age = Integer.parseInt(parts[2]);
    String breed = parts[3];
    String dogName = parts[4];
    TrainerServer.ownerMap.put(clientName, new Owner(clientName, age, breed, dogName));
    System.out.println("✅ 보호자 등록: " + clientName);
  }

  private void handleSaveAdmin(String input) {
    String[] parts = input.split(" ", 5);
    if (parts.length < 4) return;
    programName = parts[1];
    String trainerName = parts[2];
    String breed = parts[3];
    TrainerServer.adminMap.put(programName, new TrainingProgram(programName, trainerName, breed));
    int newRoomId = TrainerServer.createRoom(programName);
    System.out.println("📢 '" + programName + "' 방 생성 완료! (ID: " + newRoomId + ")");
  }

  private void handleRemoveAdmin(String input) {
    String[] parts = input.split(" ", 2);
    if (parts.length == 2) {
      String programName = parts[1];
      if (TrainerServer.adminMap.containsKey(programName)) {
        TrainerServer.adminMap.remove(programName);
        TrainerServer.roomMap.values().removeIf(room -> room.getName().equals(programName));
        out.println("✅ 프로그램이 삭제되었습니다: " + programName);
      } else {
        out.println("❌ 해당 프로그램이 존재하지 않습니다.");
      }
    } else {
      out.println("❌ 삭제할 프로그램 이름을 입력해주세요.");
    }
  }

  private void trainDog(String command) {
    Owner owner = TrainerServer.ownerMap.get(clientName);
    Admin admin = TrainerServer.adMap.get(programName);
    if (owner != null) {
      String message;
      switch (command) {
        case "/sit" -> message = "🐶 " + owner.getDog().getName() + "이(가) 앉으면서 애교를 부립니다.";
        case "/stay" -> message = "🐶 " + owner.getDog().getName() + "이(가) 밥 먹기를 기다립니다.";
        case "/fetch" -> message = "🐶 " + owner.getDog().getName() + "이(가) 공을 신나게 가져옵니다.";
        default -> message = "❌ 잘못된 훈련 명령어입니다.";
      }
      owner.addTrainingRecord(command, message);
      owner.getDog().train(message);
      out.println(message);
    } else {
      out.println("❌ 보호자를 찾을 수 없습니다.");
    }
  }

  private void getTrainingHistory() {
    Owner owner = TrainerServer.ownerMap.get(clientName);
    if (owner != null) {
      String history = owner.getTrainingHistoryString();
      if (history.equals("❌ 아직 훈련 기록이 없습니다.")) {
        out.println("📜 훈련 기록이 없습니다.");
      } else {
        out.println("📜 " + owner.getDog().getName() + "의 훈련 기록:");
        for (String record : owner.getDog().getTrainingHistory()) {
          out.println(record);
        }
      }
      out.println("/endHistory");
    } else {
      out.println("❌ 보호자를 찾을 수 없습니다.");
    }
  }

  private void joinRoom(int roomId) {
    ChatRoom room = TrainerServer.getRoomById(roomId);
    if (room == null) {
      out.println("❌ 해당 방이 존재하지 않습니다.");
      return;
    }
    if (currentRoom != null) {
      leaveRoom();
    }
    currentRoom = room;
    room.join(this);
    out.println("🏠 보호자 " + getClientInfo() + "님이 방 '" + room.getName() + "' (ID: " + roomId + ")에 입장하였습니다.");
  }

  private void leaveRoom() {
    if (currentRoom != null) {
      currentRoom.leave(this);
      out.println("🚪 보호자 " + getClientInfo() + "님이 방을 나갔습니다.");
      currentRoom = null;
    }
  }

  private void sendRoomList() {
    out.println("📜 현재 방 목록:");
    TrainerServer.roomMap.forEach((id, room) -> out.println("  [" + id + "] " + room.getName()));
  }

  public String getClientInfo() {
    Owner owner = TrainerServer.ownerMap.get(clientName);
    return (owner != null) ? owner.getName() : clientName;
  }

  public void sendMessage(String message) {
    out.println(message);
  }
}
