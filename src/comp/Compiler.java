/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package comp;

import java.io.PrintWriter;
import java.util.ArrayList;
import ast.*;
import lexer.Lexer;
import lexer.Token;

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
			catch ( RuntimeException e ) {
				e.printStackTrace();
				thereWasAnError = true;
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
		lexer.nextToken();
		
		String superclassName = "";
		
		if ( lexer.token == Token.EXTENDS ) {
			lexer.nextToken();
			
			if ( lexer.token != Token.ID ) error("Identifier expected");
			
			superclassName = lexer.getStringValue();
			lexer.nextToken();
		}

		MemberList memberList = memberList();
		
		if (memberList == null) this.error("Class member OR 'end' expected");
		
		if (lexer.token != Token.END) error("'end' expected");
		
		lexer.nextToken();
		
		return new TypeCianetoClass(openClass, className, superclassName, memberList);
	}

	private MemberList memberList() {
		
		Qualifier qualifier = null;
		Member member = null;
		
		while ( true ) {			
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
				member = methodDec();				
			} else {
				break;
			}
		}
		
		if (qualifier == null && member == null)
			this.error("'public', 'private' or '}' expected");
		
		if (member == null) return null;
		
		if (lexer.token != Token.END && lexer.token != Token.LEFTCURBRACKET)
			this.error("'public', 'private', '}' or 'end' expected (line " + lexer.getLineNumber() + ")");
				
		return new MemberList(qualifier, member);
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

	private MethodDec methodDec() {
		
		lexer.nextToken();
		
		Variable method = new Variable(Type.undefinedType, null);
		ArrayList<Variable> paramList = new ArrayList<>();
		ArrayList<Statement> statList = new ArrayList<>();
		
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
		
		if ( lexer.token != Token.LEFTCURBRACKET ) {
			error("'{' expected (func " + method.getName() + ")");
		}
		
		next();
		
		statList = statementList();
		
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
			
			var.setName(lexer.getStringValue());
			next();
			
			paramList.add(var);
			
			if (lexer.token == Token.COMMA) {
				next();
			} else {
				break;
			}
		}
		
		return paramList;
	}

	private ArrayList<Statement> statementList() {
		
		ArrayList<Statement> statList = new ArrayList<>();
		
		// only '}' is necessary in this test
		while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END ) {
			Statement e = statement();
			statList.add(e);
		}
		
		return statList;
	}

	private Statement statement() {
				
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
			breakStat();
			break;
		case SEMICOLON:
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
		
		ArrayList<Variable> idList = new ArrayList<>();
		Expr expr = null;
		
		while ( lexer.token == Token.ID ) {
			Variable var = new Variable(type, lexer.getStringValue());
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
		
		return new LocalDec(type, idList, expr);
	}

	private RepeatStat repeatStat() {
		next();
		
		ArrayList<Statement> statList = new ArrayList<>();		
				
		while ( lexer.token != Token.UNTIL &&
				lexer.token != Token.RIGHTCURBRACKET &&
				lexer.token != Token.END ) {
			Statement stat = statement();			
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

	private void breakStat() {
		next();

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
		
		ArrayList<Statement> statList = new ArrayList<>();		
		statList = statementList();
		
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
			Statement e = statement();
			ifState.add(e);
		}
		
		check(Token.RIGHTCURBRACKET, "'}' was expected");
		next();
		
		if ( lexer.token == Token.ELSE ) {
			next();
			check(Token.LEFTCURBRACKET, "'{' expected (line " + lexer.getLineNumber() + ")");
			next();		
			
			while ( lexer.token != Token.RIGHTCURBRACKET ) {
				Statement e = statement();
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
		
		next();
		
		ArrayList<Expr> exprList = exprList();
		
		if (exprList == null) {
			this.error("Command 'Out." + printName + "' without arguments");
		}
		
		if (lexer.token != Token.SEMICOLON) {
			this.error("';' expected");
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
						
			left = new CompositeExpr(left, op, right);
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
			
			left = new CompositeSimpleExpr(left, operator, right);
		}
		
		if (left == null) {
			return null;
		}
		
		return left;
	}
	
	private Expr sumSubExpr() {
		Expr left = null;
		left = term();
		
		if (lexer.token == Token.PLUS || 
			lexer.token == Token.MINUS || 
			lexer.token == Token.OR) {
			Token operator = lexer.token;
			next();
			
			if (lexer.token == Token.PLUS) {
				return left;
			}
			
			Expr right = null;
			right = term();
			
			left = new CompositeSumSubExpr(left, operator, right);
		}
		
		if (left == null) {
			return null;
		}
		
		return left;
	}
	
	private Expr term() {
		Expr left = null;
		left = signalFactor();
		
		if (lexer.token == Token.MULT || 
			lexer.token == Token.DIV || 
			lexer.token == Token.AND) {
			Token operator = lexer.token;
			next();
			Expr right = null;
			right = signalFactor();
			
			left = new CompositeTerm(left, operator, right);
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
		
		return new CompositeSignalFactor(operator, factor);
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
			return new ExprFactor(e);
		} else if (lexer.token == Token.ID || 
					lexer.token == Token.SUPER || 
					lexer.token == Token.SELF || 
					lexer.token == Token.READ) {
			return primaryExpr();
		} else if (lexer.token == Token.NOT) {
			next();
			Factor factor = factor();
			return new BooleanExpr(Token.NOT, factor);
		} else {
			return basicValue();
		}
	}
	
	private BasicValue basicValue() {
		if (lexer.token == Token.LITERALINT) {
			Integer value = lexer.getNumberValue();
			next();
			return new BasicValue(value);
		} else if (lexer.token == Token.LITERALSTRING) {
			String value = lexer.getLiteralStringValue();
			next();
			return new BasicValue(value);
		} else if (lexer.token == Token.TRUE) {
			boolean value = true;
			next();
			return new BasicValue(value);
		} else if (lexer.token == Token.FALSE) {
			boolean value = false;
			next();
			return new BasicValue(value);
		} else {
			return null;
		}	
	}
	
	private AssignExpr assignExpr () {
		Expr left = null, right = null;
		left = expr();
		
		if (left == null) return null;
				
		if (lexer.token == Token.ASSIGN) {
			next();
			
			//Se é um identificador, precisa verificar se já foi declarado
			//Mensagem de erro temporária até implementação da análise semântica			
			if (lexer.getStringValue().equals("readInt")) {
				this.error("'readInt' was not declared");
			}
			
			right = expr();
			
			if (right == null) this.error("Expression expected");			
		}
		
		check(Token.SEMICOLON, "';' expected");
		
		return new AssignExpr(left, right);
	}
	
	private Factor primaryExpr() {		
		
		if (lexer.token == Token.ID) {
			
			if (lexer.getStringValue().equals("In")) {
				return readExpr();
			}
			
			String id = lexer.getStringValue();
			next();
			
			if (lexer.token == Token.DOT ) {
				next();
				
				if (lexer.token == Token.ID) {
					
					String id2 = lexer.getStringValue();
					next();
					return new PrimarySimpleExpr(id, id2);
					
				} else if (lexer.token == Token.IDCOLON) {
					String id2 = lexer.getStringValue();
					next();
					
					ArrayList<Expr> exprList = exprList();
					
					return new PrimarySimpleExpr(id, id2, exprList);
					
				} else if (lexer.token == Token.NEW) {
					next();
					return new ObjectCreation(id);
					
				} else {
					this.error("Id or IdColon expected");
				}
			} else {
				return new PrimarySimpleExpr(id);
			}
			
		} else if (lexer.token == Token.SUPER) {
			next();
			
			if (lexer.token == Token.DOT ) {
				next();
				
				if (lexer.token == Token.ID) {
					
					String id = lexer.getStringValue();
					next();
					return new PrimarySuperExpr(id);
					
				} else if (lexer.token == Token.IDCOLON) {
					String id = lexer.getStringValue();
					next();
					
					ArrayList<Expr> exprList = exprList();
					
					return new PrimarySuperExpr(id, exprList);
					
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
							return new PrimarySelfExpr(id, id2);
							
						} else if (lexer.token == Token.IDCOLON) {
							String id2 = lexer.getStringValue();
							next();
							
							ArrayList<Expr> exprList = exprList();
							
							return new PrimarySelfExpr(id, id2, exprList);
							
						} else {
							this.error("Id or IdColon expected");
						}
					}
					
					return new PrimarySelfExpr(id);
					
				} else if (lexer.token == Token.IDCOLON) {
					String id = lexer.getStringValue();
					next();
					
					ArrayList<Expr> exprList = exprList();
					
					return new PrimarySelfExpr(id, exprList);
					
				} else {
					this.error("Id or IdColon expected");
				}
			} else {
				return new PrimarySelfExpr("self");
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
		type = type();
		
		if (type != Type.booleanType && type != Type.intType && type != Type.stringType) 
			error("Type expected");
		
		if ( lexer.token != Token.ID ) {
			this.error("A field name was expected");
		} else {
			while ( lexer.token == Token.ID  ) {
				idList.add(lexer.getStringValue());
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
				
		return new FieldDec(type, idList);
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
			type = Type.undefinedType;
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
		return new LiteralInt(value);
	}

	private static boolean startExpr(Token token) {

		return token == Token.FALSE || token == Token.TRUE
				|| token == Token.NOT || token == Token.SELF
				|| token == Token.LITERALINT || token == Token.SUPER
				|| token == Token.LEFTPAR || token == Token.NULL
				|| token == Token.ID || token == Token.LITERALSTRING;

	}
	
	private static boolean relation (Token token) {
		return token == Token.EQ || token == Token.LT || token == Token.GT
				|| token == Token.LE || token == Token.GE || token == Token.NEQ;
	}

	private SymbolTable		symbolTable;
	private Lexer			lexer;
	private ErrorSignaller	signalError;
	
	private boolean returnRequired = false;
	
	private void teste() {
		System.out.println("somente teste para commit");
	}

}
