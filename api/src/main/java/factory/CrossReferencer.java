package factory;

import lib.intermediate.SymTab;
import lib.intermediate.SymTabStack;

public class CrossReferencer {
    private static final int NAME_WIDTH = 16;

    private static final String NAME_FORMAT = "%-" + NAME_WIDTH + "s";
    private static final String NUMBERS_LABEL = " Line numbers    ";
    private static final String NUMBERS_UNDERLINE = " ----------   ";
    private static final String NUMBER_FORMAT = "%03d";

    private static final int LABEL_WIDTH = NUMBERS_LABEL.length();
    private static final int INDENT_WIDTH = NAME_WIDTH + LABEL_WIDTH;

    private static final StringBuilder INDENT = new StringBuilder(INDENT_WIDTH);
    static {
        for (int i = 0; i < INDENT_WIDTH; ++i) INDENT.append(" ");
    }

    public void print(SymTabStack symTabStack){
        System.out.println("\n==== CROSS-REFERENCE TABLE ====");
        printColumnHeadings();

        printSymTab(symTabStack.getLocalSymTab());
    }

    private void printColumnHeadings(){
        System.out.println();
        System.out.println(String.format(NAME_FORMAT,"Identifier") + NUMBERS_LABEL);
        System.out.println(String.format(NAME_FORMAT, "----------") + NUMBERS_UNDERLINE);
    }
    private void printSymTab(SymTab symTab){
        var sorted = symTab.sortedEntries();
        for (var entry: sorted) {
            var lineNumbers = entry.getLineNumbers();
            System.out.print(String.format(NAME_FORMAT, entry.getName()));
            assert lineNumbers != null;
            for (var lineNumber: lineNumbers) {
                System.out.print(String.format(NUMBER_FORMAT,lineNumber));
            }
            System.out.println();
        }
    }
}
