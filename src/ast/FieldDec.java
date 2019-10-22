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
    
    public void genJava( PW pw ) {
		pw.println("TESTE");
	};
}
