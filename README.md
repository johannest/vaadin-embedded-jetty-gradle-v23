# Vaadin Gradle Embedded Jetty example

This project demos the possibility of having Vaadin project using Gradle with an embedded Jetty.

**This example utilizes highly code of this great library https://github.com/mvysny/vaadin-boot** but makes it maybe a bit easier to do further configurations for example for other servlets directly in Main.java.

**If you do not need any custom Servlet configurations please use the Vaadin Boot library directly as shown in this simple example project: https://github.com/mvysny/vaadin-boot-example-gradle**

**This is not production ready example, but just a POC / demo!**

Prerequisites:
* Java 11 or higher
* node.js and npm. You can either let the Vaadin Gradle plugin to install `nodejs` and `npm/pnpm` for you automatically, or you can install it to your OS:
  * Windows: [node.js Download site](https://nodejs.org/en/download/) - use the .msi 64-bit installer
  * Linux: `sudo apt install npm`
* Git
* (Optionally): Intellij Ultimate

## Running In Development Mode
Set `Main.PRODUCTION_MODE = false`
Run the following command in this repo:

```bash
./gradlew clean run
```

Now you can open the [http://localhost:8080](http://localhost:8080) with your browser.

## Building In Production Mode

Set `Main.PRODUCTION_MODE = true`
Run the following command in this repo:

```bash
./gradlew clean build -Pvaadin.productionMode
./gradlew run
```

Now you can open the [http://localhost:8080](http://localhost:8080) with your browser.

## Running with Docker
* Do the Gradle build, then:
```bash
docker build . -t v24docker:latest
docker run -p 8180:8080 v24docker:latest
```