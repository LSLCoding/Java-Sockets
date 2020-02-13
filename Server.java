import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static int port = 9999;

    public static void main(String... args) throws Exception {
        System.out.println("The server has been started on port " + port + "\n");

        ServerSocket server = new ServerSocket(port);

        while (true) {
            Socket socket = server.accept();
            new ThreadServer(socket).start();
        }
    }

    public static class ThreadServer extends Thread {

        private Socket socket;

        public ThreadServer(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                String instructions = "Please input your message!" + "\r\n";
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(instructions.getBytes());

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String line = bufferedReader.readLine();

                System.out.println("Message received from " + socket.getInetAddress().getHostAddress() + ": " + line);

                run();
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }
}