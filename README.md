# Ryanair test #

This is my interview test developed for Ryanair

### How do I get set up? ###

* Summary of set up
* Dependencies
* Reactive version
* Blocking version
* How to run tests
* Deployment instructions

#### Summary of set up ####
This project has been developed with 2 different approaches (reactive and blocking)
The reason for that is because i started it as a blocking web project, but then I 
realised that the embbeded Netty server used in the webflux spring boot starter did
not support blocking calls in the WebClient implementation, so i decided to move on
to a fully reactive version as i was half way there. At that point, it kind of made
sense to leave both approaches in the source code for comparison among them and my own
learning process.
The default approach, however, is the reactive, because, you know... it is cooler ;)

The difference in the approaches can be checked hitting the below endpoints URLs
* Reactive URL endpoint: http://localhost:8080/reactive/interconnections?departure=DUB&arrival=WRO&departureDateTime=2020-08-01T07:00&arrivalDateTime=2020-08-30T07:00
* Blocking URL endpoint: http://localhost:8080/blocking/interconnections?departure=DUB&arrival=WRO&departureDateTime=2020-08-01T07:00&arrivalDateTime=2020-08-30T07:00

Be careful to adapt the dates in the URLs to something closer to whenever you decide 
to run this project (i cannot guarantee that the Ryanair API endpoints survive as long
as this repo might)

#### Dependencies ####
This project depends on the below tech stack/frameworks
* Maven
* Spring boot
* Netty server (reactive version)
* Tomcat application server (blocking version)
* Mapstruct
* Lombok

##### Reactive version #####
Once you clone this repo locally you can run the below command to generate an executable JAR

    $ mvn clean install -DskipTests

and then run the below command to start up the Netty server

    $ java -jar target/ryanair-test-0.0.1-SNAPSHOT.jar
    
After this is done without trouble, you should be able to hit the endpoint at: http://localhost:8080/reactive/interconnections?departure=DUB&arrival=WRO&departureDateTime=2020-08-01T07:00&arrivalDateTime=2020-08-30T07:00
and see the results.
##### Blocking version #####
Once you clone this repo locally you should uncomment the dependency on the spring-boot-web-starter in the pom.xml file
 
    <!--        <dependency>-->
    <!--            <groupId>org.springframework.boot</groupId>-->
    <!--            <artifactId>spring-boot-starter-web</artifactId>-->
    <!--        </dependency>-->

And then run the below command to generate the executable JAR file 

    $ mvn clean install -DskipTests

and then run the below command to start up the Tomcat server

    $ java -jar target/ryanair-test-0.0.1-SNAPSHOT.jar
    
After this is done without trouble, you should be able to hit the endpoint at: http://localhost:8080/blocking/interconnections?departure=DUB&arrival=WRO&departureDateTime=2020-08-01T07:00&arrivalDateTime=2020-08-30T07:00
and see the results.

##### How to run the tests #####
Testing this project has been a bit of a challenge. Having said that, i think i managed
to test the important bits of it that cover most of the use cases.
Since this project is dependent on some external APIs trying to mock that part was not always easy.
The WebClient class is also a static class and there are several approaches on to how to test
this kind of programming that i have tried to use in the tests. However, these tests require
access to big JSON files that i did not always have at hand, so i decided to copy/paste some
JSON spit out by the actual API and use those in my tests.
To run the tests, you just don't skip them when building the app :)

    $ mvn clean install

This might fail, as i left one of the tests depending on the external ryanair API which means
that when the time comes and we are past August 2020 this code might start failing.
Why did i do this? well, i wanted to have a quick way to hit the actual endpoints and get 
different results. Mocking all of that manually for all the use cases i was thinking
about at the time would have been a waste of time as i did not know what to expect exactly. 
Examining the data manually is what gave me clues and 
eventually the reasoning to decide that the app was doing the right thing.
