package Needle;

import java.util.ArrayList;
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

    private Lexer.Token consume(Lexer.TokenType type, String message) throws NeedleException {
        if (check(type)) {
            return advance();
        }

        throw error(peek(), message);
    }

    private boolean check(Lexer.TokenType type) {
        return peek().type == type;
    }

    private boolean isAtEnd() {
        return peek().type == Lexer.TokenType.EOF;
    }

    private NeedleException error(Lexer.Token token, String message) {
        return new NeedleException(
                "Parser error at token '" + token.lexeme + "': " + message
        );
    }

    private static class ParseError extends RuntimeException {
        public ParseError(String message) {
            super(message);
        }
    }

    public AstNode parse() throws NeedleException {
        if (check(Lexer.TokenType.EOF)) {
            return new AstNode.EmptyExpr();
        }

        AstNode expr = parseAlternation();
        consume(Lexer.TokenType.EOF, "Expected end of regex.");
        return expr;
    }

    private AstNode parseAlternation() throws NeedleException {
        List<AstNode> concatenationList = new ArrayList<>();
        concatenationList.add(parseConcatenation());
        while (match(Lexer.TokenType.PIPE)) {
            concatenationList.add(parseConcatenation());
        }
        return new AstNode.AlternationExpr(concatenationList);
    }

    private AstNode parseConcatenation() throws NeedleException {
        List<AstNode> astNodes = new ArrayList<>();
        astNodes.add(parseRepetition());
        while (isAtom()) {
            astNodes.add(parseRepetition());
        }

        return new AstNode.ConcatExpr(astNodes);
    }

    private AstNode parseRepetition() throws NeedleException {
        AstNode expr = parseAtom();

        while (isQuantifier()) {
            expr = parseQuantifier(expr);
        }

        return expr;
    }

    private AstNode parseQuantifier(AstNode child) throws NeedleException{
        if (match(Lexer.TokenType.ASTERISK)) {
            return new AstNode.StarExpr(child);
        }

        if (match(Lexer.TokenType.PLUS)) {
            return new AstNode.PlusExpr(child);
        }

        if (match(Lexer.TokenType.QUESTION)) {
            return new AstNode.OptionalExpr(child);
        }

        if (match(Lexer.TokenType.LEFT_CURLY_BRACE)) {
            int min = parseNumber();

            Integer max;

            if (match(Lexer.TokenType.RIGHT_CURLY_BRACE)) {
                max = min;              // {3}
            } else {
                consume(Lexer.TokenType.COMMA, "Expected ',' or '}' in repeat quantifier.");

                if (check(Lexer.TokenType.RIGHT_CURLY_BRACE)) {
                    max = null;         // {3,}
                } else {
                    max = parseNumber(); // {3,5}

                    if (max < min) {
                        throw error(previous(), "Repeat max cannot be smaller than min.");
                    }
                }

                consume(Lexer.TokenType.RIGHT_CURLY_BRACE, "Expected '}' after repeat quantifier.");
            }

            return new AstNode.RepeatExpr(child, min, max);
        }

        throw error(peek(), "Expected quantifier.");
    }

    private int parseNumber() throws NeedleException {
        StringBuilder builder = new StringBuilder();
        while (!isAtEnd()) {
            String rawLexeme = peek().getRawLexeme();

            if (rawLexeme == null || rawLexeme.length() != 1) {
                break;
            }

            char c = rawLexeme.charAt(0);

            if (!Character.isDigit(c)) {
                break;
            }

            builder.append(c);
            advance();
        }

        if (builder.isEmpty()) {
            throw error(peek(), "Expected number.");
        }

        try {
            return Integer.parseInt(builder.toString());
        } catch (NumberFormatException e) {
            throw error(previous(), "Invalid number.");
        }
    }

    private AstNode parseAtom() throws NeedleException {
        if (match(Lexer.TokenType.TEXT)) {
            return new AstNode.CharExpr(previous());
        }

        if (match(Lexer.TokenType.DOT)) {
            return new AstNode.DotExpr(previous());
        }

        if (match(Lexer.TokenType.LEFT_PAREN)) {
            AstNode expression = parseAlternation();

            consume(Lexer.TokenType.RIGHT_PAREN, "Expected ')' after group.");

            return new AstNode.GroupExpr(expression);
        }

        if (check(Lexer.TokenType.LEFT_BRACKET)) {
            return parseCharClass();
        }

        throw error(peek(), "Expected atom.");
    }

    private AstNode parseCharClass() throws NeedleException {
        consume(Lexer.TokenType.LEFT_BRACKET, "Expected '['.");

        boolean isNegated = match(Lexer.TokenType.CARET);

        List<AstNode.BracketExpr.BracketItem> items = new ArrayList<>();

        while (!check(Lexer.TokenType.RIGHT_BRACKET) && !isAtEnd()) {
            char start = parseCharClassChar();

            if (match(Lexer.TokenType.DASH) && !check(Lexer.TokenType.RIGHT_BRACKET)) {
                char end = parseCharClassChar();

                if (start > end) {
                    throw error(previous(), "Invalid character range.");
                }

                items.add(new AstNode.BracketExpr.BracketRange(start, end));
            } else {
                items.add(new AstNode.BracketExpr.BracketChar(start));
            }
        }

        consume(Lexer.TokenType.RIGHT_BRACKET, "Expected ']' after character class.");

        if (items.isEmpty()) {
            throw error(previous(), "Empty character class is not allowed.");
        }

        return new AstNode.BracketExpr(items, isNegated);
    }

    private char parseCharClassChar() throws NeedleException {

        if (check(Lexer.TokenType.RIGHT_BRACKET)) {
            throw error(peek(), "Unexpected ']' inside character class.");
        }

        Lexer.Token token = advance();

        if (token.lexeme == null || token.lexeme.isEmpty()) {
            throw error(token, "Expected character inside character class.");
        }

        return token.lexeme.getFirst().ch;
    }

    private boolean isAtom() {
        return check(Lexer.TokenType.TEXT)
                || check(Lexer.TokenType.DOT)
                || check(Lexer.TokenType.LEFT_BRACKET)
                || check(Lexer.TokenType.LEFT_PAREN);
    }

    private boolean isQuantifier() {
        return check(Lexer.TokenType.ASTERISK)
                || check(Lexer.TokenType.PLUS)
                || check(Lexer.TokenType.QUESTION)
                || check(Lexer.TokenType.LEFT_CURLY_BRACE);
    }

}
