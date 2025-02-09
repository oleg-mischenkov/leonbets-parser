## Application: leonbets-parser

### Description

 The leonbets-parser application is designed to extract and display pre-match sports data from the bookmaker website 
leonbets.com without using a browser or any browser emulator.


### Building the Docker Image
1.	Clone or download the project repository containing the Dockerfile, pom.xml, and src folder.
2.	Open a terminal in the root directory of the project (where the Dockerfile is located).
3.	Build the Docker image by running:
 .

     docker build -t leonbets-parser:0.1 .

### Running the Container

 Start a container from the newly created image:

    docker run --name leonbets-parser-container leonbets-parser:0.1
