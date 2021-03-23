# Instrukcja uruchomieniowa
Do uruchomienia aplikacji wymagane są: Maven, JDK (wersja 11).

Plik books.json znajduję się w "WTT-Rec\misc\books.json"

Uruchomienie aplikacji:

Uruchomić konsolę, przejść do katalogu głównego aplikacji back-endowej tj. „..\WTT-Rec”,
a następnie poleceniem: 
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Ddatasource=C:/Users/Matt/Desktop/wtt/misc/books.json"
(W miejsce "C:/Users/Matt/Desktop/wtt/misc/books.json" należy podać sciężkę do pliku .json)
uruchomić aplikację. Aplikacja będzie dostępna pod adresem: localhost:8080/.
Zapytania należy wysyłać najlepiej poprzez programy typu "Postman".

Dashboard dostępny jest pod adresem localhost:8080/dashboard
