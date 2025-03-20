package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Test3 {
  public static void main(String[] args) {
    String serverAddress = "localhost";
    int port = 5000;

    try (Socket socket = new Socket(serverAddress, port);
         BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
         BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

      System.out.println("서버에 연결되었습니다. 명령어를 입력하세요:");

      new Thread(() -> {
        try {
          String response;
          while ((response = in.readLine()) != null) {
            System.out.println("서버 응답: " + response);
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }).start();

      String userInputMessage;
      while ((userInputMessage = userInput.readLine()) != null) {
        out.println(userInputMessage);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}