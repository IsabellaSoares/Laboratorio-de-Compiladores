package comp;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ast.MetaobjectAnnotation;
import ast.PW;
import ast.Program;

public class Comp {


	public static void main( String []args ) {
		new Comp().run(args);
	}

	/**
	 * true if the compiler should generated code in Java. In this case,
	 * method genJava of the program is called
	 */
	private boolean genJava = false;
	/**
	 * directory to which the generated Java code of the program will be put. If it is
	 *      C:\out
	 * the compilation of a file
	 *      first.ci
	 * should produce a Java file
	 *      C:\out\first.java
	 *
	 */
	private String dirToJavaOutput;

	public void run( String []args ) {

		File file;
		if ( args.length == 3 && args[1].equalsIgnoreCase("-genjava") ) {
			this.genJava  = true;
			this.dirToJavaOutput = args[2];
			if ( dirToJavaOutput.endsWith(File.separator) ) {
				dirToJavaOutput = dirToJavaOutput.substring(0, dirToJavaOutput.length()-1);
			}
			File f = new File(this.dirToJavaOutput);
			if ( !f.exists() ) {
				System.out.println("Directory '" + this.dirToJavaOutput + "' given as option in the command line, after '-genJava', does not exist");
				System.exit(1);
			}
			if ( !f.isDirectory() ) {
				System.out.println("File '" + this.dirToJavaOutput + "' given as option in the command line, after '-genJava', should be a directory");
				System.exit(1);
			}
			this.filesWithCorrectlyGeneratedJavaClasses = new ArrayList<>();
			this.filesWithWrongGeneratedJavaClasses     = new ArrayList<>();
			this.filesWithJavaClassesWithCompilationErrors = new ArrayList<>();
			this.filesCompilerDidNotProducedJavaCode = new ArrayList<>();
		}
		else if ( args.length < 1 ||  args.length > 2 )  {
			System.out.println("Usage:\n   comp input");
			System.out.println("input is the file or directory to be compiled");
			System.out.println("the output file will be created in the current directory");
			System.exit(1);
		}

		//this.compileExecJava(null, args[0]);

		createGradeTable();
		numSourceFilesWithAnnotCEP = 0;
		int numSourceFiles = 0;
		shouldButWereNotList = new ArrayList<>();
		wereButShouldNotList = new ArrayList<>();
		wereButWrongLineList = new ArrayList<>();
		nullPointerAndOtherExceptionsList = new ArrayList<>();
		correctList = new ArrayList<>();
		numSourceFilesWithAnnotNCE = 0;



		PrintWriter outError;
		outError = new PrintWriter(System.out);

		PrintWriter report;
		FileOutputStream reportStream = null;
		try {
			String reportTxt;
			if ( this.genJava )  {
				reportTxt = dirToJavaOutput + File.separator + "report.txt";
			}
			else {
				reportTxt = "report.txt";
			}
			reportStream = new FileOutputStream(reportTxt);
		} catch ( FileNotFoundException  e) {
			outError.println("Could not create 'report.txt'");
			return ;
		}
		report = new PrintWriter(reportStream);


		checkNameFilenameListCompilerSucceededMap = new HashMap<>();
		checkNameFilenameListCompilerFailedMap = new HashMap<>();

		file = new File(args[0]);
		if ( ! file.exists() || ! file.canRead() ) {
			String msg = "Either the file " + args[0] + " does not exist or it cannot be read";
			System.out.println(msg);
			outError.println("-1 : " + msg);
			outError.close();
			report.close();
			return ;
		}
		if ( file.isDirectory() ) {
			// compile all files in this directory
			File fileList[] = file.listFiles();
			ArrayList<Program> programList = new ArrayList<>();

			for ( File f : fileList ) {
				String filename = f.getName();
				int lastIndexDot = filename.lastIndexOf('.');
				String ext = filename.substring(lastIndexDot + 1);
				if ( ext.equalsIgnoreCase("ci") ) {
					numSourceFiles++;
					try {
						Program program = compileProgram(f, filename, outError);
						if ( program == null ) {
							nullPointerAndOtherExceptionsList.add(filename);
							outError.flush();
						}
						else {
							programList.add(program);
							if ( this.genJava ) {
								compileExecJava(program, filename);
							}
						}
					}
					catch (RuntimeException e ) {
						nullPointerAndOtherExceptionsList.add(filename);
						System.out.println("Runtime exception");
					}
					catch (Throwable t) {
						nullPointerAndOtherExceptionsList.add(filename);
						System.out.println("Throwable exception");
					}
				}
			}
			if ( numSourceFilesWithAnnotNCE == 0 && numSourceFilesWithAnnotCEP == 0 ) {
				printErrorList(outError, programList);
			}
			else {
				printReport(numSourceFiles, report);
			}

		}
		else {
			Program program = compileProgram(file, args[0], outError);
			if ( program == null ) {
				outError.flush();
			}
			else {
				if ( this.genJava ) {
					compileExecJava(program, args[0]);
				}
				if ( numSourceFilesWithAnnotNCE == 0 && numSourceFilesWithAnnotCEP == 0 ) {
					printErrorList(outError, program);
				}
				else {
					printReport(1, report);
				}
			}

		}

		if ( this.genJava ) {
			if ( this.filesWithCorrectlyGeneratedJavaClasses != null &&
					this.filesWithWrongGeneratedJavaClasses != null &&
					this.filesWithJavaClassesWithCompilationErrors != null &&
					this.filesCompilerDidNotProducedJavaCode != null ) {
				report.println("");
				report.println("List of Java files with compilation errors: ");
				for ( String s : this.filesWithJavaClassesWithCompilationErrors ) {
					report.println("   " + s );
				}
				report.println("");
				report.println("List of files with correct Java code: ");
				for ( String s : this.filesWithCorrectlyGeneratedJavaClasses ) {
					report.println("   " + s );
				}
				report.println("");
				report.println("List of files Java code that compiles but was generated incorrectly: ");
				for ( String s : this.filesWithWrongGeneratedJavaClasses ) {
					report.println("   " + s );
				}
				report.println("");
				report.println("The compiler could not create the following .java files:");
				for ( String s : this.filesCompilerDidNotProducedJavaCode ) {
					report.println("   " + s );
				}
			}
			else {
				report.println("\nSomething wrong: null list of files that generate correct or wrong code");
			}
		}
		report.close();
		try {
			reportStream.close();
		}
		catch (IOException e) {
			System.out.println("Error in closing the report file");
		}
		System.out.println("Cianeto compiler finished");

	}


	private static void printErrorList(PrintWriter outError, ArrayList<Program> programList) {
		/*
		 * no annotation was used in the source codes
		 */
		for ( Program program : programList ) {
			printErrorList(outError, program);
		}
	}

	/**
	   @param outError
	   @param program
	 */
	private static void printErrorList(PrintWriter outError, Program program) {
		/*
		 * no annotation was used in the source codes
		 */
		for ( CompilationError error : program.getCompilationErrorList() ) {
			String s = error.getLineWithError();
			if ( s == null ) { s = ""; }
			outError.println("Error at line " + error.getLineNumber() + ", "
					+ error.getMessage() + "\n" + s );
		}
		outError.flush();
	}

	/**
	   @param numSourceFiles
	   @param report
	 */
	private void printReport(int numSourceFiles, PrintWriter report) {


		boolean compilerOk = true;

		StringBuilder partialReport = new StringBuilder();
		if ( this.checkNameFilenameListCompilerFailedMap.size() > 0 ) {
			partialReport.append("\r\n");
			partialReport.append("O compilador falhou em testar alguns aspectos (construções) de Cianeto. "
					+ "A lista abaixo consiste de entradas da forma \n"
					+ "    aspecto\n        listas de nomes de arquivos\n");
			partialReport.append("Os nomes de arquivos listados são aqueles que testam 'aspecto' mas em "
					+ "que o compilador falhou em apontar um erro, apontou um erro inexistente ou gerou código errado (se opção -genjava ou -genc foi usada).\r\n");
			if ( ! printReportCheckNameFilenameList(checkNameFilenameListCompilerFailedMap, partialReport, true) ) {
				return ;
			}
		}
		partialReport.append("\r\n");
		if ( this.checkNameFilenameListCompilerSucceededMap.size() > 0 ) {
			partialReport.append("O compilador obteve sucesso em testar alguns aspectos (construções) de Cianeto. "
					+ "A lista abaixo consiste de entradas da forma \n"
					+ "    aspecto\n        listas de nomes de arquivos\n");
			partialReport.append("Os nomes de arquivos listados são aqueles que testam 'aspecto' e nos quais "
					+ "o compilador obteve sucesso e gerou código correto (se opção -genjava ou -genc foi usada).\r\n");
			if ( ! printReportCheckNameFilenameList(checkNameFilenameListCompilerSucceededMap, partialReport, false) ) {
				return ;
			}
		}
		report.println("Relatório do Compilador");
		report.println();


		StringBuilder sb = new StringBuilder();
		sb.append(String.format("MI:  %-13s I: %-13s  PI: %-13s Exc: %d", Comp.numVeryImportantFailed, Comp.numImportantFailed,
				Comp.numLittleImportantFailed, this.nullPointerAndOtherExceptionsList.size()) + "\r\n");
		if ( numSourceFilesWithAnnotCEP > 0 ) {
			// MI: nn     I: nn     PI:  nn      Exc:
			// Dev: nn/nn/%     LE:   SSE:
			sb.append(String.format("Dev: %-13s",
					(this.shouldButWereNotList.size() + "/" + numSourceFilesWithAnnotCEP + "/" +
							(int ) (100.0*this.shouldButWereNotList.size()/this.numSourceFilesWithAnnotCEP) + "%"))
			   );
			sb.append( String.format(" LE: %-13s",
			  this.wereButWrongLineList.size() + "/" + numSourceFilesWithAnnotCEP + "/" +
			+ (int ) (100.0*this.wereButWrongLineList.size()/this.numSourceFilesWithAnnotCEP) + "%"));
		}
		if ( numSourceFiles -  numSourceFilesWithAnnotCEP != 0 ) {
			sb.append(" " + String.format("SSE: %-13s",
					this.wereButShouldNotList.size() + "/" + (numSourceFiles -  numSourceFilesWithAnnotCEP) + "/" +
					(int ) (100.0*this.wereButShouldNotList.size()/(numSourceFiles -  numSourceFilesWithAnnotCEP)) + "%"));
			//sb.append(this.wereButShouldNotList.size() + "/" + (numSourceFiles -  numSourceFilesWithAnnotCEP) + "\r\n");
			//sb.append(this.wereButWrongLineList.size() + "/" + numSourceFilesWithAnnotCEP + "\r\n");
		}

		report.println("Resumo");
		report.println("_________________________________________________________________________");
		report.println(sb.toString());
		report.println();
		report.println("MI = muito importante, I = importante, PI = pouco importante, Exc = exceções");
		report.println("Dev = deveria ter sinalizado, LE = sinalizou linha errada, SSE = sinalizado sem erro");
		report.println("_________________________________________________________________________");

		report.println();
		report.println("Número de testes 'Muito importantes' em que o compilador falhou: " + Comp.numVeryImportantFailed);
		report.println("Número de testes 'Importantes' em que o compilador falhou: " + Comp.numImportantFailed);
		report.println("Número de testes 'Pouco importantes' em que o compilador falhou: " + Comp.numLittleImportantFailed);

		report.println(partialReport.toString());

		if ( this.nullPointerAndOtherExceptionsList.size() > 0 ) {
			report.println( nullPointerAndOtherExceptionsList.size() +
					" arquivos lançaram exceções que não foram capturadas pelo compilador ou houve algum problema e o método 'compileProgram' retornou 'null'. "
					+ "A maioria delas é provavelmente NullPointerException. Estes arquivos são:");
			for ( String fn : this.nullPointerAndOtherExceptionsList ) {
				report.println("    " + fn);
			}
			report.println();
		}
		report.println("_________________________________________________________________________\r\n");

		if ( numSourceFilesWithAnnotCEP > 0 ) {
			report.println(this.shouldButWereNotList.size() + " de um total de " + numSourceFilesWithAnnotCEP +
					" erros que deveriam ser sinalizados não o foram (" +
					(int ) (100.0*this.shouldButWereNotList.size()/this.numSourceFilesWithAnnotCEP) + "%)");
			report.println(this.wereButWrongLineList.size() + " erros foram sinalizados na linha errada ("
					+ (int ) (100.0*this.wereButWrongLineList.size()/this.numSourceFilesWithAnnotCEP) + "%)");
		}
		if ( numSourceFiles -  numSourceFilesWithAnnotCEP != 0 ) {
			report.println(this.wereButShouldNotList.size() +
					" erros foram sinalizados em " + (numSourceFiles -  numSourceFilesWithAnnotCEP)
					+ " arquivos sem erro (" +
					(int ) (100.0*this.wereButShouldNotList.size()/(numSourceFiles -  numSourceFilesWithAnnotCEP)) + "%)"
					);
		}
		report.println("_________________________________________________________________________");

		if ( numSourceFilesWithAnnotCEP > 0 ) {
			if ( shouldButWereNotList.size() == 0 ) {
				report.println("Todos os erros que deveriam ter sido sinalizados o foram");
			}
			else {
				compilerOk = false;
				report.println();
				report.println("Erros que deveriam ser sinalizados mas não foram:");
				report.println();
				for (String s : this.shouldButWereNotList) {
					report.println(s);
					report.println();
				}
			}

			if ( wereButWrongLineList.size() == 0 ) {
				report.println("Um ou mais arquivos de teste tinham erros, mas estes foram sinalizados nos números de linhas corretos");
			}
			else {
				compilerOk = false;
				report.println("######################################################");
				report.println("Erros que foram sinalizados na linha errada:");
				report.println();
				for (String s : this.wereButWrongLineList) {
					report.println(s);
					report.println();
				}

			}

		}

		if ( numSourceFiles -  numSourceFilesWithAnnotCEP != 0  ) {
			if ( wereButShouldNotList.size() == 0 ) {
				report.println("O compilador não sinalizou nenhum erro que não deveria ter sinalizado");
			}
			else {
				compilerOk = false;
				report.println("######################################################");
				report.println("Erros que foram sinalizados mas não deveriam ter sido:");
				report.println();
				for (String s : this.wereButShouldNotList) {
					report.println(s);
					report.println();
				}
			}
		}

		if ( correctList.size() > 0 ) {
			report.println("######################################################");
			report.print("Em todos os testes abaixo, o compilador sinalizou o erro na linha correta (quando o teste tinha erros) ");
			report.print("ou não sinalizou o erro (quando o teste NÃO tinha erros). Mas é necessário conferir se as ");
			report.print("mensagens emitidas pelo compilador são compatíveis com as mensagens de erro sugeridas pelas chamadas aos ");
			report.print("metaobjetos dos testes. ");
			report.println();
			report.println();
			report.println("A lista abaixo contém o nome do arquivo de teste, a mensagem que ele sinalizou e a mensagem sugerida pelo arquivo de teste");
			report.println();
			for (String s : this.correctList ) {
				report.println(s);
				report.println();
			}
		}
		if ( compilerOk ) {
			if ( numSourceFiles == 1 )
				report.println("Para o caso de teste que você utilizou, o compilador está correto");
			else
				report.println("Para os casos de teste que você utilizou, o compilador está correto");

		}

	}


//	/**
//	   @param report
//	 */
//	private static void printReportCheckNameFilenameList(Map<String, String> checkNameFilenameList, PrintWriter report) {
//		for ( Entry<String, String> entry : checkNameFilenameList.entrySet() ) {
//			String checkName = entry.getKey();
//			String filenameListStr = entry.getValue();
//			report.println("    " + checkName);
//			if ( filenameListStr.indexOf(" ") < 0 ) {
//				report.println("        " + filenameListStr);
//			}
//			else {
//				String []filenameList = filenameListStr.split(" ");
//				for ( String filename : filenameList ) {
//					report.println("        " + filename);
//				}
//			}
//		}
//	}



	private static boolean printReportCheckNameFilenameList(Map<String, String> checkNameFilenameList,
			StringBuilder partialReport, boolean compilerFailed ) {
		ArrayList<TupleCheckNameText> ta = new ArrayList<>();
		for ( Entry<String, String> entry : checkNameFilenameList.entrySet() ) {
			String checkName = entry.getKey();
			int numFiles = 0;
			Integer importance = Comp.testNameWeightMap.get(checkName);
			if ( importance == null ) {
				partialReport.append("*******************************\nFailed to produce report: check name '" +
					       checkName + "' is not allowed" + "\r\n");
				return false;
			}
			StringBuilder s = new StringBuilder();
			String filenameListStr = entry.getValue();
			s.append("    " + checkName + "\r\n");
			if ( filenameListStr.indexOf(" ") < 0 ) {
				s.append("        " + filenameListStr + "\r\n");
				++numFiles;
			}
			else {
				String []filenameList = filenameListStr.split(" ");
				for ( String filename : filenameList ) {
					s.append("        " + filename + "\r\n");
					++numFiles;
				}
			}
			ta.add(new TupleCheckNameText(checkName, importance, s.toString(), numFiles));
		}
		Collections.sort(ta);
		partialReport.append("Os testes são categorizados por importância: 'Muito importante', 'Importante', 'pouco importante'\r\n");
		if ( ta.get(0).importance >= 5 ) {
			//report.println("\nTestes 'Muito importantes' em que o compilador falhou:");
			partialReport.append("\nTestes 'Muito importantes' em que o compilador " + (compilerFailed ? "falhou" : "acertou") + ":\r\n");
		}
		boolean alreadPrintMessage3 = false;
		boolean alreadPrintMessage2 = false;
		for ( TupleCheckNameText t : ta ) {
			if ( compilerFailed ) {
				if ( t.importance >= 5 ) {
					Comp.numVeryImportantFailed += t.numFiles;
				}
				else if ( t.importance > 1 ) {
					Comp.numImportantFailed += t.numFiles;
				}
				else {
					Comp.numLittleImportantFailed += t.numFiles;
				}
			}
			if ( !alreadPrintMessage3 && t.importance < 5 && t.importance > 1 ) {
				alreadPrintMessage3 = true;
				partialReport.append("\nTestes 'importantes' em que o compilador " +
				    (compilerFailed ? "falhou" : "acertou") + ":\r\n");
			}
			if ( !alreadPrintMessage2 && t.importance < 3  ) {
				alreadPrintMessage2 = true;
				partialReport.append("\nTestes 'pouco importantes' em que o compilador " +
				    (compilerFailed ? "falhou" : "acertou") + "\r\n");
			}
			partialReport.append(t.text + "\r\n");
		}
		return true;
	}
	/**
	   @param args
	   @param stream
	   @param numChRead
	   @param outError
	   @param printWriter
	 * @throws IOException
	 */
	private Program compileProgram(File file, String filename, PrintWriter outError)  {
		Program program;
		char []input = new char[ (int ) file.length() + 1 ];

		try(BufferedReader in = new BufferedReader(
				new InputStreamReader(
						new FileInputStream(file), "Cp1252"));) {
			in.read( input, 0, (int ) file.length() );
		}
		catch (UnsupportedEncodingException e)
		{
			outError.println("Encoding Cp1252 was not supported for file '" + filename + "'");
			return null;
		}
		catch (Exception e)
		{
			String msg = "Error reading file " + filename;
			outError.println(msg);
			return null;
		}

		Compiler compiler = new Compiler();
		program = null;

		try {
			// the generated code goes to a file and so are the errors
			program  = compiler.compile(input, outError );
			callMetaobjectMethods(filename, program, outError);
		}
		catch ( Throwable e ) {

		}

		return program;

	}



	public void callMetaobjectMethods(String filename, Program program, PrintWriter outError) {

		boolean foundCE = false;
		boolean foundNCE = false;
		for ( MetaobjectAnnotation annot : program.getMetaobjectCallList() ) {
			String annotName = annot.getName();
			switch ( annotName ) {
			case "cep":

				this.numSourceFilesWithAnnotCEP++;

				String message = (String ) annot.getParamList().get(2);
				int lineNumber = (Integer ) annot.getParamList().get(0);
				if ( ! program.hasCompilationErrors() ) {
					// there was no compilation error. There should be no call @cep(...)
					// The source code, through calls to "@cep(...)", informs that
					// there are errors
					String whatToCorrect = "";
					if ( annot.getParamList().size() >= 4 ) {
						whatToCorrect = (String ) annot.getParamList().get(3);
						whatToCorrect = " (" + whatToCorrect + ")";
					}
					this.shouldButWereNotList.add(filename + ", " + lineNumber + ", " + message +
							whatToCorrect
							);
					if ( foundCE )
						outError.println("More than one 'cep' metaobject calls in the same source file '" + filename + "'");
					foundCE = true;
					checkAnnotList(filename, program, outError, false);
				}
				else {
					// there was a compilation error. Check it.
					int lineOfError = program.getCompilationErrorList().get(0).getLineNumber();
					String ceMessage = (String ) annot.getParamList().get(2);
					String compilerMessage = program.getCompilationErrorList().get(0).getMessage();
					if ( lineNumber != lineOfError ) {

						String whatToCorrect = "";
						if ( annot.getParamList().size() >= 4 ) {
							whatToCorrect = (String ) annot.getParamList().get(3);
							whatToCorrect = "(" + whatToCorrect + ")";
						}

						checkAnnotList(filename, program, outError, false);

						this.wereButWrongLineList.add(filename + "\n" +
								"    correto:    " + lineNumber + ", " + ceMessage + " " + whatToCorrect + "\n" +
								"    sinalizado: " + lineOfError + ", " + compilerMessage);
					}
					else {
						// the compiler is correct. Add to correctList the message
						// that the compiler signalled and the message of the test, given in @ce
						correctList.add(filename + "\r\n" +
								"The compiler message was: \"" + compilerMessage + "\"\r\n" +
								"The 'cep' message is:      \"" + ceMessage + "\"\r\n" );
						checkAnnotList(filename, program, outError, true);
					}
				}
			break;
			case "nce":

				++this.numSourceFilesWithAnnotNCE;
				if ( foundNCE )
					outError.println("More than one 'nce' metaobject calls in the same source file '" + filename + "'");
				foundNCE = true;
				if ( program.hasCompilationErrors() ) {
					int lineOfError = program.getCompilationErrorList().get(0).getLineNumber();
					message = program.getCompilationErrorList().get(0).getMessage();
					this.wereButShouldNotList.add(filename + ", " + lineOfError + ", " + message);
					checkAnnotList(filename, program, outError, false);
				}
				else {
					checkAnnotList(filename, program, outError, true);
				}
				break;
			case "annot":
				break;
			default:
				outError.println("Annotation '" + annotName + "' is illegal");
			}
		}
		if ( foundCE && foundNCE )
			outError.println("Calls to metaobjects 'ce' and 'nce' in the same source code '" + filename + "'");

	}


	void checkAnnotList(String filename, Program program, PrintWriter outError, boolean isTestCaseCorrect) {

		for ( MetaobjectAnnotation annot : program.getMetaobjectCallList() ) {
			if ( annot.getName().equals("annot") ) {
				ArrayList<Object> objParamList = annot.getParamList();
				String isCheck = (String ) objParamList.get(0);
				if ( !isCheck.equals("check") ) { continue; }

				int size = objParamList.size();
				// class Compiler has already checked that the first parameter is "check"
				for ( int i = 1; i < size; ++i ) {
					String checkName = (String ) objParamList.get(i);
					Integer weight = testNameWeightMap.get(checkName);
					if ( weight == null ) {
						outError.println("Error in file '" + filename + "': annotation 'annot' with parameter '"
								+ checkName + "' that is not in the following list: ");
						for (int k = 0; k < testNameWeightArray.length; k += 2 ) {
							String testName = (String ) testNameWeightArray[k];
							outError.println("    " + testName);
						}
					}
					else {
						if ( isTestCaseCorrect ) {
							String filenameList = checkNameFilenameListCompilerSucceededMap.get(checkName);
							if ( filenameList == null ) {
								checkNameFilenameListCompilerSucceededMap.put(checkName, filename);
							}
							else {
								checkNameFilenameListCompilerSucceededMap.put(checkName,  filenameList + " " + filename);
							}
						}
						else {
							String filenameList = checkNameFilenameListCompilerFailedMap.get(checkName);
							if ( filenameList == null ) {
								checkNameFilenameListCompilerFailedMap.put(checkName, filename);
							}
							else {
								checkNameFilenameListCompilerFailedMap.put(checkName,  filenameList + " " + filename);
							}

						}
					}
				}
			}
		}

	}

	private static void createGradeTable() {
		if ( testNameWeightArray.length % 2 != 0 ) {
			System.out.println("Internal error in table testNameWeightArray");
			System.exit(1);
		}
		testNameWeightMap = new HashMap<>();
		for (int i = 0; i < testNameWeightArray.length; i += 2 ) {
			String testName = (String ) testNameWeightArray[i];
			int weight   = (int ) testNameWeightArray[i+1];
			//Integer previousWeight = testNameWeightMap.get(testName);
			testNameWeightMap.put(testName, weight);
		}
	}

	/**
	 * map with entries of the form
	 *       test name, filenames of tests in which the compiler succeeded
	 * the second entry is taken from all Cianeto test cases of the compilation (multiple .ci files)
	 * It contains the filenames, separated by spaces, of the test cases in which
	 * the check 'test case' succeeded. For just one file, suppose there is a file
	 *      // OK_SEM01.ci
	 *      {@literal @}annot("check", "messageToself")
	 *      ...
	 * in which the compiler compiled correctly and produced correct code. There
	 * there will be an entry
	 *          "messageToself", "OK_SEM01.ci"
	 * in
	 */
	private Map<String, String> checkNameFilenameListCompilerSucceededMap;
	/**
	 * map with entries of the form
	 *       test name, filenames of tests in which the compiler failed
	 * see the other observations for checkNameFilenameListCompilerSucceededMap
	 */
	private Map<String, String> checkNameFilenameListCompilerFailedMap;


	/**
	 * number of very important tests in which the compiler failed
	 */
	private static int numVeryImportantFailed;
	/**
	 * number of important tests in which the compiler failed
	 */
	private static int numImportantFailed;
	/**
	 * number of little important tests in which the compiler failed
	 */
	private static int numLittleImportantFailed;

	static private Map<String, Integer> testNameWeightMap;
	static private Object testNameWeightArray[] = { // test name, weight
			"comparisonOperators", 1,
			"arithmeticOperators", 1,
			"message passing", 10,
			"logicalOperators", 1,
			"while", 1,
			"if", 1,
			"readInt", 1,
			"zero", 1,
			"parameterPassing", 3,
			"self", 5,
			"super", 5,
			"fieldAccess", 5,
			"messageToself", 10,
			"privateMethodRedefinedAsPublic", 3,
			"redefinedField", 3,
			"privateMethod", 3,
			"messagePassingPrivateMethod", 5,
			"inheritance", 10,
			"polymorphism", 10,
			"runNotFirstMethodOfProgram", 1,
			"repeatUntil", 1,
			"missingReturn", 1,
			"lowerUpperCaseIdentifiers", 1,
			"variableRedeclaration", 3,
			"typeError", 10,                  // typeErrorAssign typeErrorSearchMethod, typeErrorOutPrint
			"typeErrorSearchMethodSuper", 10,
			"typeErrorSearchMethod", 10,
			"typeErrorPlusPlus", 3,
			"typeErrorEqualEqualNotEqual", 5,
			"typeErrorAssignRightSideNotSubtypeLeftSide", 10,              // right-hand side of an assignment is not subtype of the left-hand side type
			"exprRightHandSideAssignment", 1,
			"typeNotFound", 5,
			"illegalTypeOrIdentifier", 1,
			"illegalClassName", 1,
			"breakOutsideWhileRepeatUntil", 3,
			"classExtendsItself", 1,
			"localVarRedeclared", 5,
			"wrongSubclassMethodSignature", 5,
			"methodFieldEqualNames", 5,
			"methodsEqualNames", 5,
			"methodReturnValueNotUsed", 1,
			"illegalReturnStatement", 3,
			"expressionExpected", 3,
			"typeErrorOutPrint", 1,
			"superWithoutSuperclass", 3,
			"missingOverride", 5,
			"identifierNotFound", 5,
			"methodRedefinition", 5,
			"missingRunMethodInProgram", 1,
			"missingClassProgram", 1,
			"parametersInMethodRunOfClassProgram", 1,
			"returnTypeInMethodRunOfClassProgram", 1,
			"privateMethodRunOfClassProgram", 1,
			"missingClassInNew", 5,
			"booleanExprRepeatUntil", 1,
			"scopeLocalVarField", 3,
			"whileBreak", 1,
			"repeatUntilBreak", 1,
			"methodOverridingSameSignature", 1,
			"selfAsExpression", 1,
			"methodSearchSuperclass", 5,
			"openAsIdentifier", 1,
			"localVarSameClassName", 3,
			"localVarSameMethodName", 3,
			"nilAssign", 5,
			"nilEqualEqualNotEqual", 5,
			"supertypeAssignSelf", 5,
			"assert", 1,


	};

	private static final int dotJavaLength = ".java".length();

	private void compileExecJava(Program program, String filename) {
		String javaFilename = filename;
		try {
			int lastSlash = filename.lastIndexOf(File.separator);
			if ( lastSlash > 0 ) {
				javaFilename = filename.substring(lastSlash + 1);
			}

			int i = javaFilename.indexOf(".ci");
			if ( i > 0 ) {
				javaFilename = javaFilename.substring(0, i) + ".java";
			}
			String className = javaFilename.substring(0, javaFilename.length() - dotJavaLength);

			javaFilename = this.dirToJavaOutput + File.separator + javaFilename;

			try ( FileOutputStream fos = new FileOutputStream(javaFilename);
					PrintWriter printWriter = new PrintWriter(fos, true) ) {
				PW pw = new PW(printWriter);
				try {
					program.setMainJavaClassName(className);
					program.genJava(pw);
				}
				catch( Throwable e ) {
					System.out.println("Exception '" + e.getClass().getName() + "' thrown while calling method 'genJava' on file '"
							+ javaFilename + "'");
					this.filesCompilerDidNotProducedJavaCode.add(javaFilename);
					return ;
				}
			}
			catch (Throwable e ) {
				System.out.println("Error when creating file '"
						+ javaFilename + "'");
				this.filesCompilerDidNotProducedJavaCode.add(javaFilename);
				return ;
			}

			final Runtime rt = Runtime.getRuntime();
			Process proc = null;
			try {
				proc = rt.exec("javac " + javaFilename);
			}
			catch(final SecurityException e ) {
				System.out.println("Error in calling 'javac'. Probably this program is not in the PATH variable");
			}
			catch ( final IOException e ) {
				System.out.println("Error in calling 'javac'. There was an input/output error. Message: " + e.getMessage());
			}
			catch ( final NullPointerException e ) {
				System.out.println("Error in calling 'javac'. Probably an internal error of this program");
			}
			catch ( final IllegalArgumentException e ) {
				System.out.println("Internal error in '" + this.getClass().getName() +
						"'. Arguments to 'javac' are not well built");
			}
			if ( proc == null ) {
				this.filesWithJavaClassesWithCompilationErrors.add(javaFilename);
				return ;
			}
			final InputStream stderr = proc.getErrorStream();
			final InputStreamReader isr = new InputStreamReader(stderr);
			final ArrayList<String> outList = new ArrayList<>();
			String line = null;
			try ( BufferedReader br = new BufferedReader(isr) ) {
				while ( (line = br.readLine()) != null) {
					outList.add(line);
				}
			}
			int exitVal = proc.waitFor();
			if ( exitVal != 0 ) {
				System.out.println("Error when compiling the Java code of file '" + javaFilename +
						"' generated by the Cianeto compiler (exit code " + exitVal + ")");
				for ( final String s : outList ) {
					System.out.println(s);
				}
				this.filesWithJavaClassesWithCompilationErrors.add(javaFilename);
			}
			else {
				// call the program
				System.out.println("Executing the Java code of file '" + javaFilename + "'");
				Process p = null;
				try {
					p = Runtime.getRuntime().exec("java -cp " + this.dirToJavaOutput + " " + className);
				}
				catch(final SecurityException e ) {
					System.out.println("Error in calling the Java interpreter on file '" + javaFilename + "'. Probably this program is not in the PATH variable");
				}
				catch ( final IOException e ) {
					System.out.println("Error in calling the Java interpreter on file '" + javaFilename + "'. There was an input/output error. Message: " + e.getMessage());
				}
				catch ( final NullPointerException e ) {
					System.out.println("Error in calling the Java interpreter on file '" + javaFilename + "'. Probably an internal error of this program");
				}
				catch ( final IllegalArgumentException e ) {
					System.out.println("Internal error in '" + this.getClass().getName() +
							"' on file '" + javaFilename + "'. Arguments to 'java' are not well built");
				}

				if ( p == null ) {
					this.filesWithWrongGeneratedJavaClasses.add(javaFilename);
					return ;
				}


				exitVal = p.waitFor();
				try ( BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream())) ) {
					while((line = error.readLine()) != null){
						System.out.println(line);
						if ( line.startsWith("Error:") ) {
							this.filesWithWrongGeneratedJavaClasses.add(javaFilename);
							return ;
						}
					}
				}
				// error.close();
				String alloutput = "";
				String firstLine = "";
				int lineCount = 0;
				try ( BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream())) ) {
					while ( (line = input.readLine()) != null ) {
						System.out.println(line);
						if ( lineCount == 0 ) {
							firstLine = line;
						}
						else {
							alloutput += line;
						}
						++lineCount;
					}
				}
				String result = removeExtraSpacesFirstLine(firstLine).trim();
				alloutput = removeExtraSpaces(alloutput).trim();


				if ( result.equals(alloutput) ) {
					this.filesWithCorrectlyGeneratedJavaClasses.add(javaFilename);
					//System.out.println("File '" + javaFilename + "' executed correctly");
				}
				else {
					this.filesWithWrongGeneratedJavaClasses.add(javaFilename);
//					System.out.println("Class '" + className + "' of file '" + filename +
//							"' does not generate correct code. It should generate '" + result + "' but it is generating '" +
//							alloutput + "'" );
//					return ;
				}

//				final OutputStream outputStream = p.getOutputStream();
//				final PrintStream printStream = new PrintStream(outputStream);
//				printStream.println();
//				printStream.flush();
//				printStream.close();


				if ( exitVal != 0 ) {
					this.filesWithWrongGeneratedJavaClasses.add(javaFilename);
					System.out.println("Error when executing the code generated by the Java compiler (exit code " + exitVal + ")");
				}
			}


		}
		catch ( Throwable e ) {
			System.out.println("Exception '" + e.getClass().getName() + "' thrown while creating/compiling/executing on file '"
					+ javaFilename + "'");
			return ;
		}


	}


	private static String removeExtraSpaces(String str) {
		String s = "";
		int size = str.length();
		for (int i = 0; i < size; ) {
			char ch = str.charAt(i);
			if ( ch == '\r' || ch == '\n' ) {
				s += " ";
				++i;
			}
			else {
				if ( Character.isWhitespace(ch) ) {
					s += " ";
					++i;
					while ( i < size && Character.isWhitespace(str.charAt(i)) ) {
						++i;
					}
				}
				else {
					s += ch;
					++i;
				}
			}
		}
		return s;
	}

	private static String removeExtraSpacesFirstLine(String str) {
		String s = "";
		int size = str.length();
		for (int i = 0; i < size; ) {
			char ch = str.charAt(i);

			if ( ch != '\r' && ch != '\n' ) {
				// do not add end of line characters
				if ( Character.isWhitespace(ch) ) {
					// skip all white spaces, add just one
					s += " ";
					++i;
					while ( i < size && Character.isWhitespace(str.charAt(i)) ) {
						++i;
					}
				}
				else {
					s += ch;
					++i;
				}
			}
			else {
				++i;
			}
		}
		return s;
	}



	ArrayList<String> shouldButWereNotList, wereButShouldNotList, wereButWrongLineList, correctList;

	ArrayList<String> nullPointerAndOtherExceptionsList;
	/**
	 * number of tests with errors. That is, the number of tests in which there is an metaobject annotation  {@literal @}cep.
	 */
	private int	numSourceFilesWithAnnotCEP;

	/**
	 * number of source files compiled in which there is an annotation 'nce'
	 */
	private int numSourceFilesWithAnnotNCE;


	private List<String> filesWithWrongGeneratedJavaClasses;
	private List<String> filesWithJavaClassesWithCompilationErrors;
	private List<String> filesWithCorrectlyGeneratedJavaClasses;
	private List<String> filesCompilerDidNotProducedJavaCode;
}



class TupleCheckNameText implements Comparable<TupleCheckNameText> {
	public TupleCheckNameText(String checkName, int importance, String text, int numFiles) {
		this.checkName = checkName;
		this.importance = importance;
		this.text = text;
		this.numFiles = numFiles;
	}

	@Override
	public int compareTo(TupleCheckNameText other) {
		if ( importance > other.importance ) {
			return -1;
		}
		else if ( importance < other.importance ) {
			return 1;
		}
		else {
			return 0;
		}
	}

	String checkName;
	int importance;
	String text;
	int numFiles;

}
