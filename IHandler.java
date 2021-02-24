import java.io.InputStream;
import java.io.OutputStream;

public interface IHandler {

    public void handle(InputStream input , OutputStream output) throws Exception;

    
}
