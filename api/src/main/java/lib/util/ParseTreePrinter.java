package lib.util;

import lib.intermediate.ICode;
import lib.intermediate.ICodeNode;
import lib.intermediate.SymTabEntry;
import lib.intermediate.implementation.ICodeNodeImpl;

import java.io.PrintStream;
import java.util.ArrayList;

public class ParseTreePrinter {
    private static final int INDENT_WIDTH = 4;
    private static final int LINE_WIDTH = 80;

    private PrintStream ps;
    private int length;
    private String indent;
    private String indentation;
    private StringBuilder line;

    public ParseTreePrinter(PrintStream ps){
        this.ps = ps;
        this.length = 0;
        this.indentation = "";
        this.line = new StringBuilder();


        this.indent = "";
        for (int i = 0; i < INDENT_WIDTH; i++){
            this.indent += " ";
        }
    }

    public void print(ICode iCode){
        ps.println("\n===== INTERMEDIATE CODE =====\n");
        printNode((ICodeNodeImpl) iCode.getRoot());
        printLine();
    }

    private void printNode(ICodeNodeImpl node){
        append(indentation); append("<" + node.toString());

        printAttributes(node);
        printTypeSpec(node);

        var childNodes = node.getChildren();

        if((childNodes != null) && (childNodes.size() > 0)){
            append(">");
            printLine();
            printChildNodes(childNodes);
            append(indentation); append("</" + node.toString() + "/>");
        }else{
            append(" "); append("/>");
        }
        printLine();
    }
    private void printAttributes(ICodeNodeImpl node){
        String saveIndetation = indentation;
        indentation += indent;
        var attributes = node.entrySet();
        var it = attributes.iterator();

        while (it.hasNext()){
            var attribute = it.next();
            printAttribute(attribute.getKey().toString(), attribute.getValue());
        }
        indentation = saveIndetation;
    }

    private void printAttribute(String keyString, Object value){
        var isSymTabEntry = value instanceof SymTabEntry;

        var valueString = isSymTabEntry ? ((SymTabEntry) value).getName() : value.toString();

        var text = keyString.toLowerCase() + "=\"" + valueString + "\"";
        append(" "); append(text);

        if(isSymTabEntry){
            int level = ((SymTabEntry) value).getSymTab().getNestingLevel();
            printAttribute("LEVEL",level);
        }
    }

    private void printChildNodes(ArrayList<ICodeNode> childNodes){
        var saveIndentation = indentation;
        indentation += indent;

        for (ICodeNode child: childNodes) {
            printNode((ICodeNodeImpl) child);
        }
        indentation = saveIndentation;
    }
    private void printTypeSpec(ICodeNodeImpl node){}

    private void append(String text){
        int textLength = text.length();
        boolean lineBreak = false;
        if(length + textLength > LINE_WIDTH){
            printLine();
            line.append(indentation);
            length = indentation.length();
            lineBreak = true;
        }
        if(!(lineBreak && text.equals(" "))){
            line.append(text);
            length += textLength;
        }
    }
    private void printLine(){
        if(length > 0){
            ps.println(line);
            line.setLength(0);
            length = 0;
        }
    }
}

