package ch.bumfuzzle.websocket;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

  private final JwtDecoder jwtDecoder;

  public JwtHandshakeInterceptor(final JwtDecoder jwtDecoder) {
    this.jwtDecoder = jwtDecoder;
  }

  @Override
  public boolean beforeHandshake(
      final ServerHttpRequest request,
      final ServerHttpResponse response,
      final WebSocketHandler wsHandler,
      final Map<String, Object> attributes
  ) {
    if (request instanceof final ServletServerHttpRequest servletRequest) {

      final String token = servletRequest
          .getServletRequest()
          .getParameter("token");

      if (token == null) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return false;
      }

      try {
        jwtDecoder.decode(token);
      } catch (final JwtException e) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return false;
      }
    }

    return true;
  }

  @Override
  public void afterHandshake(
      final ServerHttpRequest request,
      final ServerHttpResponse response,
      final WebSocketHandler wsHandler,
      final Exception exception
  ) {
  }
}
