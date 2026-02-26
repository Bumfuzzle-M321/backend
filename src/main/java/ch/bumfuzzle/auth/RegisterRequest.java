package ch.bumfuzzle.auth;

public record RegisterRequest(
        String username,
        String password
) {
}
