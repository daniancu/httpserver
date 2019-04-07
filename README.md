# Project achitecture

`Picture here`


Project is structured in 3 layers

`website api` - contains APIs to manage the website (add, remove, locate resouces)

`http api` - contains classes start/ stop a HTTP server and handle HTTP requests

`server app` - a sample HTTP server application and it's configuration for demo purposes. It runs by default on port `8001` and expose an embedded website



To build and runt the server use Maven command
```
mvn clean install exec:java
```
 
  