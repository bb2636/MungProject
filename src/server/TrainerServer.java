package server;

import admin.Admin;
import admin.TrainingProgram;
import client.Owner;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TrainerServer {
  private static final int PORT = 5000;
  private static final Map<Integer, ChatRoom> roomMap = new HashMap<>();
  private static final Map<String, Integer> nameToRoomId = new HashMap<>();
  private static final Map<String, Owner> ownerMap = new HashMap<>();
  private static final Map<String, TrainingProgram> adminMap = new HashMap<>();
  private static final Map<String, Admin> adMap = new HashMap<>();
  private static int nextRoomId = 1;
  private static final int MAX_THREADS = 100;
  private static final ExecutorService threadPool = Executors.newFixedThreadPool(MAX_THREADS);



  public static void main(String[] args) {
    System.out.println("✅ 서버 시작... 포트 " + PORT);

    try (ServerSocket serverSocket = new ServerSocket(PORT)) {
      while (true) {
        Socket clientSocket = serverSocket.accept();
        System.out.println("✅ 새 클라이언트 접속: " + clientSocket);
        threadPool.execute(new ClientHandler(clientSocket)); // 풀에서 관리
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static Map<String, Owner> getOwnerMap() {
    return ownerMap;
  }
  public static Map<String, Admin> getAdminMap() {
    return adMap;
  }

  private static synchronized int createRoom(String roomName) {
    if (nameToRoomId.containsKey(roomName)) {
      return nameToRoomId.get(roomName);
    }
    int roomId = nextRoomId++;
    ChatRoom room = new ChatRoom(roomId, roomName);
    roomMap.put(roomId, room);
    nameToRoomId.put(roomName, roomId);
    return roomId;
  }

  private static synchronized ChatRoom getRoomById(int roomId) {
    return roomMap.get(roomId);
  }

  private static class ChatRoom {
    private final int id;
    private final String name;
    private final Set<ClientHandler> clients = new HashSet<>();

    public ChatRoom(int id, String name) {
      this.id = id;
      this.name = name;
    }

    public synchronized void join(ClientHandler client) {
      clients.add(client);
      broadcast("📢 보호자 " + client.getClientInfo() + " 님이 방에 입장했습니다.");
    }

    public synchronized void leave(ClientHandler client) {
      clients.remove(client);
      broadcast("🚪 보호자 " + client.getClientInfo() + " 님이 방을 떠났습니다.");
    }

    public synchronized void broadcast(String message) {
      for (ClientHandler client : clients) {
        client.sendMessage(message);
      }
    }

    public String getName() {
      return name;
    }
  }

  private static class ClientHandler extends Thread {
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
            String[] parts = input.split(" ", 5);
            if (parts.length < 5) continue;

            clientName = parts[1];
            int age = Integer.parseInt(parts[2]);
            String breed = parts[3];
            String dogName = parts[4];

            ownerMap.put(clientName, new Owner(clientName, age, breed, dogName));
            System.out.println("✅ 보호자 등록: " + clientName);
          }else if (input.startsWith("/saveAdmin ")) {
            String[] parts = input.split(" ", 5);
            if (parts.length < 4) continue;

            programName = parts[1];
            String trainerName = parts[2];
            String breed = parts[3];

            adminMap.put(programName, new TrainingProgram(programName, trainerName, breed));
            System.out.println("✅ 프로그램 등록: " + programName);

            // 프로그램 이름으로 방 생성
            int newRoomId = createRoom(programName);
            System.out.println("📢 '" + programName + "' 방 생성 완료! (ID: " + newRoomId + ")");
          }else if (input.startsWith("/removeAdmin ")) {
            String[] parts = input.split(" ", 2); // 🔥 split은 2개만
            if (parts.length == 2) {
              String programName = parts[1];
              if (adminMap.containsKey(programName)) {
                adminMap.remove(programName);
                roomMap.values().removeIf(room -> room.getName().equals(programName));
                out.println("✅ 프로그램이 삭제되었습니다: " + programName);
              } else {
                out.println("❌ 해당 프로그램이 존재하지 않습니다.");
              }
            } else {
              out.println("❌ 삭제할 프로그램 이름을 입력해주세요.");
            }
          }
          else if (input.equals("/listRooms")) {
            sendRoomList();
          }
          else if (input.startsWith("/joinRoom ")) {
            int roomId = Integer.parseInt(input.split(" ")[1]);
            joinRoom(roomId);
          }
          else if (input.equals("/leaveRoom")) {
            leaveRoom();
          }
          else if (input.equals("/exit")) {
            leaveRoom();
            socket.close();
            return;
          }
          else if (input.equals("/getHistory")) {
            getTrainingHistory();
          }
          else if (input.startsWith("/sit") || input.startsWith("/stay") || input.startsWith("/fetch")) {
            trainDog(input);
          }
          else {
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

    private void trainDog(String command) {
      Owner owner = ownerMap.get(clientName);
      Admin admin = adMap.get(programName);
      if (owner != null) {
        String message;
        switch (command) {
          case "/sit":
            message = "🐶 " + owner.getDog().getName() + "이(가) 앉으면서 애교를 부립니다.";
            break;
          case "/stay":
            message = "🐶 " + owner.getDog().getName() + "이(가) 밥 먹기를 기다립니다.";
            break;
          case "/fetch":
            message = "🐶 " + owner.getDog().getName() + "이(가) 공을 신나게 가져옵니다.";
            break;
          default:
            message = "❌ 잘못된 훈련 명령어입니다.";
        }

        // 훈련 기록을 Owner에도 저장
        owner.addTrainingRecord(command, message);

        owner.getDog().train(message);
        out.println(message);
      } else {
        out.println("❌ 보호자를 찾을 수 없습니다.");
      }
    }

    // 클라이언트가 훈련 기록 조회 요청 시 실행되는 함수
    private void getTrainingHistory() {
      Owner owner = ownerMap.get(clientName);
      if (owner != null) {
        String history = owner.getTrainingHistoryString(); // 🔥 Owner에서 훈련 기록 가져오기
        if (history.equals("❌ 아직 훈련 기록이 없습니다.")) {
          out.println("📜 훈련 기록이 없습니다.");
        } else {
          out.println("📜 " + owner.getDog().getName() + "의 훈련 기록:");
          for (String record : owner.getDog().getTrainingHistory()) {
            out.println(record);
          }
        }
        out.println("/endHistory"); // 🔥 클라이언트가 종료 신호를 받을 수 있도록 추가
      } else {
        out.println("❌ 보호자를 찾을 수 없습니다.");
      }
    }

    private void joinRoom(int roomId) {
      ChatRoom room = getRoomById(roomId);
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
      roomMap.forEach((id, room) -> out.println("  [" + id + "] " + room.getName()));
    }

    private String getClientInfo() {
      Owner owner = ownerMap.get(clientName);
      return (owner != null) ? owner.getName() : clientName;
    }

    public void sendMessage(String message) {
      out.println(message);
    }
  }
}
