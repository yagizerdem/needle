import Needle.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AstPrinterTest {

    private String parse(String regex) {
        try {
            Preprocessor preprocessor = new Preprocessor();
            List<Preprocessor.Pchar> preprocessed = preprocessor.process(regex);
            Lexer lexer = new Lexer(preprocessed);
            List<Lexer.Token> tokens = lexer.scan();
            Parser parser = new Parser(tokens);
            AstNode root = parser.parse();
            AstPrinter printer = new AstPrinter();
            return root.accept(printer);
        }catch (NeedleException ex) {
            Assertions.fail(ex.getMessage());
        }
        return null;
    }

    @Test
    public void singleLiteral() {
        Assertions.assertEquals("a", parse("a"));
    }

    @Test
    public void multipleLiterals() {
        Assertions.assertEquals("abc", parse("abc"));
    }

    @Test
    public void dot() {
        Assertions.assertEquals(".", parse("."));
    }

    @Test
    public void dotInConcatenation() {
        Assertions.assertEquals("a.b", parse("a.b"));
    }

    @Test
    public void starQuantifier() {
        Assertions.assertEquals("a*", parse("a*"));
    }

    @Test
    public void plusQuantifier() {
        Assertions.assertEquals("a+", parse("a+"));
    }

    @Test
    public void questionMarkQuantifier() {
        Assertions.assertEquals("a?", parse("a?"));
    }

    @Test
    public void dotStar() {
        Assertions.assertEquals(".*", parse(".*"));
    }

    @Test
    public void multipleQuantifiersInConcatenation() {
        Assertions.assertEquals("a*b+c?", parse("a*b+c?"));
    }

    @Test
    public void exactQuantifier() {
        Assertions.assertEquals("a{3}", parse("a{3}"));
    }

    @Test
    public void atLeastQuantifier() {
        Assertions.assertEquals("a{2,}", parse("a{2,}"));
    }

    @Test
    public void rangeQuantifier() {
        Assertions.assertEquals("a{2,5}", parse("a{2,5}"));
    }

    @Test
    public void rangeQuantifierOnGroup() {
        Assertions.assertEquals("(ab){1,3}", parse("(ab){1,3}"));
    }

    @Test
    public void simpleAlternation() {
        Assertions.assertEquals("a|b", parse("a|b"));
    }

    @Test
    public void multipleAlternation() {
        Assertions.assertEquals("a|b|c", parse("a|b|c"));
    }

    @Test
    public void alternationWithConcatenation() {
        Assertions.assertEquals("ab|cd", parse("ab|cd"));
    }

    @Test
    public void alternationWithQuantifier() {
        Assertions.assertEquals("a*|b+", parse("a*|b+"));
    }

    @Test
    public void simpleGroup() {
        Assertions.assertEquals("(ab)", parse("(ab)"));
    }

    @Test
    public void groupWithStar() {
        Assertions.assertEquals("(ab)*", parse("(ab)*"));
    }

    @Test
    public void groupWithAlternation() {
        Assertions.assertEquals("(a|b)", parse("(a|b)"));
    }

    @Test
    public void nestedGroups() {
        Assertions.assertEquals("((a|b)*c)", parse("((a|b)*c)"));
    }

    @Test
    public void groupFollowedByLiteral() {
        Assertions.assertEquals("(a|b)*c", parse("(a|b)*c"));
    }

    @Test
    public void multipleGroups() {
        Assertions.assertEquals("(ab)(cd)", parse("(ab)(cd)"));
    }

    @Test
    public void simpleCharClass() {
        Assertions.assertEquals("[abc]", parse("[abc]"));
    }

    @Test
    public void negatedCharClass() {
        Assertions.assertEquals("[^abc]", parse("[^abc]"));
    }

    @Test
    public void charClassWithRange() {
        Assertions.assertEquals("[a-z]", parse("[a-z]"));
    }

    @Test
    public void charClassWithMultipleRanges() {
        Assertions.assertEquals("[a-zA-Z]", parse("[a-zA-Z]"));
    }

    @Test
    public void charClassWithRangeAndLiteral() {
        Assertions.assertEquals("[a-z0]", parse("[a-z0]"));
    }

    @Test
    public void negatedCharClassWithRange() {
        Assertions.assertEquals("[^0-9]", parse("[^0-9]"));
    }

    @Test
    public void charClassWithQuantifier() {
        Assertions.assertEquals("[a-z]+", parse("[a-z]+"));
    }

    @Test
    public void charClassWithDigitRange() {
        Assertions.assertEquals("[0-9]", parse("[0-9]"));
    }

    @Test
    public void escapedDot() {
        Assertions.assertEquals(".", parse("\\."));
    }

    @Test
    public void escapedStar() {
        Assertions.assertEquals("*", parse("\\*"));
    }

    @Test
    public void escapedParen() {
        Assertions.assertEquals("(", parse("\\("));
    }

    @Test
    public void escapeInCharClass() {
        Assertions.assertEquals("[d]", parse("[\\d]"));
    }

    @Test
    public void escapedW() {
        Assertions.assertEquals("w+", parse("w+"));
    }

    @Test
    public void emailLike() {
        Assertions.assertEquals("[a-z]+@[a-z]+.[a-z]+", parse("[a-z]+@[a-z]+\\.[a-z]+"));
    }

    @Test
    public void ipSegment() {
        Assertions.assertEquals("[0-9]{1,3}", parse("[0-9]{1,3}"));
    }

    @Test
    public void complexAlternationWithGroups() {
        Assertions.assertEquals("(foo|bar){2,}", parse("(foo|bar){2,}"));
    }

    @Test
    public void nestedAlternationInGroup() {
        Assertions.assertEquals("(a|(b|c))", parse("(a|(b|c))"));
    }

    @Test
    public void originalFromSpec() {
        Assertions.assertEquals("(a|b)*c?", parse("(a|b)*c?"));
    }

    @Test
    public void dotStarAnything() {
        Assertions.assertEquals(".*foo.*", parse(".*foo.*"));
    }

    @Test
    public void wordBoundaryLike() {
        Assertions.assertEquals("w{3,10}", parse("\\w{3,10}"));
    }

    @Test
    public void optionalGroup() {
        Assertions.assertEquals("(abc)?", parse("(abc)?"));
    }

    @Test
    public void complexFullRegex() {
        String regex = "([a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}|\\+?[0-9]{1,3}[\\-. ]?\\(?[0-9]{3}\\)?[\\-. ]?[0-9]{3}[\\-. ]?[0-9]{4})([ \\t]+#[^\\n]*)?";
        Assertions.assertEquals("([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}|+?[0-9]{1,3}[-. ]?(?[0-9]{3})?[-. ]?[0-9]{3}[-. ]?[0-9]{4})([ t]+#[^n]*)?", parse(regex));
    }
}
