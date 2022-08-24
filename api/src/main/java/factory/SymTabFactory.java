package factory;

import lib.intermediate.*;
import lib.intermediate.implementation.*;

public class SymTabFactory {
    public static SymTabStack createSymTabStack(){
        return new SymTabStackImpl();
    }

    public static SymTab createSymTab(int nestingLevel){
        return new SymTabImpl(nestingLevel);
    }

    public static SymTabEntry createSymTabEntry(String name, SymTab symTab){
        return new SymTabEntryImpl(name,symTab);
    }
}
