package lib.intermediate;

import java.util.*;

public interface SymTabEntry {

    String getName();

    SymTab getSymTab();

    void appendLineNumber(int lineNumber);

    ArrayList<Integer> getLineNumbers();

    void setAttribute(SymTabKey key, Object value);

    Object getAttribute(SymTabKey key);
}
