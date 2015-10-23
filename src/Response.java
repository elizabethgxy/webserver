/**
 * Created by xinyiguo on 10/10/15.
 * analyze the request string, send back response based on the request url.
 */
import java.io.*;
public class Response {
    private static final int BUFFER_SIZE = 1024;
    Request request;
    OutputStream output;

    public Response(OutputStream output) {
        this.output = output;
    }

    public void setRequest(Request request) {
        this.request = request;
    }


    public void sendStaticResource() throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fis = null;
        try {
            String HEADER = "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: text/html\r\n" +
                            "Accept-Ranges: bytes\r\n";
            //output.write(HEADER.getBytes());

            File file = new File(ThreadPoolServer.WEB_ROOT, request.getUri());
            if(file.isDirectory()) {
                File[] subfiles = file.listFiles();
                String body = "<html>\n" +
                              "    <head>\n" +
                              "    </head>\n" +
                              "\n" +
                              "    <body>\n" +
                              "        <ul>";

                output.write(body.getBytes());

                for (File subfile : subfiles) {
                    String link = " <li><a href=" + request.getUri()
                                    + (request.getUri().equals("/")?"":"/")
                                    + subfile.getName()+ ">"
                                    + subfile.getName() + "</a></li>";

                    output.write(link.getBytes());
                }

                String bodyback = "</ul>\n"+
                                  "</body>\n"+
                                  "</html>";

                output.write(bodyback.getBytes());
            }
            else  if (file.exists()) {

                fis = new FileInputStream(file);
                int ch = fis.read(bytes, 0, BUFFER_SIZE);
                while (ch!=-1) {
                    output.write(bytes, 0, ch);
                    ch = fis.read(bytes, 0, BUFFER_SIZE);
                }

            }
            else {
                String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
                                      "Content-Type: text/html\r\n" +
                                      "Content-Length: 23\r\n" +
                                      "\r\n" +
                                      "File Not Found";
                output.write(errorMessage.getBytes());
            }
        }
        catch (Exception e) {
            System.out.println(e.toString() );
        }
        finally {
            if (fis!=null)
                fis.close();
        }
    }
}


