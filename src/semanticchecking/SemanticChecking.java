package semanticchecking;

import java.util.ArrayList;
import java.util.HashMap;

import ast.FieldDec;
import ast.LocalDec;
import ast.MethodDec;
import ast.TypeCianetoClass;
import ast.Variable;

public class SemanticChecking {

	private HashMap<String, TypeCianetoClass> hashClasses = new HashMap<String, TypeCianetoClass>();
	private HashMap<String, FieldDec> hashGlobalVariables = new HashMap<String, FieldDec>();
	private HashMap<String, LocalDec> hashLocalVariables = new HashMap<String, LocalDec>();
	private HashMap<String, Variable> hashParameters = new HashMap<String, Variable>();
	private HashMap<String, MethodDec> hashMethodsList = new HashMap<String, MethodDec>();
	private String currentClassName = "";
	private TypeCianetoClass superClass;
	private ArrayList<Variable> paramList;
	private Variable currentMethod;
	
	public void setCurrentClassName(String currentClassName) {
		this.currentClassName = currentClassName;
	}
	
	public void setSuperClass(TypeCianetoClass superClass) {
		this.superClass = superClass;
	}
	
	public TypeCianetoClass getSuperClass() {
		return superClass;
	}
	
	public TypeCianetoClass getTypeCianetoClass(String className) {
		return hashClasses.get(className);
	}
	
	public Variable getCurrentMethodVariable() {
		return currentMethod;
	}
	
	public String getCurrentClassName() {
		return currentClassName;
	}
	
	public FieldDec getFieldDec(String key) {
		return hashGlobalVariables.get(key);
	}
	
	public LocalDec getLocalDec(String key) {
		return hashLocalVariables.get(key);
	}
	
	public Variable getParamVariable(String key) {
		return hashParameters.get(key);
	}
	
	public MethodDec getMethodDec(String key) {
		return hashMethodsList.get(key);
	}
	
	public HashMap<String, MethodDec> getHashMethodsList() {
		return hashMethodsList;
	}
	
	public void putInHashClasses(String key, TypeCianetoClass value) {
		hashClasses.put(key, value);
	}
	
	public void putInHashMethodsList(String key, MethodDec value) {
		hashMethodsList.put(key, value);
	}
	
	public void putInLocalVariables(String key, LocalDec value) {
		hashLocalVariables.put(key, value);
	}
	
	public void putInHashParameter(String key, Variable value) {
		hashParameters.put(key, value);
	}
	
	public void putInHashGlobalVariables(String key, FieldDec value) {
		hashGlobalVariables.put(key, value);
	}
	
	public void clearHashMethodList() {
		hashMethodsList.clear();
	}
	
	public void clearHashGlobalVariables() {
		hashGlobalVariables.clear();
	}
	
	public void clearHashParameters() {
		hashParameters.clear();
	}
	
	public void clearHashLocalVariables() {
		hashLocalVariables.clear();
	}
}
