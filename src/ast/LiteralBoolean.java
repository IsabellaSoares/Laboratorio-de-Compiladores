/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;

public class LiteralBoolean extends Expr {

    public LiteralBoolean( boolean value , Type type) {
    	super(type);
        this.value = value;
    }

    @Override
	public void genJava( PW pw, boolean putParenthesis ) {
       pw.print( value ? "1" : "0" );
    }


	/*
	 * @Override public Type getType() { return Type.booleanType; }
	 */

    public static LiteralBoolean True  = new LiteralBoolean(true, new TypeBoolean());
    public static LiteralBoolean False = new LiteralBoolean(false, new TypeBoolean());

    private boolean value;

}
