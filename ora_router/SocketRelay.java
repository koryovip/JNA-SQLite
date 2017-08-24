package sst;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketRelay implements Runnable {

    private Socket clientSocket;
    private Socket serverSocket;

    private class Relay implements Runnable {
        private InputStream inputstream;
        private OutputStream outputstream;
        private final int BUFFERSIZE = 1024 * 16;
        private byte buffer[] = new byte[BUFFERSIZE];

        public Relay(InputStream inputstream, OutputStream outputstream) {
            this.inputstream = inputstream;
            this.outputstream = outputstream;
        }

        public void run() {
            try {
                while (true) {
                    int ret = this.inputstream.read(buffer);
                    if (ret == -1) {
                        break;
                    }
                    synchronized (outputstream) {
                        System.out.write(buffer, 0, ret);
                    }
                    this.outputstream.write(buffer, 0, ret);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public SocketRelay(Socket clients, Socket servers) {
        this.serverSocket = servers;
        this.clientSocket = clients;
    }

    public void run() {
        try {
            InputStream clientInput = this.clientSocket.getInputStream();
            OutputStream clientOutput = this.clientSocket.getOutputStream();
            InputStream serverInput = this.serverSocket.getInputStream();
            OutputStream serverOutput = this.serverSocket.getOutputStream();

            Relay pushRelay = new Relay(serverInput, clientOutput);
            Relay pullRelay = new Relay(clientInput, serverOutput);
            Thread pushThread = new Thread(pushRelay);
            Thread pullThread = new Thread(pullRelay);

            pushThread.start();
            pullThread.start();
            pullThread.join();
            pushThread.stop();

            serverInput.close();
            serverOutput.close();
            clientInput.close();
            clientOutput.close();

            this.serverSocket.close();
            this.clientSocket.close();
        } catch (Exception e) {
        }
    }

    public static void main(String args[]) throws IOException {
        ServerSocket serverSocket = null;
        Socket connectedSocket, clientSocket, oraConnSocket;
        int inport, outport;
        try {
            //inport = new Integer(args[1]).intValue();
            //outport = new Integer(args[2]).intValue();
            serverSocket = new ServerSocket(1234);
            while (true) {
                connectedSocket = serverSocket.accept();
                oraConnSocket = new Socket("172.20.0.2", 1521);
                //clientSocket = new Socket(args[0], outport);
                SocketRelay sr = new SocketRelay(connectedSocket, oraConnSocket);
                new Thread(sr).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}