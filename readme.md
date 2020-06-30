# Santorini - Sguanci Spatafora Vergani INGSW 19-20
![Santorini](https://i.ibb.co/nszm4hX/santorini-logo.png)

## In order to run these executables, you need to install JDK 1.8 (available [here](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html))

JAR Executable files are placed in /deliveries/executables folder.

To run the Server Executable, run this command from terminal / cmd:

```
java -jar server.jar
```

To run the CLI Client Executable, run this command from terminal / cmd:

```
java -jar cli.jar
```

If you're on Windows, you should first run this command to allow the game to use ANSI codes to show its beautiful (and colorful) CLI interface:

```
REG ADD HKCU\CONSOLE /f /v VirtualTerminalLevel /t REG_DWORD /d 1
```

To run the GUI Client Executable, you have 2 options: the classic command from terminal / cmd:

```
java -jar gui.jar
```

Or you can simply double click the JAR file.

## Dependencies

| Dependency | Usage |
| --- | --- |
| `maven` | Package manager used for building and managing the project, with a particular focus on lifecycle handling (running tests, building executables). |
| `junit` | Tool used to unit test Java classes. In particular, it was used in this project to test Model and Controller components (MVC). |
| `gson` | Used to serialize the game Board as a JSON String in order to avoid the necessity to send complex object through the Socket's stream. |
| `mockito` | Used as a support to JUnit tests and to improve the code coverage of the tests themselves. It adds the capability to test that a specific method with certain parameter has been called under certain conditions. |

## Tests Coverage Details

![Coverage](https://i.ibb.co/9H0zSVj/coverage-details-readme.png)

##### Tests coverage report is available [here](https://github.com/CosimoSguanci/ing-sw-2020-sguanci-spatafora-vergani/blob/master/deliveries/coverage-report)

#### To run the tests, run this command:

```
mvn test
```

#### Note that, to test Server-side concurrency, an intensive stress [test](https://github.com/CosimoSguanci/ing-sw-2020-sguanci-spatafora-vergani/blob/master/src/test/java/it/polimi/ingsw/controller/ControllerConcurrencyTest.java) has been launched. However, it is marked as @Disabled in production (it won't run with `mvn test`) to make the test phase faster (that test lasts ~20 seconds).

## JavaDoc

#### JavaDoc can be found [here](https://github.com/CosimoSguanci/ing-sw-2020-sguanci-spatafora-vergani/blob/master/deliveries/javadoc)


## Features developed

- Complete rule set of the game;
- CLI;
- GUI;
- Socket communication between server and client.

### Additional features

- Multiple matches at the same time;
- 5 Advanced Gods implemented.

