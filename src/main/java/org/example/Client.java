package org.example;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Socket client = new Socket();
        String message;
        String inMessage;
        client.connect(new InetSocketAddress(InetAddress.getLocalHost(), 15_000));
        PrintStream outputStream = new PrintStream(client.getOutputStream());
        BufferedReader inputStream =
                new BufferedReader(new InputStreamReader(client.getInputStream()));
        outputStream.println("files");
        outputStream.flush();
        while (true) {
            inMessage = inputStream.readLine();
            System.out.println("Server send message : " + inMessage);
            if (inMessage.equals("send")) {
                break;
            }
        }
        System.out.println("Введите название файла который вы хотите получить");
        message = scanner.nextLine();
        outputStream.println(message);
        outputStream.flush();
        File copied = new File("received files/" + message);
        try {
            int read = 0;
            FileOutputStream toFile1 = new FileOutputStream(copied);
            BufferedOutputStream toFile = new BufferedOutputStream(toFile1);
            BufferedInputStream buffInpStr = new BufferedInputStream(client.getInputStream());

            while (read != -1) {
                read = buffInpStr.read();
                toFile.write(read);
            }
            System.out.println("Файл успешно получен !!!");
            try {
                toFile1.close();
                toFile.close();
                buffInpStr.close();
            } catch (Exception e) {

            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        outputStream.close();
        inputStream.close();
        client.close();

    }
}
