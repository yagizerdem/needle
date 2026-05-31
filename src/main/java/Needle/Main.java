package Needle;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        String regex = "(a|b)*c";

        Preprocessor preprocessor = new Preprocessor();
        List<Preprocessor.Pchar> processed = preprocessor.process(regex);




    }
}