import java.io.*;
import java.net.Socket;
/* *
* socket object which implements runnable class.
* */
public class WorkerRunnable implements Runnable{
    protected Socket clientSocket = null;

    public WorkerRunnable(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
            InputStream input  = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            Request request = new Request(input);
            request.parse();

            Response response = new Response(output);
            response.setRequest(request);
            response.sendStaticResource();

            output.close();
            input.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

