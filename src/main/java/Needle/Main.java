package Needle;

import javax.xml.stream.events.EntityReference;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        try {

            String regex = "(a|b|x|y)*c+123?";
            Preprocessor preprocessor = new Preprocessor();
            List<Preprocessor.Pchar> preprocessed =  preprocessor.process(regex);
            Lexer lexer = new Lexer(preprocessed);
            List<Lexer.Token> tokens = lexer.scan();

            Parser parser = new Parser(tokens);
            AstNode root = parser.parse();


            NfaBuilder nfaBuilder = new NfaBuilder(root);
            NfaBuilder.NfaFragment graph = nfaBuilder.build();

            NfaFragmentPrinter  nfaFragmentPrinter = new NfaFragmentPrinter();
            nfaFragmentPrinter.print(graph);

            System.out.println(graph);


        }catch (Exception ex) {
            System.out.println(ex.getMessage());
        }



    }
}