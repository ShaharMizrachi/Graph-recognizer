import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

public class Index implements Serializable {

    private int i,j;

    public Index(int i , int j) {
        setI(i);
        setJ(j);
    }

    public static Index getRandomIndex(int rangeX , int rangeY) { // rand new index
        Random rand = new Random();
        return new Index(rand.nextInt(rangeX) , rand.nextInt(rangeY));
    }

    public int getI() {
        return i;
    }

    public void setI(int i)  {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    @Override
    public String toString() {
        return "(" + this.i + "," + this.j + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Index index = (Index) o;
        return i == index.i &&
                j == index.j;
    }

    @Override
    public int hashCode() {
        return Objects.hash(i, j);
    }
}
