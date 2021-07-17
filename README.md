# EndpointsMonitoringService
This service is used to create monitored endpoints with specific URL and interval. The service apon monitored endpoint creation or startup of the service will within the interval keep connecting onto every URL and store the results such as payload and HTTP code.

## Running the project
As IDE, Intellij IDEA is recommended. Start by cloning the repository with `git clone https://github.com/TStrecha/EndpointsMonitoringService`.

Make sure the service will be able to connect to the MySQL database. All the logins and database name are in `src/main/resources/application.yaml`. Don't worry about tables and columns, liquibase will take care of it.

Last but not least make sure that port `8080` isn't taken by another application. If it is, you can easily change the port of the service in `src/main/resources/application.yaml`.

If you have all this, you can run the main class `/src/main/java/io.dxheroes.endpointsmonitoringservice/EndpointsMonitoringService.java` or in root directory call `mvn spring-boot:run` command.

### Running in docker
To run the service in a docker, make sure you have a mysql container running in your docker. Then run `mvn clean package`. This will create a `.jar` executable file in the target directory.

Now you can proceed to run a command `docker build -t endpoints-monitoring-service .` in the root directory of the service. This will build a new image called `endpoints-monitoring-service`. Now, simply start the container using command `docker run -dp 3000:3000 endpoints-monitoring-service` and the application should be available at your docker ip on port `3000`. Feel free to change the port of the docker container to your needs.

## Testing
Once you have run the service, the swagger should be available in a browser at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).

Swagger will show you all the endpoints available and will suggest you how the request should look like. 

You can also test service through other applications for REST API, but I really suggest you to use swagger as it is optimized to show you everything you need to pass to the request.

## Using the service
First of all you need to create user. The endpoint `POST /v1/user` allows you to create user with his username, email and **access token**. Access token has to be UUID and is not mandatory attribute in the user creation endpoint. If you don't pass access token, the code will automatically generate one for you. Remember to note down the access token - it is the only way to authorize the user in other endpoints. 

To create a monitored endpoint, use `POST /v1/monitored-endpoint` and pass an access token of user. The user has to exist. Keep in mind that everytime you work with this monitored endpoint only the user who created it has access to it.

Once monitored endpoint is created, the service will keep connecting on the url specified when creating/updating monitored endpoint. To get these results, use `/v1/monitoring-result/last/{count}/for/{monitored-endpoint-id}` to get last few results. In the path variable you decide how many results you want.

## Contribution
### Entities
Have you changed entity? Run `mvn liquibase:generateChangeLog` to generate the diff, check if it's alright, move it into `src/main/resources/liquibase/{current-year}` and include it in `src/main/resources/liquibase/changelog.yaml` if necessary.

### Adding new features
If you have added a new feature, please make a Unit test to test this feature. Make few test cases in which at least one will succeed and one will fail on purpose. Also, make sure all test including the previous ones do work by running `mvn test`. If not, change previous tests to make them work.

### Changing pom
If you have changed `pom.xml` run `mvn dependency:analyze` to list unused dependencies. Look through that list and remove those dependencies that are not needed.

### Changing the port
If you have changed the port in `src/main/resources/application.yaml`, change it back to `8080`.