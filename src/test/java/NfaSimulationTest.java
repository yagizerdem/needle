import Needle.Core;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NfaSimulationTest {

    public boolean isExactMatch(String regex, String input){
        try {
            Core core = new Core();
            return core.isExactMatch(core.compile(regex), input);

        }catch (Needle.NeedleException ex) {
            Assertions.fail(ex.getMessage());
        }
        return false;
    }

    @Test
    public void basic() {
        boolean flag = isExactMatch("(a|b)*c+123?",  "abbc12");
        Assertions.assertTrue(flag);
    }

    @Test
    public void emptyRegexMatchesEmptyString() {
        Assertions.assertTrue(isExactMatch("", ""));
    }

    @Test
    public void emptyRegexMatchesBlankString() {
        Assertions.assertTrue(isExactMatch(" ", " "));
    }

    @Test
    public void emptyRegexMatchesBlankString2() {
        Assertions.assertTrue(isExactMatch("\t", "\t"));
    }

    @Test
    public void emptyRegexMatchesBlankString3() {
        Assertions.assertTrue(isExactMatch("\n", "\n"));
    }

    @Test
    public void emptyRegexMatchesBlankStringMixed() {
        Assertions.assertTrue(isExactMatch("\n \t\t \n ", "\n \t\t \n "));
    }


    @Test
    public void emptyRegexDoesNotMatchNonEmpty() {
        Assertions.assertTrue(isExactMatch(".", "a"));
    }

    @Test
    public void starOnEmptyMatchesEmpty() {
        Assertions.assertTrue(isExactMatch("a*", ""));
    }

    @Test
    public void optionalMatchesEmpty() {
        Assertions.assertTrue(isExactMatch("a?", ""));
    }

    @Test
    public void optionalMatchesSingle() {
        Assertions.assertTrue(isExactMatch("a?", "a"));
    }

    @Test
    public void optionalDoesNotMatchTwo() {
        Assertions.assertFalse(isExactMatch("a?", "aa"));
    }

    @Test
    public void singleLiteralMatch() {
        Assertions.assertTrue(isExactMatch("a", "a"));
    }

    @Test
    public void singleLiteralNoMatchWrongChar() {
        Assertions.assertFalse(isExactMatch("a", "b"));
    }

    @Test
    public void singleLiteralNoMatchExtraChar() {
        Assertions.assertFalse(isExactMatch("a", "aa"));
    }

    @Test
    public void singleLiteralNoMatchEmpty() {
        Assertions.assertFalse(isExactMatch("a", ""));
    }

    @Test
    public void starMatchesEmpty() {
        Assertions.assertTrue(isExactMatch("a*", ""));
    }

    @Test
    public void starMatchesOne() {
        Assertions.assertTrue(isExactMatch("a*", "a"));
    }

    @Test
    public void starMatchesMany() {
        Assertions.assertTrue(isExactMatch("a*", "aaaaaaa"));
    }

    @Test
    public void starDoesNotMatchWrongChar() {
        Assertions.assertFalse(isExactMatch("a*", "b"));
    }

    @Test
    public void starDoesNotMatchMixed() {
        Assertions.assertFalse(isExactMatch("a*", "aab"));
    }

    @Test
    public void dotStarMatchesEmpty() {
        Assertions.assertTrue(isExactMatch(".*", ""));
    }

    @Test
    public void dotStarMatchesAnything() {
        Assertions.assertTrue(isExactMatch(".*", "x1!Yz"));
    }

    @Test
    public void plusDoesNotMatchEmpty() {
        Assertions.assertFalse(isExactMatch("a+", ""));
    }

    @Test
    public void plusMatchesOne() {
        Assertions.assertTrue(isExactMatch("a+", "a"));
    }

    @Test
    public void plusMatchesMany() {
        Assertions.assertTrue(isExactMatch("a+", "aaaa"));
    }

    @Test
    public void plusDoesNotMatchWrongChar() {
        Assertions.assertFalse(isExactMatch("a+", "b"));
    }

    @Test
    public void alternationLeftBranch() {
        Assertions.assertTrue(isExactMatch("a|b", "a"));
    }

    @Test
    public void alternationRightBranch() {
        Assertions.assertTrue(isExactMatch("a|b", "b"));
    }

    @Test
    public void alternationNoMatchNeither() {
        Assertions.assertFalse(isExactMatch("a|b", "c"));
    }

    @Test
    public void alternationNoMatchBoth() {
        Assertions.assertFalse(isExactMatch("a|b", "ab"));
    }

    @Test
    public void alternationThreeBranches() {
        Assertions.assertTrue(isExactMatch("x|y|z", "z"));
        Assertions.assertFalse(isExactMatch("x|y|z", "w"));
    }

    @Test
    public void concatExact() {
        Assertions.assertTrue(isExactMatch("abc", "abc"));
    }

    @Test
    public void concatTooShort() {
        Assertions.assertFalse(isExactMatch("abc", "ab"));
    }

    @Test
    public void concatTooLong() {
        Assertions.assertFalse(isExactMatch("abc", "abcd"));
    }

    @Test
    public void concatWithStarInMiddle() {
        Assertions.assertTrue(isExactMatch("ab*c", "ac"));
        Assertions.assertTrue(isExactMatch("ab*c", "abbc"));
        Assertions.assertFalse(isExactMatch("ab*c", "abbd"));
    }

    @Test
    public void dotMatchesAnyChar() {
        Assertions.assertTrue(isExactMatch(".", "a"));
        Assertions.assertTrue(isExactMatch(".", "1"));
        Assertions.assertTrue(isExactMatch(".", "!"));
    }

    @Test
    public void dotDoesNotMatchEmpty() {
        Assertions.assertFalse(isExactMatch(".", ""));
    }

    @Test
    public void dotDoesNotMatchTwo() {
        Assertions.assertFalse(isExactMatch(".", "ab"));
    }


    @Test
    public void charClassMatchesFirstInRange() {
        Assertions.assertTrue(isExactMatch("[a-z]", "a"));
    }

    @Test
    public void charClassMatchesLastInRange() {
        Assertions.assertTrue(isExactMatch("[a-z]", "z"));
    }

    @Test
    public void charClassNoMatchOutsideRange() {
        Assertions.assertFalse(isExactMatch("[a-z]", "A"));
    }

    @Test
    public void charClassNoMatchEmpty() {
        Assertions.assertFalse(isExactMatch("[a-z]", ""));
    }

    @Test
    public void negatedCharClassMatchesOutside() {
        Assertions.assertTrue(isExactMatch("[^0-9]", "a"));
    }

    @Test
    public void negatedCharClassNoMatchInside() {
        Assertions.assertFalse(isExactMatch("[^0-9]", "5"));
    }

    @Test
    public void charClassPlusNoMatchEmpty() {
        Assertions.assertFalse(isExactMatch("[a-z]+", ""));
    }

    @Test
    public void charClassPlusMatchesMany() {
        Assertions.assertTrue(isExactMatch("[a-z]+", "hello"));
    }

    @Test
    public void charClassPlusNoMatchDigit() {
        Assertions.assertFalse(isExactMatch("[a-z]+", "hello1"));
    }

    @Test
    public void exactRepeatMatch() {
        Assertions.assertTrue(isExactMatch("a{3}", "aaa"));
    }

    @Test
    public void exactRepeatTooFew() {
        Assertions.assertFalse(isExactMatch("a{3}", "aa"));
    }

    @Test
    public void exactRepeatTooMany() {
        Assertions.assertFalse(isExactMatch("a{3}", "aaaa"));
    }

    @Test
    public void atLeastRepeatMatchesMin() {
        Assertions.assertTrue(isExactMatch("a{2,}", "aa"));
    }

    @Test
    public void atLeastRepeatMatchesMore() {
        Assertions.assertTrue(isExactMatch("a{2,}", "aaaaa"));
    }

    @Test
    public void atLeastRepeatNoMatchBelowMin() {
        Assertions.assertFalse(isExactMatch("a{2,}", "a"));
    }

    @Test
    public void rangeRepeatMatchesMin() {
        Assertions.assertTrue(isExactMatch("a{2,4}", "aa"));
    }

    @Test
    public void rangeRepeatMatchesMax() {
        Assertions.assertTrue(isExactMatch("a{2,4}", "aaaa"));
    }

    @Test
    public void rangeRepeatMatchesMid() {
        Assertions.assertTrue(isExactMatch("a{2,4}", "aaa"));
    }

    @Test
    public void rangeRepeatNoMatchBelow() {
        Assertions.assertFalse(isExactMatch("a{2,4}", "a"));
    }

    @Test
    public void rangeRepeatNoMatchAbove() {
        Assertions.assertFalse(isExactMatch("a{2,4}", "aaaaa"));
    }

    @Test
    public void nestedGroupStarEmpty() {
        Assertions.assertTrue(isExactMatch("(ab)*", ""));
    }

    @Test
    public void nestedGroupStarOne() {
        Assertions.assertTrue(isExactMatch("(ab)*", "ab"));
    }

    @Test
    public void nestedGroupStarMany() {
        Assertions.assertTrue(isExactMatch("(ab)*", "ababab"));
    }

    @Test
    public void nestedGroupStarPartial() {
        Assertions.assertFalse(isExactMatch("(ab)*", "aba"));
    }

    @Test
    public void alternationInsideStarEmpty() {
        Assertions.assertTrue(isExactMatch("(a|b)*", ""));
    }

    @Test
    public void alternationInsideStarMixed() {
        Assertions.assertTrue(isExactMatch("(a|b)*", "baaabba"));
    }

    @Test
    public void alternationInsideStarWrongChar() {
        Assertions.assertFalse(isExactMatch("(a|b)*", "bac"));
    }

    @Test
    public void deeplyNestedGroups() {
        Assertions.assertTrue(isExactMatch("((a|b)*c)+", "c"));
        Assertions.assertTrue(isExactMatch("((a|b)*c)+", "abc"));
        Assertions.assertTrue(isExactMatch("((a|b)*c)+", "abcbc"));
        Assertions.assertFalse(isExactMatch("((a|b)*c)+", ""));
        Assertions.assertFalse(isExactMatch("((a|b)*c)+", "ab"));
    }

    @Test
    public void quantifierOnCharClass() {
        Assertions.assertTrue(isExactMatch("[0-9]{3}\\-[0-9]{4}", "555-1234"));
        Assertions.assertFalse(isExactMatch("[0-9]{3}\\-[0-9]{4}", "55-1234"));
        Assertions.assertFalse(isExactMatch("[0-9]{3}\\-[0-9]{4}", "555-123"));
    }

    @Test
    public void originalFromSpec() {
        Assertions.assertTrue(isExactMatch("(a|b)*c+123?",  "abbc12"));
        Assertions.assertTrue(isExactMatch("(a|b)*c+123?",  "c12"));
        Assertions.assertTrue(isExactMatch("(a|b)*c+123?",  "c123"));
        Assertions.assertTrue(isExactMatch("(a|b)*c+123?",  "cc12"));
        Assertions.assertTrue(isExactMatch("(a|b)*c+123?",  "aabcc123"));

        Assertions.assertFalse(isExactMatch("(a|b)*c+123?", "c1"));
        Assertions.assertFalse(isExactMatch("(a|b)*c+123?", "c1234"));
        Assertions.assertFalse(isExactMatch("(a|b)*c+123?", "abc"));
        Assertions.assertFalse(isExactMatch("(a|b)*c+123?", "12"));
        Assertions.assertFalse(isExactMatch("(a|b)*c+123?", ""));
    }

    @Test
    public void starAcceptsEmptyPrefix() {
        Assertions.assertTrue(isExactMatch("(a|b)*c", "c"));
    }

    @Test
    public void starAcceptsManyRepeats() {
        Assertions.assertTrue(isExactMatch("(a|b)*c", "abababbc"));
    }

    @Test
    public void starRejectsInvalidChar() {
        Assertions.assertFalse(isExactMatch("(a|b)*c", "abxc"));
    }

    @Test
    public void plusRequiresAtLeastOne() {
        Assertions.assertFalse(isExactMatch("a+b", "b"));
    }

    @Test
    public void plusAcceptsMany() {
        Assertions.assertTrue(isExactMatch("a+b", "aaaaab"));
    }

    @Test
    public void optionalAcceptsMissingChar() {
        Assertions.assertTrue(isExactMatch("ab?c", "ac"));
    }

    @Test
    public void optionalAcceptsPresentChar() {
        Assertions.assertTrue(isExactMatch("ab?c", "abc"));
    }

    @Test
    public void optionalRejectsDoubleChar() {
        Assertions.assertFalse(isExactMatch("ab?c", "abbc"));
    }

    @Test
    public void alternationAcceptsLeft() {
        Assertions.assertTrue(isExactMatch("cat|dog", "cat"));
    }

    @Test
    public void alternationAcceptsRight() {
        Assertions.assertTrue(isExactMatch("cat|dog", "dog"));
    }

    @Test
    public void alternationRejectsPartial() {
        Assertions.assertFalse(isExactMatch("cat|dog", "ca"));
    }

    @Test
    public void concatMustMatchExactFullInput() {
        Assertions.assertFalse(isExactMatch("abc", "abcd"));
    }

    @Test
    public void dotMatchesSingleChar() {
        Assertions.assertTrue(isExactMatch("a.c", "abc"));
    }

    @Test
    public void dotDoesNotMatchMissingChar() {
        Assertions.assertFalse(isExactMatch("a.c", "ac"));
    }

    @Test
    public void dotStarAcceptsAnything() {
        Assertions.assertTrue(isExactMatch(".*", "hello123"));
    }

    @Test
    public void charClassAcceptsChar() {
        Assertions.assertTrue(isExactMatch("[abc]", "b"));
    }

    @Test
    public void charClassRejectsChar() {
        Assertions.assertFalse(isExactMatch("[abc]", "x"));
    }

    @Test
    public void charClassRangeAcceptsChar() {
        Assertions.assertTrue(isExactMatch("[a-z]", "m"));
    }

    @Test
    public void charClassRangeRejectsChar() {
        Assertions.assertFalse(isExactMatch("[a-z]", "M"));
    }

    @Test
    public void negatedCharClassAcceptsOutside() {
        Assertions.assertTrue(isExactMatch("[^0-9]", "a"));
    }

    @Test
    public void negatedCharClassRejectsInside() {
        Assertions.assertFalse(isExactMatch("[^0-9]", "5"));
    }

    @Test
    public void exactRepeatAcceptsExactCount() {
        Assertions.assertTrue(isExactMatch("a{3}", "aaa"));
    }

    @Test
    public void exactRepeatRejectsTooFew() {
        Assertions.assertFalse(isExactMatch("a{3}", "aa"));
    }

    @Test
    public void exactRepeatRejectsTooMany() {
        Assertions.assertFalse(isExactMatch("a{3}", "aaaa"));
    }

    @Test
    public void rangeRepeatAcceptsMin() {
        Assertions.assertTrue(isExactMatch("a{2,4}", "aa"));
    }

    @Test
    public void rangeRepeatAcceptsMiddle() {
        Assertions.assertTrue(isExactMatch("a{2,4}", "aaa"));
    }

    @Test
    public void rangeRepeatAcceptsMax() {
        Assertions.assertTrue(isExactMatch("a{2,4}", "aaaa"));
    }

    @Test
    public void rangeRepeatRejectsTooFew() {
        Assertions.assertFalse(isExactMatch("a{2,4}", "a"));
    }

    @Test
    public void rangeRepeatRejectsTooMany() {
        Assertions.assertFalse(isExactMatch("a{2,4}", "aaaaa"));
    }

    @Test
    public void atLeastRepeatAcceptsMin() {
        Assertions.assertTrue(isExactMatch("a{2,}", "aa"));
    }

    @Test
    public void atLeastRepeatAcceptsMore() {
        Assertions.assertTrue(isExactMatch("a{2,}", "aaaaaa"));
    }

    @Test
    public void atLeastRepeatRejectsTooFew() {
        Assertions.assertFalse(isExactMatch("a{2,}", "a"));
    }

    @Test
    public void groupedRepeat() {
        Assertions.assertTrue(isExactMatch("(ab){2,3}", "abab"));
    }

    @Test
    public void groupedRepeatRejectsBrokenConcat() {
        Assertions.assertFalse(isExactMatch("(ab){2,3}", "aba"));
    }

    @Test
    public void complexRegexAcceptsWithoutOptional3() {
        Assertions.assertTrue(isExactMatch("(a|b|x|y)*c+123?", "abbc12"));
    }

    @Test
    public void complexRegexAcceptsWithOptional3() {
        Assertions.assertTrue(isExactMatch("(a|b|x|y)*c+123?", "xyabccc123"));
    }

    @Test
    public void complexRegexRejectsMissingC() {
        Assertions.assertFalse(isExactMatch("(a|b|x|y)*c+123?", "ab12"));
    }

    @Test
    public void complexRegexRejectsExtra3() {
        Assertions.assertFalse(isExactMatch("(a|b|x|y)*c+123?", "abbc1233"));
    }
    @Test
    public void precedenceAlternationVsConcat() {
        Assertions.assertTrue(isExactMatch("ab|cd", "ab"));
        Assertions.assertTrue(isExactMatch("ab|cd", "cd"));
        Assertions.assertFalse(isExactMatch("ab|cd", "ad"));
        Assertions.assertFalse(isExactMatch("ab|cd", "abcd"));
    }

    @Test
    public void groupedAlternationConcat() {
        Assertions.assertTrue(isExactMatch("a(b|c)d", "abd"));
        Assertions.assertTrue(isExactMatch("a(b|c)d", "acd"));
        Assertions.assertFalse(isExactMatch("a(b|c)d", "abcd"));
    }

    @Test
    public void quantifierAppliesOnlyPreviousAtom() {
        Assertions.assertTrue(isExactMatch("ab*", "a"));
        Assertions.assertTrue(isExactMatch("ab*", "abbbb"));
        Assertions.assertFalse(isExactMatch("ab*", "b"));
    }

    @Test
    public void escapedMetacharacters() {
        Assertions.assertTrue(isExactMatch("\\.", "."));
        Assertions.assertTrue(isExactMatch("\\*", "*"));
        Assertions.assertTrue(isExactMatch("\\+", "+"));
        Assertions.assertTrue(isExactMatch("\\?", "?"));
        Assertions.assertTrue(isExactMatch("\\|", "|"));
        Assertions.assertTrue(isExactMatch("\\(", "("));
        Assertions.assertTrue(isExactMatch("\\)", ")"));
    }

    @Test
    public void escapedBackslash() {
        Assertions.assertTrue(isExactMatch("\\\\", "\\"));
        Assertions.assertFalse(isExactMatch("\\\\", "\\\\"));
    }

    @Test
    public void escapedDashOutsideClass() {
        Assertions.assertTrue(isExactMatch("a\\-b", "a-b"));
        Assertions.assertFalse(isExactMatch("a\\-b", "ab"));
    }

    @Test
    public void charClassLiteralDashAtEnd() {
        Assertions.assertTrue(isExactMatch("[ab\\-]", "-"));
        Assertions.assertTrue(isExactMatch("[ab\\-]", "a"));
        Assertions.assertFalse(isExactMatch("[ab\\-]", "c"));
    }

    @Test
    public void charClassLiteralDashAtStart() {
        Assertions.assertTrue(isExactMatch("[-ab]", "-"));
        Assertions.assertTrue(isExactMatch("[-ab]", "b"));
    }

    @Test
    public void charClassEscapedSpecials() {
        Assertions.assertTrue(isExactMatch("[\\]\\[]", "]"));
        Assertions.assertTrue(isExactMatch("[\\]\\[]", "["));
        Assertions.assertFalse(isExactMatch("[\\]\\[]", "a"));
    }

    @Test
    public void negatedCharClassRejectsOnlyListedChars() {
        Assertions.assertFalse(isExactMatch("[^abc]", "a"));
        Assertions.assertFalse(isExactMatch("[^abc]", "b"));
        Assertions.assertTrue(isExactMatch("[^abc]", "x"));
    }

    @Test
    public void repeatZeroTimes() {
        Assertions.assertTrue(isExactMatch("a{0}", ""));
        Assertions.assertFalse(isExactMatch("a{0}", "a"));
    }

    @Test
    public void repeatZeroToMany() {
        Assertions.assertTrue(isExactMatch("a{0,3}", ""));
        Assertions.assertTrue(isExactMatch("a{0,3}", "aaa"));
        Assertions.assertFalse(isExactMatch("a{0,3}", "aaaa"));
    }

    @Test
    public void repeatOnGroupWithAlternation() {
        Assertions.assertTrue(isExactMatch("(ab|cd){2}", "abcd"));
        Assertions.assertTrue(isExactMatch("(ab|cd){2}", "cdab"));
        Assertions.assertFalse(isExactMatch("(ab|cd){2}", "ababab"));
    }

    @Test
    public void optionalGroup() {
        Assertions.assertTrue(isExactMatch("(ab)?c", "c"));
        Assertions.assertTrue(isExactMatch("(ab)?c", "abc"));
        Assertions.assertFalse(isExactMatch("(ab)?c", "abbc"));
    }

    @Test
    public void plusGroup() {
        Assertions.assertTrue(isExactMatch("(ab)+", "ab"));
        Assertions.assertTrue(isExactMatch("(ab)+", "ababab"));
        Assertions.assertFalse(isExactMatch("(ab)+", ""));
        Assertions.assertFalse(isExactMatch("(ab)+", "aba"));
    }

    @Test
    public void dotInsideConcat() {
        Assertions.assertTrue(isExactMatch("a.*z", "az"));
        Assertions.assertTrue(isExactMatch("a.*z", "abcdefz"));
        Assertions.assertFalse(isExactMatch("a.*z", "abcdef"));
    }

    @Test
    public void nestedOptionalAndStar() {
        Assertions.assertTrue(isExactMatch("(a?)*", ""));
        Assertions.assertTrue(isExactMatch("(a?)*", "aaa"));
        Assertions.assertFalse(isExactMatch("(a?)*", "b"));
    }

    @Test
    public void ambiguousNfaPathStillAccepts() {
        Assertions.assertTrue(isExactMatch("(a|aa)*", ""));
        Assertions.assertTrue(isExactMatch("(a|aa)*", "a"));
        Assertions.assertTrue(isExactMatch("(a|aa)*", "aaaaa"));
        Assertions.assertFalse(isExactMatch("(a|aa)*", "aaaab"));
    }

    @Test
    public void longInputStress() {
        Assertions.assertTrue(isExactMatch("a*b", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaab"));
        Assertions.assertFalse(isExactMatch("a*b", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaac"));
    }

    @Test
    public void massiveRegexFullMatch() {
        // Hedef format:
        // YYYY-MM-DD | HH:MM:SS | (INFO|WARN|ERROR) | [modul] | mesaj (en az 1 char) | (ozet)?
        // Ornek: 2024-03-15 | 14:22:01 | ERROR | [auth] | login failed | summary
        String regex =
                "[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}" +   // YYYY-MM-DD
                        " \\| " +                               // separator
                        "[0-9]{2}:[0-9]{2}:[0-9]{2}" +         // HH:MM:SS
                        " \\| " +
                        "(INFO|WARN|ERROR)" +                   // log level
                        " \\| " +
                        "\\[[a-z]+\\]" +                        // [modul]
                        " \\| " +
                        "[a-zA-Z0-9 ]{1,64}" +                 // mesaj
                        "( \\| [a-zA-Z0-9 ]{1,32})?";          // opsiyonel ozet

        // --- gecerli ---
        Assertions.assertTrue(isExactMatch(regex,
                "2024-03-15 | 14:22:01 | ERROR | [auth] | login failed | bad credentials"));

        Assertions.assertTrue(isExactMatch(regex,
                "2024-03-15 | 14:22:01 | INFO | [db] | connection ok"));

        Assertions.assertTrue(isExactMatch(regex,
                "1999-01-01 | 00:00:00 | WARN | [cache] | eviction triggered | lru policy"));

        Assertions.assertTrue(isExactMatch(regex,
                "2000-12-31 | 23:59:59 | INFO | [api] | request received"));

        // --- gecersiz: yanlis level ---
        Assertions.assertFalse(isExactMatch(regex,
                "2024-03-15 | 14:22:01 | DEBUG | [auth] | login failed"));

        // --- gecersiz: tarih formati bozuk ---
        Assertions.assertFalse(isExactMatch(regex,
                "24-03-15 | 14:22:01 | INFO | [auth] | ok"));

        // --- gecersiz: modul buyuk harf ---
        Assertions.assertFalse(isExactMatch(regex,
                "2024-03-15 | 14:22:01 | INFO | [Auth] | ok"));

        // --- gecersiz: mesaj bos ---
        Assertions.assertFalse(isExactMatch(regex,
                "2024-03-15 | 14:22:01 | INFO | [api] | "));

        // --- gecersiz: separator eksik ---
        Assertions.assertFalse(isExactMatch(regex,
                "2024-03-15 14:22:01 | INFO | [api] | ok"));

        // --- gecersiz: tamamen bos ---
        Assertions.assertFalse(isExactMatch(regex, ""));
    }

    @Test
    public void massiveRegex_validFullLog() {
        String regex =
                "[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}" +
                        " \\| " +
                        "[0-9]{2}:[0-9]{2}:[0-9]{2}" +
                        " \\| " +
                        "(INFO|WARN|ERROR)" +
                        " \\| " +
                        "\\[[a-z]+\\]" +
                        " \\| " +
                        "[a-zA-Z0-9 ]{1,64}" +
                        "( \\| [a-zA-Z0-9 ]{1,32})?";

        // valid: all fields present including optional summary
        Assertions.assertTrue(isExactMatch(regex,
                "2024-03-15 | 14:22:01 | ERROR | [auth] | login failed | bad credentials"));

        // valid: no optional summary
        Assertions.assertTrue(isExactMatch(regex,
                "2024-03-15 | 14:22:01 | INFO | [db] | connection ok"));

        // valid: WARN level with summary
        Assertions.assertTrue(isExactMatch(regex,
                "1999-01-01 | 00:00:00 | WARN | [cache] | eviction triggered | lru policy"));

        // valid: midnight boundary timestamp
        Assertions.assertTrue(isExactMatch(regex,
                "2000-12-31 | 23:59:59 | INFO | [api] | request received"));

        // valid: single char message (min length = 1)
        Assertions.assertTrue(isExactMatch(regex,
                "2024-01-01 | 00:00:00 | INFO | [x] | a"));

        // valid: message exactly 64 chars
        Assertions.assertTrue(isExactMatch(regex,
                "2024-01-01 | 00:00:00 | INFO | [mod] | " + "a".repeat(64)));

        // valid: summary exactly 32 chars
        Assertions.assertTrue(isExactMatch(regex,
                "2024-01-01 | 00:00:00 | INFO | [mod] | msg | " + "b".repeat(32)));

        // valid: all digits in message
        Assertions.assertTrue(isExactMatch(regex,
                "2024-01-01 | 00:00:00 | WARN | [net] | 12345678"));

        // valid: module name is single char
        Assertions.assertTrue(isExactMatch(regex,
                "2024-01-01 | 00:00:00 | ERROR | [a] | something went wrong"));

        // valid: module name is long
        Assertions.assertTrue(isExactMatch(regex,
                "2024-01-01 | 00:00:00 | INFO | [authentication] | ok"));
    }

    @Test
    public void massiveRegex_invalidLevel() {
        String regex =
                "[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}" +
                        " \\| " +
                        "[0-9]{2}:[0-9]{2}:[0-9]{2}" +
                        " \\| " +
                        "(INFO|WARN|ERROR)" +
                        " \\| " +
                        "\\[[a-z]+\\]" +
                        " \\| " +
                        "[a-zA-Z0-9 ]{1,64}" +
                        "( \\| [a-zA-Z0-9 ]{1,32})?";

        // invalid: DEBUG is not an accepted level
        Assertions.assertFalse(isExactMatch(regex,
                "2024-03-15 | 14:22:01 | DEBUG | [auth] | login failed"));

        // invalid: lowercase level
        Assertions.assertFalse(isExactMatch(regex,
                "2024-03-15 | 14:22:01 | info | [auth] | login failed"));

        // invalid: mixed case level
        Assertions.assertFalse(isExactMatch(regex,
                "2024-03-15 | 14:22:01 | Info | [auth] | login failed"));

        // invalid: empty level
        Assertions.assertFalse(isExactMatch(regex,
                "2024-03-15 | 14:22:01 |  | [auth] | login failed"));

        // invalid: TRACE not in alternation
        Assertions.assertFalse(isExactMatch(regex,
                "2024-03-15 | 14:22:01 | TRACE | [auth] | login failed"));
    }

    @Test
    public void massiveRegex_invalidDate() {
        String regex =
                "[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}" +
                        " \\| " +
                        "[0-9]{2}:[0-9]{2}:[0-9]{2}" +
                        " \\| " +
                        "(INFO|WARN|ERROR)" +
                        " \\| " +
                        "\\[[a-z]+\\]" +
                        " \\| " +
                        "[a-zA-Z0-9 ]{1,64}" +
                        "( \\| [a-zA-Z0-9 ]{1,32})?";

        // invalid: 2-digit year
        Assertions.assertFalse(isExactMatch(regex,
                "24-03-15 | 14:22:01 | INFO | [auth] | ok"));

        // invalid: slash separator instead of dash
        Assertions.assertFalse(isExactMatch(regex,
                "2024/03/15 | 14:22:01 | INFO | [auth] | ok"));

        // invalid: month missing leading zero would have 1 digit → only 7 total date chars
        Assertions.assertFalse(isExactMatch(regex,
                "2024-3-15 | 14:22:01 | INFO | [auth] | ok"));

        // invalid: date completely missing
        Assertions.assertFalse(isExactMatch(regex,
                "14:22:01 | INFO | [auth] | ok"));

        // invalid: letters in date
        Assertions.assertFalse(isExactMatch(regex,
                "2024-MM-DD | 14:22:01 | INFO | [auth] | ok"));
    }

    @Test
    public void massiveRegex_invalidTime() {
        String regex =
                "[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}" +
                        " \\| " +
                        "[0-9]{2}:[0-9]{2}:[0-9]{2}" +
                        " \\| " +
                        "(INFO|WARN|ERROR)" +
                        " \\| " +
                        "\\[[a-z]+\\]" +
                        " \\| " +
                        "[a-zA-Z0-9 ]{1,64}" +
                        "( \\| [a-zA-Z0-9 ]{1,32})?";

        // invalid: time uses dash instead of colon
        Assertions.assertFalse(isExactMatch(regex,
                "2024-03-15 | 14-22-01 | INFO | [auth] | ok"));

        // invalid: missing seconds segment
        Assertions.assertFalse(isExactMatch(regex,
                "2024-03-15 | 14:22 | INFO | [auth] | ok"));

        // invalid: extra digit in hour
        Assertions.assertFalse(isExactMatch(regex,
                "2024-03-15 | 140:22:01 | INFO | [auth] | ok"));

        // invalid: time completely missing
        Assertions.assertFalse(isExactMatch(regex,
                "2024-03-15 | INFO | [auth] | ok"));
    }

    @Test
    public void massiveRegex_invalidModule() {
        String regex =
                "[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}" +
                        " \\| " +
                        "[0-9]{2}:[0-9]{2}:[0-9]{2}" +
                        " \\| " +
                        "(INFO|WARN|ERROR)" +
                        " \\| " +
                        "\\[[a-z]+\\]" +
                        " \\| " +
                        "[a-zA-Z0-9 ]{1,64}" +
                        "( \\| [a-zA-Z0-9 ]{1,32})?";

        // invalid: uppercase letter in module
        Assertions.assertFalse(isExactMatch(regex,
                "2024-03-15 | 14:22:01 | INFO | [Auth] | ok"));

        // invalid: digit in module
        Assertions.assertFalse(isExactMatch(regex,
                "2024-03-15 | 14:22:01 | INFO | [auth2] | ok"));

        // invalid: empty module brackets
        Assertions.assertFalse(isExactMatch(regex,
                "2024-03-15 | 14:22:01 | INFO | [] | ok"));

        // invalid: missing closing bracket
        Assertions.assertFalse(isExactMatch(regex,
                "2024-03-15 | 14:22:01 | INFO | [auth | ok"));

        // invalid: missing opening bracket
        Assertions.assertFalse(isExactMatch(regex,
                "2024-03-15 | 14:22:01 | INFO | auth] | ok"));

        // invalid: no brackets at all
        Assertions.assertFalse(isExactMatch(regex,
                "2024-03-15 | 14:22:01 | INFO | auth | ok"));
    }

    @Test
    public void massiveRegex_invalidMessage() {
        String regex =
                "[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}" +
                        " \\| " +
                        "[0-9]{2}:[0-9]{2}:[0-9]{2}" +
                        " \\| " +
                        "(INFO|WARN|ERROR)" +
                        " \\| " +
                        "\\[[a-z]+\\]" +
                        " \\| " +
                        "[a-zA-Z0-9 ]{1,64}" +
                        "( \\| [a-zA-Z0-9 ]{1,32})?";

        // invalid: empty message (min = 1)
        Assertions.assertFalse(isExactMatch(regex,
                "2024-03-15 | 14:22:01 | INFO | [api] | "));

        // invalid: message exceeds 64 chars
        Assertions.assertFalse(isExactMatch(regex,
                "2024-03-15 | 14:22:01 | INFO | [api] | " + "a".repeat(65)));

        // invalid: special char in message
        Assertions.assertFalse(isExactMatch(regex,
                "2024-03-15 | 14:22:01 | INFO | [api] | hello!"));

        // invalid: newline in message
        Assertions.assertFalse(isExactMatch(regex,
                "2024-03-15 | 14:22:01 | INFO | [api] | hello\nworld"));
    }

    @Test
    public void massiveRegex_invalidSummary() {
        String regex =
                "[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}" +
                        " \\| " +
                        "[0-9]{2}:[0-9]{2}:[0-9]{2}" +
                        " \\| " +
                        "(INFO|WARN|ERROR)" +
                        " \\| " +
                        "\\[[a-z]+\\]" +
                        " \\| " +
                        "[a-zA-Z0-9 ]{1,64}" +
                        "( \\| [a-zA-Z0-9 ]{1,32})?";

        // invalid: summary exceeds 32 chars
        Assertions.assertFalse(isExactMatch(regex,
                "2024-01-01 | 00:00:00 | INFO | [mod] | msg | " + "b".repeat(33)));

        // invalid: summary separator present but summary body empty
        Assertions.assertFalse(isExactMatch(regex,
                "2024-01-01 | 00:00:00 | INFO | [mod] | msg | "));

        // invalid: special char in summary
        Assertions.assertFalse(isExactMatch(regex,
                "2024-01-01 | 00:00:00 | INFO | [mod] | msg | bad!summary"));

        // invalid: two summaries (optional appears only once)
        Assertions.assertFalse(isExactMatch(regex,
                "2024-01-01 | 00:00:00 | INFO | [mod] | msg | sum1 | sum2"));
    }

    @Test
    public void massiveRegex_invalidSeparators() {
        String regex =
                "[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}" +
                        " \\| " +
                        "[0-9]{2}:[0-9]{2}:[0-9]{2}" +
                        " \\| " +
                        "(INFO|WARN|ERROR)" +
                        " \\| " +
                        "\\[[a-z]+\\]" +
                        " \\| " +
                        "[a-zA-Z0-9 ]{1,64}" +
                        "( \\| [a-zA-Z0-9 ]{1,32})?";

        // invalid: separator missing spaces around pipe
        Assertions.assertFalse(isExactMatch(regex,
                "2024-03-15|14:22:01|INFO|[auth]|ok"));

        // invalid: separator uses comma instead of pipe
        Assertions.assertFalse(isExactMatch(regex,
                "2024-03-15 , 14:22:01 , INFO , [auth] , ok"));

        // invalid: double pipe separator
        Assertions.assertFalse(isExactMatch(regex,
                "2024-03-15 || 14:22:01 || INFO || [auth] || ok"));

        // invalid: completely empty input
        Assertions.assertFalse(isExactMatch(regex, ""));
    }

    @Test
    public void deepNestedGroups() {
        Assertions.assertTrue(
                isExactMatch("((((((((((a))))))))))", "a")
        );
    }

    @Test
    public void hugeConcatenation() {
        String regex = "a".repeat(1000);
        String input = "a".repeat(1000);

        Assertions.assertTrue(isExactMatch(regex, input));
    }

    @Test
    public void nestedStars() {
        Assertions.assertTrue(
                isExactMatch("((a*)*)*", "")
        );

        Assertions.assertTrue(
                isExactMatch("((a*)*)*", "aaaa")
        );
    }

    @Test
    public void manyAlternatives() {
        Assertions.assertTrue(
                isExactMatch(
                        "a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p",
                        "o"
                )
        );
    }

    @Test
    public void nestedQuantifiers() {
        Assertions.assertTrue(isExactMatch("(ab?)+", "ababab"));
        Assertions.assertTrue(isExactMatch("(ab?)+", "aaa"));
        Assertions.assertTrue(isExactMatch("(ab?)+", "aba"));

        Assertions.assertFalse(isExactMatch("(ab?)+", "abababb"));
        Assertions.assertFalse(isExactMatch("(ab?)+", "b"));
        Assertions.assertFalse(isExactMatch("(ab?)+", ""));
    }

    @Test
    public void unicodeLiteral() {
        Assertions.assertTrue(
                isExactMatch("ğüşöçı", "ğüşöçı")
        );
    }

    @Test
    public void hugeInput() {
        Assertions.assertTrue(
                isExactMatch(
                        "a*",
                        "a".repeat(100000)
                )
        );
    }

    @Test
    public void ambiguousPaths() {
        Assertions.assertTrue(
                isExactMatch("(a|aa|aaa)*", "aaaaaaaaaa")
        );
    }
}
