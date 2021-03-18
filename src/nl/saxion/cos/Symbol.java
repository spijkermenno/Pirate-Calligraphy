package nl.saxion.cos;

public class Symbol {

    private final String name;

    private DataType type;

    private DataType returntype;

    private int index;

    public Symbol(String name, DataType type, int index) {
        this.name = name;
        this.type = type;
        this.index = index;
    }

    public Symbol(String name, DataType returntype) {
        this.name = name;
        this.returntype = returntype;
    }


    public String getName() {
        return name;
    }

    public DataType getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }



    @Override
    public String toString() {
        return "Name: " + this.getName() + "\n Type: " + this.getType()+ "\n returnType: " + this.getReturntype() + "\n Index: " + this.getIndex();
    }

    public DataType getReturntype() {
        return returntype;
    }
}
