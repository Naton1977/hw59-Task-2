package org.example;


import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {

        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress(15_000));
        Socket remoteClient;
        String input;
        String clientMessage;

        while (true) {
            System.out.println("Wait for clients...");
            remoteClient = server.accept();
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(remoteClient.getInputStream()));
            PrintStream outputStream = new PrintStream(remoteClient.getOutputStream());
            input = inputStream.readLine();
            if ("files".equals(input)) {
                System.out.println("Server got message :" + input);
                File myFolder = new File("assets");
                File[] files = myFolder.listFiles();
                for (int i = 0; i < files.length; i++) {
                    outputStream.println(files[i].getName());
                    outputStream.flush();
                }
                outputStream.println("send");
                clientMessage = inputStream.readLine();
                File file = new File("assets/" + clientMessage);

                if (file.exists()) {
                    String dataFile = "assets/" + clientMessage;

                    BufferedInputStream buffInpStr = new BufferedInputStream(new FileInputStream(dataFile));
                    BufferedOutputStream buffOutStr = new BufferedOutputStream(remoteClient.getOutputStream());
                    byte[] byteArray = new byte[8192];
                    int in;
                    while ((in = buffInpStr.read(byteArray)) != -1) {
                        buffOutStr.write(byteArray, 0, in);
                    }
                    buffInpStr.close();
                    buffOutStr.close();
                }

                if ("exit".equals(input)) {
                    inputStream.close();
                    outputStream.close();
                    server.close();
                    break;
                }
            }
        }
    }
}