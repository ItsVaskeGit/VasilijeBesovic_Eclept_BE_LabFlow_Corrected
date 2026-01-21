# LabFlow

An app designed to take in tests from patients and make the queue system to finish them.

App works via an HTTP endpoint for pretty much anything. Tests can be scheduled in 2 ways. Walk-in and Hospital.

Walk-in tests are prioritized, that means that queue system will accept your test in queue regardless of how many of them are already in a queue system.

While Hospital tests are not prioritized and when there are more than 20 tests waiting in a queue at one hospital the user will have to wait until there are less than 20 tests left in a database.

Every patient can see his tests all at once(paginated) or one at the time with accompanying id.

Meanwhile, admin can see all tests regardless of whose they are(paginated).

All authentication is done with simple JWT Token system. Authorization has been implemented on Admin's endpoint's so the only admins can access those.

By default, app does not have its sensitive properties like database credentials and others exposed in application.properties.

All needed properties for the app are put below in the .env file example.

You need to create a .env file in the root directory of the app and put all those there and customize them if needed.

The queue system utilizes events for moving forward with the tests.

If all machines available in one hospital are depleted all the technicians will be marked as busy till the refill process has commenced.

After that the system will resume queue as usual.

The app currently has 2 authorization levels: User and Admin.

Admin differs from the regular user by being able to: create, modify and delete Test Types, being able to see all tests regardless of ownership.

The app does not allow seeing other user's tests if the current user is not Admin.

Postman collection and dockerfile for the app will be provided in the repository (Postman collection will be in a form of JSON file).

# Example .env


```
spring.datasource.url=jdbc:postgresql://localhost:5432/labflow
spring.datasource.username=postgres
spring.datasource.password=123456

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

jwt-key=labflow-app-key-super-secret-classified
```

# Running in Docker Compose

Above configuration is provided to run the application in dev mode using maven.

In this repository the Dockerfile and Docker Compose file are provided to run the app as a docker container.

This docker compose file includes the app and database in it.

Running the app in docker compose should be as easy as running a command inside this folder:

```
docker compose up --build
```

User can add the data himself using the included HTTP endpoints, but the SQL file will be provided to aid the application testing.

It can be imported like this.

After successfully launching docker compose container open command prompt or terminal (by OS) and run:

```
docker ps
```

This will list all docker containers that are currently running.

You need one that has `postgres` image.

Make sure your CMD or Terminal is in the project directory.

Copy the id of said container and run the following:

```
docker cp data.sql <id>:/data.sql
```

It should have a result like:

```
Successfully copied 20kB to <id>:/data.sql
```

After that you need to run:

```
docker exec -i <id> psql -U postgres -p 5433 -d labflow -f /data.sql
```

Then the output should be like(If it's a little different that's okay):

```
COPY 4
COPY 20
COPY 4
COPY 0
COPY 2
COPY 0
COPY 4
COPY 21
```

After this procedure you app should be up and running along with the provided sample data in data.sql.

Just remember to set `spring.jpa.hibernate.ddl-auto` to none after doing this to avoid losing data everytime you restart docker container.