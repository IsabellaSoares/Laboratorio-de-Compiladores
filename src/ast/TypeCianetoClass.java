/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * Krakatoa Class
 */
public class TypeCianetoClass extends Type {

   public TypeCianetoClass( String name ) {
      super(name);
   }
	
	/*
	 * public TypeCianetoClass(boolean openClass, String name, String superName,
	 * MemberList memberList) { super(name); this.openClass = openClass; this.name =
	 * name; this.superName = superName; this.memberList = memberList; }
	 */
	
	public TypeCianetoClass(boolean openClass, String name, String superName, TypeCianetoClass superclass, List<MemberList> memberList) {
	    super(name);
		this.openClass = openClass;
		this.name = name;
		this.superName = superName;
		this.superclass = superclass;
		this.memberList = memberList;
	}

	public void setMethodList(HashMap<String, MethodDec> methodList) {
		this.methodList.putAll(methodList);
	}
	
	public MethodDec getMethod(String id) {
		MethodDec methodDec = methodList.get(id);
		if(methodDec!=null) {
			return methodDec;
		} else if (superclass!=null) {
			return superclass.getMethod(id);
		}
		return null;
	}
	
   @Override
   public String getCname() {
      return getName();
   }
   
   public void genJava (PW pw) {
	   pw.printlnIdent("private static class " + this.getCname() + " {");
	   
	   System.out.println("SUPER CLASS NAME");
	   System.out.println(superName);
		
		for(MemberList ml : memberList) {
			if (ml != null) {
				pw.add();			
				ml.genJava(pw);									
				pw.sub();
			}
		}
		
		pw.printlnIdent("}");
		pw.println();
   }

   private boolean openClass = false;
   private String name;
   private String superName;
   private List<MemberList> memberList;
   private TypeCianetoClass superclass;
   private HashMap<String, MethodDec> methodList = new HashMap<String, MethodDec>();
   // private FieldList fieldList;
   // private MethodList publicMethodList, privateMethodList;
   // m�todos p�blicos get e set para obter e iniciar as vari�veis acima,
   // entre outros m�todos
}
