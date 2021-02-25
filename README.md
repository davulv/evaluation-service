# Evaluation-Service

## Setup Guide

###### System Prerequisites
1. Have maven installed on your system
2. Need Java SDK v1.8 installed

###### Steps to start the application
1. Clone the repository into your local directory.
2. Run **"mvn clean install"** in the project root directory.
3. Once the build is success, you can see the **evaluation-service-0.0.1-SNAPSHOT.jar** file generated in  target folder.
4. Excute below command in target directory
	**java -jar evaluation-service-0.0.1-SNAPSHOT.jar**
5. This starts the app and is exposed on port number 8080

###### Testing the APIs
1. To evaluate any expression:
	* open command prompt and execute below command by replacing your-expression and userId in the url as required

	**curl -X POST "http://localhost:8080/api/v1/expression-evaluation/{userId}?expression=(your-expression)"**

	* The API returns the expression value as an integer. 
	* If he expression passed is invalid, **"Expression is invalid"** message is returned.

2. To get the most frequently used operator for a given userId:
	* replace userId in the url as required.

	**curl -X GET "http://localhost:8080/api/v1/expression-evaluation/{userId}"**

	* if user is found, the most frequently used operator is used
	* If user is not found, it returns a message **"user is not found"**