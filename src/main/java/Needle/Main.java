package Needle;

import javax.xml.stream.events.EntityReference;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        try {

            String regex = "\\w{3,10}";
            Preprocessor preprocessor = new Preprocessor();
            List<Preprocessor.Pchar> preprocessed =  preprocessor.process(regex);
            Lexer lexer = new Lexer(preprocessed);
            List<Lexer.Token> tokens = lexer.scan();

            Parser parser = new Parser(tokens);
            AstNode root = parser.parse();

            AstPrinter printer = new AstPrinter();
            String src = root.accept(printer);

            System.out.println(src);
        }catch (Exception ex) {
            System.out.println(ex.getMessage());
        }



    }
}