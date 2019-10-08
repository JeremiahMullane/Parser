import java.util.*;

public class ConcreteSyntax {
    // Recursive descent parser that inputs a Jay program and 
    // generates its abstract syntax.  Each method corresponds to
    // a concrete syntax grammar rule, which appears as a comment
    // at the beginning of the method.
    
    Token token;          // current token from the input stream
    TokenStream input;
    
public ConcreteSyntax(TokenStream ts) { // Open the Jay source program
    input = ts;                          // as a TokenStream, and
    token = input.nextToken();            // retrieve its first Token
}
  
private void match (String s) {
    if (token.value.equals(s))
        token = input.nextToken();
    else
        SyntaxError(s);
}
  
public Program program() {
    // Program --> void main ( ) '{' Declarations Statements '}'
    String[] header = {"void", "main", "(", ")"};
    Program p = new Program();
    for (int i=0; i<header.length; i++)        // bypass "void main ( )"
        match(header[i]);
    match("{");
    p.decpart = declarations();
    p.body = statements();
    match("}");
    return p;
}
  
private Declarations declarations () {
    // Declarations --> { Declaration }*
    Declarations ds = new Declarations ();  
    while (token.value.equals("int") || token.value.equals("boolean")) {
        declaration(ds);
    }  
    return ds;                
}
  
private void declaration (Declarations ds) {
    // Declaration  --> Type Identifiers ;
    Type t = type();
    identifiers(ds, t);
    match(";");
System.out.print("\n");
}
  
private Type type () {
    // Type  -->  int | boolean
    Type t = null;
    if (token.value.equals("int"))
        t = new Type(token.value);
    else if (token.value.equals("boolean"))
        t = new Type(token.value);
    else SyntaxError("int | boolean");
    token = input.nextToken(); // pass over the type
    return t;                
}
  
private void identifiers (Declarations ds, Type t) {
    // Identifiers  --> Identifier { , Identifier }*
    Declaration d = new Declaration (); // first declaration
    d.t = t;                            // its type
    if (token.type.equals("Identifier")) {
        d.v = new Variable();       
        d.v.id = token.value;         // its value
        ds.addElement(d);
        token = input.nextToken();
        while (token.value.equals(",")) {
            d = new Declaration ();             // next declaration
            d.t = t;                            // its type
            token = input.nextToken();
            if (token.type.equals("Identifier")) {
                d.v = new Variable();           // its value
                d.v.id = token.value;
                ds.addElement(d);
                token = input.nextToken(); // get "," or ";"
            }
            else SyntaxError("Identifier");
        }
    }
    else SyntaxError("Identifier");
}
  
private Statement statement() {
    // Statement --> ; | Block | Assignment | IfStatement | WhileStatement
    Statement s = new Skip();
    if (token.value.equals(";")) {             // Skip
System.out.print("SKIP\n");
        token = input.nextToken();
        return s;               
    }                  
    else if (token.value.equals("{")) {        // Block
        token = input.nextToken();
        s = statements();
        match("}");
    }
    else if (token.value.equals("if"))         // IfStatement 
        s = ifStatement();
    else if (token.value.equals("while"))      // WhileStatement 
        s = whileStatement();
    else if (token.type.equals("Identifier"))  // Assignment
        s = assignment();
    else SyntaxError("Statement");
    return s;
}
  
private Block statements () {
    // Block --> '{' Statements '}'
    Block b = new Block();
    while (!token.value.equals("}")) {
	Statement s = statement();
	b.members.addElement(s);
    }
    return b;
}
  
private Assignment assignment () {
    // Assignment --> Identifier = Expression ;
    Assignment a = new Assignment();
    if (token.type.equals("Identifier")) {
        a.target = new Variable();
        a.target.id = token.value;
        token = input.nextToken();
        match("=");
        a.source = expression();
        match(";");
    }
    else SyntaxError("Identifier");
    return a;
}
    
private Expression expression () {
    // Expression --> Conjunction { || Conjunction }*
    Binary b; Expression e;
    e = conjunction();
    while (token.value.equals("||")) {
        b = new Binary();
        b.term1 = e;
        b.op = new Operator(token.value);
        token = input.nextToken();
        b.term2 = conjunction();
        e = b;
    }
    return e;     
}
  
private Expression conjunction () {
    // Conjunction --> Relation { && Relation }*
    Binary b; Expression e;
    e = relation();
    while (token.value.equals("&&")) {
        b = new Binary();
        b.term1 = e;
        b.op = new Operator(token.value);
        token = input.nextToken();
        b.term2 = relation();
        e = b;
    }
    return e;
}
  
private Expression relation () {
    // Relation --> Addition [ < | <= | > | >= | == | != ] Addition }*
    Binary b; Expression e;
    e = addition();
    while (token.value.equals("<") || token.value.equals("<=") ||
		token.value.equals(">") || token.value.equals(">=") ||
		token.value.equals("==") || token.value.equals("!=")) {
        b = new Binary();
        b.term1 = e;
        b.op = new Operator(token.value);
        token = input.nextToken();
        b.term2 = addition();
        e = b;
    }
    return e;
}
  
private Expression addition () {
    // Addition --> Term { [ + | - ] Term }*
    Binary b; Expression e;
    e = term();
    while (token.value.equals("+") || token.value.equals("-")) {
        b = new Binary();
        b.term1 = e;
        b.op = new Operator(token.value);
        token = input.nextToken();
        b.term2 = term();
        e = b;
    }
    return e;
}
  
private Expression term () {
    // Term --> Negation { [ '*' | / ] Negation }*
    Binary b; Expression e;
    e = negation();
    while (token.value.equals("*") || token.value.equals("/")) {
        b = new Binary();
        b.term1 = e;
        b.op = new Operator(token.value);
        token = input.nextToken();
        b.term2 = negation();
        e = b;
    }
    return e;
}
  
private Expression negation() {
    // Negation --> { ! }opt Factor 
    Unary u;
    if (token.value.equals("!")) {
        u = new Unary();
        u.op = new Operator(token.value);
        token = input.nextToken();
        u.term = factor();
        return u;
    }
    else return factor();
}
  
private Expression factor () {
    // Factor --> Identifier | Literal | ( Expression )
    Expression e = null;
    if (token.type.equals("Identifier")) {
	Variable v = new Variable();
	v.id = token.value;
	e = v;
	token = input.nextToken();
    }
    else if (token.type.equals("Literal")){
	Value v = null;
	if (token.value.equals("true")) {
	    v = new Value(true);
	}
	else if (token.value.equals("false")) {
	    v = new Value(false);
	}
	else if (isInteger(token.value)) {
	    v = new Value(Integer.valueOf(token.value).intValue());
	}
	e = v;
	token = input.nextToken();
    }
    else if (token.value.equals("(")) {
	match("(");
	e = expression();
	match(")");
    }
    else SyntaxError("Expression");
    return e;
}
  
private Conditional ifStatement () {
    // IfStatement --> if ( Expression ) Statement { else Statement }opt
    Conditional c = new Conditional();
    match("if");
    match("(");
    c.test = expression();
    match(")");
    c.thenbranch = statement();
    if (token.value.equals("else")) {
	match("else");
	c.elsebranch = statement();
    }
    return c;
}
  
private Loop whileStatement () {
    // WhileStatement --> while ( Expression ) Statement
    Loop l = new Loop();
    match("while");
    match("(");
    l.test = expression();
    match(")");
    l.body = statement();
    return l;
}
  
private boolean isInteger(String s) {
    boolean result = true;
    for (int i=0; i<s.length(); i++)
        if ('0' > s.charAt(i) || '9' < s.charAt(i))
            result = false;
    return result; 
}
  
private void SyntaxError(String tok) {
    System.out.println("Syntax error: expecting: " + tok 
		     + "; saw: " + token.type + " = " + token.value);
    System.exit(0);
}
  
}
