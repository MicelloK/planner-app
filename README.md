# System do planowania spotkań
autor: Michał Kobiera

## Główne funkcjonalności systemu
- dodawanie/usuwanie spotkania o określonym terminie i uczestnikach
- edytowanie ustawień spotkania
- dodawanie/usuwanie nowych użytkowników do bazy danych
- edytowanie ustawień istniejących użytkowników
- szukanie terminu bezkolizyjnego dla podanych uczestników i planowanym czasie spotkanie

## Wymagania wstępne
Program został napisany w `openjdk-21` i zbudowany przy uzyciu `gradle 8.5`

Wszystkie ustawienia zostają dostarczone wraz z kodem źródłowym programu.

Funkcja główna programu znajduje się w klasie `Main` w folderze `src/main/java/dev/micellok/` 

## Opis programu
System został zaimplementowny w języku Java. Do niego został dołączony prosty interfejs graficzny
napisany przy użyciu JavaFX.

### Przydatne wskazówki do korzystania z programu :))
Po wybraniu kalendarza użytkownika w lewym górnym rogu, i daty spotkania w prawym górnym rogu
po prawej stronie (jeśli takie spotkania istnieją) pojawi sie lista spotkań. 
Po wyborze spotkania po lewej stronie wyświelą się informacje o tym spotkaniu.
W opcji wyszukiwania terminu spotkania czas planowanego spotkania podajemy w minutach
Po dodaniu uczestników do spotkania należy zamknąć okno przyciskiem krzyżyka.

## Bonus

Na początku zacząłem od zrobienia frontendu w React.js, ale to jednak nie byłby wtedy projekt na jeden tydzień.
Mimo to dołączam to co zrobiłem w folderze `bonusowyFront`. Nie jest to częśc rozwiązania, ale zawsze jakiś przykład mojego kodu:)

### PS
W IntelliJ czasami zmieniał mi SDK. Gdyby wystąpił taki błąd należy wybrać odpowiednią wersję
otwierając Files/Project Structure/Project

