import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.*;
import static java.nio.charset.StandardCharsets.UTF_8;

public class Server {
    private final List<PrintWriter> writerClients = new ArrayList<>();

    public static void main(String[] args) {

        new Server().doIt();
    }

    public void doIt() {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        try {
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(5000));

            while (serverChannel.isOpen()) {
                SocketChannel clientChannel = serverChannel.accept();
                PrintWriter writer = new PrintWriter(Channels.newWriter(clientChannel, UTF_8));
                writerClients.add(writer);
                threadPool.submit(new ClientService(clientChannel));
                System.out.println("Connection is ready!!!");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void sendToEveryone(String mess) {
        for (PrintWriter writer : writerClients) {
            writer.println(mess);
            writer.flush();
        }
    }

    public class ClientService implements Runnable {
        BufferedReader reader;
        SocketChannel channel;

        public ClientService(SocketChannel clientChannel) {
            channel = clientChannel;
            reader = new BufferedReader(Channels.newReader(channel, UTF_8));
        }

        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    System.out.println("Message read: " + message);
                    sendToEveryone(message);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
