package nl.saxion.cos;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

public class Checker extends Pirate_CalligraphyBaseVisitor<DataType> {
    private ParseTreeProperty<DataType> types;
    private ParseTreeProperty<Symbol> symbols;

    private Scope currentScope;

    private DataType registerType(ParseTree node, DataType type) {
        types.put(node, type);
        return type;
    }

    private Symbol registerSymbol(ParseTree node, Token id) {
        var name = id.getText();
        var symbol = currentScope.lookupVariable(name);
        if (symbol == null) {
            reportSemanticError(id, "unknown variable " + name + " -> at node " + node.getText());
        }

        symbols.put(node, symbol);
        return symbol;
    }

    private Symbol registerMethodSymbol(ParseTree node, Token id, DataType returntype) {
        var symbol = new Symbol(id.getText(), returntype);

        symbols.put(node, symbol);
        return symbol;
    }

    public ParseTreeProperty<DataType> getTypes() {
        return this.types;
    }

    private void reportSemanticError(Token token, String message) {
        var where = "line " + token.getLine() + " col " + (token.getCharPositionInLine() + 1);
        throw new CompilerException(where + ": " + message);
    }

    public Checker(ParseTreeProperty<DataType> types, ParseTreeProperty<Symbol> symbols) {
        this.types = types;
        this.symbols = symbols;
        this.currentScope = new Scope(null);
    }

    @Override
    public DataType visitStart(Pirate_CalligraphyParser.StartContext ctx) {

        for (var mds : ctx.methodDeclarationStatement()) {
            visit(mds);
        }

        currentScope = currentScope.openScope();
        for (var statement : ctx.statement()) {
            visit(statement);
        }
        currentScope = currentScope.closeScope();
        return null;
    }

    @Override
    public DataType visitIfStatement(Pirate_CalligraphyParser.IfStatementContext ctx) {
        DataType type = visit(ctx.expression());
        if (type != DataType.BOOLEAN) {
            throw new CompilerException("Expected boolean condition");
        }
        currentScope = currentScope.openScope();
        for (var statement : ctx.statement()) {
            visit(statement);
        }
        currentScope = currentScope.closeScope();

        if (ctx.elseIfStatement() != null) {
            for (var elseif : ctx.elseIfStatement()) {
                visit(elseif);
            }
        }

        if (ctx.elseStatement() != null) {
            visit(ctx.elseStatement());
        }

        return null;
    }

    @Override
    public DataType visitElseIfStatement(Pirate_CalligraphyParser.ElseIfStatementContext ctx) {

        DataType type = visit(ctx.expression());
        if (type != DataType.BOOLEAN) {
            throw new CompilerException("Expected boolean condition");
        }
        currentScope = currentScope.openScope();
        for (var statement : ctx.statement()) {
            visit(statement);
        }
        currentScope = currentScope.closeScope();
        return null;
    }

    @Override
    public DataType visitElseStatement(Pirate_CalligraphyParser.ElseStatementContext ctx) {
        currentScope = currentScope.openScope();
        for (var statement : ctx.statement()) {
            visit(statement);
        }
        currentScope = currentScope.closeScope();
        return null;
    }

    @Override
    public DataType visitWhileStatement(Pirate_CalligraphyParser.WhileStatementContext ctx) {
        if (visit(ctx.expression()) != DataType.BOOLEAN) {
            throw new CompilerException("Must be aan boolean");
        }
        currentScope = currentScope.openScope();
        for (var statement : ctx.statement()) {
            visit(statement);
        }
        currentScope = currentScope.closeScope();
        return null;
    }

    @Override
    public DataType visitForStatement(Pirate_CalligraphyParser.ForStatementContext ctx) {
        currentScope = currentScope.openScope();
        visit(ctx.incremental);
        visit(ctx.compareStatement());
        visit(ctx.exp);

        for (var spul : ctx.statement()) {
            visit(spul);
        }

        currentScope = currentScope.closeScope();
        return null;
    }

    @Override
    public DataType visitSubstractOne(Pirate_CalligraphyParser.SubstractOneContext ctx) {
        Symbol symbol = currentScope.lookupVariable(ctx.ID().getText());
        symbols.removeFrom(ctx);
        registerSymbol(ctx, ctx.ID().getSymbol());
        registerType(ctx, symbol.getType());

        return symbol.getType();
    }

    @Override
    public DataType visitAddOne(Pirate_CalligraphyParser.AddOneContext ctx) {
        Symbol symbol = currentScope.lookupVariable(ctx.ID().getText());
        symbols.removeFrom(ctx);
        registerSymbol(ctx, ctx.ID().getSymbol());
        registerType(ctx, symbol.getType());

        return symbol.getType();
    }

    @Override
    public DataType visitMethodDeclarationStatement(Pirate_CalligraphyParser.MethodDeclarationStatementContext ctx) {
        DataType returnType;
        String privacy = "";

        if (ctx.PRIVACY() == null) {
            throw new CompilerException("No privacy given.");
        }

        if (ctx.returntype.getText().equals("number")) {
            returnType = DataType.INT;
        } else if (ctx.returntype.getText().equals("duplicate")) {
            returnType = DataType.DOUBLE;
        } else if (ctx.returntype.getText().equals("rope")) {
            returnType = DataType.STRING;
        } else if (ctx.returntype.getText().equals("nothing")) {
            returnType = DataType.VOID;
        } else {
            throw new CompilerException("No return type given." + ctx.toString());
        }

        currentScope.declareMethod(ctx.methodName.getText(), returnType);

        registerMethodSymbol(ctx.ID(), ctx.methodName, returnType);

        registerType(ctx, returnType);

        types.put(ctx.ID(), returnType);

        currentScope = currentScope.openScope();

        if (ctx.parameters() != null) {
            visit(ctx.parameters());
        }

        for (var statement : ctx.statement()) {
            visit(statement);
        }

        if (returnType != DataType.VOID) {
            visit(ctx.returnStatement());
        }

        currentScope = currentScope.closeScope();
        return null;
    }


    @Override
    public DataType visitMethodCall(Pirate_CalligraphyParser.MethodCallContext ctx) {
        Symbol s = currentScope.lookupVariable(ctx.ID().getText());
        symbols.put(ctx, s);
        if (ctx.parameters() != null) {
            visit(ctx.parameters());
        }
        return s.getReturntype();
    }

    @Override
    public DataType visitParameters(Pirate_CalligraphyParser.ParametersContext ctx) {
        for(var par: ctx.parameter()) {
            visit(par);
        }

        return null;
    }

    @Override
    public DataType visitParameter(Pirate_CalligraphyParser.ParameterContext ctx) {
        DataType type = null;

        if (ctx.expression() != null) {
            type = visit(ctx.expression());
            registerType(ctx, type);

            return type;
        }

        if (ctx.INT() != null) {
            type = DataType.INT;
        } else if (ctx.DOUBLE() != null) {
            type = DataType.DOUBLE;
        } else if (ctx.ID() != null) {
            type = DataType.STRING;
        } else if (ctx.BOOLEAN() != null) {
            type = DataType.BOOLEAN;
        }

        registerType(ctx, type);
        currentScope.declareVariable(ctx.ID().getText(), type);
        registerSymbol(ctx, ctx.ID().getSymbol());

        return type;
    }

    @Override
    public DataType visitReturnStatement(Pirate_CalligraphyParser.ReturnStatementContext ctx) {
        DataType t = visit(ctx.expression());
        registerType(ctx, t);
        return t;
    }

    @Override
    public DataType visitVariableDeclaration(Pirate_CalligraphyParser.VariableDeclarationContext ctx) {

        String name = ctx.ID().getText();
        DataType type = null;
        if (ctx.INT() != null) {
            type = DataType.INT;
        } else if (ctx.DOUBLE() != null) {
            type = DataType.DOUBLE;

        } else if (ctx.ID() != null) {
            type = DataType.STRING;
        } else if (ctx.BOOLEAN() != null) {
            type = DataType.BOOLEAN;
        }
        if (ctx.expression() != null) {
            if (visit(ctx.expression()) != type) {
                throw new CompilerException("Variable declaration needs to be the same type");
            }
        }

        currentScope.declareVariable(name, type);
        registerSymbol(ctx, ctx.ID().getSymbol());

        return null;

        //TODO be sure to add all types
    }

    @Override
    public DataType visitVariableReDeclaration(Pirate_CalligraphyParser.VariableReDeclarationContext ctx) {
        Symbol symbol = currentScope.lookupVariable(ctx.ID().getText());
        DataType type = visit(ctx.expression());
        registerType(ctx, type);
        if (type != symbol.getType()) {
            throw new CompilerException("Variable ReDeclaration needs to be the same type");
        }
        symbols.removeFrom(ctx);
        registerSymbol(ctx, ctx.ID().getSymbol());
        return null;
        //TODO fix symbol mess

    }

    @Override
    public DataType visitVariableName(Pirate_CalligraphyParser.VariableNameContext ctx) {
        Symbol symbol = currentScope.lookupVariable(ctx.ID().getText());
        symbols.removeFrom(ctx);
        registerSymbol(ctx, ctx.ID().getSymbol());
        return symbol.getType();
        //TODO fix symbol mess
    }

    @Override
    public DataType visitPrintStatement(Pirate_CalligraphyParser.PrintStatementContext ctx) {
        DataType type = visit(ctx.expression());

        if (currentScope.lookupVariable(ctx.expression().getText()) != null) {
            type = currentScope.lookupVariable(ctx.expression().getText()).getType();
        }

        registerType(ctx, type);
        return null;
    }

    @Override
    public DataType visitAdd(Pirate_CalligraphyParser.AddContext ctx) {
        DataType left = visit(ctx.left);
        DataType right = visit(ctx.right);

        if (left != right) {
            throw new CompilerException("You need to use the same types");
        }

        if (left == null) {
            registerType(ctx, right);
            return right;
        } else {
            registerType(ctx, left);
            return left;
        }
    }

    @Override
    public DataType visitParentheses(Pirate_CalligraphyParser.ParenthesesContext ctx) {
        return visit(ctx.expression());
    }

    @Override
    public DataType visitCompare(Pirate_CalligraphyParser.CompareContext ctx) {
        DataType left = visit(ctx.left);
        DataType right = visit(ctx.right);

        if (left != right) {
            throw new CompilerException("You need yo use the same types, you've submitted: " + left + " | " + right + " | " + ctx.getText());
        }

        return DataType.BOOLEAN;
    }

    @Override
    public DataType visitDivide(Pirate_CalligraphyParser.DivideContext ctx) {
        DataType left = visit(ctx.left);
        DataType right = visit(ctx.right);
        if (left != right) {
            throw new CompilerException("You need yo use the same types, dividing types of " + left + " WITH " + right + " | " + ctx.getText());
        }

        if (left == null) {
            registerType(ctx, right);
            return right;
        } else {
            registerType(ctx, left);
            return left;
        }
    }

    @Override
    public DataType visitId(Pirate_CalligraphyParser.IdContext ctx) {
        return DataType.STRING;
    }

    @Override
    public DataType visitMultiply(Pirate_CalligraphyParser.MultiplyContext ctx) {
        DataType left = visit(ctx.left);
        DataType right = visit(ctx.right);
        if (left != right) {
            throw new CompilerException("You need yo use the same types");
        }
        if (left == null) {
            registerType(ctx, right);
            return right;
        } else {
            registerType(ctx, left);
            return left;
        }
    }

    @Override
    public DataType visitSubstract(Pirate_CalligraphyParser.SubstractContext ctx) {
        DataType left = visit(ctx.left);
        DataType right = visit(ctx.right);
        if (left != right) {
            throw new CompilerException("You need yo use the same types");
        }
        if (left == null) {
            registerType(ctx, right);
            return right;
        } else {
            registerType(ctx, left);
            return left;
        }
    }

    @Override
    public DataType visitInt(Pirate_CalligraphyParser.IntContext ctx) {
        return DataType.INT;
    }

    @Override
    public DataType visitBoolean(Pirate_CalligraphyParser.BooleanContext ctx) {
        return DataType.BOOLEAN;
    }

    @Override
    public DataType visitDouble(Pirate_CalligraphyParser.DoubleContext ctx) {
        return DataType.DOUBLE;
    }

    @Override
    public DataType visitCompareStatement(Pirate_CalligraphyParser.CompareStatementContext ctx) {
        DataType left = visit(ctx.left);
        DataType right = visit(ctx.right);

        if (left != right) {
            throw new CompilerException("You need yo use the same types");
        }

        return DataType.BOOLEAN;
    }
}
