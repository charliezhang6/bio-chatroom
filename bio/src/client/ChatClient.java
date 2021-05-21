package client;

import server.ChatServer;

import java.io.*;
import java.net.Socket;

public class ChatClient {
    private final String DEFAULT_SERVER_HOST ="127.0.0.1";
    private final int DEFAULT_SERVER_PORT = 8888;
    private final String QUIT ="quit";

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;



    public void send(String msg) throws IOException {
        writer.write(msg+"\n");
        writer.flush();
    }

    public String receive() throws IOException {
        String msg=reader.readLine();
        return msg;
    }

    public void start(){
        try {
            socket=new Socket(DEFAULT_SERVER_HOST,DEFAULT_SERVER_PORT);
            reader=new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );
            writer=new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())
            );
            new Thread(new UserInputHandler(this)).start();
            String msg=null;
            while((msg= receive())!=null){
                System.out.println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private void close() {
        try {
            writer.close();
            System.out.println("socket已关闭");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ChatClient chatClient=new ChatClient();
        chatClient.start();
    }

    public boolean readyToQuit(String input) {
        return input.equals(QUIT);
    }
}
