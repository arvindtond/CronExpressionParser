# Cron Expression Parser

## Implementation

Each field is parsed individually by maintaining separate classes for each field in cron expression.

All of these classes extends base class `FieldParser`.

For each char type special validations checks are performed.



## How to Run

Install [`maven 3.x`](http://maven.apache.org/install.html) and [`JDK 1.8`](https://openjdk.java.net/install/). 

To run unit test cases execute from the project root folder - 
 ```
 mvn clean test
 ```

To compile and create jar file execute from the project root folder - 
 ```
 mvn clean package
 ```

