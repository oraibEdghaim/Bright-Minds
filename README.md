# Bright-Minds

## General Information
This project is created to Bright Minds, as coding exercise.

### GitHub Link:
https://github.com/oraibEdghaim/Bright-Minds

## Language and technologies
This web application was created by eclipse using maven with these specifications.

 - JSP and servlet - web maven project (JEE)

 - Micrsoft access DB

## How to install the project and deply it in localhost 

1- download the project

2- in your eclipse import the project as maven project

3- configure DB property file: 
   - path : /src/main/webapp/WEB-INF/classes/dbConnection.properties
   - proprity name ## db-url : configure db path here

4- update maven project

5- Build maven project

6- add the project to you server (tomcat)

7- in your browser, hit "[server name]:[port]/BMAssignment/"


## Known Issues 
1- The same user can login twice in different browsers but the application manges it in the same browser.

## Missing requirments
1- JUnit 

2- Hashed account number

