/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package comp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ast.*;
import lexer.Lexer;
import lexer.Token;
import semanticchecking.SemanticChecking;

public class Compiler {

	public Compiler() { }

	// compile must receive an input with an character less than
	// p_input.lenght
	public Program compile(char[] input, PrintWriter outError) {

		ArrayList<CompilationError> compilationErrorList = new ArrayList<>();
		signalError = new ErrorSignaller(outError, compilationErrorList);
		symbolTable = new SymbolTable();
		lexer = new Lexer(input, signalError);
		signalError.setLexer(lexer);

		Program program = null;
		lexer.nextToken();
		program = program(compilationErrorList);
		return program;
	}

	private Program program(ArrayList<CompilationError> compilationErrorList) {
		ArrayList<MetaobjectAnnotation> metaobjectCallList = new ArrayList<>();
		ArrayList<TypeCianetoClass> CianetoClassList = new ArrayList<>();
		Program program = new Program(CianetoClassList, metaobjectCallList, compilationErrorList);
		boolean thereWasAnError = false;
		
		while ( lexer.token == Token.CLASS ||
				(lexer.token == Token.ID && lexer.getStringValue().equals("open") ) ||
				lexer.token == Token.ANNOT ) {
			try {
				while ( lexer.token == Token.ANNOT ) {
					metaobjectAnnotation(metaobjectCallList);
				}
				CianetoClassList.add(classDec());
			}
			catch( CompilerError e) {
				// if there was an exception, there is a compilation error
				thereWasAnError = true;
				/*while ( lexer.token != Token.CLASS && lexer.token != Token.EOF ) {
					try {
						next();
					}
					catch ( RuntimeException ee ) {
						e.printStackTrace();
						return program;
					}
				}*/
			}
			/*catch ( RuntimeException e ) {
				e.printStackTrace();
				thereWasAnError = true;
			}*/
			catch ( Throwable e ) {
	            e.printStackTrace();
	            thereWasAnError = true;
	            // adicione as linhas abaixo
	            try {
	                error("Exception '" + e.getClass().getName() + "' was thrown and not caught. "
	                        + "Its message is '" + e.getMessage() + "'");
	            }
	            catch( CompilerError ee) {
	            }
	            return program; // add this line
	        }

		}
		
		if ( !thereWasAnError && lexer.token != Token.EOF ) {
			try {
				//error("End of file expected");
				error("'class' expected");
			}
			catch( CompilerError e) {
			}
		}
		
		return program;
	}

	/**  parses a metaobject annotation as <code>{@literal @}cep(...)</code> in <br>
     * <code>
     * {@literal @}cep(5, "'class' expected") <br>
     * class Program <br>
     *     func run { } <br>
     * end <br>
     * </code>
     *

	 */
	@SuppressWarnings("incomplete-switch")
	private void metaobjectAnnotation(ArrayList<MetaobjectAnnotation> metaobjectAnnotationList) {
		
		String name = lexer.getMetaobjectName();
		int lineNumber = lexer.getLineNumber();
		lexer.nextToken();
		ArrayList<Object> metaobjectParamList = new ArrayList<>();
		boolean getNextToken = false;
		if ( lexer.token == Token.LEFTPAR ) {
			// metaobject call with parameters
			lexer.nextToken();
			while ( lexer.token == Token.LITERALINT || lexer.token == Token.LITERALSTRING ||
					lexer.token == Token.ID ) {
				switch ( lexer.token ) {
				case LITERALINT:
					metaobjectParamList.add(lexer.getNumberValue());
					break;
				case LITERALSTRING:
					metaobjectParamList.add(lexer.getLiteralStringValue());
					break;
				case ID:
					metaobjectParamList.add(lexer.getStringValue());
				}
				lexer.nextToken();
				if ( lexer.token == Token.COMMA )
					lexer.nextToken();
				else
					break;
			}
			if ( lexer.token != Token.RIGHTPAR )
				error("')' expected after annotation with parameters");
			else {
				getNextToken = true;
			}
		}
		switch ( name ) {
		case "nce":
			if ( metaobjectParamList.size() != 0 )
				error("Annotation 'nce' does not take parameters");
			break;
		case "cep":
			if ( metaobjectParamList.size() != 3 && metaobjectParamList.size() != 4 )
				error("Annotation 'cep' takes three or four parameters");
			if ( !( metaobjectParamList.get(0) instanceof Integer)  ) {
				error("The first parameter of annotation 'cep' should be an integer number");
			}
			else {
				int ln = (Integer ) metaobjectParamList.get(0);
				metaobjectParamList.set(0, ln + lineNumber);
			}
			if ( !( metaobjectParamList.get(1) instanceof String) ||  !( metaobjectParamList.get(2) instanceof String) )
				error("The second and third parameters of annotation 'cep' should be literal strings");
			if ( metaobjectParamList.size() >= 4 && !( metaobjectParamList.get(3) instanceof String) )
				error("The fourth parameter of annotation 'cep' should be a literal string");
			break;
		case "annot":
			if ( metaobjectParamList.size() < 2  ) {
				error("Annotation 'annot' takes at least two parameters");
			}
			for ( Object p : metaobjectParamList ) {
				if ( !(p instanceof String) ) {
					error("Annotation 'annot' takes only String parameters");
				}
			}
			if ( ! ((String ) metaobjectParamList.get(0)).equalsIgnoreCase("check") )  {
				error("Annotation 'annot' should have \"check\" as its first parameter");
			}
			break;
		default:
			error("Annotation '" + name + "' is illegal");
		}
		metaobjectAnnotationList.add(new MetaobjectAnnotation(name, metaobjectParamList));
		if ( getNextToken ) lexer.nextToken();
	}

	private TypeCianetoClass classDec() {
		boolean openClass = false;
		
		if ( lexer.token == Token.ID && lexer.getStringValue().equals("open") ) {
			openClass = true;
			next();
		}
		
		if ( lexer.token != Token.CLASS ) error("'class' expected");
		
		lexer.nextToken();
		
		if ( lexer.token != Token.ID ) error("Identifier expected");
		
		String className = lexer.getStringValue();
		semanticChecking.setCurrentClassName(className);
		lexer.nextToken();
		
		String superclassName = "";
		
		if ( lexer.token == Token.EXTENDS ) {
			lexer.nextToken();
			
			if ( lexer.token != Token.ID ) error("Identifier expected");
			
			superclassName = lexer.getStringValue();
		
			if(superclassName.equals(className)) {
				this.error("Class '"+className+"' is inheriting from itself (comp.Compiler.classDec())");
			}
			
			semanticChecking.setSuperClass(semanticChecking.getTypeCianetoClass(superclassName));
			lexer.nextToken();
		}

		List<MemberList> list = new ArrayList<MemberList>();
		if (lexer.token != Token.END) {
			semanticChecking.clearHashMethodList();
			list = memberList();
			
			if (list == null || list.isEmpty()) this.error("Class member OR 'end' expected");
			
			if (lexer.token != Token.END) error("'end' expected");
		}
		
		lexer.nextToken();
		
		TypeCianetoClass c = new TypeCianetoClass(openClass, className, superclassName, semanticChecking.getSuperClass(), list);
		c.setMethodList(semanticChecking.getHashMethodsList());
		semanticChecking.putInHashClasses(className, c);
		return c;
	}

	private List<MemberList> memberList() {
		
		List<MemberList> list = new ArrayList<MemberList>();
		semanticChecking.clearHashGlobalVariables();
		
		while ( true ) { 
			Qualifier qualifier = null;
			Member member = null;
			if (lexer.token != Token.END &&
				lexer.token != Token.VAR &&
				lexer.token != Token.FUNC) {
				qualifier = qualifier();
			}
			
			if ( lexer.token == Token.VAR ) {
				if ((qualifier != null) && (qualifier.getToken1() == Token.PUBLIC))
					this.error("Attempt to declare public instance variable in line " + lexer.getLineNumber());
								
				member = fieldDec();
			} else if ( lexer.token == Token.FUNC ) {
				if (qualifier == null)
					qualifier = new Qualifier(Token.PUBLIC, null, null, null);
				
				MethodDec methodDec = methodDec(qualifier);
				
				semanticChecking.putInHashMethodsList(methodDec.getMethodName(), methodDec);
				member = methodDec;
			} else {
				break;
			}
			
			if (qualifier == null && member == null)
				this.error("'public', 'private' or '}' expected");
			
			//if (member == null) return null;
			
			MemberList memberList = new MemberList(qualifier, member);
			list.add(memberList);
		}
		
		if (lexer.token != Token.END && lexer.token != Token.LEFTCURBRACKET)
			this.error("'public', 'private', '}' or 'end' expected (line " + lexer.getLineNumber() + ")");
				
		return list;
	}

	private void error(String msg) {
		this.signalError.showError(msg);
	}


	private void next() {
		lexer.nextToken();
	}

	private void check(Token shouldBe, String msg) {
		if ( lexer.token != shouldBe ) {
			error(msg);
		}
	}

	private MethodDec methodDec(Qualifier qualifier) {
		
		lexer.nextToken();
		
		Variable method = new Variable(Type.nullType, null);
		ArrayList<Variable> paramList = new ArrayList<>();
		ArrayList<Statement> statList = new ArrayList<>();
		
		semanticChecking.clearHashParameters();
		semanticChecking.clearHashLocalVariables();
		
		if ( lexer.token == Token.ID ) {
			method.setName(lexer.getStringValue());
			lexer.nextToken();
		} else if ( lexer.token == Token.IDCOLON ) {
			method.setName(lexer.getStringValue());
			lexer.nextToken();			
			paramList = paramList();
		} else {
			error("An identifier or identifer: was expected after 'func'");
		}
		
		if ( lexer.token == Token.MINUS_GT ) {
			lexer.nextToken();
			Type methodType = type();
			
			if (methodType == null)
				this.error("Type expected to method in line " + lexer.getLineNumber());
						
			method.setType(methodType);
			
			returnRequired = true;
		}
		
		if(qualifier.getToken1()==Token.OVERRIDE) {
			String methodName = method.getName();
			TypeCianetoClass cianetoClass = semanticChecking.getTypeCianetoClass(semanticChecking.getSuperClass().getName());
			if(cianetoClass!=null) {
				MethodDec superMethod = cianetoClass.getMethod(methodName);
				if(superMethod!=null) {
					int sizeParam = paramList.size();
					int sizeSuperParam = superMethod.getParamList().size();
					ArrayList<Variable> superParamList = superMethod.getParamList();
					if(sizeParam==sizeSuperParam) {
						for(int i=0; i<sizeParam; i++) {
							Variable v1 = paramList.get(i);
							Variable v2 = superParamList.get(i);
							if(!v1.getType().getName().equals(v2.getType().getName())) {
								this.error("Method '"+methodName+"' of subclass '"+semanticChecking.getCurrentClassName()+"' has a signature different from method inherited from superclass '"+cianetoClass.getName()+"' (comp.Compiler.methodDec())");
							}
						}
						
						if(!superMethod.getType().getName().equals(method.getType().getName())) {
							this.error("Wrong type of return");
						}
						
					} else {
						this.error("Method '"+methodName+"' of subclass '"+semanticChecking.getCurrentClassName()+"' has a signature different from method inherited from superclass '"+cianetoClass.getName()+"' (comp.Compiler.methodDec())");
					}
				} else {
					this.error("there is no method "+methodName+" to overwrite");
				}
			} else {
				this.error("nonexistent superclass " + cianetoClass.getName());
			}
		}
		
		if ( lexer.token != Token.LEFTCURBRACKET ) {
			error("'{' expected (func " + method.getName() + ")");
		}
		
		semanticChecking.setCurrentMethod(method);
		next();
		
		statList = statementList("methodDec");
		
		if (returnRequired) {
			error("missing 'return' statement");
		}
		
		if ( lexer.token != Token.RIGHTCURBRACKET) {			
			error("'}' expected");
		}
		
		if (lexer.token != Token.END) {
			next();
		}		
		
		return new MethodDec(method, statList, paramList);
	}
	
	private ArrayList<Variable> paramList() {
		
		ArrayList<Variable> paramList = new ArrayList<>();
		
		while (true) {
			Variable var = new Variable(Type.undefinedType, null);
			
			Type varType = type();
			
			if (varType == null)
				this.error("Type expected to parameter in line " + lexer.getLineNumber());
						
			var.setType(varType);
			
			if (lexer.token != Token.ID) {
				this.error("Identifier expected");
			}
			
			String name = lexer.getStringValue();
			
			if (semanticChecking.getLocalDec(name)!=null) {
				this.error(name+" is already declared as local variable");
			} else if (semanticChecking.getParamVariable(name)!=null) {
				this.error(name+" is already declared as a parameter");
			}
			
			var.setName(name);
			next();
			
			semanticChecking.putInHashParameter(name, var);
			paramList.add(var);
			
			if (lexer.token == Token.COMMA) {
				next();
			} else {
				break;
			}
		}
		
		return paramList;
	}

	private ArrayList<Statement> statementList(String origin) {
		
		ArrayList<Statement> statList = new ArrayList<>();
		
		// only '}' is necessary in this test
		while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END ) {
			Statement e = statement(origin);
			statList.add(e);
		}
		
		return statList;
	}

	private Statement statement(String origin) {
				
		boolean checkSemiColon = true;
		
		Statement e = null;
		
		switch ( lexer.token ) {
		case IF:
			e = ifStat();
			checkSemiColon = false;
			break;
		case WHILE:
			e = whileStat();
			checkSemiColon = false;
			break;
		case RETURN:
			e = returnStat();
			checkSemiColon = false;
			break;
		case BREAK:
			if(origin.equals("methodDec")) {
				this.error("'break' statement found outside a 'while' or 'repeat-until' statement (comp.Compiler.statement()))");	
			}
			e = breakStat();
			break;
		case SEMICOLON:
			e = new SemicolonStatement();
			checkSemiColon = false;
			next();
			break;
		case REPEAT:
			e = repeatStat();
			break;
		case VAR:
			e = localDec();	
			
			if (lexer.token == Token.SEMICOLON) {
				checkSemiColon = false;
			}
			
			next();
			
			break;
		case ASSERT:
			e = assertStat();
			checkSemiColon = false;
			break;
		default:
			if ( lexer.token == Token.ID && lexer.getStringValue().equals("Out") ) {
				e = writeStat();
				
				if (lexer.token == Token.SEMICOLON) {
					checkSemiColon = false;
				}
				
				next();				
			} else if (lexer.getStringValue().equals("print:") || 
						lexer.getStringValue().equals("println:")) {
				this.error("Missing 'Out.'");
			} else {
				e = assignExpr();
				
				if (e == null) this.error("Statement expected");
								
				if (lexer.token == Token.SEMICOLON) checkSemiColon = false;
								
				next();
			}
		}
		
		if (e == null) this.error("Statement expected");
				
		if ( checkSemiColon ) check(Token.SEMICOLON, "';' expected");
				
		return e;
	}

	private LocalDec localDec() {
		next();
		
		Type type = Type.undefinedType;		
		type = type();
		
		check(Token.ID, "Identifier expected");
		
		if(type instanceof TypeNull) {
			if(semanticChecking.getTypeCianetoClass(type.getName())==null && !semanticChecking.getCurrentClassName().equals(type.getName())) {
				this.error("ype '"+type.getName()+"' was not found (comp.Compiler.localDec())");
			}
		}
		
		ArrayList<Variable> idList = new ArrayList<>();
		Expr expr = null;
		
		while ( lexer.token == Token.ID ) {
			String name = lexer.getStringValue();
			
			if (semanticChecking.getLocalDec(name)!=null) {
				this.error(name+" is already declared as local variable");
			} else if (semanticChecking.getParamVariable(name)!=null) {
				this.error(name+" is already declared as a parameter");
			}
			
			
			Variable var = new Variable(type, name);
			idList.add(var);
			
			next();
			
			if ( lexer.token == Token.COMMA ) {
				next();				
				check(Token.ID, "Missing identifier");
			} else {
				break;
			}
		}
		
		if (lexer.token == Token.DOT) {
			this.error("Invalid Character: '" + lexer.token.toString() + "'");
		}
		
		if ( lexer.token == Token.ASSIGN ) {
			next();
			// check if there is just one variable
			expr = expr();
		}
		
		LocalDec localDec = new LocalDec(type, idList, expr);
		for(Variable var : idList) {
			semanticChecking.putInLocalVariables(var.getName(), localDec);
		}
		return localDec;
	}

	private RepeatStat repeatStat() {
		next();
		
		ArrayList<Statement> statList = new ArrayList<>();		
				
		while ( lexer.token != Token.UNTIL &&
				lexer.token != Token.RIGHTCURBRACKET &&
				lexer.token != Token.END ) {
			Statement stat = statement("repeatStat");			
			statList.add(stat);
		}
		
		//if (!lexer.token.toString().matches("^[a-zA-Z0-9]*$"))
			//this.error("'" + lexer.token.toString() + "' not expected before 'until'");
				
		check(Token.UNTIL, "missing keyword 'until'");
		next();
		
		Expr expr = expr();
		
		if (lexer.token == Token.RIGHTPAR)
			this.error("')' unexpected");
		
		return new RepeatStat(statList, expr);
	}

	private BreakStat breakStat() {
		next();
		return new BreakStat();
	}

	private ReturnStat returnStat() {
		next();
		Expr expr = expr();
		
		check(Token.SEMICOLON, "';' expected");		
		next();	
		
		returnRequired = false;
		return new ReturnStat(expr);
	}

	private WhileStat whileStat() {
		next();
		
		Expr e = expr();		
		next();
		
		if(!e.getType().getName().equals("boolean")) {
			this.error("non-boolean expression in 'while' command (comp.Compiler.whileStatement())");
		}
		
		ArrayList<Statement> statList = new ArrayList<>();		
		statList = statementList("while");
		
		check(Token.RIGHTCURBRACKET, "missing '}' after 'while' body");
		next();
		
		return new WhileStat(e, statList);
	}

	private IfStat ifStat() {
		next();
		
		Expr expr = expr();
		
		if (lexer.token == Token.PLUS || lexer.token == Token.MINUS || 
			lexer.token == Token.DIV || lexer.token == Token.MULT || 
			lexer.token == Token.ASSIGN) {
			this.error("Expression expected OR invalid sequence of symbols");
		}
		
		check(Token.LEFTCURBRACKET, "'{' expected (line " + lexer.getLineNumber() + ")");
		next();
		
		ArrayList<Statement> ifState = new ArrayList<>();
		ArrayList<Statement> elseState = new ArrayList<>();
		
		while ( lexer.token != Token.RIGHTCURBRACKET &&
				lexer.token != Token.END && lexer.token != Token.ELSE ) {
			Statement e = statement("ifStat");
			ifState.add(e);
		}
		
		check(Token.RIGHTCURBRACKET, "'}' was expected");
		next();
		
		if ( lexer.token == Token.ELSE ) {
			next();
			check(Token.LEFTCURBRACKET, "'{' expected (line " + lexer.getLineNumber() + ")");
			next();		
			
			while ( lexer.token != Token.RIGHTCURBRACKET ) {
				Statement e = statement("ifStat");
				elseState.add(e);
			}
			
			check(Token.RIGHTCURBRACKET, "'}' was expected");
			next();
		}
		
		//next();
		
		return new IfStat(expr, ifState, elseState);
	}

	/**

	 */
	private WriteStat writeStat() {
		next();
		check(Token.DOT, "a '.' was expected after 'Out'");		
		next();
		
		if (!lexer.getStringValue().equals("print:") && 
			!lexer.getStringValue().equals("println:")) {
			this.error("Command 'Out.' without arguments");
		}
		
		String printName = lexer.getStringValue();
		int lineNumber = lexer.getLineNumber();
		next();
		
		ArrayList<Expr> exprList = exprList();		
		
		if(exprList!=null) {
			for(Expr expr : exprList) {
				if(expr.getType().getName().equals("boolean")) {
					this.error("Attempt to print a "+expr.getType().getName()+" expression");
				}
			}
		}
		
		if (exprList == null) {
			this.error("Command 'Out." + printName + "' without arguments");
		}
		
		if (lexer.token != Token.SEMICOLON) {
			//System.out.println("linha: " + lineNumber);
			int line = lexer.getLineNumber();
			lexer.setLineNumber(lineNumber);
			this.error("';' expected");
			lexer.setLineNumber(line);
		}
		
		return new WriteStat(exprList, printName);
	}
	
	private ArrayList<Expr> exprList() {
		ArrayList<Expr> exprList = new ArrayList<>();
		
		Expr e = null;
		e = expr();
		
		if (e == null) return null;
				
		exprList.add(e);
		
		while (lexer.token == Token.COMMA) {
			next();
			
			e = expr();
			
			if (e == null) {
				this.error("Expression expected");
			}
			
			exprList.add(e);
		}
		
		
		return exprList;
	}

	private Expr expr() {
		Expr left = null;
		left = simpleExpr();
		
		if (relation(lexer.token)) {	
			Token op = lexer.token;
			next();
			
			Expr right = null;
			right = simpleExpr();
			
			if (right == null)
				this.error("Expression expected OR Unknown sequence of symbols");
			
			if(left!=null && right!=null) {
				//System.out.println("left type: " + left.getType());
				//System.out.println("right type: " + right.getType());
			}
			
			if(!left.getType().getName().equals(right.getType().getName())) {
				if( !(left.getType().getName().equals("NullType")) && !(right.getType().getName().equals("NullType"))  ) {
					this.error("Type not equals");
				}
			}
			
			left = new CompositeExpr(left, op, right, Type.booleanType);
		}
		
		if (left == null) {
			return null;
		}
		
		return left;
	}
	
	private Expr simpleExpr() {
		Expr left = null;
		left = sumSubExpr();
		
		while (lexer.token == Token.CONCAT) {
			Token operator = lexer.token;
			next();
			
			Expr right = null;
			right = sumSubExpr();
			
			if(!left.getType().getName().equals(right.getType().getName())) {
				this.error("Type not equals");
			}
			
			left = new CompositeSimpleExpr(left, operator, right, left.getType());
		}
		
		if (left == null) {
			return null;
		}
		
		return left;
	}
	
	private Expr sumSubExpr() {
		Expr left = null;
		left = term();
		
		while (lexer.token == Token.PLUS || 
			lexer.token == Token.MINUS || 
			lexer.token == Token.OR) {
			Token operator = lexer.token;
			next();
			
			if (lexer.token == Token.PLUS) {
				return left;
			}
			
			Expr right = null;
			right = term();
			
			if(!left.getType().getName().equals(right.getType().getName())) {
				this.error("Type not equals");
			}
			
			if(operator == Token.PLUS && (left.getType().getName().equals("boolean") || right.getType().getName().equals("boolean")) ) {
				this.error("type boolean does not support operation '+' (comp.Compiler.simpleExpr())");
			}
			
			left = new CompositeSumSubExpr(left, operator, right, left.getType());
		}
		
		if (left == null) {
			return null;
		}
		
		return left;
	}
	
	private Expr term() {
		Expr left = null;
		left = signalFactor();
		
		while (lexer.token == Token.MULT || 
			lexer.token == Token.DIV || 
			lexer.token == Token.AND) {
			Token operator = lexer.token;
			next();
			Expr right = null;
			right = signalFactor();
			
			if(!left.getType().getName().equals(right.getType().getName())) {
				this.error("Type not equals");
			}
			
			if(operator == Token.AND && (left.getType().getName().equals("int") || right.getType().getName().equals("int"))) {
				this.error("type 'int' does not support operator '&&' (comp.Compiler.term())");
			}
			
			left = new CompositeTerm(left, operator, right, left.getType());
		}
		
		if (left == null) {
			return null;
		}
		
		return left;
	}
	
	private SignalFactor signalFactor() {
		Token operator = null;
		
		if (lexer.token == Token.PLUS || 
			lexer.token == Token.MINUS || 
			lexer.token == Token.OR) {
			operator = lexer.token;
			next();
		}
		
		Factor factor = factor();
		
		if (factor == null) {
			return null;
		}
		
		return new CompositeSignalFactor(operator, factor, factor.getType());
	}
	
	private Factor factor() {
		if (lexer.token == Token.LEFTPAR) {
			next();
			
			Expr e = null;
			e = expr();
			
			if (e == null) this.error("Expression expected");
						
			if (lexer.token != Token.RIGHTPAR) this.error("')' expected");
						
			if (e == null) return null;
						
			next();
			return new ExprFactor(e, e.getType());
		} else if (lexer.token == Token.ID || 
					lexer.token == Token.SUPER || 
					lexer.token == Token.SELF || 
					lexer.token == Token.READ) {
			return primaryExpr();
		} else if (lexer.token == Token.NOT) {
			next();
			Factor factor = factor();
			if(!factor.getType().getName().equals("boolean")) {
				this.error("Operator '!' does not accepts '"+factor.getType().getName()+"' values (comp.Compiler.factor())");
			}
			return new BooleanExpr(Token.NOT, factor, factor.getType());
		} else if(lexer.token == Token.NULL) {
			next();
			return new NullExpr(Type.nullType);
		} else {
			return basicValue();
		}
	}
	
	private BasicValue basicValue() {
		if (lexer.token == Token.LITERALINT) {
			Integer value = lexer.getNumberValue();
			next();
			return new BasicValue(value, Type.intType);
		} else if (lexer.token == Token.LITERALSTRING) {
			String value = lexer.getLiteralStringValue();
			next();
			return new BasicValue(value, Type.stringType);
		} else if (lexer.token == Token.TRUE) {
			boolean value = true;
			next();
			return new BasicValue(value, Type.booleanType);
		} else if (lexer.token == Token.FALSE) {
			boolean value = false;
			next();
			return new BasicValue(value, Type.booleanType);
		} else {
			return null;
		}	
	}
	
	private AssignExpr assignExpr () {
		Expr left = null, right = null;
		int lineNumber = lexer.getLineNumber();
		if(lexer.token == Token.BOOLEAN
			|| lexer.token == Token.LITERALINT
			|| lexer.token == Token.LITERALSTRING) {
			this.error("Cannot assign to a literal");
		}
		left = expr();
		
		if (left == null) return null;
		
		if (lexer.token == Token.ASSIGN) {
			lineNumber = lexer.getLineNumber();
			next();
			
			//Se é um identificador, precisa verificar se já foi declarado
			//Mensagem de erro temporária até implementação da análise semântica			
			if (lexer.getStringValue().equals("readInt")) {
				this.error("'readInt' was not declared");
			}
			
			right = expr();
			
			if (right == null) this.error("Expression expected");
			if(!left
					.getType()
					.getName()
					.equals(
							right
							.getType()
							.getName())) {
				this.error("Type error: value of the right-hand side is not subtype of the variable of the left-hand side.");
			}
		}
		
		//check(Token.SEMICOLON, "';' expected");
		if ( lexer.token != Token.SEMICOLON ) {
			//System.out.println("linha: " + lineNumber);
			int line = lexer.getLineNumber();
			lexer.setLineNumber(lineNumber);
			error("';' expected");
			lexer.setLineNumber(line);
		}
		
		return new AssignExpr(left, right);
	}
	
	private Factor primaryExpr() {		
		
		if (lexer.token == Token.ID) {
			
			if (lexer.getStringValue().equals("In")) {
				return readExpr();
			}
			
			String id = lexer.getStringValue();
			
			if(semanticChecking.getTypeCianetoClass(id)==null 
					&& semanticChecking.getFieldDec(id)==null 
					&& semanticChecking.getLocalDec(id)==null 
					&& semanticChecking.getParamVariable(id)==null) {
				
				if(!id.equals(semanticChecking.getCurrentClassName())) {
					this.error(id+" was not declared");
				}
			}
			
			next();
			
			if (lexer.token == Token.DOT ) {
				next();
				
				if (lexer.token == Token.ID) {
					
					String id2 = lexer.getStringValue();
					next();
					
					Type t1 = getTypeOfId(id);
					
					//TypeCianetoClass cClass = hashClasses.get(t1.getName());
					MethodDec method = null;
					TypeCianetoClass cClass = semanticChecking.getTypeCianetoClass(t1.getName());
					if(cClass==null) {
						if(t1.getName().equals(semanticChecking.getCurrentClassName())) {
							method = semanticChecking.getMethodDec(id2);
						} else {
							this.error("The class of "+t1.getName()+" is not declared");
						}
					} else {
						method = cClass.getMethod(id2);
					}
					
					
					if(method==null) {
						this.error("method "+id2+" is not declared in "+t1.getName());
					}
					
					return new PrimarySimpleExpr(id, id2, method.getType());
					
				} else if (lexer.token == Token.IDCOLON) {
					String id2 = lexer.getStringValue();
					next();
					
					ArrayList<Expr> exprList = exprList();
					
					Type t = verifySendMessage(id, id2, exprList);
					
					return new PrimarySimpleExpr(id, id2, exprList, t);
					
				} else if (lexer.token == Token.NEW) {
					next();
					return new ObjectCreation(id, getTypeOfId(id));
					
				} else {
					this.error("Id or IdColon expected");
				}
			} else {
				return new PrimarySimpleExpr(id, getTypeOfId(id));
			}
			
		} else if (lexer.token == Token.SUPER) {
			next();
			
			if (lexer.token == Token.DOT ) {
				next();
				
				if (lexer.token == Token.ID) {
					
					String id = lexer.getStringValue();
					
					if(semanticChecking.getSuperClass()==null) {
						this.error("cannot call super, the class "+semanticChecking.getCurrentClassName()+" has not super class");
					}
					
					MethodDec methodDec = semanticChecking.getSuperClass().getMethod(id);
					if(methodDec==null) {
						this.error(id+" is not declared");
					}
					
					next();
					return new PrimarySuperExpr(id, methodDec.getType());
					
				} else if (lexer.token == Token.IDCOLON) {
					String id = lexer.getStringValue();
					next();
					
					ArrayList<Expr> exprList = exprList();
					
					if(semanticChecking.getSuperClass()==null) {
						this.error("cannot call super, the class "+semanticChecking.getCurrentClassName()+" has not super class");
					}
					
					MethodDec methodDec = semanticChecking.getSuperClass().getMethod(id);
					if(methodDec==null) {
						this.error(id+" is not declared");
					}
					Type t = verifyArgumentsOfSendMessage(id, methodDec, exprList);
					
					return new PrimarySuperExpr(id, exprList, t);
					
				} else {
					this.error("Id or IdColon expected");
				}
			}
		} else if (lexer.token == Token.SELF) {			
			next();
			
			if (lexer.token == Token.DOT ) {
				next();
				
				if (lexer.token == Token.ID) {
					
					String id = lexer.getStringValue();
					next();
					
					if (lexer.token == Token.DOT) {
						next();
						
						if (lexer.token == Token.ID) {
							
							String id2 = lexer.getStringValue();
							next();
							
							Type t1 = getTypeOfId(id);
							Type t2 = getTypeOfId(id2);
							if(!t1.getName().equals(t2.getName())) {
								this.error("Type not equals");
							}
							return new PrimarySelfExpr(id, id2, t1);
							
						} else if (lexer.token == Token.IDCOLON) {
							String id2 = lexer.getStringValue();
							next();
							
							ArrayList<Expr> exprList = exprList();
							
							Type t = verifySendMessage(id, id2, exprList);
							
							return new PrimarySelfExpr(id, id2, exprList, t);
							
						} else {
							this.error("Id or IdColon expected");
						}
					}
					
					//FieldDec fieldDec = hashGlobalVariables.get(id);
					//MethodDec methodDec = hashMethodsList.get(id);
					FieldDec fieldDec = semanticChecking.getFieldDec(id);
					MethodDec methodDec = semanticChecking.getMethodDec(id);
					if(methodDec==null && semanticChecking.getSuperClass()!=null) {
						methodDec = semanticChecking.getSuperClass().getMethod(id);
					}
					Type type = Type.undefinedType;
					if(fieldDec==null && methodDec==null) {
						this.error(id+" is not declared");
					}
					
					if(fieldDec!=null) {
						type = fieldDec.getType();
					} else if (methodDec!=null) {
						type = methodDec.getType();
					}
					return new PrimarySelfExpr(id, type);
					
				} else if (lexer.token == Token.IDCOLON) {
					String id = lexer.getStringValue();
					next();
					
					ArrayList<Expr> exprList = exprList();
					
					Type t = verifySendMessage("self", id, exprList);
					
					return new PrimarySelfExpr(id, exprList, t);
					
				} else {
					this.error("Id or IdColon expected");
				}
			} else {
				Type type = Type.nullType;
				type.setName(semanticChecking.getCurrentClassName());
				return new PrimarySelfExpr("self", type);
			}		
		}
		
		return null;
	}
	
	private ReadExpr readExpr() {
		next();		
		check(Token.DOT, "a '.' was expected after 'Out'");		
		next();
		
		if (lexer.getStringValue().equals("readInt")) {
			next();
			return new ReadExpr(Type.intType);
		} else if (lexer.getStringValue().equals("readString")) {
			next();
			return new ReadExpr(Type.stringType);
		} else {
			this.error("Command 'In.' without arguments");
		}
		
		return null;
	}

	private FieldDec fieldDec() {
		next();
		
		ArrayList<String> idList = new ArrayList<>();
		Type type = Type.undefinedType;
		type.setName(lexer.getStringValue());
		type = type();
		
		if (type == null) 
			error("Type expected");
		
		if ( lexer.token != Token.ID ) {
			this.error("A field name was expected");
		} else {
			while ( lexer.token == Token.ID  ) {
				String name = lexer.getStringValue();
				checkVariable(name);
				idList.add(name);
				next();
				
				if ( lexer.token == Token.COMMA ) {
					lexer.nextToken();
				} else {
					break;
				}
			}
		}
		
		if (lexer.token == Token.SEMICOLON) next();
		
		if (lexer.token == Token.DOT)
			this.error("Invalid Character: '" + lexer.token.toString() + "'");

		FieldDec field = new FieldDec(type, idList);
		for(String id : idList) {
			semanticChecking.putInHashGlobalVariables(id, field);
		}
		return field;
	}

	private Type type() {
		Type type = Type.undefinedType;
		
		if ( lexer.token == Token.INT ) {
			type = Type.intType;
			next();
		}
		else if ( lexer.token == Token.BOOLEAN ) {
			type = Type.booleanType;
			next();
		}
		else if ( lexer.token == Token.STRING ) {
			type = Type.stringType;
			next();
		}
		else if ( lexer.token == Token.ID ) {
			type = Type.nullType;
			/*type = semanticChecking.getTypeCianetoClass(lexer.getStringValue());
			System.out.println("cianeto");
			System.out.println(type.getName());*/
			type.setName(lexer.getStringValue());
			next();
		}
		else {
			//this.error("A type was expected in line " + lexer.getLineNumber());
			return null;
		}
		
		return type;
	}


	private Qualifier qualifier() {
		
		Token q1 = null, q2 = null, q3 = null, q4 = null;	
		
		if (lexer.token != Token.PUBLIC && lexer.token != Token.PRIVATE && 
				lexer.token != Token.OVERRIDE && lexer.token != Token.FINAL && 
				lexer.token != Token.FUNC) {
			return null;
		}
		
		if (lexer.token == Token.FUNC) {			
			q1 = Token.PUBLIC;
			return new Qualifier(q1, q2, q3, q4);
		} else {
			q1 = lexer.token;
		}
		
		next();
		
		if ( lexer.token == Token.PRIVATE ) {
			q2 = lexer.token;
			next();
		} else if ( lexer.token == Token.PUBLIC ) {
			q2 = lexer.token;
			next();
		} else if ( lexer.token == Token.OVERRIDE ) {
			q2 = lexer.token;
			next();
			
			if ( lexer.token == Token.PUBLIC ) {
				q3 = lexer.token;
				next();
			}
		} else if ( lexer.token == Token.FINAL ) {
			q2 = lexer.token;
			next();
			
			if ( lexer.token == Token.PUBLIC ) {
				q3 = lexer.token;
				next();
			} else if ( lexer.token == Token.OVERRIDE ) {
				q3 = lexer.token;
				next();
				
				if ( lexer.token == Token.PUBLIC ) {
					q4 = lexer.token;
					next();
				}
			}
		}
		
		return new Qualifier(q1, q2, q3, q4);
	}
	
	private AssertStat assertStat() {
		next();	
		
		Expr expr = expr();
		
		check(Token.COMMA, "',' expected after the expression of the 'assert' statement");
		next();
		
		check(Token.LITERALSTRING, "A literal string expected after the ',' of the 'assert' statement");
		
		String message = lexer.getLiteralStringValue();
		next();	
		
		check(Token.SEMICOLON, "';' expected");
		next();		

		return new AssertStat(expr, message);
	}

	private LiteralInt literalInt() {

		LiteralInt e = null;

		// the number value is stored in lexer.getToken().value as an object of
		// Integer.
		// Method intValue returns that value as an value of type int.
		int value = lexer.getNumberValue();
		lexer.nextToken();
		return new LiteralInt(value, Type.intType);
	}

	private static boolean relation (Token token) {
		return token == Token.EQ || token == Token.LT || token == Token.GT
				|| token == Token.LE || token == Token.GE || token == Token.NEQ || token == Token.AND;
	}

	private void checkVariable(String identifier) {
		FieldDec fieldDec = semanticChecking.getFieldDec(identifier);
		LocalDec localDec = semanticChecking.getLocalDec(identifier);
		Variable parameter = semanticChecking.getParamVariable(identifier);
		
		if(fieldDec!=null) {
			this.error(identifier+" is already declared as global variable");
		} else if (localDec!=null) {
			this.error(identifier+" is already declared as local variable");
		} else if (parameter!=null) {
			this.error(identifier+" is already declared as a parameter");
		}
	}
	
	private boolean isVariable(String identifier) {
		FieldDec fieldDec = semanticChecking.getFieldDec(identifier);
		LocalDec localDec = semanticChecking.getLocalDec(identifier);
		Variable parameter = semanticChecking.getParamVariable(identifier);
		
		if(fieldDec!=null || localDec!=null || parameter!=null) {
			return true;
		} else {
			return false;
		}
	}
	
	private Type getTypeOfId(String id) {
		
		TypeCianetoClass cianetoClass = semanticChecking.getTypeCianetoClass(id);
		if(cianetoClass!=null) {
			Type type = Type.nullType;
			type.setName(cianetoClass.getName());
			return type;
		}
		
		if(id.equals(semanticChecking.getCurrentClassName())) {
			Type type = Type.nullType;
			type.setName(id);
			return type;
		}
		
		FieldDec fieldDec = semanticChecking.getFieldDec(id);
		if(fieldDec!=null) {
			return fieldDec.getType();
		} 

		LocalDec localDec = semanticChecking.getLocalDec(id);
		if (localDec!=null) {
			return localDec.getType();
		} 

		Variable parameter = semanticChecking.getParamVariable(id);
		if (parameter!=null) {
			return parameter.getType();
		}
		
		this.error(id+" is not declared");
		return Type.undefinedType;
	}
	
	private Type verifySendMessage(String variableName, String methodName, ArrayList<Expr> exprList) {
		//System.out.println(variableName+"."+methodName);
		
		MethodDec method = null;
		if(!variableName.equals("self")) {
			String className = "";
			FieldDec fieldDec = semanticChecking.getFieldDec(variableName);
			if(fieldDec==null) {
				LocalDec localDec = semanticChecking.getLocalDec(variableName);
				if(localDec==null) {
					Variable variable = semanticChecking.getParamVariable(variableName);
					if(variable==null) {
						this.error("erro ao procurar variavel");
					} else {
						className = variable.getType().getName();
					}
				} else {
					className = localDec.getType().getName();
				}
			} else {
				className = fieldDec.getType().getName();
			}
			
			TypeCianetoClass cianetoClass = semanticChecking.getTypeCianetoClass(className);
			if(cianetoClass==null) {
				
				if(className.equals(semanticChecking.getCurrentClassName())) {
					method = semanticChecking.getMethodDec(methodName);
					if(method==null) {
						if(methodName.equals(semanticChecking.getCurrentMethodVariable().getName())) {
							//verifyMethodsParams(paramList, exprList);
							return semanticChecking.getCurrentMethodVariable().getType();
						} else {
							this.error("method "+methodName+" is not declared");
						}
					}
				} else {
					this.error("erro ao obter classe: " + className);
				}
			} else {
				method = cianetoClass.getMethod(methodName);
			}
		} else {
			method = semanticChecking.getMethodDec(methodName);
		}
		if(method==null) {
			this.error("method "+methodName+" is not declared");
		}
		
		verifyMethodsParams(method.getParamList(), exprList);
		return method.getType();
	}
	
	private void verifyMethodsParams(ArrayList<Variable> paramList, ArrayList<Expr> exprList) {
		int sizeParam = paramList.size();
		int sizeExpr = exprList.size();
		if(sizeParam!=sizeExpr) {
			this.error("wrong amount of parameters");
		}
		for(int i=0; i<sizeParam; i++) {
			Variable var = paramList.get(i);
			Expr expr = exprList.get(i);
			if(!var.getType().getName().equals(expr.getType().getName())) {
				this.error("wrong type of parameter");
			}
		}
	}
	
	private Type verifyArgumentsOfSendMessage(String methodName, MethodDec method, ArrayList<Expr> exprList) {
		if(method==null) {
			this.error("method "+methodName+" is not declared");
		}
		
		ArrayList<Variable> paramList = method.getParamList();
		int sizeParam = paramList.size();
		int sizeExpr = exprList.size();
		if(sizeParam!=sizeExpr) {
			this.error("wrong amount of parameters");
		}
		for(int i=0; i<sizeParam; i++) {
			Variable var = paramList.get(i);
			Expr expr = exprList.get(i);
			if(!var.getType().getName().equals(expr.getType().getName())) {
				this.error("wrong type of parameter");
			}
		}
		return method.getType();
	}
	
	private SymbolTable		symbolTable;
	private Lexer			lexer;
	private ErrorSignaller	signalError;
	
	private boolean returnRequired = false;
	
	private SemanticChecking semanticChecking = new SemanticChecking();
}
