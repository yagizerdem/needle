package Needle;

import java.util.List;

public class Core {

    public NfaBuilder.NfaFragment compile(String regex) throws NeedleException {
        Preprocessor preprocessor = new Preprocessor();
        List<Preprocessor.Pchar> preprocessed =  preprocessor.process(regex);
        Lexer lexer = new Lexer(preprocessed);
        List<Lexer.Token> tokens = lexer.scan();

        Parser parser = new Parser(tokens);
        AstNode root = parser.parse();


        NfaBuilder nfaBuilder = new NfaBuilder(root);
        return nfaBuilder.build();
    }

    public boolean isExactMatch(NfaBuilder.NfaFragment graph, String input) {
        NfaSimulation simulation = new NfaSimulation(graph);
        return simulation.matches(input);
    }

    public boolean contains(NfaBuilder.NfaFragment graph, String input) {
        NfaSimulation simulation = new NfaSimulation(graph);
        return simulation.contains(input);
    }
}
