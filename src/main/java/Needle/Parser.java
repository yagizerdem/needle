package Needle;

import java.util.List;

public class Parser {
    private final List<Lexer.Token> tokens;
    private int current = 0;

    public Parser(List<Lexer.Token> tokens) {
        this.tokens = tokens;
    }

    private Lexer.Token peek() {
        return tokens.get(current);
    }

    private Lexer.Token peekNext() {
        if (current + 1 >= tokens.size()) {
            return tokens.getLast(); // EOF
        }

        return tokens.get(current + 1);
    }

    private Lexer.Token previous() {
        return tokens.get(current - 1);
    }

    private Lexer.Token advance() {
        if (!isAtEnd()) {
            current++;
        }

        return previous();
    }

    private boolean match(Lexer.TokenType... types) {
        for (Lexer.TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private Lexer.Token consume(Lexer.TokenType type, String message) {
        if (check(type)) {
            return advance();
        }

        throw error(peek(), message);
    }

    private boolean check(Lexer.TokenType type) {
        if (isAtEnd()) {
            return false;
        }

        return peek().type == type;
    }

    private boolean isAtEnd() {
        return peek().type == Lexer.TokenType.EOF;
    }

    private ParseError error(Lexer.Token token, String message) {
        return new ParseError(
                "Parser error at token '" + token.lexeme + "': " + message
        );
    }

    private static class ParseError extends RuntimeException {
        public ParseError(String message) {
            super(message);
        }
    }

    public


}
