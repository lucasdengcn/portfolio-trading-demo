# Portfolio Trading Demo

This is a demo application. showing simulation on ticker data generation, refresh stock price, option price in real time,
and update portfolio NAV in real time.

## Tech Stack

- JDK 21
- Spring Boot 3.3.4
- H2 2.1.214
- Gradle 8.1+
- Protobuf 4.28.3
- Guava 33.3.1-jre
- JUnit 5.8.2

## Gradle Plugins

- protobuf (for compile .proto file)
- Jacoco (for code coverage)
- Spotless (for code formatting)

## Module
- broker
- market
- math
- portfolio
- pricing

## Schema Design

### Database Schema

```
src/main/java/resources/schema.sql
```

### Protobuf Schema

```
src/main/java/proto/*/market.proto
src/main/java/proto/*/portfolio.proto
```

## View Code on IDE

* make sure pick JDK 1.8

* import as Gradle project

* trigger protobuf plugin to generate java classes, check out build tasks as following

```shell
./gradlew tasks
./gradlew generateProto
```

## How to run?

* make sure JAVA_HOME point to JDK 1.8.

* make sure at the project root directory, and run following command in shell.

```shell
./gradlew bootRun
```

## Sample data

to demo this application, would need following csv files in the designed csv folder in project root directory.

if want to change the file content, please make sure following the csv headers.

```shell
cat csv/sample-options.csv

symbol,strikePrice,maturity(years),symbolType,stockSymbol
AAPL-OCT-2020-110-C,200,1,CALL,AAPL
AAPL-OCT-2020-110-P,400,1,PUT,AAPL
TELSA-NOV-2020-400-C,180,1,CALL,TELSA
TELSA-DEC-2020-400-P,260,1,PUT,TELSA

```

```shell
cat csv/sample-portfolio.csv

symbol,positionSize,stockSymbol
AAPL,1000,
AAPL-OCT-2020-110-C,-20000,AAPL
AAPL-OCT-2020-110-P,20000,AAPL
TELSA,-500,
TELSA-NOV-2020-400-C,10000,TELSA
TELSA-DEC-2020-400-P,-10000,TELSA

```

```shell
cat csv/sample-stock.csv

symbol,price,expected_return,deviation
AAPL,300.5,0.2,0.123
TELSA,200.2,0.3,0.234

```

## Configuration

to demo this application, also having follow configuration prepared.

```yaml

app:
  portfolio-relative-path: csv/sample-portfolio.csv # relative file path to project root directory
  stock-relative-path: csv/sample-stock.csv # relative file path to project root directory
  option-relative-path: csv/sample-options.csv # relative file path to project root directory

market:
  quote-producer-thread.enabled: true
  broker-dispatch-thread:
    enabled: true
    timeout: 10
  risk-free-interest-rate: 2 # for calculate option's price

```

## Code Coverage

![Code Coverage](code-coverage-report.png)

## Format Code

please ensure JAVA_HOME point to JDK 1.8.

```shell
./gradlew spotlessApply
```

## Build application with unit test

please ensure JAVA_HOME point to JDK 1.8.

```shell
./gradlew build
```

## Outcome

- Protobuf is good for messaging between systems given the super faster serialization and deserialization.
- Pure POJO is good for Object Pooling to avoid NEW Object every time. would be nice for parsing input and output data.
- ensure Object from Object Pool is immutable outside of pool.

## Upgrading from Java8

- rename javax.persistence to jakarta.persistence

## H2

https://h2database.com/html/features.html#compatibility

