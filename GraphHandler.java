import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class GraphHandler implements IHandler {

    Graph graph;


    @Override
    public void handle(InputStream in, OutputStream out) throws Exception {
        ObjectOutputStream output = new ObjectOutputStream(out);
        ObjectInputStream input = new ObjectInputStream(in);
        output.writeObject(true);
        String message;
        boolean continueToServe = true;
        while (continueToServe) {
            message = input.readObject().toString();
            switch (message) {
                case "new graph" : {
                    int matrix[][] = (int[][])input.readObject();
                    this.graph = new Graph(matrix);
                    output.writeObject("Your graph is updated");
                    break;
                }
                case "components" : {
                    output.writeObject(graph.getGraphComponents());
                    break;
                }
                case "shortest path" : {
                        Index s = (Index) input.readObject();
                        Index d = (Index) input.readObject();
                        String str = "Source : " + s + " Destination : " + d + "\n";
                        output.writeObject(str + graph.shortestPaths(s,d));
                    break;
                }
                case "submarine amount" : {
                    output.writeObject(graph.subCount());
                    break;
                }
                case "stop" : {
                    continueToServe = false;
                    break;
                }
            }
        }
        input.close();
        output.close();
    }
}
