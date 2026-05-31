package Needle;

import java.util.ArrayList;
import java.util.List;

public class NfaBuilder  implements AstNode.AstVisitor<NfaBuilder.NfaFragment> {

    public static final class NfaNode {
        public List<Transition> transitions = new ArrayList<>();
    }


    public enum TransitionType {
        ANY,
        CHAR,
        EPS
    }

    public static final class Transition {
        public NfaNode from;
        public NfaNode to;
        public TransitionType type;
        public List<Character> accepts = new ArrayList<>();
    }

    public static final class NfaFragment {
        public final NfaNode start;
        public final NfaNode end;

        public NfaFragment(NfaNode start, NfaNode end) {
            this.start = start;
            this.end = end;
        }
    }


    public AstNode ast;

    public NfaBuilder(AstNode ast) {
        this.ast = ast;
    }

    public NfaFragment build() {
        NfaNode start = new NfaNode();
        NfaNode end = new NfaNode();
        return this.ast.accept(this);
    }


    @Override
    public NfaFragment visitDotExpr(AstNode.DotExpr expr) {
        NfaNode start = new NfaNode();
        NfaNode end = new NfaNode();

        Transition transition = new Transition();
        transition.from = start;
        transition.to = end;
        transition.type = TransitionType.ANY;

        start.transitions.add(transition);

        return new NfaFragment(start, end);
    }

    @Override
    public NfaFragment visitCharExpr(AstNode.CharExpr expr) {
        NfaNode start = new NfaNode();
        NfaNode end = new NfaNode();

        Transition transition = new Transition();
        transition.from = start;
        transition.to = end;
        transition.type = TransitionType.CHAR;

        if(expr.atom.getRawLexeme() != null) {
            transition.accepts.add(expr.atom.getRawLexeme().charAt(0));
        }

        start.transitions.add(transition);

        return new NfaFragment(start, end);
    }

    @Override
    public NfaFragment visitBracketExpr(AstNode.BracketExpr expr) {
        NfaNode start = new NfaNode();
        NfaNode end = new NfaNode();

        Transition transition = new Transition();
        transition.from = start;
        transition.to = end;
        transition.type = TransitionType.CHAR;

        List<Character> chars = new ArrayList<>();

        for (AstNode.BracketExpr.BracketItem item : expr.items) {
            if (item instanceof AstNode.BracketExpr.BracketChar ch) {
                chars.add(ch.value());
            } else if (item instanceof AstNode.BracketExpr.BracketRange range) {
                for (char c = range.start(); c <= range.end(); c++) {
                    chars.add(c);
                }
            }
        }

        if (expr.isNegated) {
            for (char c = 0; c < 128; c++) {
                if (!chars.contains(c)) {
                    transition.accepts.add(c);
                }
            }
        } else {
            transition.accepts.addAll(chars);
        }

        start.transitions.add(transition);

        return new NfaFragment(start, end);
    }

    @Override
    public NfaFragment visitGroupExpr(AstNode.GroupExpr expr) {
        return expr.expression.accept(this);
    }

    @Override
    public NfaFragment visitStarExpr(AstNode.StarExpr expr) {
        NfaNode start = new NfaNode();
        NfaNode end = new NfaNode();
        NfaFragment child = expr.child.accept(this);
        addEps(start, child.start);
        addEps(start, end);
        addEps(child.end, child.start);
        addEps(child.end, end);
        return new NfaFragment(start, end);
    }

    @Override
    public NfaFragment visitPlusExpr(AstNode.PlusExpr expr) {
        NfaNode start = new NfaNode();
        NfaNode end = new NfaNode();
        NfaFragment child = expr.child.accept(this);
        addEps(start, child.start);
        addEps(child.end, child.start);
        addEps(child.end, end);
        return new NfaFragment(start, end);
    }

    @Override
    public NfaFragment visitOptionalExpr(AstNode.OptionalExpr expr) {
        NfaNode start = new NfaNode();
        NfaNode end = new NfaNode();
        NfaFragment child = expr.child.accept(this);
        addEps(start, child.start);
        addEps(child.end, end);
        addEps(start, end);
        return new NfaFragment(start, end);
    }

    @Override
    public NfaFragment visitRepeatExpr(AstNode.RepeatExpr expr) {
        NfaNode start = new NfaNode();
        NfaNode end = new NfaNode();

        NfaFragment current = new NfaFragment(start, start);

        for (int i = 0; i < expr.min; i++) {
            NfaFragment child = expr.child.accept(this);

            addEps(current.end, child.start);
            current = new NfaFragment(current.start, child.end);
        }

        // {m,}
        if (expr.max == null) {
            NfaFragment star = new AstNode.StarExpr(expr.child).accept(this);

            addEps(current.end, star.start);
            addEps(star.end, end);

            return new NfaFragment(start, end);
        }

        // {m,n}
        int optionalCount = expr.max - expr.min;

        for (int i = 0; i < optionalCount; i++) {
            NfaFragment optional = new AstNode.OptionalExpr(expr.child).accept(this);

            addEps(current.end, optional.start);
            current = new NfaFragment(current.start, optional.end);
        }

        addEps(current.end, end);

        return new NfaFragment(start, end);
    }

    @Override
    public NfaFragment visitConcatExpr(AstNode.ConcatExpr expr) {
        if (expr.parts.isEmpty()) {
            NfaNode start = new NfaNode();
            NfaNode end = new NfaNode();
            addEps(start, end);
            return new NfaFragment(start, end);
        }

        NfaFragment current = expr.parts.getFirst().accept(this);

        for (int i = 1; i < expr.parts.size(); i++) {
            NfaFragment next = expr.parts.get(i).accept(this);

            addEps(current.end, next.start);

            current = new NfaFragment(current.start, next.end);
        }

        return current;
    }

    @Override
    public NfaFragment visitAlternationExpr(AstNode.AlternationExpr expr) {
        NfaNode start = new NfaNode();
        NfaNode end = new NfaNode();
        NfaFragment cur = new NfaFragment(start, end);

        if (expr.alternatives.isEmpty()) {
            addEps(start, end);
            return cur;
        }

        NfaFragment left = expr.alternatives.getFirst().accept(this);
        addEps(cur.start, left.start);
        addEps(left.end, cur.end);

        for (int i = 1; i < expr.alternatives.size(); i++) {
            NfaFragment right = expr.alternatives.get(i).accept(this);

            addEps(cur.start, right.start);
            addEps(right.end, cur.end);
        }

        return cur;
    }

    // helpers
    private void addEps(NfaNode from, NfaNode to) {
        Transition transition = new Transition();
        transition.from = from;
        transition.to = to;
        transition.type = TransitionType.EPS;
        from.transitions.add(transition);
    }

    private void addCharTransition(NfaNode from, NfaNode to, List<Character> acceptes) {
        Transition transition = new Transition();
        transition.from = from;
        transition.to = to;
        transition.type = TransitionType.CHAR;
        transition.accepts = acceptes;
        from.transitions.add(transition);
    }



}
