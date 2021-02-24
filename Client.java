import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class Client {

    public static void main(String []args) {
        try {
            Socket socket = new Socket("127.0.0.1" , 8000);
            Matrix matrix = null;
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            Scanner scan = new Scanner(System.in);
            Random rand = new Random();
            String message;
            if ((int)input.readObject() == 2) {
                System.out.println("The server is closed for request");
                return;
            }
            System.out.println("You are connected");
            boolean connected = true;
            while (connected) {
                message = scan.nextLine();
                if (!message.equals("new graph") && !message.equals("stop") && matrix == null) {
                    System.out.println("Error ! you need to enter a graph");
                    continue;
                }
                output.writeObject(message);
                switch (message) {
                    case "new graph" : {
                        matrix = new Matrix(rand.nextInt(6) + 1, rand.nextInt(6) + 1);
                        output.writeObject(matrix.getMatrix());
                        System.out.println(input.readObject().toString());
                        System.out.println(matrix);
                        break;
                    }
                    case "components" :
                    case "submarine amount" : {
                        System.out.println(input.readObject());
                        break;
                    }
                    case "shortest path" : {
                        output.writeObject(Index.getRandomIndex(matrix.getMatrix().length , matrix.getMatrix()[0].length));
                        output.writeObject(Index.getRandomIndex(matrix.getMatrix().length , matrix.getMatrix()[0].length));
                        System.out.println(input.readObject());
                        break;
                    }
                    case "stop" : {
                        connected = false;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Connection is stop");
        }
    }







}
