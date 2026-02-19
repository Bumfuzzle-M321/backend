package ch.bumfuzzle.websocket.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiration}")
  private long jwtExpiration;

  private SecretKey getKey() {
    final String base64Key = Base64.getEncoder()
        .encodeToString(jwtSecret.getBytes(StandardCharsets.UTF_8));
    return Keys.hmacShaKeyFor(base64Key.getBytes(StandardCharsets.UTF_8));
  }

  public boolean isValid(final String token) {
    try {
      Jwts.parser()
          .verifyWith(getKey())
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (final Exception e) {
      return false;
    }
  }

  public String extractUsername(final String token) {
    final Claims claims = Jwts.parser()
                              .verifyWith(getKey())
                              .build()
                              .parseSignedClaims(token)
                              .getPayload();

    return claims.getSubject();
  }

  public String generateToken(final String username) {

    final long now = System.currentTimeMillis();

    return Jwts.builder()
               .subject(username)
               .issuedAt(new Date(now))
               .expiration(new Date(now + jwtExpiration))
               .signWith(getKey())
               .compact();
  }
}

