package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserInputHandler1 implements Runnable{
    private ChatClient1 chatClient;
    public UserInputHandler1(ChatClient1 chatClient){
        this.chatClient=chatClient;
    }

    @Override
    public void run() {
        BufferedReader consoleReader =new BufferedReader(
                new InputStreamReader(System.in)
        );
        while(true){
            try {
                String input=consoleReader.readLine();
                chatClient.send(input);
                if(chatClient.readyToQuit(input)){
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
