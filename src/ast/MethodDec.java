/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;
import java.util.ArrayList;

public class MethodDec extends Member {
	private Variable method;
	private ArrayList<Statement> statList = new ArrayList<>();
	private ArrayList<Variable> paramList = new ArrayList<>();
	
	public MethodDec (Variable method, ArrayList<Statement> statList, ArrayList<Variable> paramList) {
		this.method = method;
		this.statList = statList;
		this.paramList = paramList;
	}
	
	public void genJava( PW pw ) {};
}