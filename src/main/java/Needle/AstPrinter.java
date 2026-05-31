package Needle;


public class AstPrinter implements AstNode.AstVisitor<String> {

    @Override
    public String visitDotExpr(AstNode.DotExpr expr) {
        return expr.dot.getRawLexeme();
    }

    @Override
    public String visitCharExpr(AstNode.CharExpr expr) {
        return expr.atom.getRawLexeme();
    }

    @Override
    public String visitBracketExpr(AstNode.BracketExpr expr) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");

        if(expr.isNegated) {
            builder.append("^");
        }

        for(AstNode.BracketExpr.BracketItem item :  expr.items) {
            builder.append(item.toString());
        }

        builder.append("]");
        return builder.toString();
    }

    @Override
    public String visitGroupExpr(AstNode.GroupExpr expr) {
        return "(" + expr.expression.accept(this) + ")";
    }

    @Override
    public String visitStarExpr(AstNode.StarExpr expr) {
        StringBuilder builder = new StringBuilder();
        builder.append(expr.child.accept(this));
        builder.append("*");
        return builder.toString();
    }

    @Override
    public String visitPlusExpr(AstNode.PlusExpr expr) {
        StringBuilder builder = new StringBuilder();
        builder.append(expr.child.accept(this));
        builder.append("+");
        return builder.toString();
    }

    @Override
    public String visitOptionalExpr(AstNode.OptionalExpr expr) {
        StringBuilder builder = new StringBuilder();
        builder.append(expr.child.accept(this));
        builder.append("?");
        return builder.toString();
    }

    @Override
    public String visitRepeatExpr(AstNode.RepeatExpr expr) {
        StringBuilder builder = new StringBuilder();

        builder.append(expr.child.accept(this));
        builder.append("{");
        builder.append(expr.min);

        if (expr.max == null) {
            builder.append(",");
        } else if (!expr.max.equals(expr.min)) {
            builder.append(",");
            builder.append(expr.max);
        }

        builder.append("}");

        return builder.toString();
    }

    @Override
    public String visitConcatExpr(AstNode.ConcatExpr expr) {
        StringBuilder builder = new StringBuilder();
        expr.parts.forEach(p -> builder.append(p.accept(this)));
        return builder.toString();
    }

    @Override
    public String visitAlternationExpr(AstNode.AlternationExpr expr) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < expr.alternatives.size(); i++) {
            builder.append(expr.alternatives.get(i).accept(this));
            if (i < expr.alternatives.size() - 1) {
                builder.append("|");
            }
        }
        return builder.toString();
    }

}