package lib.intermediate.implementation;

import lib.intermediate.*;


import java.util.ArrayList;
import java.util.HashMap;

public class SymTabEntryImpl extends HashMap<SymTabKey, Object> implements SymTabEntry {
    private final String name;
    private final SymTab symTab;

    private final ArrayList<Integer> lineNumbers;

    public SymTabEntryImpl(String name, SymTab symTab){
        this.name = name;
        this.symTab = symTab;
        this.lineNumbers = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SymTab getSymTab() {
        return symTab;
    }

    @Override
    public void appendLineNumber(int lineNumber) {
        lineNumbers.add(lineNumber);
    }

    @Override
    public ArrayList<Integer> getLineNumbers() {
        return lineNumbers;
    }

    @Override
    public void setAttribute(SymTabKey key, Object value) {
        put(key,value);
    }

    @Override
    public Object getAttribute(SymTabKey key) {
        return get(key);
    }
}
