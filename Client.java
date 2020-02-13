import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String... args) {
        try {
            Socket socket = new Socket("localhost", 9999);

            new OutputThread(socket, socket.getInputStream()).start();
        } catch (Exception e) {
            System.out.println("Cannot connect to the server. Please try again later.");
        }
    }

    public static class OutputThread extends Thread {

        private InputStream inputstream;
        private Socket socket;

        public OutputThread(Socket socket, InputStream inputstream) {
            this.inputstream = inputstream;
            this.socket = socket;
        }

        @Override
        public void run() {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputstream));
            while (true) {
                try {
                    String line = bufferedReader.readLine();
                    System.out.println(line);

                    OutputStream outputStream = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(outputStream, true);

                    Scanner scanner = new Scanner(System.in);

                    String message = scanner.nextLine();

                    writer.println(message);
                    System.out.println("Your message '" + message + "' has been sent to the server!");

                } catch (IOException e) {
                    System.out.println("The server has experienced an issue! Attempting to reconnect in five seconds.");

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                    main();
                    break;
                }
            }
        }
    }
}