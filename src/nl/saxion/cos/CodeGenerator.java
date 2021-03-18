package nl.saxion.cos;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

public class CodeGenerator extends Pirate_CalligraphyBaseVisitor<Void> {
    private JasminBytecode jasminCode;
    private ParseTreeProperty<DataType> types;
    private ParseTreeProperty<Symbol> symbols;

    private int unique = 0;

    public CodeGenerator(JasminBytecode jasminCode, ParseTreeProperty<DataType> types, ParseTreeProperty<Symbol> symbols) {
        this.jasminCode = jasminCode;
        this.symbols = symbols;
        this.types = types;
    }

    private String newLabel() {
        return "L" + ++unique;
    }

    @Override
    public Void visitStart(Pirate_CalligraphyParser.StartContext ctx) {
        for (var mds : ctx.methodDeclarationStatement()) {
            visit(mds);
        }

        if (ctx.BEGIN() != null) {
            // Main method
            jasminCode.add(".method public static main([Ljava/lang/String;)V");
            jasminCode.add(".limit stack 99");
            jasminCode.add(".limit locals 99");
            jasminCode.add("");
            //Generate code to evaluate expression

            for (var statement : ctx.statement()) {
                visit(statement);
            }

            //Print top stack
            // TODO: Make sure the value is printed here!

            jasminCode.add("return");
            jasminCode.add(".end method");

        }
        return null;
    }

    @Override
    public Void visitPrintStatement(Pirate_CalligraphyParser.PrintStatementContext ctx) {
        jasminCode.add("getstatic java/lang/System/out Ljava/io/PrintStream;");

        visit(ctx.expression());

        DataType type = types.get(ctx);

        if (type == DataType.INT) {
            jasminCode.add("invokevirtual java/io/PrintStream/println(I)V");
        } else if (type == DataType.DOUBLE) {
            jasminCode.add("invokevirtual java/io/PrintStream/println(D)V");
        } else if (type == DataType.STRING) {
            jasminCode.add("invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V");
        } else if (type == DataType.BOOLEAN) {
            jasminCode.add("invokevirtual java/io/PrintStream/println(I)V");
        } else {
            throw new CompilerException("Unknown type: " + ctx.expression().getText());
        }

        return null;
    }

    @Override
    public Void visitIfStatement(Pirate_CalligraphyParser.IfStatementContext ctx) {

        visit(ctx.expression());

        // expression equals true

        String ifFalseLabel = newLabel();
        String ifDoneLabel = newLabel();

        jasminCode.add("ifeq " + ifFalseLabel);

        for (var statement : ctx.statement()) {
            visit(statement);
        }

        jasminCode.add("ldc 1");
        jasminCode.add("goto " + ifDoneLabel);
        jasminCode.add(ifFalseLabel + ":");
        jasminCode.add("ldc 0");
        jasminCode.add(ifDoneLabel + ":");

        // expression equals false

        String elseFalseLabel = newLabel();
        String elseCloseLabel = newLabel();

        jasminCode.add("ifne " + elseFalseLabel);

        for (var statement : ctx.elseIfStatement()) {
            visit(statement);
        }

        if (ctx.elseStatement() != null) {
            if (ctx.elseIfStatement().size() != 0) {
                jasminCode.add("ifne " + elseFalseLabel);
            }
            visit(ctx.elseStatement());
        }

        jasminCode.add("goto " + elseCloseLabel);
        jasminCode.add(elseFalseLabel + ":");
        jasminCode.add(elseCloseLabel + ":");

        return null;
    }

    @Override
    public Void visitElseIfStatement(Pirate_CalligraphyParser.ElseIfStatementContext ctx) {
        for (var statements : ctx.statement()) {
            visit(statements);
        }
        return null;
    }

    @Override
    public Void visitElseStatement(Pirate_CalligraphyParser.ElseStatementContext ctx) {
        for (var statements : ctx.statement()) {
            visit(statements);
        }
        return null;
    }

    @Override
    public Void visitWhileStatement(Pirate_CalligraphyParser.WhileStatementContext ctx) {
        String test = newLabel();
        String done = newLabel();

        jasminCode.add(test + ":");
        visit(ctx.expression());
        jasminCode.add("ifeq " + done);
        for (var statement : ctx.statement()) {
            visit(statement);
        }
        jasminCode.add("goto " + test);
        jasminCode.add(done + ":");
        jasminCode.add();

        return null;
    }

    @Override
    public Void visitForStatement(Pirate_CalligraphyParser.ForStatementContext ctx) {
        String loop = newLabel();
        String done = newLabel();

        // creating label for the loop function
        visit(ctx.incremental);

        jasminCode.add();
        jasminCode.add(loop + ":");

        // loading variables needed for equation
        //visit(ctx.condition);

        // equation (if true go to done label)
        visit(ctx.compareStatement().left);
        visit(ctx.compareStatement().right);
        jasminCode.add("if_icmp" + getOpposite(ctx.compareStatement().op.getText()) + " " + done);

        // if equation not true do the expression needed for the loop.

        // visit al the statements.
        for (var statement : ctx.statement()) {
            visit(statement);
        }

        visit(ctx.exp);

        jasminCode.add("goto " + loop);
        jasminCode.add(done + ":");
        jasminCode.add();

        return null;
    }

    @Override
    public Void visitMethodDeclarationStatement(Pirate_CalligraphyParser.MethodDeclarationStatementContext ctx) {

        DataType returnType = types.get(ctx);

        String privacy = "";
        String methodName = ctx.methodName.getText();

        switch (ctx.PRIVACY().getText()) {
            case "loud":
                privacy = "public";
                break;

            case "covered":
                privacy = "protected";
                break;

            case "quite":
                privacy = "private";
                break;
        }

        // parameters
        StringBuilder parameters = new StringBuilder();
        if (ctx.parameters() != null) {
            int a = 0;
            for (var parameter : ctx.parameters().parameter()) {
                switch (parameter.type.getText()) {
                    case "rope":
                        parameters.append("Ljava/lang/String;");
                        break;

                    case "number":
                        parameters.append("I");
                        break;

                    case "duplicate":
                        parameters.append("D");
                        break;
                }

                if (!parameter.type.getText().equals("rope") && a > 0) {
                    parameters.append(';');
                }
                a++;
            }
        }

        String returnTypeString = "V";

        if (returnType != null) {
            switch (returnType) {
                case STRING:
                    returnTypeString = "Ljava/lang/String;";
                    break;

                case INT:
                    returnTypeString = "I";
                    break;

                case DOUBLE:
                    returnTypeString = "D";
                    break;
            }
        }

        jasminCode.add(".method " + privacy + " static " + methodName + "(" + parameters + ")" + returnTypeString);

        jasminCode.add(".limit stack 99");
        jasminCode.add(".limit locals 99");

        for (var statement : ctx.statement()) {
            visit(statement);
        }

        if (returnType != DataType.VOID) {
            if (ctx.returnStatement() != null) {
                visit(ctx.returnStatement());
            } else {
                throw new CompilerException("No return statement.");
            }
        } else {
            jasminCode.add("return");
        }

        jasminCode.add(".end method");
        jasminCode.add("");

        return null;
    }

    @Override
    public Void visitReturnStatement(Pirate_CalligraphyParser.ReturnStatementContext ctx) {
        DataType t = types.get(ctx);
        visit(ctx.expression());

        switch (t) {
            case STRING:
                jasminCode.add("areturn");
                break;

            case DOUBLE:
                jasminCode.add("dreturn");
                break;

            case INT:
                jasminCode.add("ireturn");
                break;
        }

        return null;
    }

    @Override
    public Void visitMethodCall(Pirate_CalligraphyParser.MethodCallContext ctx) {
        Symbol s = symbols.get(ctx);

        String methodName = ctx.ID().getText();

        // parameters
        StringBuilder parameters = new StringBuilder();
        if (ctx.parameters() != null) {
            int i = 0;
            for (var parameter : ctx.parameters().parameter()) {
                visit(parameter);

                switch (types.get(parameter)) {
                    case STRING:
                        parameters.append("[Ljava/lang/String;");
                        break;

                    case INT:
                        parameters.append("I");
                        break;

                    case DOUBLE:
                        parameters.append("D");
                        break;
                }
                if (types.get(parameter) != DataType.STRING && i > 0) {
                    parameters.append(';');
                }
                i++;
            }
        }


        String returntype = "";

        switch (s.getReturntype()) {
            case STRING:
                returntype = "[Ljava/lang/String";
                break;

            case INT: returntype = "I";
                break;

            case DOUBLE:
                returntype = "D";
            break;

            case VOID:
                returntype = "V";
                break;
        }

        jasminCode.add("invokestatic main/" + methodName + "(" + parameters + ")" + returntype);
        return null;
    }

    @Override
    public Void visitVariableDeclaration(Pirate_CalligraphyParser.VariableDeclarationContext ctx) {
        DataType type = null;

        if (ctx.INT() != null) {
            type = DataType.INT;
        } else if (ctx.DOUBLE() != null) {
            type = DataType.DOUBLE;
        } else if (ctx.STRING() != null) {
            type = DataType.STRING;
        } else if (ctx.BOOLEAN() != null) {
            type = DataType.BOOLEAN;
        }

        boolean empty = true;
        if (ctx.expression() != null) {
            visit(ctx.expression());
            empty = false;
        }

        Symbol symbol = symbols.get(ctx);

        if (type == DataType.INT) {
            if (empty) {
                jasminCode.add("iconst_m1"); //Empty int
            }
            jasminCode.add("istore " + symbol.getIndex());
        } else if (type == DataType.DOUBLE) {
            if (empty) {
                jasminCode.add("dconst_0"); //Empty double
            }
            jasminCode.add("dstore " + symbol.getIndex());
        } else if (type == DataType.STRING) {
            if (empty) {
                jasminCode.add("ldc \"$empty\""); //Empty string
            }
            jasminCode.add("astore " + symbol.getIndex());
        }
        return null;
    }

    @Override
    public Void visitVariableReDeclaration(Pirate_CalligraphyParser.VariableReDeclarationContext ctx) {
        visit(ctx.expression());
        DataType type = types.get(ctx);
        Symbol symbol = symbols.get(ctx);

        if (type == DataType.INT) {
            jasminCode.add("istore " + symbol.getIndex());
        } else if (type == DataType.DOUBLE) {
            jasminCode.add("dstore " + symbol.getIndex());
        } else if (type == DataType.STRING) {
            jasminCode.add("astore " + symbol.getIndex());
        }
        return null;
    }

    @Override
    public Void visitVariableName(Pirate_CalligraphyParser.VariableNameContext ctx) {

        Symbol symbol = symbols.get(ctx);

        if (symbol != null) {
            switch (symbol.getType()) {
                case INT:
                case BOOLEAN:
                    jasminCode.add("iload " + symbol.getIndex());
                    break;
                case DOUBLE:
                    jasminCode.add("dload " + symbol.getIndex());
                    break;
                case STRING:
                    jasminCode.add("aload " + symbol.getIndex());
                    break;
                default:
                    throw new CompilerException("Error in: " + symbol.toString());
            }
        }
        return null;
    }

    @Override
    public Void visitAdd(Pirate_CalligraphyParser.AddContext ctx) {
        visit(ctx.left);
        visit(ctx.right);

        DataType type = types.get(ctx);
        if (type == DataType.INT) {
            jasminCode.add("iadd");
        } else if (type == DataType.DOUBLE) {
            jasminCode.add("dadd");
        } else {
            System.out.println("ADD | Wrong datatype: " + type + " -> in " + ctx.getText());
        }
        //TODO add more Types

        return null;
    }

    @Override
    public Void visitParentheses(Pirate_CalligraphyParser.ParenthesesContext ctx) {
        visit(ctx.expression());
        return null;
    }

    @Override
    public Void visitBoolean(Pirate_CalligraphyParser.BooleanContext ctx) {
        if (ctx.BOOLEAN().getText().equals("aye")) {
            jasminCode.add("iconst_1");
        } else {
            jasminCode.add("iconst_0");
        }
        return null;
    }

    @Override
    public Void visitDouble(Pirate_CalligraphyParser.DoubleContext ctx) {
        jasminCode.add("ldc2_w " + ctx.DOUBLE_VALUE().getText());
        return null;
    }

    @Override
    public Void visitDivide(Pirate_CalligraphyParser.DivideContext ctx) {
        // Push left expression value
        visit(ctx.expression(0));
        // Push right expression value
        visit(ctx.expression(1));

        DataType type = types.get(ctx);
        if (type == DataType.INT) {
            jasminCode.add("irem");
        } else if (type == DataType.DOUBLE) {
            jasminCode.add("drem");
        } else {
            System.out.println("DIVIDE | Wrong datatype: " + type + " -> in " + ctx.getText());
        }

        //TODO add more Types

        return null;
    }

    @Override
    public Void visitId(Pirate_CalligraphyParser.IdContext ctx) {
        jasminCode.add("ldc " + ctx.ID_HOOKS().getText());
        return null;
    }

    @Override
    public Void visitMultiply(Pirate_CalligraphyParser.MultiplyContext ctx) {
        // Push left expression value
        visit(ctx.expression(0));
        // Push right expression value
        visit(ctx.expression(1));

        DataType type = types.get(ctx);
        if (type == DataType.INT) {
            jasminCode.add("imul");
        } else if (type == DataType.DOUBLE) {
            jasminCode.add("dmul");
        } else {
            System.out.println("MULTIPLY | Wrong datatype: " + type + " -> in " + ctx.getText());
        }
        //TODO add more Types

        return null;
    }

    @Override
    public Void visitInt(Pirate_CalligraphyParser.IntContext ctx) {
        jasminCode.add("iconst_" + ctx.INT_VALUE().getText());
        return null;
    }

    @Override
    public Void visitSubstract(Pirate_CalligraphyParser.SubstractContext ctx) {
        visit(ctx.left);
        visit(ctx.right);

        DataType type = types.get(ctx);
        if (type == DataType.INT) {
            jasminCode.add("isub");
        } else if (type == DataType.DOUBLE) {
            jasminCode.add("dsub");
        } else {
            System.out.println("SUBSTRACT | Wrong datatype: " + type + " -> in " + ctx.getText());
        }

        //TODO add more Types

        return null;
    }

    @Override
    public Void visitCompare(Pirate_CalligraphyParser.CompareContext ctx) {
        visit(ctx.left);
        visit(ctx.right);

        String falseLabel = newLabel(), doneLabel = newLabel();
        jasminCode.add("if_icmp" + getOpposite(ctx.op.getText()) + " " + falseLabel);
        jasminCode.add("ldc 1");
        jasminCode.add("goto " + doneLabel);
        jasminCode.add(falseLabel + ":");
        jasminCode.add("ldc 0");
        jasminCode.add(doneLabel + ":");
        return null;
    }

    public static String getOpposite(String inputOp) {
        switch (inputOp) {
            case "~~":
                return "ne"; // ==
            case "!~":
                return "eq"; // !=
            case "above":
                return "le";
            case "below":
                return "ge";
        }
        return null;
    }

    @Override
    public Void visitAddOne(Pirate_CalligraphyParser.AddOneContext ctx) {
        Symbol symbol = symbols.get(ctx);

        jasminCode.add("iinc " + symbol.getIndex() + " 1");
        return null;
    }

    @Override
    public Void visitSubstractOne(Pirate_CalligraphyParser.SubstractOneContext ctx) {
        Symbol symbol = symbols.get(ctx);

        jasminCode.add("iinc " + symbol.getIndex() + " -1");
        return null;
    }

    @Override
    public Void visitCompareStatement(Pirate_CalligraphyParser.CompareStatementContext ctx) {

        if (ctx.op.getText().equals("||")) {
            System.out.println("shit");
        }

        visit(ctx.left);
        visit(ctx.right);

        String falseLabel = newLabel(), doneLabel = newLabel();
        jasminCode.add("if_icmp" + getOpposite(ctx.op.getText()) + " " + falseLabel);
        jasminCode.add("ldc 1");
        jasminCode.add("goto " + doneLabel);
        jasminCode.add(falseLabel + ":");
        jasminCode.add("ldc 0");
        jasminCode.add(doneLabel + ":");
        return null;
    }
}

