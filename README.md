# CLOUDTEAMS
> Cloudteams project, implementation of Github, Bitbucket, JIRA, SonanQube and PaaSport widgets. These widgets are part of the whole CloudTeams platform and that are integrated at UI level, however, each widget can be deployed and run as standalone microservices.

## Prerequisites

* JDK 1.8.0_latest
* Maven 3.x
* Mysql 5.5.x

Before moving on, make sure you have the required JDK and Maven version.
 
	$ mvn -version
	$ java -version
	$ javac -version

###### Install Maven on Ubuntu 
	sudo apt-get install maven
###### Install Maven on OS X
	brew install maven
  


## Clone the Application

	$ git clone git@github.com:singularlogic/cloudteams.git   or instead: git clone https://github.com/singularlogic/cloudteams.git
	$ cd cloudteams
  
## Configure application properties

>In application.properties of each microservice configure the database used, service port and other properties.
  $ vim bitbucket-microservice/bitbucket-app/src/main/resources/application.properties
  $ vim github-microservice/github-app/src/main/resources/application.properties
  $ vim jira-microservice/jira-app/src/main/resources/application.properties
  $ vim paasport-microservice/paasport-app/src/main/resources/application.properties
  $ vim sonarqube-microservice/sonarqube-app/src/main/resources/application.properties

## Build all the services together

	$ mvn clean install
  
## Run the services

###### Run Bitbucket microservice 

	$ cd bitbucket-microservice/bitbucket-app
	$ mvn spring-boot:run
  
  ###### Run Bitbucket microservice 
  
	$ cd bitbucket-microservice/bitbucket-app
	$ mvn spring-boot:run
  
  ###### Run Github microservice 

	$ cd github-microservice/github-app
	$ mvn spring-boot:run
  
 ###### Run Jira microservice 

	$ cd jira-microservice/jira-app
	$ mvn spring-boot:run
  
 ###### Run PaaSport microservice 

	$ cd paasport-microservice/paasport-app
	$ mvn spring-boot:run
    
 ###### Run SonarQube microservice 

	$ cd sonarqube-microservice/sonarqube-app
	$ mvn spring-boot:run
  
  ###### Run a UI that renders the services 

	$ cd integration-ui
	$ mvn spring-boot:run
  
