package Needle;

import java.util.List;

public class AstNode {

    public interface UnaryExpr {}

    public interface BinaryExpr {}

    public interface Atom {}

    // atom
    public static final class DotExpr extends AstNode implements Atom {
        public final Lexer.Token dot;

        public DotExpr(Lexer.Token dot) {
            this.dot = dot;
        }
    }

    public static final class CharExpr extends AstNode implements Atom {
        public final Lexer.Token atom;

        public CharExp(Lexer.Token atom) {
            this.atom = atom;
        }
    }

    public static final class BracketExpr extends AstNode implements Atom {
        public final List<BracketItem> items;
        public final boolean isNegated;

        public BracketExpr(List<BracketItem> items, boolean isNegated) {
            this.items = items;
            this.isNegated = isNegated;
        }


        public sealed interface BracketItem
                permits BracketChar, BracketRange {
        }

        public record BracketChar(char value) implements BracketItem {}

        public record BracketRange(char start, char end) implements BracketItem {}
    }

    public static final class GroupExpr extends AstNode implements Atom {
        public final AstNode expression;

        public GroupExpr(AstNode expression) {
            this.expression = expression;
        }
    }


    // unary expr
    public static final class StarExpr extends AstNode implements UnaryExpr {
        public final AstNode child;

        public StarExpr(AstNode child) {
            this.child = child;
        }
    }

    public static final class PlusExpr extends AstNode implements UnaryExpr {
        public final AstNode child;

        public PlusExpr(AstNode child) {
            this.child = child;
        }
    }

    public static final class OptionalExpr extends AstNode implements UnaryExpr {
        public final AstNode child;

        public OptionalExpr(AstNode child) {
            this.child = child;
        }
    }

    public static final class RepeatExpr extends AstNode implements UnaryExpr {
        public final AstNode child;
        public final int min;
        public final Integer max;

        public RepeatExpr(AstNode child, int min, Integer max) {
            this.child = child;
            this.min = min;
            this.max = max;
        }
    }

    // binary expr
    public static final class ConcatExpr extends AstNode {
        public final List<AstNode> parts;

        public ConcatExpr(List<AstNode> parts) {
            this.parts = parts;
        }
    }

    public static final class AlternationExpr extends AstNode {
        public final List<AstNode> alternatives;

        public AlternationExpr(List<AstNode> alternatives) {
            this.alternatives = alternatives;
        }
    }

}
