import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Matrix {

    private int matrix[][];

    public Matrix(int matrix[][]) {
        setMatrix(matrix);
    }

    public Matrix(int row , int col) {
        this.matrix = new int[row][];
        Random rand = new Random();
        for (int i=0 ; i<row ; i++) {
            this.matrix[i] = new int[col];
            for (int j=0 ; j<col ; j++) {
                this.matrix[i][j] = rand.nextInt(2);
            }
        }
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int arr[] : this.matrix) {
            for (int e : arr) {
                str.append(e + " ");
            }
            str.append('\n');
        }
        return str.toString();
    }

    public int getValue(Index index) throws ArrayIndexOutOfBoundsException {
        return this.matrix[index.getI()][index.getJ()];
    }

    public List<Index> getNeighbors(Index index) {
        ArrayList<Index> neighbors = new ArrayList<>();
        int row = index.getI() , col = index.getJ();
        for (int i=row-1 ; i<row+2 ; i++) {
            for (int j=col-1 ; j < col+2 ; j++) {
                if (i != row || j != col) isNeighbor(neighbors , new Index(i,j));
            }
        }
        return neighbors;
    }

    private void isNeighbor(List<Index> neighbors , Index index) {
        try {
            getValue(index);
            neighbors.add(index);
        } catch (Exception e){}
    }


    public List<Index> getAdjacentNeighbor(Index index) {
        return getNeighbors(index).stream().filter(i->getValue(i) == 1).collect(Collectors.toList());
    }




}
