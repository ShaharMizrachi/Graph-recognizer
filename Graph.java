import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class Graph {

    Matrix matrix;

    public Graph(int row , int col) {
        this.matrix = new Matrix(row , col);
    }

    public Graph(int matrix[][]) {
        this.matrix = new Matrix(matrix);
    }

    /**
     *
     * @return  list of all components in a given graph
     */

    public List<Set<Index>> getGraphComponents() {
        List<Set<Index>> components = new ArrayList<>();
        Set<Index> checked = new HashSet<>();  // the function is handle every index only one time.
        for (int i=0 ; i<matrix.getMatrix().length ; i++) {
            for (int j=0 ; j<matrix.getMatrix()[0].length ; j++) {
                Index current = new Index(i,j);
                if (matrix.getValue(current) == 1 && !checked.contains(current)) { // if the function not handle this index
                    Set<Index> indexComponent = new HashSet<>(BFS(current).keySet()); // convert map distance table to set component
                    checked.addAll(indexComponent); // add all the index that the function is handle
                    components.add(indexComponent); // add the new component to the list
                }
            }
        }
        return components.stream().sorted((e1,e2)->e1.size() >= e2.size() ? 1 : -1).collect(Collectors.toList());
    }




    /**
     *
     * @param s as source index
     * @return distance table as HashMap
     */
    public HashMap<Index,Integer> BFS(Index s) {
        HashMap<Index,Integer> component = new HashMap<>();
        Queue<Index> queue = new LinkedList<>(); // poll the top , and push his neighbors
        component.put(s,0);
        queue.add(s); // initialize the queue
        while (!queue.isEmpty()) {
            Index top = queue.poll();
            // get all index neighbors with value one and that function not handle yet
            List<Index> neighbors = matrix.getNeighbors(top).stream()
                    .filter(e->matrix.getValue(e) == 1 && !component.containsKey(e)).collect(Collectors.toList());
            for (Index neighbor : neighbors) {
                component.put(neighbor , component.get(top) + 1); // each index get distance of his father node + 1
                queue.add(neighbor);
            }
        }
        return component;
    }

    /**
     *
     * @param s source index
     * @param d destination index
     * @return list of shortest path from source index to destination index
     */

    public List<List<Index>> shortestPaths(Index s , Index d) {
        if (matrix.getMatrix().length > 50 || matrix.getMatrix()[0].length > 50 || matrix.getValue(s) == 0 || matrix.getValue(d) == 0 ) return null;
        Map<Index,Integer> distances = BFS(s); // get distance table from the source index
        if (!distances.containsKey(d)) return null; // if there is no path from source index to destination index
        List<List<Index>> shortestPath = new ArrayList<>(); // contain all shortest path
        Queue<List<Index>> listQueue = new LinkedList<>(); // poll the top path, and push it with addition of next index in the path to the source
        List<Index> Path = new ArrayList<>();
        Path.add(d); // the first path that contain the destination index
        listQueue.add(Path); // initialize the queue
        while (!listQueue.isEmpty()) {
            List<Index> path = listQueue.poll();
            // get all neighbors of the closets index to the source
            List<Index> neighbors = matrix.getNeighbors(path.get(0)).stream()
                    .filter(e->matrix.getValue(e) == 1 && distances.get(path.get(0)) - 1 == distances.get(e)).collect(Collectors.toList());
            for (Index i : neighbors) {
                List<Index> nPath = new ArrayList<>(path);
                nPath.add(0,i); // add the index to the path
                if (i.equals(s)) { // if the index is the source index, this path is complete
                    shortestPath.add(nPath);
                } else { // else add it to the queue to get closer to the source index
                    listQueue.add(nPath);
                }
            }
        }
        return shortestPath;
    }

    /**
     *
     * @return the amount of submarine of a given graph
     */

    public int subCount() {
        List<Set<Index>> components = getGraphComponents(); // get all the components of the graph
        int sub = 0;
        for (Set<Index> component : components) {
            if (componentIsSub(component)) { // for each component, check if is valid submarine
                sub ++;
            }
            else return 0; // if there is not valid component, return zero submarine
        }
        return sub;
    }

    /**
     *
     * @param component
     * @return valid/invalid submarine
     * check if the component is a rectangle. (the component is submarine if the component is a rectangle)
     */

    private boolean componentIsSub(Set<Index> component) {
        if (component.size() == 1) return false; // component with one element is not a valid submarine
        Index top = getComponentTopIndex(component.toArray()); // get the left top corner of a component
        int countFirstRow = 0;
        for (int j=top.getJ() ; j<matrix.getMatrix()[0].length ; j++ ) {
            if (matrix.getValue(new Index(top.getI(),j)) == 1) {
                countFirstRow++; // count the amount of element in the first row of the component
            }
            else break;
        }
        int elementCount = countFirstRow;
        //for each row of the component, check if the amount of element is equal to the first one
        for (int i=top.getI() + 1 ; i<matrix.getMatrix().length ; i++) {
            for (int j=top.getJ() ; j< top.getJ() + countFirstRow ; j++ ) {
                if (matrix.getValue(new Index(i,j)) == 0) {
                    return elementCount == component.size(); // check if you count all the element in the component
                }
            }
            elementCount += countFirstRow;
        }
        return elementCount == component.size();
    }


    /**
     *
     * @param component as Array of index
     * @return top corner index of a component
     * helper function for submarine 
     */
    private Index getComponentTopIndex(Object[] component) {
        Index top = (Index)component[0];
        for (int i = 1; i < component.length; i++) {
            Index current = (Index)component[i];
            if ((current.getI() < top.getI()) || (current.getI() == top.getI() && current.getJ() < top.getJ())) {
                top = current;
            }
        }
        return top;
    }

    public static void main(String[] args) {
        int matrix[][] = {
                {1,1,0,0,0},
                {1,1,0,0,0},
                {1,1,0,0,0},
                {1,1,0,0,0},

        };
        Graph graph = new Graph(matrix);
        System.out.println(graph.subCount());

    }





}
