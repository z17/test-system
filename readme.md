## Test System

### How to run? 

    ./gradlew bootRun
   
### Settings

In `src/main/resources/application.properties` there are the following lines: 

    spring.datasource.url=jdbc:h2:file:./db/test-system
    server.port=8080
    
`./db/test-system` - path to database files (in this case `./db/test-system.mv.db`)

`8080` - server port. App launch at `http://localhost:8080`

### Default user

Login: admin <br>
Password: qwerty