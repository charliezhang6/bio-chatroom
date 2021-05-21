package server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;



public class ChatServer {
    private final int DEFAULT_PORT=8888;
    private final String QUIT ="quit";
    private ServerSocket serverSocket;
    private Map<Integer, Writer> connectedClients;
    public ChatServer() {
        connectedClients =new HashMap<>();
    }
    public void addClient(Socket socket) throws IOException {
        if(socket!=null){
            int port= socket.getPort();
            BufferedWriter writer=new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())
            );
            connectedClients.put(port,writer);
            System.out.println("客户端"+port+"已连接");
        }
    }

    public void removeClient(Socket socket) throws IOException {
        if(socket!=null){
            int port=socket.getPort();
            if(connectedClients.containsKey(port)) connectedClients.get(port).close();
            connectedClients.remove(port);
            System.out.println("客户端"+port+"已断开");
        }
    }

    public void forwardMessage(Socket socket,String fwdMsg) throws IOException {
        for(Integer id :connectedClients.keySet()){
            if(!id.equals(socket.getPort())){
                Writer writer=connectedClients.get(id);
                writer.write((fwdMsg));
                writer.flush();
            }
        }
    }
    public void start(){
        try {
            serverSocket =new ServerSocket(DEFAULT_PORT);
            System.out.println("启动服务器，开始监听，端口为："+DEFAULT_PORT);
            while(true){
                Socket socket=serverSocket.accept();
                new Thread(new ChatHandler(this,socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close();
        }
    }
    public void close(){
        if(serverSocket!=null){
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("服务器已关闭");
        }
    }

    public static void main(String[] args) {
        ChatServer server=new ChatServer();
        server.start();
    }

    public boolean readyToQuit(String msg) {
        if(msg.equals(QUIT)){
            return true;
        }else {return false;}
    }
}
