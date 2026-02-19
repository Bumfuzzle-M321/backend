# Auth Flow Vereinfachung mit Spring Security

## Zusammenfassung der Änderungen

Dein Auth-Flow wurde mit Spring Security Funktionen modernisiert und vereinfacht. Hier sind die wichtigsten Verbesserungen:

## Was hat sich geändert?

### ✅ Vorher (Problematisch)
- ❌ Hardcodierte Credentials (admin/password)
- ❌ Keine Passwort-Hashing
- ❌ Manuelle Credential-Validierung im Controller
- ❌ Keine zentralisierte Authentication/Authorization
- ❌ Einfache RuntimeException für Fehler

### ✅ Nachher (Modern mit Spring Security)
- ✅ Benutzer in Datenbank mit gehashten Passwörtern (BCrypt)
- ✅ Spring Security `AuthenticationManager` für Credential-Validierung
- ✅ JWT-Filter für automatische Token-Validierung
- ✅ Zentralisierte SecurityConfig
- ✅ Bessere Fehlerbehandlung mit HTTP-Status-Codes

## Neue Komponenten

### 1. **User Entity** (`User.java`)
- Implementiert `UserDetails` Interface
- Integriert mit Spring Security
- Gehashte Passwörter in der Datenbank

### 2. **CustomUserDetailsService** (`CustomUserDetailsService.java`)
- Lädt User aus der Datenbank
- Wird von Spring Security verwendet

### 3. **JwtAuthenticationFilter** (`JwtAuthenticationFilter.java`)
- Validiert JWT-Tokens automatisch bei jedem Request
- Extrahiert Username aus Token
- Setzt Spring Security Context

### 4. **SecurityConfig** (`SecurityConfig.java`)
- Konfiguriert Spring Security
- Definiert öffentliche Routes (`/auth/**`, `/ws/**`)
- Konfiguriert CORS
- Setzt SessionCreationPolicy auf STATELESS (wichtig für JWT)

### 5. **DataLoader** (`DataLoader.java`)
- Erstellt Initial Test-User beim Start
- User: `admin` | Passwort: `password`

## Konfiguration

### application.properties
```properties
jwt.secret=your-secret-key-change-this-in-production-with-at-least-32-characters
jwt.expiration=3600000  # 1 Stunde in Millisekunden
```

⚠️ **WICHTIG**: `jwt.secret` in Production ändern!

## API-Nutzung

### Login
```bash
POST /auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "password"
}

Response:
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### Token verwenden
```bash
GET /protected-endpoint
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

### WebSocket mit Token
```javascript
// Bearer Token im Authorization Header
const socket = new WebSocket(
  `ws://localhost:8080/ws?token=${token}`
);
```

## Migration für bestehende Code

### 1. AuthController ist vereinfacht
- Keine manuelle Credential-Validierung mehr
- Spring Security macht das automatisch
- Besseres Exception Handling

### 2. JWT wird überall validiert
- `JwtAuthenticationFilter` für HTTP-Requests
- `JwtHandshakeInterceptor` für WebSockets (aktualisiert)
- Keine manuelle Checks mehr nötig

### 3. Passwörter sind jetzt sicher
- BCrypt Hashing (Algorithmus: bcrypt)
- Alte plaintext Passwörter funktionieren nicht mehr

## Nächste Schritte (Optional)

1. **Benutzer-Registrierung hinzufügen**
   ```java
   @PostMapping("/register")
   public User register(@RequestBody RegistrationRequest request) {
       User user = new User(request.username(), 
           passwordEncoder.encode(request.password()));
       return userRepository.save(user);
   }
   ```

2. **Refresh Token implementieren**
   - Längere Token-Lebensdauer mit Refresh-Mechanismus

3. **Rollen/Authorities hinzufügen**
   - ROLE_ADMIN, ROLE_USER, etc.
   - @Secured oder @PreAuthorize Annotations

4. **Audit Logging**
   - Login/Logout Events tracken
   - Failed Login Attempts

## Debugging

### Test User erstellen
Beim Start wird automatisch erstellt:
- Username: `admin`
- Password: `password` (BCrypt gehashed)

### Token testen
Verwende `test/get-token.http` mit dem neuen Format:
```http
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "password"
}
```

## Dependencies

Neue Dependencies in pom.xml:
- `spring-boot-starter-security` - Spring Security Framework
- Existierende JJWT Dependencies bleiben unverändert

## Häufige Fehler

| Problem | Lösung |
|---------|--------|
| "Invalid credentials" | User existiert nicht oder Passwort falsch |
| "401 Unauthorized" | Token fehlt, abgelaufen oder ungültig |
| CORS Error | Überprüfe SecurityConfig corsConfigurationSource() |
| WebSocket Auth fehlgeschlagen | Token im Authorization Header oder Query Parameter verwenden |

## Performance Hinweise

- Spring Security Filter läuft auf jedem Request (sehr effizient)
- JWT Token Validation ist O(1)
- Keine Sessions in der Datenbank (stateless)
- Skalierbar auf mehrere Server (keine Session-Persistierung nötig)

