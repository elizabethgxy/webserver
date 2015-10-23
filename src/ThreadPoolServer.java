import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

    /**
    * A simple web server uses thread pool to support concurrency requests.
    */
public class ThreadPoolServer implements Runnable{
    public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";
    private int serverPort;
    private ServerSocket serverSocket;
    private boolean shutdown = false;
    private Thread  runningThread;
    private ExecutorService threadPool = null;
    public ThreadPoolServer(int port){
        this.serverPort = port;
    }



    public static void main(String[] args) {
        int serverPort = Integer.parseInt(args[0]);
        int numThread = Integer.parseInt(args[1]);
        ThreadPoolServer threadserver = new ThreadPoolServer(serverPort);
        threadserver.threadPool =  threadPoolBuild(numThread);
        new Thread(threadserver).start();
        threadserver.attachShutDownHook();
        while (true) {
            try {
                Thread.sleep(20 * 10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static ExecutorService threadPoolBuild(int numThreadPool){
        return  Executors.newFixedThreadPool(numThreadPool);
    }

    /**
     * server socket running process.
     */
    public void run() {
        synchronized(this) {
            this.runningThread = Thread.currentThread();
        }
        try {
            this.serverSocket = new ServerSocket(this.serverPort, 1, InetAddress.getByName("0.0.0.0"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        while (!shutdown) {
            Socket socket = null;
            try {
                socket = this.serverSocket.accept();
            } catch (Exception e) {
                if (isStopped()) {
                    System.out.println("Server Stopped.");
                    break;
                }
                e.printStackTrace();
                continue;
            }
            this.threadPool.execute(new WorkerRunnable(socket));
        }
        this.threadPool.shutdown();
        System.out.println("Server Stopped.") ;
    }

    private synchronized boolean isStopped() {
        return this.shutdown;
    }

    public synchronized void stopServer(){
        this.shutdown = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }
    /**
    * gracefully shut down the server.
    * */
    public synchronized void attachShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                stopServer();
                System.exit(0);
            }
        });
    }

}