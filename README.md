## Author
- Rheetik Sharma
- rheetiksharma6034@gmail.com
- 9372910325

  
# End to End Log ingestor System


This application is using kafka, Mongo DB and elastic search component for implementing the functionality

Mongo DB is used to search the data based on a column filter and elastic search is used for text based search in the whole log.

Kafka queues are used so that the insertion of data happens asynchronously without blocking the user. User will call the insert log API which will just insert the log in kafka queu. Then kafka consumer will be trigerred which will consume the log messages and insert the data in mongo DB and elastic search.

## Supported versions:

- Java 17
- Spring boot 3.1.5
- MongoDB 7.0
- MongoDB Java driver 4.11.0
- Maven 3.8.7
- OpenAPI 3
- Elastic search 7.15.0


## Commands


- Install the kafka client locally then go to the home directory of kafka where it's installed, and then Start the kafka client by running below commands.
    run following command to start zookeeper
    ```bash
    .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
    ```

    run the following command to start the kafka
    ```bash
    .\bin\windows\kafka-server-start.bat .\config\server.properties
    ```

    create kafka topic
    ```bash
    .\bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 3 --topic log-topic
    ```
- Update the configuration of kafka if you have kafka running in some other port , or else provide the existing kafka details.
- Start the server in a console with `mvn spring-boot:run`.
- Now to View the Query interface go to http://localhost:3000/index.html
- You can build the project with : `mvn clean package`.
- You can run the project with the fat jar and the embedded Tomcat: `java -jar target/java-spring-boot-mongodb-starter-1.0.0.jar`.

## Example API Calls

 - First of all clear all the data from both Mongo db and elastic search
     ```bash
      curl --location 'http://localhost:3000/api/logs/deleteAll' \
      --data ''
     ```

 - To insert the log data 
    ```bash
    curl --location 'http://localhost:3000/api/logs/insert' \
    --header 'Content-Type: application/json' \
    --data ' {
      "level": "error",
      "message": "Failed to connect to DB",
        "resourceId": "server-1234",
      "timestamp": "2023-09-15T08:00:00Z",
      "traceId": "abc-xyz-123",
        "spanId": "span-456",
        "commit": "5e5342f",
        "metadata": {
            "parentResourceId": "server-0987"
        }
    }
    '
    ```

  - To Retrieve the data by using text based search
      ```bash
      curl --location 'http://localhost:3000/api/logs/textBasedSearch' \
      --header 'Content-Type: application/json' \
      --data ' {
        "textSearch": "info"
      }
      '
      ```

        
  - To Retrieve the data by using filter based search
      ```bash
      curl --location 'http://localhost:3000/api/logs/filterBasedSearch' \
      --header 'Content-Type: application/json' \
      --data ' {
        "filterType": "level",
        "filterText": "info"
      }
      '
      ```

