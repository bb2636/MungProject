package server;

import java.util.HashSet;
import java.util.Set;

public class ChatRoom {
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