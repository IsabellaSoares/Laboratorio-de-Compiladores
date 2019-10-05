/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;
import java.util.ArrayList;

public class RepeatStat extends Statement {
	private Expr expr = null;
	private ArrayList<Statement> statList = new ArrayList<>();
	
	public RepeatStat (ArrayList<Statement> statList, Expr expr) {
		this.statList = statList;
		this.expr = expr;
	}
	
	public void genJava( PW pw ) {};
}