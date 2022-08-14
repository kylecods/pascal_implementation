package lib.interpreter.executors;

import lib.intermediate.ICodeNode;
import lib.interpreter.Executor;

public class CompoundExecutor extends StatementExecutor{
    public CompoundExecutor(Executor parent) {
        super(parent);
    }

    public Object execute(ICodeNode node){
        var statementExecutor = new StatementExecutor(this);

        var children = node.getChildren();

        for (var child: children) {
            statementExecutor.execute(child);
        }
        return null;
    }
}
