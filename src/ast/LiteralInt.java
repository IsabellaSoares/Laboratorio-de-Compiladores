/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;

public class LiteralInt extends Expr {
    
    public LiteralInt( int value , Type type) {
    	super(type);
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    public void genJava( PW pw, boolean putParenthesis ) {
        pw.printIdent("" + value);
    }
    
	/*
	 * public Type getType() { return Type.intType; }
	 */
    
    private int value;
}
