# 💹 Rates Search

### 🎯 Objective
Provides conversion of predetermined currencies, keeping records per user,
and the possibility of recovering the history of conversions performed by the user.

### 🏷️ Features
The API implements layers that simplify its understanding, maintenance and scalability.

### 🖥️ Technology Choices
The API is based on Kotlin 1.4.30 and Spring Boot with Webflux version 2.4.6. This choice considerably reduces setup time for a reactive project, as well as abundant documentation and tutorials on the internet.
The option for Kotlin is because it is the language I have more work with at the moment and because it has a native way, through common means of writing code without blocking or optimized for less use of resources. 
For the combined storage of data, R2DBC was adopted, which also allows operations to be carried out reactively, generating greater synergy with the proposed non-blocking of the project. For documentation, we adopt openapi and swagger consolidated libraries for documentation with Java or Kotlin.
For testing, Mockito was adopted for having a simple integration, but at the same time robust in terms of features for Kotlin, along with Mockito, MockWebServer was adopted to simulate the responses from the external API used to get the quotes, both have multiple features that cater to different sizes of projects.

### 🗄️ Layer Separation
The development model is based on the domain-oriented design architecture (DDD) very common in microservices today because it abstracts the complexity of the business through a simplified representation of the same, strong separation of the domain into layers and maintenance of the domain logic in the innermost layer. 
The choice for this architecture is due to the use of it in my most recent works and for applying modern concepts in the development of consistent and scalable microservices.

### 🏃🏃 How to run it

1 - Requirements:

* At least [Java Development Kit 11](https://openjdk.java.net/install/) configured on the machine.
* [Docker](https://docs.docker.com/get-docker/) environment set and ready.
* Make sure you have at least IntelliJ IDEA 2018.1 and Kotlin plugin 1.4.3X. This project uses a [Kotlin based Gradle](https://docs.gradle.org/6.3/dsl/) configuration, here some [examples](https://github.com/gradle/kotlin-dsl-samples).

2 - Clone this project locally.

3 - Run app:\
Linux/macOS: `./gradlew clean bootRun`\
Windows: `.\gradlew.bat clean bootRun`

### ⌨️ Usage
When performing a query, an id must be entered for the user who is making the request.\
Here are some examples of using the API:\
\
Endpoint to get conversion:\
curl --location --request GET 'http://localhost:8080/exchange-search/rates?userId=123&originCurrency=GBP&destinationCurrency=USD&value=2.0'

Endpoint to get a user's conversion history:\
curl --location --request GET 'http://localhost:8080/exchange-search/rates/transactions-history?userId=123'

### 🧪  Run All Tests Locally
Linux/macOS: `./gradlew clean test`\
Windows: `.\gradlew.bat clean test`

### 🧪  Check coverage with jacoco
Linux/macOS: `./gradlew jacocoTestReport`\
Windows: `.\gradlew.bat jacocoTestReport`

### 📑 Rest API Documentation
http://localhost:8080/exchange-search/api-docs \
http://localhost:8080/exchange-search/swagger-ui.html