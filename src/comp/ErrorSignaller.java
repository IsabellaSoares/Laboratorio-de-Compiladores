/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565 
*/
package comp;
import java.io.PrintWriter;
import java.util.ArrayList;
import lexer.Lexer;

public class ErrorSignaller {


    public ErrorSignaller( PrintWriter out, ArrayList<CompilationError> compilationErrorList ) {
          // output of an error is done in out
        this.out = out;
        foundCompilerError = false;
        this.compilationErrorList = compilationErrorList;
    }


    public void setLexer( Lexer lexer ) {
        this.lexer = lexer;
    }


    public boolean wasAnErrorSignalled() {
        return foundCompilerError;
    }


    public void showError( String strMessage ) {
        showError( strMessage, false);
    }


    public void showError( String strMessage, boolean goPreviousToken ) {
        // is goPreviousToken is true, the error is signalled at the line of the
        // previous token, not the last one.
        if ( goPreviousToken )
           showError( strMessage, lexer.getLineBeforeLastToken(),
                 lexer.getLineNumberBeforeLastToken() );
        else
           showError( strMessage, lexer.getCurrentLine(), lexer.getLineNumber() );
    }


   public void showError( String errorMessage, String lineWithError, int lineNumber ) {
      if ( out.checkError() ) {
         out.flush();
         System.out.println("Error in signaling an error");
      }
      foundCompilerError = true;
      CompilationError newError = new CompilationError(errorMessage, lineNumber, lineWithError);
      compilationErrorList.add(newError);
      throw new CompilerError(errorMessage);
   }




	public ArrayList<CompilationError> getCompilationErrorList() {
		return compilationErrorList;
	}



    private Lexer lexer;
    private PrintWriter out;
    private boolean foundCompilerError;

    private ArrayList<CompilationError> compilationErrorList;

}

/*
begin expected

*/