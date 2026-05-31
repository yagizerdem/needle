package Needle;

import java.util.List;

public abstract class AstNode {

    public interface AstVisitor<R> {
        R visitDotExpr(DotExpr expr);
        R visitCharExpr(CharExpr expr);
        R visitBracketExpr(BracketExpr expr);
        R visitGroupExpr(GroupExpr expr);

        R visitStarExpr(StarExpr expr);
        R visitPlusExpr(PlusExpr expr);
        R visitOptionalExpr(OptionalExpr expr);
        R visitRepeatExpr(RepeatExpr expr);

        R visitConcatExpr(ConcatExpr expr);
        R visitAlternationExpr(AlternationExpr expr);
    }

    public interface UnaryExpr {}

    public interface BinaryExpr {}

    public interface Atom {}

    public abstract <R> R accept(AstVisitor<R> visitor);

    // atom
    public static final class DotExpr extends AstNode implements Atom {
        public final Lexer.Token dot;

        public DotExpr(Lexer.Token dot) {
            this.dot = dot;
        }

        @Override
        public <R> R accept(AstVisitor<R> visitor) {
            return visitor.visitDotExpr(this);
        }
    }

    public static final class CharExpr extends AstNode implements Atom {
        public final Lexer.Token atom;

        public CharExpr(Lexer.Token atom) {
            this.atom = atom;
        }

        @Override
        public <R> R accept(AstVisitor<R> visitor) {
            return visitor.visitCharExpr(this);
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

        public record BracketChar(char value) implements BracketItem {
            @Override
            public String toString() {
                return String.valueOf(value());
            }
        }

        public record BracketRange(char start, char end) implements BracketItem {
            @Override
            public String toString() {
                return String.valueOf(start()) + "-" + String.valueOf(end());
            }
        }

        @Override
        public <R> R accept(AstVisitor<R> visitor) {
            return visitor.visitBracketExpr(this);
        }
    }

    public static final class GroupExpr extends AstNode implements Atom {
        public final AstNode expression;

        public GroupExpr(AstNode expression) {
            this.expression = expression;
        }

        @Override
        public <R> R accept(AstVisitor<R> visitor) {
            return visitor.visitGroupExpr(this);
        }
    }


    // unary expr
    public static final class StarExpr extends AstNode implements UnaryExpr {
        public final AstNode child;

        public StarExpr(AstNode child) {
            this.child = child;
        }

        @Override
        public <R> R accept(AstVisitor<R> visitor) {
            return visitor.visitStarExpr(this);
        }
    }

    public static final class PlusExpr extends AstNode implements UnaryExpr {
        public final AstNode child;

        public PlusExpr(AstNode child) {
            this.child = child;
        }

        @Override
        public <R> R accept(AstVisitor<R> visitor) {
            return visitor.visitPlusExpr(this);
        }
    }

    public static final class OptionalExpr extends AstNode implements UnaryExpr {
        public final AstNode child;

        public OptionalExpr(AstNode child) {
            this.child = child;
        }

        @Override
        public <R> R accept(AstVisitor<R> visitor) {
            return visitor.visitOptionalExpr(this);
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

        @Override
        public <R> R accept(AstVisitor<R> visitor) {
            return visitor.visitRepeatExpr(this);
        }
    }

    // binary expr
    public static final class ConcatExpr extends AstNode {
        public final List<AstNode> parts;

        public ConcatExpr(List<AstNode> parts) {
            this.parts = parts;
        }

        @Override
        public <R> R accept(AstVisitor<R> visitor) {
            return visitor.visitConcatExpr(this);
        }
    }

    public static final class AlternationExpr extends AstNode {
        public final List<AstNode> alternatives;

        public AlternationExpr(List<AstNode> alternatives) {
            this.alternatives = alternatives;
        }

        @Override
        public <R> R accept(AstVisitor<R> visitor) {
            return visitor.visitAlternationExpr(this);
        }
    }

}
