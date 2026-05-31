import Needle.Core;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SubstringSearchTest {

    public boolean contains(String regex, String input){
        try {
            Core core = new Core();
            return core.contains(core.compile(regex), input);

        }catch (Needle.NeedleException ex) {
            Assertions.fail(ex.getMessage());
        }
        return false;
    }

    @Test
    public void basic() {
        Assertions.assertTrue(contains("(a|b)", "yabiz"));
    }

    @Test
    public void matchAtStart() {
        Assertions.assertTrue(contains("abc", "abcdef"));
    }

    @Test
    public void matchAtEnd() {
        Assertions.assertTrue(contains("def", "abcdef"));
    }

    @Test
    public void matchInMiddle() {
        Assertions.assertTrue(contains("cde", "abcdefgh"));
    }

    @Test
    public void noMatchWrongChars() {
        Assertions.assertFalse(contains("xyz", "abcdef"));
    }

    @Test
    public void noMatchEmptyInput() {
        Assertions.assertFalse(contains("a", ""));
    }

    @Test
    public void emptyRegexMatchesAnything() {
        Assertions.assertTrue(contains("", "anything"));
    }

    @Test
    public void emptyRegexMatchesEmptyInput() {
        Assertions.assertTrue(contains("", ""));
    }

    @Test
    public void singleCharFoundAmongMany() {
        Assertions.assertTrue(contains("z", "aabbcczddeeff"));
    }

    @Test
    public void singleCharNotPresent() {
        Assertions.assertFalse(contains("z", "abcdefgh"));
    }

    @Test
    public void singleCharRepeatedInInput() {
        Assertions.assertTrue(contains("a", "aaaa"));
    }

    @Test
    public void singleCharAtExactPosition() {
        Assertions.assertTrue(contains("e", "abcde"));
    }

    @Test
    public void dotMatchesAnyCharInMiddle() {
        Assertions.assertTrue(contains("a.c", "xyzabcxyz"));
    }

    @Test
    public void dotMatchesDigit() {
        Assertions.assertTrue(contains("a.c", "a9c"));
    }

    @Test
    public void dotStarMatchesEverything() {
        Assertions.assertTrue(contains(".*", "anything at all"));
    }

    @Test
    public void dotStarMatchesEmpty() {
        Assertions.assertTrue(contains(".*", ""));
    }

    @Test
    public void dotRequiresAtLeastOneChar() {
        Assertions.assertFalse(contains("a.c", "ac"));
    }

    @Test
    public void starMatchesZeroOccurrences() {
        // "ab*c" matches "ac" as substring inside "xacx"
        Assertions.assertTrue(contains("ab*c", "xacx"));
    }

    @Test
    public void starMatchesMultipleOccurrences() {
        Assertions.assertTrue(contains("ab*c", "xabbbbcx"));
    }

    @Test
    public void starOnWrongChar() {
        Assertions.assertFalse(contains("x*y", "aaabbbccc"));
    }

    @Test
    public void plusRequiresAtLeastOne() {
        // "ab+c" does NOT match "ac" as substring
        Assertions.assertFalse(contains("ab+c", "xacx"));
    }

    @Test
    public void plusMatchesOne() {
        Assertions.assertTrue(contains("ab+c", "xabcx"));
    }

    @Test
    public void plusMatchesMany() {
        Assertions.assertTrue(contains("ab+c", "xabbbbcx"));
    }

    @Test
    public void plusInLongerInput() {
        Assertions.assertTrue(contains("x+", "aaaxxxxxbbb"));
    }

    @Test
    public void optionalPresentInInput() {
        Assertions.assertTrue(contains("colou?r", "the colour is red"));
    }

    @Test
    public void optionalAbsentInInput() {
        Assertions.assertTrue(contains("colou?r", "the color is red"));
    }

    @Test
    public void optionalNoMatchAtAll() {
        Assertions.assertFalse(contains("colou?r", "the colouur is red"));
    }

    @Test
    public void alternationFirstBranchFound() {
        Assertions.assertTrue(contains("cat|dog", "I have a cat at home"));
    }

    @Test
    public void alternationSecondBranchFound() {
        Assertions.assertTrue(contains("cat|dog", "I have a dog at home"));
    }

    @Test
    public void alternationNeitherBranchFound() {
        Assertions.assertFalse(contains("cat|dog", "I have a fish at home"));
    }

    @Test
    public void alternationBothPresentMatchesFirst() {
        Assertions.assertTrue(contains("cat|dog", "cat and dog"));
    }

    @Test
    public void alternationSingleCharOptions() {
        Assertions.assertTrue(contains("a|b|c", "xyz c xyz"));
        Assertions.assertFalse(contains("a|b|c", "xyz d xyz"));
    }

    @Test
    public void groupWithStar() {
        Assertions.assertTrue(contains("(ab)*c", "xababcx"));
    }

    @Test
    public void groupWithPlus() {
        Assertions.assertTrue(contains("(ha)+", "hahaha that is funny"));
    }

    @Test
    public void groupNotFound() {
        Assertions.assertFalse(contains("(ha)+", "hello world"));
    }

    @Test
    public void groupAlternationInSentence() {
        Assertions.assertTrue(contains("(foo|bar)baz", "this is foobaz right here"));
        Assertions.assertTrue(contains("(foo|bar)baz", "this is barbaz right here"));
        Assertions.assertFalse(contains("(foo|bar)baz", "this is quxbaz right here"));
    }

    @Test
    public void charClassFoundInInput() {
        Assertions.assertTrue(contains("[0-9]+", "the answer is 42 ok"));
    }

    @Test
    public void charClassNotFoundInInput() {
        Assertions.assertFalse(contains("[0-9]+", "no digits here"));
    }

    @Test
    public void charClassRangeMiddle() {
        Assertions.assertTrue(contains("[m-p]", "the letter n is here"));
    }

    @Test
    public void negatedCharClassFound() {
        Assertions.assertTrue(contains("[^aeiou]", "hello"));
    }

    @Test
    public void negatedCharClassNotFound() {
        Assertions.assertFalse(contains("[^aeiou]+", "aeiou"));
    }

    @Test
    public void charClassWithQuantifier() {
        Assertions.assertTrue(contains("[a-z]{3}", "xy abc 123"));
        Assertions.assertFalse(contains("[a-z]{5}", "ab 12"));
    }

    @Test
    public void exactRepeatFoundInLongerInput() {
        Assertions.assertTrue(contains("a{3}", "xyzaaaxyz"));
    }

    @Test
    public void exactRepeatNotEnoughConsecutive() {
        Assertions.assertFalse(contains("a{4}", "xyzaaaxyz"));
    }

    @Test
    public void atLeastRepeatFound() {
        Assertions.assertTrue(contains("a{2,}", "xaaaaax"));
    }

    @Test
    public void atLeastRepeatTooFew() {
        Assertions.assertFalse(contains("a{3,}", "xax"));
    }

    @Test
    public void rangeRepeatFoundExact() {
        Assertions.assertTrue(contains("a{2,4}", "xaax"));
    }

    @Test
    public void rangeRepeatFoundMax() {
        Assertions.assertTrue(contains("a{2,4}", "xaaaax"));
    }

    @Test
    public void rangeRepeatNotFound() {
        Assertions.assertFalse(contains("b{3,5}", "xbbx"));
    }

    @Test
    public void escapedDotMatchesLiteralDot() {
        Assertions.assertTrue(contains("\\.", "version 1.0 released"));
    }

    @Test
    public void escapedDotDoesNotMatchNonDot() {
        Assertions.assertFalse(contains("\\.", "no dot here"));
    }

    @Test
    public void escapedPipeMatchesLiteralPipe() {
        Assertions.assertTrue(contains("a\\|b", "the value is a|b ok"));
    }

    @Test
    public void escapedParenMatchesLiteralParen() {
        Assertions.assertTrue(contains("\\(ok\\)", "result is (ok) done"));
    }

    @Test
    public void matchOccursOnlyAtVeryStart() {
        Assertions.assertTrue(contains("hello", "hello world"));
    }

    @Test
    public void matchOccursOnlyAtVeryEnd() {
        Assertions.assertTrue(contains("world", "hello world"));
    }

    @Test
    public void patternLongerThanInput() {
        Assertions.assertFalse(contains("abcdefgh", "abc"));
    }

    @Test
    public void patternSameLengthAsInputMatch() {
        Assertions.assertTrue(contains("abc", "abc"));
    }

    @Test
    public void patternSameLengthAsInputNoMatch() {
        Assertions.assertFalse(contains("xyz", "abc"));
    }

    @Test
    public void multipleMatchesInInput() {
        Assertions.assertTrue(contains("ab", "ab_ab_ab"));
    }

    @Test
    public void emailLocalPartFound() {
        Assertions.assertTrue(contains("[a-zA-Z0-9]+@[a-zA-Z]+", "contact me at john@example please"));
    }

    @Test
    public void emailLocalPartNotFound() {
        Assertions.assertFalse(contains("[a-zA-Z0-9]+@[a-zA-Z]+", "no email here"));
    }

    @Test
    public void versionNumberFound() {
        Assertions.assertTrue(contains("[0-9]+\\.[0-9]+\\.[0-9]+", "released v2.14.3 today"));
    }

    @Test
    public void versionNumberNotFound() {
        Assertions.assertFalse(contains("[0-9]+\\.[0-9]+\\.[0-9]+", "no version info"));
    }

    @Test
    public void repeatedWordFound() {
        Assertions.assertTrue(contains("(ha){2,}", "hahahaha that is funny"));
    }

    @Test
    public void repeatedWordNotFound() {
        Assertions.assertFalse(contains("(ha){3,}", "ha that is only once"));
    }

    @Test
    public void nestedGroupInSentence() {
        Assertions.assertTrue(contains("((a|b)+c){2}", "prefix aabcbc suffix"));
        Assertions.assertFalse(contains("((a|b)+c){2}", "prefix aabcsuffix"));
    }

    @Test
    public void longInputWithMatchNearEnd() {
        String padding = "x".repeat(1000);
        Assertions.assertTrue(contains("needle", padding + "needle"));
    }

    @Test
    public void longInputNoMatch() {
        String padding = "x".repeat(1000);
        Assertions.assertFalse(contains("needle", padding));
    }

    @Test
    public void longPatternFoundInLongInput() {
        String input = "z".repeat(500) + "ababababab" + "z".repeat(500);
        Assertions.assertTrue(contains("(ab){5}", input));
    }

    // -----------------------------------------
// LONG EDGE CASES - SUBSTRING SEARCH
// -----------------------------------------

    @Test
    public void logLinePatternFoundInMultiSegmentInput() {
        // real-world log line buried inside noise
        String input = "garbage_data|noise|||" +
                "2024-03-15 | 14:22:01 | ERROR | [auth] | login failed" +
                "|||more_noise|||";
        String regex = "[0-9]{4}\\-[0-9]{2}\\-[0-9]{2} \\| [0-9]{2}:[0-9]{2}:[0-9]{2} \\| (INFO|WARN|ERROR) \\| \\[[a-z]+\\] \\| [a-zA-Z0-9 ]{1,64}";
        Assertions.assertTrue(contains(regex, input));
    }

    @Test
    public void logLinePatternNotFoundWhenLevelWrong() {
        // level is DEBUG which is not in alternation
        String input = "2024-03-15 | 14:22:01 | DEBUG | [auth] | login failed";
        String regex = "[0-9]{4}\\-[0-9]{2}\\-[0-9]{2} \\| [0-9]{2}:[0-9]{2}:[0-9]{2} \\| (INFO|WARN|ERROR) \\| \\[[a-z]+\\] \\| [a-zA-Z0-9 ]{1,64}";
        Assertions.assertFalse(contains(regex, input));
    }

    @Test
    public void versionTagFoundDeepInsideLongString() {
        // version string buried after 500 chars of noise
        String noise  = "x1z!@#qwerty".repeat(40);
        String input  = noise + "v3.11.204-beta" + noise;
        String regex  = "v[0-9]+\\.[0-9]+\\.[0-9]+\\-[a-z]+";
        Assertions.assertTrue(contains(regex, input));
    }

    @Test
    public void versionTagNotFoundWhenHyphenMissing() {
        String input = "release v3.11.204beta is out";
        String regex = "v[0-9]+\\.[0-9]+\\.[0-9]+\\-[a-z]+";
        Assertions.assertFalse(contains(regex, input));
    }

    @Test
    public void hexColorFoundInCssBlob() {
        String input = "body { background: #1aFFb0; color: #000; margin: 0; }";
        String regex = "#[0-9a-fA-F]{6}";
        Assertions.assertTrue(contains(regex, input));
    }

    @Test
    public void hexColorNotFoundWhenTooShort() {
        // #000 is only 3 hex digits — does not match {6}
        String input = "body { color: #000; }";
        String regex = "#[0-9a-fA-F]{6}";
        Assertions.assertFalse(contains(regex, input));
    }

    @Test
    public void hexColorNotFoundWhenNoHash() {
        String input = "body { color: 1aFFb0; }";
        String regex = "#[0-9a-fA-F]{6}";
        Assertions.assertFalse(contains(regex, input));
    }

    @Test
    public void ipAddressFoundInServerLog() {
        String input = "[2024-01-01] connection from 192.168.1.254 accepted";
        String regex = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}";
        Assertions.assertTrue(contains(regex, input));
    }

    @Test
    public void ipAddressNotFoundWhenSegmentMissing() {
        String input = "connection from 192.168.1 accepted";
        String regex = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}";
        Assertions.assertFalse(contains(regex, input));
    }

    @Test
    public void ipAddressNotFoundWhenLettersPresent() {
        String input = "connection from abc.def.ghi.jkl accepted";
        String regex = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}";
        Assertions.assertFalse(contains(regex, input));
    }

    @Test
    public void jwtLikeTokenFoundInAuthHeader() {
        // simplified JWT: three base64-like segments separated by dots
        String input  = "Authorization: Bearer eyJhbGc.eyJzdWI.SflKxwRJ";
        String regex  = "[a-zA-Z0-9]{6,}\\.[a-zA-Z0-9]{6,}\\.[a-zA-Z0-9]{6,}";
        Assertions.assertTrue(contains(regex, input));
    }

    @Test
    public void jwtLikeTokenNotFoundWhenSegmentTooShort() {
        // middle segment is only 3 chars — below {6,}
        String input = "Authorization: Bearer eyJhbGc.abc.SflKxwRJ";
        String regex = "[a-zA-Z0-9]{6,}\\.[a-zA-Z0-9]{6,}\\.[a-zA-Z0-9]{6,}";
        Assertions.assertFalse(contains(regex, input));
    }

    @Test
    public void repeatedKeywordFoundInNoisyText() {
        // "error" appears after 300 chars of unrelated text
        String noise = "info debug trace warn ".repeat(15);
        String input = noise + "error" + noise;
        Assertions.assertTrue(contains("error", input));
    }

    @Test
    public void repeatedKeywordNotFoundWhenTypoed() {
        String noise = "info debug trace warn ".repeat(15);
        String input = noise + "errror" + noise; // triple r
        Assertions.assertFalse(contains("error", input));
    }

    @Test
    public void phoneNumberFoundInContactPage() {
        String input = "Call us at +1-800-555-0199 for support.";
        String regex = "\\+[0-9]{1,3}\\-[0-9]{3}\\-[0-9]{3}\\-[0-9]{4}";
        Assertions.assertTrue(contains(regex, input));
    }

    @Test
    public void phoneNumberNotFoundWhenCountryCodeMissing() {
        String input = "Call us at 800-555-0199 for support.";
        String regex = "\\+[0-9]{1,3}\\-[0-9]{3}\\-[0-9]{3}\\-[0-9]{4}";
        Assertions.assertFalse(contains(regex, input));
    }

    @Test
    public void phoneNumberNotFoundWhenDashReplacedBySpace() {
        String input = "Call us at +1 800 555 0199 for support.";
        String regex = "\\+[0-9]{1,3}\\-[0-9]{3}\\-[0-9]{3}\\-[0-9]{4}";
        Assertions.assertFalse(contains(regex, input));
    }

    @Test
    public void sqlSelectFoundInQueryLog() {
        String input = "2024-01-01 slow_query: SELECT id FROM users WHERE active=1 -- 320ms";
        String regex = "SELECT [a-zA-Z0-9 ,]+ FROM [a-zA-Z0-9_]+";
        Assertions.assertTrue(contains(regex, input));
    }

    @Test
    public void sqlSelectNotFoundWhenKeywordLowercase() {
        String input = "slow_query: select id from users";
        String regex = "SELECT [a-zA-Z0-9 ,]+ FROM [a-zA-Z0-9_]+";
        Assertions.assertFalse(contains(regex, input));
    }

    @Test
    public void markdownHeaderFoundInDocument() {
        String input = "some paragraph text\n## Introduction\nmore text here";
        String regex = "## [a-zA-Z ]+";
        Assertions.assertTrue(contains(regex, input));
    }

    @Test
    public void markdownHeaderNotFoundWhenHashMissing() {
        String input = "some paragraph text\nIntroduction\nmore text here";
        String regex = "## [a-zA-Z ]+";
        Assertions.assertFalse(contains(regex, input));
    }

    @Test
    public void uuidFoundInErrorDump() {
        // simplified UUID: 8-4-4-4-12 hex digits
        String input  = "trace_id=550e8400-e29b-41d4-a716-446655440000 status=fail";
        String regex  = "[0-9a-f]{8}\\-[0-9a-f]{4}\\-[0-9a-f]{4}\\-[0-9a-f]{4}\\-[0-9a-f]{12}";
        Assertions.assertTrue(contains(regex, input));
    }

    @Test
    public void uuidNotFoundWhenSegmentTooShort() {
        // first segment only 7 hex chars
        String input = "trace_id=550e840-e29b-41d4-a716-446655440000 status=fail";
        String regex = "[0-9a-f]{8}\\-[0-9a-f]{4}\\-[0-9a-f]{4}\\-[0-9a-f]{4}\\-[0-9a-f]{12}";
        Assertions.assertFalse(contains(regex, input));
    }

    @Test
    public void uuidNotFoundWhenUppercase() {
        // UUID has uppercase — [0-9a-f] does not cover A-F
        String input = "trace_id=550E8400-E29B-41D4-A716-446655440000 status=fail";
        String regex = "[0-9a-f]{8}\\-[0-9a-f]{4}\\-[0-9a-f]{4}\\-[0-9a-f]{4}\\-[0-9a-f]{12}";
        Assertions.assertFalse(contains(regex, input));
    }

    @Test
    public void creditCardPatternFoundInPaymentLog() {
        // 4 groups of 4 digits separated by spaces
        String input  = "payment processed for card 4111 1111 1111 1111 amount 99.99";
        String regex  = "[0-9]{4} [0-9]{4} [0-9]{4} [0-9]{4}";
        Assertions.assertTrue(contains(regex, input));
    }

    @Test
    public void creditCardPatternNotFoundWhenDashSeparated() {
        String input = "payment processed for card 4111-1111-1111-1111";
        String regex = "[0-9]{4} [0-9]{4} [0-9]{4} [0-9]{4}";
        Assertions.assertFalse(contains(regex, input));
    }

    @Test
    public void repeatedGroupFoundInBase64Like() {
        // at least 8 repetitions of [A-Za-z0-9] groups of 4
        String input = "data: " + "ABcd".repeat(10) + " end";
        String regex = "([A-Za-z0-9]{4}){8,}";
        Assertions.assertTrue(contains(regex, input));
    }

    @Test
    public void repeatedGroupNotFoundWhenTooShort() {
        // only 3 repetitions of 4-char groups — below {8,}
        String input = "data: " + "ABcd".repeat(3) + " end";
        String regex = "([A-Za-z0-9]{4}){8,}";
        Assertions.assertFalse(contains(regex, input));
    }

    @Test
    public void htmlTagFoundInMarkup() {
        String input = "<html><body><h1>Hello World</h1></body></html>";
        String regex = "<[a-z]+>";
        Assertions.assertTrue(contains(regex, input));
    }

    @Test
    public void htmlTagNotFoundWhenUppercase() {
        String input = "<HTML><BODY></BODY></HTML>";
        String regex = "<[a-z]+>";
        Assertions.assertFalse(contains(regex, input));
    }

    @Test
    public void htmlTagNotFoundWhenSelfClosing() {
        // <br/> has a slash — [a-z]+ does not cover /
        String input = "<br/><hr/>";
        String regex = "<[a-z]+>";
        Assertions.assertFalse(contains(regex, input));
    }

    @Test
    public void envVariableAssignmentFound() {
        String input = "export DATABASE_URL=postgres://localhost:5432/mydb";
        String regex = "[A-Z_]+=[a-z0-9:/\\.]+";
        Assertions.assertTrue(contains(regex, input));
    }

    @Test
    public void envVariableNotFoundWhenNoEquals() {
        String input = "DATABASE URL postgres localhost 5432 mydb";
        String regex = "[A-Z_]+=";
        Assertions.assertFalse(contains(regex, input));
    }

    @Test
    public void needleAtVeryEndOfHugeInput() {
        String padding = "abcdefghij".repeat(200);
        String input   = padding + "FIND_ME_123";
        String regex   = "[A-Z_]+[0-9]{3}";
        Assertions.assertTrue(contains(regex, input));
    }

    @Test
    public void needleNeverAppearsInHugeInput() {
        String input = "abcdefghij".repeat(200);
        String regex = "[A-Z_]+[0-9]{3}";
        Assertions.assertFalse(contains(regex, input));
    }

    @Test
    public void overlappingCandidatesOnlyOneValid() {
        // "aab" contains "ab" but also "aab" — pattern "a{2}b" should match
        String input = "xyzaabxyz";
        Assertions.assertTrue(contains("a{2}b", input));
        Assertions.assertFalse(contains("a{3}b", input));
    }

    @Test
    public void adjacentMatchesPatternAppearsMultipleTimes() {
        // pattern "ab" appears 5 times back to back
        String input = "ababababab";
        Assertions.assertTrue(contains("ab", input));
        Assertions.assertTrue(contains("(ab){5}", input));
        Assertions.assertFalse(contains("(ab){6}", input));
    }

}
