/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package ast;
/*
 * Krakatoa Class
 */
public class TypeCianetoClass extends Type {

   public TypeCianetoClass( String name ) {
      super(name);
   }
	
	public TypeCianetoClass(boolean openClass, String name, String superName, MemberList memberList) {
	    super(name);
		this.openClass = openClass;
		this.name = name;
		this.superName = superName;
		this.memberList = memberList;
	}

   @Override
   public String getCname() {
      return getName();
   }
   
   public MemberList getMemberList () {
	   return this.memberList;
   }
   
   public void genJava (PW pw) {
	   pw.printlnIdent("private static class " + this.getCname() + " {");
		
		if (memberList != null) {
			pw.add();			
			memberList.genJava(pw);									
			pw.sub();
		}
		
		pw.printlnIdent("}");
		pw.println();
   }

   private boolean openClass = false;
   private String name;
   private String superName;
   private MemberList memberList;
   private TypeCianetoClass superclass;
   // private FieldList fieldList;
   // private MethodList publicMethodList, privateMethodList;
   // m�todos p�blicos get e set para obter e iniciar as vari�veis acima,
   // entre outros m�todos
}
