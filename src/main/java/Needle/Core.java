package Needle;

import java.util.List;

public class Core {

    public boolean isExactMatch(String regex, String input) throws NeedleException {
        Preprocessor preprocessor = new Preprocessor();
        List<Preprocessor.Pchar> preprocessed =  preprocessor.process(regex);
        Lexer lexer = new Lexer(preprocessed);
        List<Lexer.Token> tokens = lexer.scan();

        Parser parser = new Parser(tokens);
        AstNode root = parser.parse();


        NfaBuilder nfaBuilder = new NfaBuilder(root);
        NfaBuilder.NfaFragment graph = nfaBuilder.build();

        NfaSimulation simulation = new NfaSimulation(graph);
        return simulation.matches(input);
    }

    public boolean contains(String regex, String input) throws NeedleException {
        Preprocessor preprocessor = new Preprocessor();
        List<Preprocessor.Pchar> preprocessed =  preprocessor.process(regex);
        Lexer lexer = new Lexer(preprocessed);
        List<Lexer.Token> tokens = lexer.scan();

        Parser parser = new Parser(tokens);
        AstNode root = parser.parse();


        NfaBuilder nfaBuilder = new NfaBuilder(root);
        NfaBuilder.NfaFragment graph = nfaBuilder.build();

        NfaSimulation simulation = new NfaSimulation(graph);
        return simulation.contains(input);
    }
}
