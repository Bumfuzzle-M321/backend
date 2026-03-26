package ch.bumfuzzle.websocket;

import ch.bumfuzzle.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtDecoder jwtDecoder;
    private final UserService userService;

    @Override
    public boolean beforeHandshake(
            final ServerHttpRequest request,
            final ServerHttpResponse response,
            final WebSocketHandler wsHandler,
            final Map<String, Object> attributes
    ) {
        if (request instanceof final ServletServerHttpRequest servletRequest) {
            String token = UriComponentsBuilder.fromUri(request.getURI())
                    .build()
                    .getQueryParams()
                    .getFirst("token");

            if (token == null) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }

            try {
                Jwt jwt = jwtDecoder.decode(token);
                attributes.put("sub", jwt.getSubject());
                userService.createUserIfNotExists(jwt);
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