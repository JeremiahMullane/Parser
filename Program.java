//Program.java
// Abstract syntax for the language Jay with display methods added

import java.util.Vector;

class Indenter {
   public int level;
   public Indenter(int nextlevel) { level = nextlevel; }

   public void display(String message) {
       // displays a message on the next line at the current level
      String tab = "";
      System.out.println();
      for (int i=0; i<level; i++)
          tab = tab + "  ";
      System.out.print(tab + message);
   }
}

class Program {
// Program = Declarations decpart ; Block body
    Declarations decpart;
    Block body;
    public void display () { 
        int level = 0;
        Indenter indent = new Indenter(level);
        indent.display("Program (abstract syntax): "); 
            decpart.display(level+1); 
            body.display(level+1);
        System.out.println(); 
    }
}

class Declarations extends Vector { 
// Declarations = Declaration *
//                (a Vector of declarations d1, d2, ..., dn)
    public void display (int level) { 
          Indenter indent = new Indenter(level);
          indent.display(getClass().toString().substring(6) + ": ");
          indent.display("  Declarations = {");
          for (int i=0; i<size(); i++) {
              ((Declaration)elementAt(i)).display(); 
              if (i<size()-1) System.out.print(", ");
          }
          System.out.print("}");
    }
}

class Declaration {
// Declaration = Variable v; Type t
    Variable v;
    Type t;
    public void display () { 
       System.out.print("<" + v.id + ", " + t.id + ">"); }
    }

class Type {
// Type = int | boolean | undefined
    final static String INTEGER = "int";
    final static String BOOLEAN = "boolean";
    final static String UNDEFINED = "undefined";
    String id;

	Type (String t) { id = t; }
    public boolean isBoolean ( ) { return id.equals(BOOLEAN); }
    public boolean isInteger ( ) { return id.equals(INTEGER); }
    public boolean isUndefined ( ) { return id.equals(UNDEFINED); }
}

class Statement {
// Statement = Skip | Block | Assignment | Conditional | Loop

    public void display (int level) {
         Indenter indent = new Indenter(level);
         indent.display(getClass().toString().substring(6) + ": ");
    }
}

class Skip extends Statement { 

    public void display (int level) {
       super.display(level);
    }
}

class Block extends Statement {
// Block = Statement *
//         (a Vector of members)
    public Vector members = new Vector();

    public void display(int level) {
        super.display(level);
        for (int i=0; i<members.size(); i++)
            ((Statement)members.elementAt(i)).display(level+1); 
    }
}

class Assignment extends Statement {
// Assignment = Variable target; Expression source
    Variable target;
    Expression source;

    public void display (int level) {
       super.display(level);
       target.display(level+1);
       source.display(level+1);
    }
}

class Conditional extends Statement {
// Conditional = Expression test; Statement thenbranch, elsebranch
    Expression test;
    Statement thenbranch, elsebranch; 
                   // elsebranch == null means "if... then" statement

    public void display (int level) {
       super.display(level);
       test.display(level+1);
       thenbranch.display(level+1);
       if (elsebranch!=null)
          elsebranch.display(level+1); 
    }
}

class Loop extends Statement {
// Loop = Expression test; Statement body
    Expression test;
    Statement body;

    public void display (int level) {
       super.display(level);
       test.display(level+1);
       body.display(level+1);
    }
}

class Expression {
// Expression = Variable | Value | Binary | Unary

    public void display (int level) {
         Indenter indent = new Indenter(level);
         indent.display(getClass().toString().substring(6) + ": ");
    }
} 

class Variable extends Expression {
// Variable = String id
    String id;
    public boolean equals (Object obj) {
       String s = ((Variable) obj).id;
	  return id.equalsIgnoreCase(s); // case-insensitive identifiers
    } 
    public int hashCode ( ) { return id.hashCode( ); }

    public void display (int level) {
         super.display(level);
         System.out.print(id);
    }
}

class Value extends Expression {
// Value = int intValue | boolean boolValue
    Type type;
    int intValue; boolean boolValue;
    Value (int i)     { type = new Type(Type.INTEGER); intValue = i; }
    Value (boolean b) { type = new Type(Type.BOOLEAN); boolValue = b; }
    Value ( )  { type = new Type(Type.UNDEFINED); }

    public void display (int level) {
        super.display(level);
        if (type.isInteger()) 
           System.out.print(intValue);
        else if (type.isBoolean())
           System.out.print(boolValue);
    }
}

class Binary extends Expression {
// Binary = Operator op; Expression term1, term2
    Operator op;
    Expression term1, term2;

    public void display (int level) {
       super.display(level);
       op.display(level+1);
       term1.display(level+1);
       term2.display(level+1);
    }
}

class Unary extends Expression {
// Unary = Operator op; Expression term
    Operator op;
    Expression term;

    public void display (int level) {
       super.display(level);
       op.display(level+1);
       term.display(level+1);
    }
}

class Operator {
// Operator = BooleanOp | RelationalOp | ArithmeticOp | UnaryOp
// BooleanOp = && | ||
    final static String AND = "&&";
    final static String OR  = "||";
// RelationalOp = < | <= | == | != | >= | >
    final static String LT = "<";
    final static String LE = "<=";
    final static String EQ = "==";
    final static String NE = "!=";
    final static String GT = ">";
    final static String GE = ">=";
// ArithmeticOp = + | - | * | /
    final static String PLUS = "+";
    final static String MINUS = "-";
    final static String TIMES = "*";
    final static String DIV = "/";
// UnaryOp = !    
    final static String NOT = "!";

    String val;

    Operator (String s) { val = s; }
    
    boolean BooleanOp ( ) { return val.equals(AND) || val.equals(OR); }
    boolean RelationalOp ( ) { 
      return val.equals(LT) || val.equals(LE) || val.equals(EQ) 
          || val.equals(NE) || val.equals(GT) || val.equals(GE); 
    }
    boolean ArithmeticOp ( ) { 
      return val.equals(PLUS) || val.equals(MINUS)
          || val.equals(TIMES) || val.equals(DIV); 
    }
    boolean UnaryOp ( ) { return val.equals(NOT); }

    public void display (int level) {
         Indenter indent = new Indenter(level);
         indent.display(getClass().toString().substring(6) + ": " + val);
    }
}