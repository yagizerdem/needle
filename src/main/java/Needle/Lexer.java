package Needle;

import java.util.ArrayList;
import java.util.List;

public class Lexer {

    private final List<Preprocessor.Pchar> preprocessed;

    public Lexer(List<Preprocessor.Pchar> preprocessed) {
        this.preprocessed = preprocessed;
    }

    private int cursor = 0;

    private int start = 0;


    private boolean isAtEnd() {
        return cursor >= preprocessed.size();
    }

    private Preprocessor.Pchar peek() {
        if (isAtEnd()) {
            return null;
        }

        return preprocessed.get(cursor);
    }

    private Preprocessor.Pchar peekNext() {
        if (cursor + 1 >= preprocessed.size()) {
            return null;
        }

        return preprocessed.get(cursor + 1);
    }

    private Preprocessor.Pchar advance() {
        if (isAtEnd()) {
            return null;
        }

        return preprocessed.get(cursor++);
    }

    private boolean match(char expected) {
        if (isAtEnd()) {
            return false;
        }

        if (preprocessed.get(cursor).ch != expected) {
            return false;
        }

        cursor++;
        return true;
    }

    private List<Token> tokenStream;



    public List<Token> scan() {
        this.tokenStream = new ArrayList<>();
        this.cursor = 0;
        this.start =0;
        while (peek()!= null) {
            this.start = this.cursor;
            step();
        }

        tokenStream.add(new Token(null, TokenType.EOF));

        return tokenStream;
    }

    private void step() {
        Preprocessor.Pchar pchar = advance();
        if(pchar == null) {
            return;
        }

        switch (pchar.ch) {
            case '.' -> addToken(pchar.isEscaped ? TokenType.TEXT : TokenType.DOT);
            case '|' -> addToken(pchar.isEscaped ? TokenType.TEXT : TokenType.PIPE);

            case '*' -> addToken(pchar.isEscaped ? TokenType.TEXT : TokenType.ASTERISK);
            case '+' -> addToken(pchar.isEscaped ? TokenType.TEXT : TokenType.PLUS);
            case '?' -> addToken(pchar.isEscaped ? TokenType.TEXT : TokenType.QUESTION);

            case '(' -> addToken(pchar.isEscaped ? TokenType.TEXT : TokenType.LEFT_PAREN);
            case ')' -> addToken(pchar.isEscaped ? TokenType.TEXT : TokenType.RIGHT_PAREN);

            case '[' -> addToken(pchar.isEscaped ? TokenType.TEXT : TokenType.LEFT_BRACKET);
            case ']' -> addToken(pchar.isEscaped ? TokenType.TEXT : TokenType.RIGHT_BRACKET);

            case '{' -> addToken(pchar.isEscaped ? TokenType.TEXT : TokenType.LEFT_CURLY_BRACE);
            case '}' -> addToken(pchar.isEscaped ? TokenType.TEXT : TokenType.RIGHT_CURLY_BRACE);

            case ',' -> addToken(pchar.isEscaped ? TokenType.TEXT : TokenType.COMMA);
            case '-' -> addToken(pchar.isEscaped ? TokenType.TEXT : TokenType.DASH);

            case '^' -> addToken(pchar.isEscaped ? TokenType.TEXT : TokenType.CARET);
            case '$' -> addToken(pchar.isEscaped ? TokenType.TEXT : TokenType.DOLLAR);

            default -> addToken(TokenType.TEXT);

        }

    }

    public void addToken(TokenType type) {
        Token token = new Token();
        token.lexeme = this.preprocessed.subList(start, cursor);
        token.type = type;
        this.tokenStream.add(token);
    }

    public static final class Token {
        public List<Preprocessor.Pchar> lexeme;
        public TokenType type;

        public Token() {}

        public Token(List<Preprocessor.Pchar> lexeme, TokenType type) {
            this.lexeme = lexeme;
            this.type = type;
        }

        public String getRawLexeme() {
            if(lexeme == null) return null;
            StringBuilder builder = new StringBuilder();
            for(Preprocessor.Pchar pc : lexeme) {
                builder.append(pc.ch);
            }
            return builder.toString();
        }
    }

    public enum TokenType {

        // Literals
        TEXT,

        // Operators
        DOT,                // .
        PIPE,               // |
        ASTERISK,           // *
        PLUS,               // +
        QUESTION,           // ?

        // Grouping
        LEFT_PAREN,         // (
        RIGHT_PAREN,        // )

        // Character Classes
        LEFT_BRACKET,       // [
        RIGHT_BRACKET,      // ]
        CARET,              // ^
        DASH,               // -

        // Quantifiers
        LEFT_CURLY_BRACE,         // {
        RIGHT_CURLY_BRACE,        // }
        COMMA,              // ,

        DOLLAR,         // $

        // End of input
        EOF
    }
}
