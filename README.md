# Course Management
J2EE Course project

## Requirements
    - MySQL
    - Java 8

## Build and run project
1. Set-up database

    `$ mysql -u<username> -p<password>
    mysql> CREATE DATABASE course_management;
    mysql> quit;`

2. Rename `application.yml.sample` to `application.yml`
3. Change the username and password to that of your MySQL database
4. Run 
    + cd course_management
    + ./gradlew bootRun
