package lib.intermediate.implementation;

import factory.SymTabFactory;
import lib.intermediate.*;
import java.util.*;
;

public class SymTabImpl extends TreeMap<String,SymTabEntry> implements SymTab {
    private final int nestingLevel;

    public SymTabImpl(int nestingLevel){
        this.nestingLevel = nestingLevel;
    }

    @Override
    public int getNestingLevel() {
        return nestingLevel;
    }

    @Override
    public SymTabEntry enter(String name) {
        var entry = SymTabFactory.createSymTabEntry(name, this);

        put(name,entry);

        return entry;
    }

    @Override
    public SymTabEntry lookup(String name) {
        return get(name);
    }

    @Override
    public ArrayList<SymTabEntry> sortedEntries() {
        var entries = values();

        var iter = entries.iterator();

        var list = new ArrayList<SymTabEntry>(size());

        while (iter.hasNext()){
            list.add(iter.next());
        }

        return list;
    }
}
