package Needle;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        String regex = "(a|b)*c?";
        Preprocessor preprocessor = new Preprocessor();
        List<Preprocessor.Pchar> preprocessed =  preprocessor.process(regex);
        Lexer lexer = new Lexer(preprocessed);
        List<Lexer.Token> tokens = lexer.scan();

        Parser parser = new Parser(tokens);
        AstNode root = parser.parse();

        System.out.println(root);

    }
}