/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

import java.util.ArrayList;

/**
 *
 * @author marce
 */
public class FieldDec extends Member {
    private Type type;
    private ArrayList<String> idList;
    
    public FieldDec(Type type, ArrayList<String> idList){
        this.type = type;
        this.idList = idList;
    }
    
    public Type getType() {
		return type;
	}
    
    public void genJava( PW pw ) {
		pw.printIdent(type.getName() + " ");
		
		if (idList != null) {
			for (int i = 0; i < idList.size(); i++) {
				if(i > 0) {
					pw.print(", ");
				}
				
				pw.print(idList.get(i).toString());
			}
		}
		
		pw.println(";");
	};
}
