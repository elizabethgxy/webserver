# webserver
This project aims to build a simple thread pool web server supports GET requests from clients.

In order to run the project, in the project settings, we have two arguments. First, is the port we would like to listen 
to(example:8080). Second is the maximum number of threads running in the thread pool at the same time(example:20). 
You could set the parameters in the project configurations.

The web server supports simple dynamic GET requests. After the server running, users could build connections to the server 
concurrently. In our server file system, the default root path is /webroot. If users send GET request with url that exists 
in the file system, load and response with the context in the url or the links of subdirectories of the url. If the  url does 
not exists, throw error message. For concurrency testing, we could use ApacheBench as a simple tool.

PS: This is just a simple web server, for it seems in the requirement PDF I should start from socket programming. With Java 
servlet or Struts 2, its much easier to make fancy application servers.

