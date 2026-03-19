package ch.bumfuzzle.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  private final KafkaWebsocketHandler handler;
  private final JwtHandshakeInterceptor jwtInterceptor;

  public WebSocketConfig(
      final KafkaWebsocketHandler handler,
      final JwtHandshakeInterceptor jwtInterceptor
  ) {
    this.handler = handler;
    this.jwtInterceptor = jwtInterceptor;
  }

  @Override
  public void registerWebSocketHandlers(final WebSocketHandlerRegistry registry) {
    registry.addHandler(handler, "/websocket")
            .addInterceptors(jwtInterceptor)
            .setAllowedOrigins("*")
            .setAllowedOrigins("http://localhost:5173");
  }
}
