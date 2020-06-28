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

## Tests Coverage Details:

![Coverage](https://i.ibb.co/9H0zSVj/coverage-details-readme.png)

## Features developed:

- Complete rule set of the game;
- CLI;
- GUI;
- Socket communication between server and client.

### Additional features:

- Multiple matches at the same time;
- 5 Advanced Gods implemented.

