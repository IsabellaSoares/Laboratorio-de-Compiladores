/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;

abstract public class Type {

    public Type( String name ) {
        this.name = name;
    }

    public static Type booleanType = new TypeBoolean();
    public static Type intType = new TypeInt();
    public static Type stringType = new TypeString();
    public static Type undefinedType = new TypeUndefined();
    public static Type nullType = new TypeNull();

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
    	this.name = name;
    }

    abstract public String getCname();

    private String name;
}
