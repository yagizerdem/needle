package Needle;

import java.util.ArrayList;
import java.util.List;

public class Lexer {

    private List<Preprocessor.Pchar> preprocessed;

    public Lexer(List<Preprocessor.Pchar> preprocessed) {
        this.preprocessed = preprocessed;
    }

    public int cursor = 0;

    private static final char END = '\0';

    private boolean isAtEnd() {
        return cursor >= preprocessed.size();
    }

    private char peek() {
        if (isAtEnd()) {
            return END;
        }

        return preprocessed.get(cursor).ch;
    }

    private char peekNext() {
        if (cursor + 1 >= preprocessed.size()) {
            return END;
        }

        return preprocessed.get(cursor + 1).ch;
    }

    private char advance() {
        if (isAtEnd()) {
            return END;
        }

        return preprocessed.get(cursor++).ch;
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


    public List<Token> scan() {
        List<Token> tokenStream = new ArrayList<>();


        return tokenStream;
    }


    public class Token {
        public List<Preprocessor.Pchar> lexeme;
        public TokenType type;

        public String getLexeme() {
            StringBuilder builder = new StringBuilder();
            for(Preprocessor.Pchar pc : lexeme) {
                builder.append(pc.c);
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

        ESCAPE,             // \

        NUMBER,

        START_ANCHOR,       // ^
        END_ANCHOR,         // $

        // End of input
        EOF
    }
}
