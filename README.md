# Java HTTP Server 

This project contains the first development iteration of a Java HTTP Server.  
Its is not a full webserver implementation as this will require much more time. 
Rather, it defines an initial architecture and implements some basic web server functions: GET, HEAD, PUT, DELETE, OPTIONS methods
It supports HTTP 1.1. Connection keep-alive is not supported. 
 
The project architecture has 3 main layers, each layer's components being stored in a separate Java package    

`websiteapi` - contains the core API that manages the website (it exposes basic CRUD APIs for resource management)

`httpapi` - contains APIs to run a HTTP server and handle HTTP requests for website resources

`serverapp` - a sample HTTP server application and it's configuration for demo purposes. It runs by default on port `8001` and exposes an embedded website


The layer dependency can be seen below:
(top layers depend on lower layers with the core having no dependencies like in a Onion style architecture) 


```
serverapp --> httpapi --> websiteapi
    |________________________^
```


Software dependencies: 

*  Java 8 SDK  
* [Project Lombok](https://projectlombok.org/) library to generate logging and getter/setters
* JUnit 4 for unit testing
* Apache Maven 3  to build and run the server


To start the server use Maven command:
```
mvn clean install exec:java
```

Then point your browser to [localhost:8001/](http://localhost:8001)

**Note:** The server was tested on Windows and Linux, MacOS behavior is not tested  
  