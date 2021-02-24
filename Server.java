import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server {

    private final int port;
    private ThreadPoolExecutor pool;
    private volatile boolean stopServer;
    private ServerSocket server;


    public Server(int port) {
        this.port = port;
        this.stopServer = true;
    }

    public void runServer() { // open server socket, accept new client and serve them until the server is closed.
        try {
            System.out.println("The server is running...");
            this.stopServer = false;
            this.server = new ServerSocket(this.port);
            this.pool = new ThreadPoolExecutor(5,10,10, TimeUnit.SECONDS,new LinkedBlockingQueue<>());
            new Thread(
                    ()->{
                        try {
                            while (!this.server.isClosed()) { // run while server socket is open
                                Socket socket = server.accept();
                                if (!stopServer) {
                                    pool.execute(this.toDoTask(socket));
                                } else {
                                    socket.close();
                                }
                            }
                        } catch (Exception e) {
                        };
                    }
            ).start();
        } catch (Exception e) {
        }
    }

    public void Menu() {
        Scanner scan = new Scanner(System.in);
        System.out.println("enter 'run server' to start the server");
        System.out.println("enter 'stop server' to close  the server");
        while (true) {
            switch (scan.nextLine()) {
                case "run server" : { // if user run the server , the server run in different thread
                    if (this.server == null || server.isClosed()) new Thread(()->{this.runServer();}).start();
                    else System.out.println("The server is already running");
                    break;
                }
                case "stop server" : { // if user close the server , the close action run in a different thread
                    if (!this.stopServer) new Thread(()->this.closeServer()).start();
                    else System.out.println("The server is closed");
                    break;
                }
            }
        }
    }

    private void closeServer() { // shutdown the server (continue to serve old clients, do not  accept new ones)
        try {
            System.out.println("Closing Process");
            this.stopServer = true;
            this.pool.shutdown();
            this.pool.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
            this.server.close();
            System.out.println("Server is Closed");
        } catch (Exception e) {
        }
    }



    public Runnable toDoTask(Socket socket) { // the runnable that serve the client
        return ()->{
            try {
                new GraphHandler().handle(socket.getInputStream() , socket.getOutputStream());
            } catch (Exception e) {
            }
            try {
                socket.close();
            } catch (Exception e) {};
        };
    }


    public static void main(String[] args) {
        Server server = new Server(8000);
        server.Menu();
    }

}
