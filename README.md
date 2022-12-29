# Overview
The project is used to decrypt MD5 encrypted hashes. It consists of two microservices: an authorization service which authorizes the user and validates his credentials, and a hash-translator service, which sends the application to decrypt hashes to the exterior website (https://md5decrypt.net/en/Api/api.php). Both services are written on Java 17 and Spring 2.5.2. Both are built by Gradle. The authorization service uses PostgreSQL, the translator service uses MongoDB. And both services are containerized with Docker.  
# Requirements
Docker is installed and running.
# Build & launch  
Both services are built and launched simultaneously with the following commands:
```
cd path/to/project
docker-compose up --build authorization-service --build hash-translator
```  
# Operation 
## Authorization service.  
To start the work with the application, you need to go to the http://localhost:80/login URL. If your input corresponds to the admins' email and password (they are "admin@gmail.com" and "admin" by default), you will be granted access to adding and deleting new users via /admin/add_user and /admin/delete_user with their credentials as request parameters. If you entered the login information of a normal user, you will be redirected to the second service.  
## Hash-translator service  
From the authorization service you are redirected to the http://localhost:81/api URL. There you are able to submit a JSON with hashes to decrypt. You can also visit /api/receive to submit an id of already existing application. When you submit the data (in both these URLs), it is passed to a transitional controller, which interacts with the authorization service, which validates the credentials you have entered. If they are correct you will see the result of your submission. If you have submitted JSON with hashes, you will see the message that your application was processed and its id. If you have submitted the id and it is correct, you will see the result of the application.
# Important notes
- The website that decrypts hashes has a limit of how many hashes it is able to try to decrypt per day. If you need more than this limit, you need to make your own account on the website and change the `mapOfParams` method located in the `hashtranslator-service/src/main/java/tt/hashtranslator/util/Utils.java` file accordingly.
- If you need to launch the application on Linux/Mac, it could be necessary to modify the `docker-compose.yml` file.