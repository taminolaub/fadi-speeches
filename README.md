# Readme

Dieses Repository enthält eine mögliche Lösung der Testaufgabe von Fashion Digital implementiert in Kotlin.



## Ausführen der Anwendung

Die Anwendung ist in Kotlin mit Hilfe des Buildtools Maven implementiert. Verwendet wird das Framework Spring-Boot.

Mit folgendem Befehl kann die Anwendung gestartet werden (Standardport: 8080)
```
mvn spring-boot run
```

Um die inkludierten JUnit-Tests auszuführen kann der folgende Befehl verwendet werden.
```
mvn test
```

Falls keine Maven-Version auf dem Computer installiert ist, kann der enthaltene Maven-Wrapper verwendet werden, dazu muss der Befehl `mvn` in den oben genannten Ausdrücken durch `./mvnw` ersetzt werden.