# ProjectFinderBackend
## O działaniu aplikacji
Aplikacja do wyszukiwania i tworzenia grup dla osób o podobnych zainteresowaniach. Działanie aplikacji polega na utworzeniu konta za pomocą którego użytkownik może dołączyć do istniejącej grupy lub stworzyć własną. Każda grupa posiada czat w czasie rzeczywistym oraz przynależy do odpowienich kategorii. Użytkownik może dołączyć do grupy na 2 sposoby: bezpośrednio lub za pomocą kodu wygenerowanego przez lidera grupy. Szukanie grup odbywa się w obrębie głównych kategorii dla których dostępne są zależne od nich metody filtrowania. Każda grupa posiada lidera który ma możliwość zarządzania nią. Każdy użytkownik ma dostep do swojego profilu i  może edytować swoje dane. Serwis posiada również system znajomych a także możliwość wysyłania prywatnych wiadomości. Serwis wyposażony jest w system zarządzania użytkownikami i grupami za pomocą panelu administratora pozwalającego na przejrzenie listy zgłoszeń dotyczącej użytkownika, jego historii czatów lub historii czatu danej grupy. Strona posiada system powiadomień w czasie rzeczywistym. Dodatkową możliwością jest połączenie konta z kontem Discord w celu uaktualnienia danych w odpowiednim miejscu na profilu użytkownika. Wszystkie akcje związane z kontem tj. tworzenie konta, zmiana adresu, usuniecie konta musi zostać zweryfikowana za pomocą emaila.
## Technologia
- Java 11
- Spring Framework 
  - Spring Boot
  - Spring Security  
  - Spring Data
  - Spring MVC  
- WebSockets
- SSE
- JUnit 5
- Maven
- Hibernate
- H2
- Lombok 
- MapStruct 

## O projekcie
- Aplikacja napisana w stylu REST API z wykorzystaniem Spring Boot'a
- Implementacja czatu z wykorzystaniem Websocket'a
- Aktualizacja danych za pomocą SSE
- Niestandardowe wyjątki wraz z ich obsługą
- Weryfikacja mailowa
- Testy jednostkowe i integracyjne
- Lombok, Mapstruct, Maven, JUnit5
