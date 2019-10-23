/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;

public class LiteralString extends Expr {
    
    public LiteralString( String literalString , int type) {
    	super(type);
        this.literalString = literalString;
    }
    
    public void genJava( PW pw, boolean putParenthesis ) {
        pw.print(literalString + "literalstring");
    }

    
    public Type getType() {
        return Type.stringType;
    }
    
    private String literalString;
}
