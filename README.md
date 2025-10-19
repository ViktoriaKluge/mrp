# mrp
Architektur & Überblick

Ein Überblick über das Repos: Was die Anwendung macht, wie sie aufgebaut ist, welche Aufgaben die 
einzelnen Schichten haben und wie die wichtigsten Request-Flows aussehen.

Kurzbeschreibung

mrp ist eine kleine HTTP-Anwendung mit (derzeit nur funktionierenden) Endpoints für Authentifizierung 
und User-Funktionalität. Sie ist in Layers aufgeteilt mit einem HTTP-Layer (Server/Handler), der 
HTTP-Exchanges in interne Requests/Responses umwandelt, Controllern für Endpoint-Routing und Input-Mapping, 
Services für Business-Logik (Validierung, Autorisierung) und Repositories (derzeit nicht mit einer 
Datenbank verknüpft - nur lokales Memory).

Hauptfeatures:
(derzeit funktionierend)

Auth: Registrieren und Login (legt Nutzer an, gibt Tokens zurück).

Users: Anzeigen/Ändern von Nutzerdaten (Profil, Ratings, Favorites, Update/Delete).

Architektur
(der derzeitg funktionierenden Komponenten)

Ich organisiere das Projekt in vier Schichten mit klaren Verantwortlichkeiten. Jede Schicht spricht nur 
mit der direkt darüber/darunterliegenden.

1) Server / HTTP

Komponenten: HTTP-Server, Handler, RequestMapper, MrpApplication, Router; 
Aufgabe: Rohes HTTP annehmen, in Request umwandeln, an die App weitergeben. Der Handler baut aus 
HttpExchange ein internes Request-Objekt (Methode, Pfad, Header, Body).

2) Controller

Komponenten: AuthController, UserController; 
Aufgabe: Pfad/Methoden/Body auswerten, JSON → DTO mappen, HTTP-seitige Pflichtfelder prüfen, passenden 
Service aufrufen, Response zurückgeben.

3) Service

Komponenten: AuthService, UserService; Aufgabe: Kernlogik – Validierung, Autorisierung, Repositories 
kombinieren, Domain-Modelle/DTOs erzeugen/transformieren.

Beispiele:

AuthService.register: DTO validieren, User bauen, über Repository speichern.
AuthService.createToken: Credentials prüfen, Token-DTO liefern.
UserService: Autorisierung prüfen, UserRepository für Datenzugriffe verwenden.

4) Repository

Komponenten: AuthRepository, UserRepository (Interfaces) + jeweils MemoryRepository;
Aufgabe: Speicherschnittstelle. In-Memory bis es eine Verbindung zur DB gibt.

5) Extra

Exception-Mapping: Service/Repo-Exceptions werden über einen ExceptionMapper in HTTP-Antworten 
(Status + Body) übersetzt.
DTOs/Modelle: Controller mappen eingehende Bodies auf DTOs; Services wandeln DTOs ↔ Domain-Modelle.

Kurzbeschreibung Auth und User - Flow

POST /auth/register

Controller: JSON → UserCreateDto.
Service: register(dto) prüft isUser()/Validierung, erzeugt User (UUID), repository.save(user).
Konflikt (User existiert): UnprocessableEntityException.
Erfolg: 201 Created + User-DTO

POST /auth/login

Controller: JSON → UserLoginDto.
Service: createToken(dto) prüft Credentials via repository.login(), liefert UserLoggedInDto mit Token.
Fehler (Mismatch): EntityNotFoundException.
Tokenformat im Code: username + "-mrpToken".

Users (Überblick)

GET /users → Übersicht.
GET /users/{id}/profile → Profil.
GET /users/{id}/ratings, /favorites → Listen.
PUT /users/{id}/profile → Update (inkl. Passwort-Validierung).
DELETE /users/{id} → Löschen.

Autorisierung

UserServices erwarten einen Bearer-Token imt Format username + "-mrpToken" und gleichen ihn mit 
Nutzerdaten ab.

Fehlerbehandlung

Domain-Exceptions signalisieren Not-Found, Unauthorized, Validierungsfehler; ExceptionMapper setzt 
passenden HTTP-Status.

MemoryRepositories

Auth: MockUser werden automatisch erstellt. Bei der Registrierung wird zusätzlich zur Vollständigkeit
auch darauf geachtet, ob es Username oder Emailadresse schon gibt. Nach der Registrierung kann auch mit
dem neuen User eingeloggt werden.

Users: MockUser werden automatisch erstellt. AuthMemoryRepository und UserMemoryRepository werden derzeit
nicht synchronisiert. Es werden automatisch erstellte Favorites und Ratings angezeigt. Es können
Passwort und Username geupdatet werden oder ein User gelöscht - Änderungen werden beim nächsten Login
berücksichtigt.

Addition:

In /docs sind .puml-Dateien für Diagramme der Flows von Auth und Users, der Architektur und der AuthKlasse
als Beispiel. Zum Ansehen entweder das PlugIn für PlantUML downloaden oder auf editor.plantuml.com den 
Code eingeben. 

/api/test.http beinhaltet die derzeit funktionierenden Hauptfunktionen (MockUser-Daten als Kommentar 
enthalten).

GitHub-Link:
https://github.com/ViktoriaKluge/mrp.git
