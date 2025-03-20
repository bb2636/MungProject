package client;

import java.io.*;
import java.net.*;
import java.util.*;

public class TrainerServer {
  private static final int PORT = 5000;
  private static final String DEFAULT_ROOM_NAME = "훈련받을 사람 여기 모여라!";
  private static final Map<Integer, ChatRoom> roomMap = new HashMap<>();
  private static final Map<String, Integer> nameToRoomId = new HashMap<>();
  private static final Map<String, Owner> ownerMap = new HashMap<>();
  private static int nextRoomId = 1;

  public static void main(String[] args) {
    System.out.println("✅ 서버 시작... 포트 " + PORT);

    // ✅ 기본 방 생성
    int defaultRoomId = createRoom(DEFAULT_ROOM_NAME);
    System.out.println("🏠 기본 방 생성 완료! (ID: " + defaultRoomId + ", 이름: " + DEFAULT_ROOM_NAME + ")");

    try (ServerSocket serverSocket = new ServerSocket(PORT)) {
      while (true) {
        Socket clientSocket = serverSocket.accept();
        System.out.println("✅ 새 클라이언트 접속: " + clientSocket);
        new ClientHandler(clientSocket).start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // ✅ 방 생성 (nameToRoomId에도 저장)
  private static synchronized int createRoom(String roomName) {
    if (nameToRoomId.containsKey(roomName)) {
      return nameToRoomId.get(roomName);
    }
    int roomId = nextRoomId++;
    ChatRoom room = new ChatRoom(roomId, roomName);
    roomMap.put(roomId, room);
    nameToRoomId.put(roomName, roomId);
    System.out.println("🏠 새로운 방 생성: " + roomName + " (ID: " + roomId + ")");
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

    public int getId() {
      return id;
    }
  }

  private static class ClientHandler extends Thread {
    private final Socket socket;
    private PrintWriter out;
    private Scanner in;
    private ChatRoom currentRoom;
    private String clientName;

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
            String[] parts = input.split(" ", 4);
            if (parts.length < 4) continue;

            clientName = parts[1];
            int age = Integer.parseInt(parts[2]);
            String breed = parts[3];

            ownerMap.put(clientName, new Owner(clientName, age, breed));
            System.out.println("✅ 보호자 등록: " + clientName);
          } else if (input.equals("/listRooms")) {
            sendRoomList();
          } else if (input.startsWith("/joinRoom ")) {
            String[] parts = input.split(" ");
            if (parts.length < 2) {
              out.println("❌ 올바른 방 ID를 입력하세요.");
              continue;
            }
            int roomId;
            try {
              roomId = Integer.parseInt(parts[1]);
              joinRoom(roomId);
            } catch (NumberFormatException e) {
              out.println("❌ 올바른 숫자를 입력하세요.");
            }
          } else if (input.equals("/leaveRoom")) {
            leaveRoom();
          } else if (input.equals("/exit")) {
            leaveRoom();
            socket.close();
            return;
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

    // ✅ 방 목록 출력 기능 추가
    private void sendRoomList() {
      if (roomMap.isEmpty()) {
        out.println("❌ 현재 방이 없습니다.");
        return;
      }
      out.println("📜 현재 방 목록:");
      roomMap.entrySet().stream()
          .sorted(Map.Entry.comparingByKey())
          .forEach(entry -> out.println("  [" + entry.getKey() + "] " + entry.getValue().getName()));
    }

    private String getClientInfo() {
      Owner owner = ownerMap.get(clientName);
      if (owner != null) {
        return owner.getName();
      }
      return clientName;
    }

    public void sendMessage(String message) {
      out.println(message);
    }
  }
}
