package server;

import admin.Admin;
import admin.TrainingProgram;
import client.Owner;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TrainerServer {
  private static final int PORT = 5000;
  private static final int MAX_THREADS = 100;
  private static final ExecutorService threadPool = Executors.newFixedThreadPool(MAX_THREADS);

  public static final Map<Integer, ChatRoom> roomMap = new HashMap<>();
  public static final Map<String, Integer> nameToRoomId = new HashMap<>();
  public static final Map<String, Owner> ownerMap = new HashMap<>();
  public static final Map<String, TrainingProgram> adminMap = new HashMap<>();
  public static final Map<String, Admin> adMap = new HashMap<>();
  public static int nextRoomId = 1;

  public static void main(String[] args) {
    System.out.println("✅ 서버 시작... 포트 " + PORT);
    try (ServerSocket serverSocket = new ServerSocket(PORT)) {
      while (true) {
        Socket clientSocket = serverSocket.accept();
        System.out.println("✅ 새 클라이언트 접속: " + clientSocket);
        threadPool.execute(new ClientHandler(clientSocket));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static synchronized int createRoom(String roomName) {
    if (nameToRoomId.containsKey(roomName)) {
      return nameToRoomId.get(roomName);
    }
    int roomId = nextRoomId++;
    ChatRoom room = new ChatRoom(roomId, roomName);
    roomMap.put(roomId, room);
    nameToRoomId.put(roomName, roomId);
    return roomId;
  }

  public static synchronized ChatRoom getRoomById(int roomId) {
    return roomMap.get(roomId);
  }

  public static Map<String, Owner> getOwnerMap() {
    return ownerMap;
  }
}
