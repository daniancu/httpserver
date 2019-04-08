# Java HTTP Server  

This project contains APIS to run a HTTP server embedded or as a standalone application. 
 
Project is structured in 3 layers, each layer components are packaged in it onw java package. 

`websiteapi` - contains APIs to manage the website (add, remove, locate resouces)

`httpapi` - contains classes start/stop and embeddable HTTP server and handle HTTP requests

`serverapp` - a sample HTTP server application and it's configuration for demo purposes. It runs by default on port `8001` and expose an embedded website



To build and runt the server use Maven command
```
mvn clean install exec:java
```
 
 Then point your browser to [http://localhost:8001/]
  