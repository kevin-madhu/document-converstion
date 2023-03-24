# originalDocument-converstion
A SpringBoot application for XML originalDocument conversion

To run the program, we need Kafka to be running. Docker compose can be used.

docker-compose -f src/main/docker/docker-compose up

Use environment variable DOCUMENT_CAMEL_WATCH_FILE_DIRECTORY to
change the directory Camel should watch for file uploads. Eg:

DOCUMENT_CAMEL_WATCH_FILE_DIRECTORY=/home/kevin/z
