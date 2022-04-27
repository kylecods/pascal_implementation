package lib.intermediate;

import java.util.*;

public interface SymTab {

    int getNestingLevel();
    SymTabEntry enter(String name);

    SymTabEntry lookup(String name);

    ArrayList<SymTabEntry> sortedEntries();
}
