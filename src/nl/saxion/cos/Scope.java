package nl.saxion.cos;

import java.util.HashMap;

public class Scope {

    private final Scope parentScope;

    private int nextFreeSlot;

    private final HashMap<String, Symbol> symbols;

    public Scope(Scope parentScope) {
        this.parentScope = parentScope;
        nextFreeSlot = parentScope == null ? 0 : parentScope.getFreeSlot();
        symbols = new HashMap<>();
    }

    public Symbol declareVariable(String name, DataType type) {
        assert !symbols.containsKey(name);
        Symbol symbol = null;
        //TODO fix :)
        if (type == DataType.DOUBLE) {
            symbol = new Symbol(name, type,  nextFreeSlot++);
            nextFreeSlot++;
        } else {
            symbol = new Symbol(name, type, nextFreeSlot++);
        }

        symbols.put(name, symbol);
        return symbol;
    }

    public Symbol declareMethod(String name, DataType type) {

        Symbol symbol = null;
        symbol = new Symbol(name, type);

        symbols.put(name, symbol);
        return symbol;
    }

    public Symbol lookupVariable(String name) {
        var symbol = symbols.get(name);
        if (symbol == null && parentScope != null) {
            symbol = parentScope.lookupVariable(name);
        }
        return symbol;
    }

    public Scope openScope() {
        return new Scope(this);
    }

    public Scope closeScope() {
        return parentScope;
    }

    public int getFreeSlot() {
        return nextFreeSlot;
    }
}
