package Needle;

import java.util.ArrayList;
import java.util.List;

public class Preprocessor {
    public static final class Pchar {
        public char c;
        public boolean isEscaped;
        public int charAt;

        public Pchar() {}
        public Pchar(char c, boolean isEscaped, int charAt) {
            this.c = c;
            this.isEscaped = isEscaped;
            this.charAt = charAt;
        }
    }

    public List<Pchar> process(String regex) {
        List<Pchar> pChars = new ArrayList<>();
        boolean isEscaped = false;
        for(int i = 0; i < regex.length(); i++) {
            char c = regex.charAt(i);
            if(c == '\\' && !isEscaped) {
                isEscaped = true;
                continue;
            }
            Pchar pchar = new Pchar(c, isEscaped, i);
            isEscaped = false;
            pChars.add(pchar);
        }
        return pChars;
    }
}