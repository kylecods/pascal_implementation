package lib.intermediate;

public interface SymTabStack {

    int getCurrentNestingLevel();

    SymTab getLocalSymTab();

    SymTabEntry enterLocal(String name);

    SymTabEntry lookupLocal(String name);

    SymTabEntry lookup(String name);
}
