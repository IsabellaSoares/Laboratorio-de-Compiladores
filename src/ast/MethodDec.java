/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;
import java.util.ArrayList;

public class MethodDec extends Member {
	private String id;
	private Type type = Type.nullType;
	private ArrayList<Statement> statList = new ArrayList<>();
	//private ArrayList<Variable> paramList = new ArrayList<>();
	
	public MethodDec (String id, Type type, ArrayList<Statement> statList) {
		this.id = id;
		this.type = type;
		this.statList = statList;
		//this.paramList = paramList;
	}
	
	public void genC( PW pw ) {};
}