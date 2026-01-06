# mrp – Media Rating Platform

## Überblick

**mrp** ist eine HTTP-basierte Anwendung zur Verwaltung und Bewertung von Medien mit Benutzer-, Authentifizierungs-, 
Autorisierungs- und Empfehlungsfunktionalität.

Das Projekt folgt einer **klaren Layered Architecture** mit strikter Trennung von Verantwortlichkeiten zwischen 
HTTP-Handling, Applikationslogik, Controllern, Services und Repositories.  


---

## Kernfunktionen

- **Authentifizierung**
    - Registrierung neuer Benutzer
    - Login mit Token-Erstellung (Bearer Token)
- **Users**
    - Profil anzeigen & ändern
    - Ratings & Favorites anzeigen
    - Empfehlungen abrufen
    - Benutzer löschen
- **Media**
    - Medien anlegen, anzeigen, ändern, löschen
    - Medien bewerten und favorisieren
- **Ratings**
    - Eigene Ratings ändern & löschen
    - Ratings anderer User liken
    - Ratings bestätigen (Admin)
- **Leaderboard**
    -  Rankings aktiver User

---

## Architekturübersicht

Die Anwendung ist in **klar definierte Schichten** gegliedert.  
Jede Schicht kommuniziert ausschließlich mit den direkt angrenzenden Schichten.

---

## Schichten im Detail

### 1) Server / HTTP Layer

**Aufgabe**
- Annahme roher HTTP-Anfragen
- Parsen von Methode, Pfad, Headern und Body
- Weitergabe interner `Request`-Objekte an die Applikation

**Zentrale Komponenten**
- `Server`
- `Handler`
- `Request`
- `Response`

Diese Schicht enthält **keine Business-Logik**.

---

### 2) Application Core

**Zentrale Klasse:** `MrpApplication`

**Aufgaben**
- Zentrale Eintrittsstelle für jeden Request
- Steuerung des gesamten Request-Flows
- Zentrales Exception-Handling

**Ablauf**
1. `Request → RequestDto`
2. Authentifizierung über `AuthMiddleware`
3. Controller-Auflösung über `Router`
4. Weitergeben an passenden Controller
5. Exception-Mapping über `ExceptionMapper`

---

### 3) Middleware (Cross-Cutting Concerns)

**AuthMiddleware**
- Extrahiert Bearer-Token aus den Headern
- Validiert Authentifizierung
- Setzt den authentifizierten `User` im `RequestDto`
- Öffentliche Endpoints werden ohne Token akzeptiert

Die Authentifizierung ist damit **klar vom Controller-Code getrennt**. Dadurch kommen nur Requests von 
authentifizierten Usern in die Controller.

---

### 4) Controller Layer

- `UserController`
- `MediaController`
- `RatingController`
- `LeaderboardController`

**Aufgaben**
- Auswertung von Pfad und HTTP-Methode
- Mapping des Request-Bodys auf DTOs
- Autorisierungsprüfungen
- Aufruf der Service-Schicht
- Rückgabe einer HTTP-`Response`

Controller greifen **nicht direkt auf die Datenbank** zu. Nur für diesen User erlaubte Handlungen werden an den 
Service Layer weiter gegeben.

---

### 5) Service Layer

- `AuthService`
- `UserService`
- `MediaService`
- `RatingService`
- `FavoritesService`
- `RecommendationService`
- `LeaderboardService`

**Aufgaben**
- Geschäftslogik
- Validierung & Autorisierungsregeln
- Orchestrierung mehrerer Repositories
- Transformation zwischen DTOs und Domain-Modellen

---

### 6) Repository Layer

**Interfaces**
- `UserRepository`
- `MediaRepository`
- `RatingRepository`

**Implementierungen**
- `DbUserRepository`
- `DbMediaRepository`
- `DbRatingRepository`

**Aufgaben**
- Datenbankzugriff (PostgreSQL)
- SQL-Mapping
- Rückgabe über `Optional`
- Keine Business-Logik

---

## Vereinfachter Request-Flow

- HTTP Request
- Server / Handler
- MrpApplication
- AuthMiddleware
- Router
- Controller
- Service
- Repository
- Response


---

## Authentifizierung & Autorisierung

- Authentifizierung erfolgt über **Bearer-Token**
- Token-Format: username-mrpToken

- Token wird bei allen geschützten Endpoints geprüft
- Autorisierung (Owner/Admin) erfolgt in Controllern

---

## Fehlerbehandlung

- Fachliche Fehler werden über **Domain-Exceptions** signalisiert
- `ExceptionMapper` übersetzt diese zentral in HTTP-Responses:
- `401 Unauthorized`
- `404 Not Found`
- `422 Unprocessable Entity`
- weitere Statuscodes je nach Fehler

Dadurch ist die Fehlerbehandlung **konsistent und zentralisiert**.

---

## Technische Entscheidungen & Begründungen


### Warum `RequestDto`?
- Bündelt alle Request-Eingaben
- Entkoppelt HTTP-Parsing von Applikationslogik
- Vereinfacht Controller-Signaturen
- Gut testbar

### Warum AuthMiddleware?
- Authentifizierung ist ein Cross-Cutting Concern
- Controller bleiben fokussiert auf Domänenlogik
- Einheitliche Token-Behandlung
- Sehr gut isoliert testbar

---

## Unit-Test-Strategie

### Abgedeckte Bereiche

- **AuthMiddleware**
- Token-Parsing
- Öffentliche vs. geschützte Endpoints
- **Controller**
- Routing
- Autorisierung
- korrekte Service-Aufrufe
- **Services**
- Business-Logik
- Validierung
- Fehler- und Edge-Cases
- **DTOs**
- Validierungslogik
- Passwort- & Input-Konsistenz
- **Request-Parsing**
- Header-Verarbeitung
- JSON-Parsing

### Warum genau diese Logik getestet wurde
- Controller-Tests sichern korrektes Routing & Autorisierung
- Service-Tests schützen die Kernlogik der Anwendung
- DTO-Tests sind notwendig, da Validierungslogik enthalten ist
- Repositories werden in Unit-Tests gemockt 

---

## Aufgetretene Probleme & Lösungen

### 1) Private Methoden testen

**Problem:**  
Wichtige Logik befand sich in privaten Methoden und war daher nicht direkt testbar.

**Lösung:**  
Das Verhalten wurde über öffentliche Methoden getestet.  
Seiteneffekte und übergebene Parameter wurden mithilfe von `ArgumentCaptor` überprüft, anstatt private Methoden 
direkt aufzurufen.

---

### 2) DTO-Validierung vs. Business-Logik

**Problem:**  
Es bestand die Gefahr, Business-Logik in DTOs zu verlagern.

**Lösung:**  
DTOs enthalten ausschließlich minimale Konsistenz- und Eingabeprüfungen.  
Die eigentliche Geschäftslogik bleibt strikt in der Service-Schicht.

---

### 3) Token-Handling & Edge-Cases

**Problem:**  
Fehlende oder ungültige `Authorization`-Header führten zu uneindeutigem oder inkonsistentem Verhalten.

**Lösung:**  
Das Token-Parsing wurde strikt implementiert und wirft bei ungültigen Zuständen explizite Exceptions.  
Zusätzlich wurden gezielte Unit-Tests für Edge-Cases (fehlender Header, falsches Format) ergänzt.

---

### 4) Mockito Strict-Stubbing-Probleme

**Problem:**  
`PotentialStubbingProblem`-Fehler traten auf, da mutierte Objekte nicht mehr mit den ursprünglich gestubbten 
Argumenten übereinstimmten.

**Lösung:**  
Verwendung von `any()`-Matchern in Kombination mit `ArgumentCaptor`, um relevante Parameter nach dem Methodenaufruf 
gezielt zu überprüfen.

---
### 5) Authentifizierung vs. Autorisierung klar trennen

**Problem:**  
Zu Beginn war nicht klar abgegrenzt, wo Authentifizierung (Ist das Token gültig?) und Autorisierung (Darf der User 
diese Aktion ausführen?) stattfinden sollen.

**Lösung:**
- **Authentifizierung** erfolgt zentral in der `AuthMiddleware` (Token-Validierung und Zuordnung des Users).
- **Autorisierung** erfolgt explizit in den Controllern (z.B. Owner- und Admin-Prüfungen).
- Services gehen ausschließlich von bereits autorisierten Aufrufen aus und enthalten keine Rollen- oder Berechtigungslogik.

---

### 6) Zu feine Aufteilung nach Endpoints

**Problem:**  
Zu Beginn wurde für nahezu jeden Pfad (z.B. `users`, `auth`, `media`, `ratings`) jeweils ein eigener Controller, 
Service und ein eigenes Repository angelegt. Diese sehr umständliche Struktur führte zu einer hohen Anzahl an Klassen 
mit stark überlappender Verantwortung und erschwerte das Verständnis des Gesamtsystems.

**Lösung:**  
Es wurde erkannt, dass eine fachlich sinnvollere Bündelung notwendig ist.  
Controller, Services und Repositories wurden entlang klarer Domänen (z.B. User, Media, Rating) zusammengeführt, 
anstatt strikt nach einzelnen Endpoints zu trennen.

Dieses Refactoring erforderte einen erheblichen Zeitaufwand, führte jedoch zu:
- klareren Verantwortlichkeiten
- weniger Redundanz
- besserer Wartbarkeit
- verständlicherer Architektur

---

### 7) Typ-Mapping zwischen SQL und Java (Timestamp, Double, eigene Klassen)

**Problem:**  
Beim Mapping zwischen SQL-Datenbankwerten und Java-Objekten traten wiederholt Probleme mit inkompatiblen oder unerwarteten Typen auf.  
Insbesondere folgende Fälle waren fehleranfällig:

- `TIMESTAMP` ↔ `LocalDateTime` / `Instant`
- numerische SQL-Typen ↔ `Double`
- Aggregatwerte (z.B. `AVG`) mit möglichem `null`-Ergebnis
- Mapping von SQL-Ergebnissen auf eigene DTOs und Domain-Klassen

Dies führte u.a. zu Laufzeitfehlern beim Deserialisieren, unerwarteten `null`-Werten oder nicht unterstützten Typkonvertierungen.

**Lösung:**
- Explizite Typkonvertierung beim Lesen aus dem `ResultSet` (z.B. `getTimestamp()`, `getDouble()`).
- Bewusster Umgang mit `null`-Werten, insbesondere bei Aggregatfunktionen wie `AVG`.
- Trennung zwischen:
    - **SQL-spezifischen DTOs** (z.B. `SQLMediaDto`, `SQLRatingDto`)
    - **Domain-Modellen** (`Media`, `Rating`, `User`)
- Keine automatische oder implizite Typkonvertierung zwischen SQL und Domain-Klassen.

---

## Dokumentation & Tests

### `/docs`
- PlantUML-Diagramme zur Architektur und zu zentralen Request-Flows
- Zeitaufwand und Arbeitsaufteilung

### `/api/test.http`
- End-to-End-Tests für die wichtigsten Use-Cases der Anwendung

---
