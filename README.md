 Java Challenge
 ============================
 
 One backend API to the SkipTheDishes Java Challenge, built using docker, Spring Boot, JPA, MySQL, Redis, Nginx and Thymeleaf.
 
 # Requirements
 * JDK 1.8+
 * Docker
 
 # Compiling
 
 To compile run the following command:
 
 ```bash
 ./mvnw clean package docker:build
 ```
 
 It will compile, package and build the docker image
 
 # Run
 
 To run this project, you must execute the following docker-compose command:
 
  ```bash
  docker-compose up
  ```
 It will bring up Nginx, MySQL, REDIS, the App, and Solr
  
 To access the "/api/v1/Order" endpoint you must use basic auth with the following credential: username = user, password = password
 
 In a successful docker-compose command this Java app will be available at:  
 
 * `http://127.0.0.1`
 * `http://127.0.0.1/api/v1/Cousine`
 * `http://127.0.0.1/api/v1/Customer`
 
 and so on 
 
 It was not a part of the challenge, but I did one integration with Solr, so that you can upload one PDF file and get it indexed, using the following addresses:
 
 * `http://127.0.0.1/files`
 * `http://127.0.0.1/solrsearch`
 