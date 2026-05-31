import Needle.Lexer;
import Needle.Preprocessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class LexerTest {

    @Test
    public void scan() {
        String regex = "(a|b)*c?";
        Preprocessor preprocessor = new Preprocessor();
        List<Preprocessor.Pchar> preprocessed =  preprocessor.process(regex);
        Lexer lexer = new Lexer(preprocessed);
        List<Lexer.Token> tokens = lexer.scan();

        Assertions.assertEquals(9, tokens.size());

        Assertions.assertEquals(Lexer.TokenType.LEFT_PAREN, tokens.get(0).type);
        Assertions.assertEquals("(", tokens.get(0).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.TEXT, tokens.get(1).type);
        Assertions.assertEquals("a", tokens.get(1).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.PIPE, tokens.get(2).type);
        Assertions.assertEquals("|", tokens.get(2).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.TEXT, tokens.get(3).type);
        Assertions.assertEquals("b", tokens.get(3).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.RIGHT_PAREN, tokens.get(4).type);
        Assertions.assertEquals(")", tokens.get(4).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.ASTERISK, tokens.get(5).type);
        Assertions.assertEquals("*", tokens.get(5).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.TEXT, tokens.get(6).type);
        Assertions.assertEquals("c", tokens.get(6).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.QUESTION, tokens.get(7).type);
        Assertions.assertEquals("?", tokens.get(7).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.EOF, tokens.get(8).type);

    }

    private List<Lexer.Token> tokenize(String regex) {
        Preprocessor preprocessor = new Preprocessor();
        List<Preprocessor.Pchar> preprocessed = preprocessor.process(regex);
        Lexer lexer = new Lexer(preprocessed);
        return lexer.scan();
    }

    @Test
    public void singleLiteral() {
        List<Lexer.Token> tokens = tokenize("a");

        Assertions.assertEquals(2, tokens.size());

        Assertions.assertEquals(Lexer.TokenType.TEXT, tokens.get(0).type);
        Assertions.assertEquals("a", tokens.get(0).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.EOF, tokens.get(1).type);
    }

    @Test
    public void multipleConsecutiveLiterals() {
        List<Lexer.Token> tokens = tokenize("abc");

        Assertions.assertEquals(4, tokens.size());

        Assertions.assertEquals(Lexer.TokenType.TEXT, tokens.get(0).type);
        Assertions.assertEquals("a", tokens.get(0).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.TEXT, tokens.get(1).type);
        Assertions.assertEquals("b", tokens.get(1).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.TEXT, tokens.get(2).type);
        Assertions.assertEquals("c", tokens.get(2).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.EOF, tokens.get(3).type);
    }


    @Test
    public void alternation() {
        List<Lexer.Token> tokens = tokenize("a|b");

        Assertions.assertEquals(4, tokens.size());

        Assertions.assertEquals(Lexer.TokenType.TEXT, tokens.get(0).type);
        Assertions.assertEquals("a", tokens.get(0).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.PIPE, tokens.get(1).type);
        Assertions.assertEquals("|", tokens.get(1).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.TEXT, tokens.get(2).type);
        Assertions.assertEquals("b", tokens.get(2).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.EOF, tokens.get(3).type);
    }

    @Test
    public void multipleAlternations() {
        List<Lexer.Token> tokens = tokenize("a|b|c");

        Assertions.assertEquals(6, tokens.size());

        Assertions.assertEquals(Lexer.TokenType.TEXT,  tokens.get(0).type);
        Assertions.assertEquals(Lexer.TokenType.PIPE,  tokens.get(1).type);
        Assertions.assertEquals(Lexer.TokenType.TEXT,  tokens.get(2).type);
        Assertions.assertEquals(Lexer.TokenType.PIPE,  tokens.get(3).type);
        Assertions.assertEquals(Lexer.TokenType.TEXT,  tokens.get(4).type);
        Assertions.assertEquals(Lexer.TokenType.EOF,   tokens.get(5).type);
    }

    @Test
    public void asteriskQuantifier() {
        List<Lexer.Token> tokens = tokenize("a*");

        Assertions.assertEquals(3, tokens.size());

        Assertions.assertEquals(Lexer.TokenType.TEXT,     tokens.get(0).type);
        Assertions.assertEquals("a",                      tokens.get(0).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.ASTERISK, tokens.get(1).type);
        Assertions.assertEquals("*",                      tokens.get(1).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.EOF,      tokens.get(2).type);
    }

    @Test
    public void plusQuantifier() {
        List<Lexer.Token> tokens = tokenize("a+");

        Assertions.assertEquals(3, tokens.size());

        Assertions.assertEquals(Lexer.TokenType.TEXT, tokens.get(0).type);
        Assertions.assertEquals("a",                  tokens.get(0).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.PLUS, tokens.get(1).type);
        Assertions.assertEquals("+",                  tokens.get(1).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.EOF,  tokens.get(2).type);
    }

    @Test
    public void questionQuantifier() {
        List<Lexer.Token> tokens = tokenize("a?");

        Assertions.assertEquals(3, tokens.size());

        Assertions.assertEquals(Lexer.TokenType.TEXT,     tokens.get(0).type);
        Assertions.assertEquals("a",                      tokens.get(0).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.QUESTION, tokens.get(1).type);
        Assertions.assertEquals("?",                      tokens.get(1).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.EOF,      tokens.get(2).type);
    }

    @Test
    public void exactQuantifier() {
        List<Lexer.Token> tokens = tokenize("a{3}");

        Assertions.assertEquals(5, tokens.size());

        Assertions.assertEquals(Lexer.TokenType.TEXT,              tokens.get(0).type);
        Assertions.assertEquals("a",                               tokens.get(0).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.LEFT_CURLY_BRACE,  tokens.get(1).type);
        Assertions.assertEquals("{",                               tokens.get(1).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.TEXT,              tokens.get(2).type);
        Assertions.assertEquals("3",                               tokens.get(2).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.RIGHT_CURLY_BRACE, tokens.get(3).type);
        Assertions.assertEquals("}",                               tokens.get(3).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.EOF,               tokens.get(4).type);
    }

    @Test
    public void atLeastQuantifier() {
        List<Lexer.Token> tokens = tokenize("a{2,}");

        Assertions.assertEquals(6, tokens.size());

        Assertions.assertEquals(Lexer.TokenType.TEXT,              tokens.get(0).type);
        Assertions.assertEquals(Lexer.TokenType.LEFT_CURLY_BRACE,  tokens.get(1).type);

        Assertions.assertEquals(Lexer.TokenType.TEXT,              tokens.get(2).type);
        Assertions.assertEquals("2",                               tokens.get(2).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.COMMA,             tokens.get(3).type);
        Assertions.assertEquals(",",                               tokens.get(3).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.RIGHT_CURLY_BRACE, tokens.get(4).type);
        Assertions.assertEquals(Lexer.TokenType.EOF,               tokens.get(5).type);
    }

    @Test
    public void rangeQuantifier() {
        List<Lexer.Token> tokens = tokenize("a{2,5}");

        Assertions.assertEquals(7, tokens.size());

        Assertions.assertEquals(Lexer.TokenType.TEXT,              tokens.get(0).type);
        Assertions.assertEquals(Lexer.TokenType.LEFT_CURLY_BRACE,  tokens.get(1).type);

        Assertions.assertEquals(Lexer.TokenType.TEXT,              tokens.get(2).type);
        Assertions.assertEquals("2",                               tokens.get(2).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.COMMA,             tokens.get(3).type);

        Assertions.assertEquals(Lexer.TokenType.TEXT,              tokens.get(4).type);
        Assertions.assertEquals("5",                               tokens.get(4).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.RIGHT_CURLY_BRACE, tokens.get(5).type);
        Assertions.assertEquals(Lexer.TokenType.EOF,               tokens.get(6).type);
    }

    @Test
    public void simpleGroup() {
        List<Lexer.Token> tokens = tokenize("(a)");

        Assertions.assertEquals(4, tokens.size());

        Assertions.assertEquals(Lexer.TokenType.LEFT_PAREN,  tokens.get(0).type);
        Assertions.assertEquals("(",                         tokens.get(0).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.TEXT,        tokens.get(1).type);
        Assertions.assertEquals("a",                         tokens.get(1).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.RIGHT_PAREN, tokens.get(2).type);
        Assertions.assertEquals(")",                         tokens.get(2).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.EOF,         tokens.get(3).type);
    }

    @Test
    public void groupWithAlternation() {
        List<Lexer.Token> tokens = tokenize("(a|b)");

        Assertions.assertEquals(6, tokens.size());

        Assertions.assertEquals(Lexer.TokenType.LEFT_PAREN,  tokens.get(0).type);
        Assertions.assertEquals(Lexer.TokenType.TEXT,        tokens.get(1).type);
        Assertions.assertEquals("a",                         tokens.get(1).getRawLexeme());
        Assertions.assertEquals(Lexer.TokenType.PIPE,        tokens.get(2).type);
        Assertions.assertEquals(Lexer.TokenType.TEXT,        tokens.get(3).type);
        Assertions.assertEquals("b",                         tokens.get(3).getRawLexeme());
        Assertions.assertEquals(Lexer.TokenType.RIGHT_PAREN, tokens.get(4).type);
        Assertions.assertEquals(Lexer.TokenType.EOF,         tokens.get(5).type);
    }

    @Test
    public void nestedGroups() {
        List<Lexer.Token> tokens = tokenize("((a))");

        Assertions.assertEquals(6, tokens.size());

        Assertions.assertEquals(Lexer.TokenType.LEFT_PAREN,  tokens.get(0).type);
        Assertions.assertEquals(Lexer.TokenType.LEFT_PAREN,  tokens.get(1).type);
        Assertions.assertEquals(Lexer.TokenType.TEXT,        tokens.get(2).type);
        Assertions.assertEquals(Lexer.TokenType.RIGHT_PAREN, tokens.get(3).type);
        Assertions.assertEquals(Lexer.TokenType.RIGHT_PAREN, tokens.get(4).type);
        Assertions.assertEquals(Lexer.TokenType.EOF,         tokens.get(5).type);
    }

    @Test
    public void simpleCharClass() {
        List<Lexer.Token> tokens = tokenize("[abc]");

        Assertions.assertEquals(6, tokens.size());

        Assertions.assertEquals(Lexer.TokenType.LEFT_BRACKET,  tokens.get(0).type);
        Assertions.assertEquals("[",                           tokens.get(0).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.TEXT,          tokens.get(1).type);
        Assertions.assertEquals("a",                           tokens.get(1).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.TEXT,          tokens.get(2).type);
        Assertions.assertEquals("b",                           tokens.get(2).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.TEXT,          tokens.get(3).type);
        Assertions.assertEquals("c",                           tokens.get(3).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.RIGHT_BRACKET, tokens.get(4).type);
        Assertions.assertEquals("]",                           tokens.get(4).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.EOF,           tokens.get(5).type);
    }

    @Test
    public void negatedCharClass() {
        List<Lexer.Token> tokens = tokenize("[^abc]");

        Assertions.assertEquals(7, tokens.size());

        Assertions.assertEquals(Lexer.TokenType.LEFT_BRACKET,  tokens.get(0).type);

        Assertions.assertEquals(Lexer.TokenType.CARET,         tokens.get(1).type);
        Assertions.assertEquals("^",                           tokens.get(1).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.TEXT,          tokens.get(2).type);
        Assertions.assertEquals("a",                           tokens.get(2).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.TEXT,          tokens.get(3).type);
        Assertions.assertEquals(Lexer.TokenType.TEXT,          tokens.get(4).type);
        Assertions.assertEquals(Lexer.TokenType.RIGHT_BRACKET, tokens.get(5).type);
        Assertions.assertEquals(Lexer.TokenType.EOF,           tokens.get(6).type);
    }

    @Test
    public void charClassRange() {
        List<Lexer.Token> tokens = tokenize("[a-z]");

        Assertions.assertEquals(6, tokens.size());

        Assertions.assertEquals(Lexer.TokenType.LEFT_BRACKET,  tokens.get(0).type);

        Assertions.assertEquals(Lexer.TokenType.TEXT,          tokens.get(1).type);
        Assertions.assertEquals("a",                           tokens.get(1).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.DASH,          tokens.get(2).type);
        Assertions.assertEquals("-",                           tokens.get(2).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.TEXT,          tokens.get(3).type);
        Assertions.assertEquals("z",                           tokens.get(3).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.RIGHT_BRACKET, tokens.get(4).type);
        Assertions.assertEquals(Lexer.TokenType.EOF,           tokens.get(5).type);
    }

    @Test
    public void negatedCharClassWithRange() {
        List<Lexer.Token> tokens = tokenize("[^a-z]");

        Assertions.assertEquals(7, tokens.size());

        Assertions.assertEquals(Lexer.TokenType.LEFT_BRACKET,  tokens.get(0).type);
        Assertions.assertEquals(Lexer.TokenType.CARET,         tokens.get(1).type);
        Assertions.assertEquals(Lexer.TokenType.TEXT,          tokens.get(2).type);
        Assertions.assertEquals("a",                           tokens.get(2).getRawLexeme());
        Assertions.assertEquals(Lexer.TokenType.DASH,          tokens.get(3).type);
        Assertions.assertEquals(Lexer.TokenType.TEXT,          tokens.get(4).type);
        Assertions.assertEquals("z",                           tokens.get(4).getRawLexeme());
        Assertions.assertEquals(Lexer.TokenType.RIGHT_BRACKET, tokens.get(5).type);
        Assertions.assertEquals(Lexer.TokenType.EOF,           tokens.get(6).type);
    }


    @Test
    public void dotAtom() {
        List<Lexer.Token> tokens = tokenize(".");

        Assertions.assertEquals(2, tokens.size());

        Assertions.assertEquals(Lexer.TokenType.DOT, tokens.get(0).type);
        Assertions.assertEquals(".",                 tokens.get(0).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.EOF, tokens.get(1).type);
    }

    @Test
    public void dotWithQuantifier() {
        List<Lexer.Token> tokens = tokenize(".*");

        Assertions.assertEquals(3, tokens.size());

        Assertions.assertEquals(Lexer.TokenType.DOT,      tokens.get(0).type);
        Assertions.assertEquals(Lexer.TokenType.ASTERISK,  tokens.get(1).type);
        Assertions.assertEquals(Lexer.TokenType.EOF,       tokens.get(2).type);
    }


    @Test
    public void escapeSequence() {
        List<Lexer.Token> tokens = tokenize("\\d");

        Assertions.assertEquals(Lexer.TokenType.EOF, tokens.get(tokens.size() - 1).type);

        StringBuilder raw = new StringBuilder();
        for (int i = 0; i < tokens.size() - 1; i++) {
            raw.append(tokens.get(i).getRawLexeme());
        }
        Assertions.assertEquals("d", raw.toString());
    }

    @Test
    public void escapedSpecialCharacter() {
        List<Lexer.Token> tokens = tokenize("\\(");

        Assertions.assertEquals(Lexer.TokenType.EOF, tokens.get(tokens.size() - 1).type);

        StringBuilder raw = new StringBuilder();
        for (int i = 0; i < tokens.size() - 1; i++) {
            raw.append(tokens.get(i).getRawLexeme());
        }
        Assertions.assertEquals("(", raw.toString());
    }

    @Test
    public void originalExampleFromSpec() {
        List<Lexer.Token> tokens = tokenize("(a|b)*c?");

        Assertions.assertEquals(9, tokens.size());

        Assertions.assertEquals(Lexer.TokenType.LEFT_PAREN,  tokens.get(0).type);
        Assertions.assertEquals("(",                         tokens.get(0).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.TEXT,        tokens.get(1).type);
        Assertions.assertEquals("a",                         tokens.get(1).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.PIPE,        tokens.get(2).type);
        Assertions.assertEquals("|",                         tokens.get(2).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.TEXT,        tokens.get(3).type);
        Assertions.assertEquals("b",                         tokens.get(3).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.RIGHT_PAREN, tokens.get(4).type);
        Assertions.assertEquals(")",                         tokens.get(4).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.ASTERISK,    tokens.get(5).type);
        Assertions.assertEquals("*",                         tokens.get(5).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.TEXT,        tokens.get(6).type);
        Assertions.assertEquals("c",                         tokens.get(6).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.QUESTION,    tokens.get(7).type);
        Assertions.assertEquals("?",                         tokens.get(7).getRawLexeme());

        Assertions.assertEquals(Lexer.TokenType.EOF,         tokens.get(8).type);
    }

    @Test
    public void complexRegexWithCharClassAndQuantifier() {
        List<Lexer.Token> tokens = tokenize("[a-z]+|[0-9]*");

        Assertions.assertEquals(14, tokens.size());

        Assertions.assertEquals(Lexer.TokenType.LEFT_BRACKET,  tokens.get(0).type);
        Assertions.assertEquals(Lexer.TokenType.TEXT,          tokens.get(1).type);
        Assertions.assertEquals("a",                           tokens.get(1).getRawLexeme());
        Assertions.assertEquals(Lexer.TokenType.DASH,          tokens.get(2).type);
        Assertions.assertEquals(Lexer.TokenType.TEXT,          tokens.get(3).type);
        Assertions.assertEquals("z",                           tokens.get(3).getRawLexeme());
        Assertions.assertEquals(Lexer.TokenType.RIGHT_BRACKET, tokens.get(4).type);
        Assertions.assertEquals(Lexer.TokenType.PLUS,          tokens.get(5).type);

        Assertions.assertEquals(Lexer.TokenType.PIPE,          tokens.get(6).type);

        Assertions.assertEquals(Lexer.TokenType.LEFT_BRACKET,  tokens.get(7).type);
        Assertions.assertEquals(Lexer.TokenType.TEXT,          tokens.get(8).type);
        Assertions.assertEquals("0",                           tokens.get(8).getRawLexeme());
        Assertions.assertEquals(Lexer.TokenType.DASH,          tokens.get(9).type);
        Assertions.assertEquals(Lexer.TokenType.TEXT,          tokens.get(10).type);
        Assertions.assertEquals("9",                           tokens.get(10).getRawLexeme());
        Assertions.assertEquals(Lexer.TokenType.RIGHT_BRACKET, tokens.get(11).type);
        Assertions.assertEquals(Lexer.TokenType.ASTERISK,      tokens.get(12).type);

    }

    @Test
    public void groupWithCurlyQuantifier() {
        List<Lexer.Token> tokens = tokenize("(ab){3,5}");

        Assertions.assertEquals(10, tokens.size());

        Assertions.assertEquals(Lexer.TokenType.LEFT_PAREN,        tokens.get(0).type);
        Assertions.assertEquals(Lexer.TokenType.TEXT,              tokens.get(1).type);
        Assertions.assertEquals("a",                               tokens.get(1).getRawLexeme());
        Assertions.assertEquals(Lexer.TokenType.TEXT,              tokens.get(2).type);
        Assertions.assertEquals("b",                               tokens.get(2).getRawLexeme());
        Assertions.assertEquals(Lexer.TokenType.RIGHT_PAREN,       tokens.get(3).type);
        Assertions.assertEquals(Lexer.TokenType.LEFT_CURLY_BRACE,  tokens.get(4).type);
        Assertions.assertEquals(Lexer.TokenType.TEXT,              tokens.get(5).type);
        Assertions.assertEquals("3",                               tokens.get(5).getRawLexeme());
        Assertions.assertEquals(Lexer.TokenType.COMMA,             tokens.get(6).type);
        Assertions.assertEquals(Lexer.TokenType.TEXT,              tokens.get(7).type);
        Assertions.assertEquals("5",                               tokens.get(7).getRawLexeme());
        Assertions.assertEquals(Lexer.TokenType.RIGHT_CURLY_BRACE, tokens.get(8).type);
        Assertions.assertEquals(Lexer.TokenType.EOF,               tokens.get(9).type);
    }

    @Test
    public void emptyRegex() {
        List<Lexer.Token> tokens = tokenize("");

        Assertions.assertEquals(1, tokens.size());
        Assertions.assertEquals(Lexer.TokenType.EOF, tokens.get(0).type);
    }

    @Test
    public void dotInCharClass() {
        List<Lexer.Token> tokens = tokenize("[a.]");

        Assertions.assertEquals(5, tokens.size());

        Assertions.assertEquals(Lexer.TokenType.LEFT_BRACKET,  tokens.get(0).type);
        Assertions.assertEquals(Lexer.TokenType.TEXT,          tokens.get(1).type);
        Assertions.assertEquals("a",                           tokens.get(1).getRawLexeme());
        // "." inside class → TEXT or DOT depending on impl; we check lexeme only
        Assertions.assertEquals(".",                           tokens.get(2).getRawLexeme());
        Assertions.assertEquals(Lexer.TokenType.RIGHT_BRACKET, tokens.get(3).type);
        Assertions.assertEquals(Lexer.TokenType.EOF,           tokens.get(4).type);
    }

}
