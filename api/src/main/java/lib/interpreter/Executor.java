package lib.interpreter;

import lib.backend.Backend;
import lib.intermediate.ICode;
import lib.intermediate.SymTab;
import lib.intermediate.SymTabStack;
import lib.interpreter.executors.StatementExecutor;
import lib.message.Message;
import lib.message.MessageType;

public class Executor extends Backend {
    protected static int executionCount;
    protected static RuntimeErrorHandler errorHandler;

    static {
        executionCount = 0;
        errorHandler = new RuntimeErrorHandler();
    }
    public Executor(){}

    public Executor(Executor parent){
        super();
    }
    @Override
    public void process(ICode iCode, SymTabStack symTabStack) throws Exception {
        this.symTabStack = symTabStack;
        this.iCode = iCode;

        var startTime  = System.currentTimeMillis();

        var rootNode = iCode.getRoot();
        var statementExecutor = new StatementExecutor(this);
        statementExecutor.execute(rootNode);

        var elapsedTime = (System.currentTimeMillis() - startTime) /1000f;

        int runtimeErrors = errorHandler.getErrorCount();

        sendMessage(new Message(MessageType.INTERPRETER_SUMMARY, new Number[]{
                executionCount,
                runtimeErrors,
                elapsedTime
        }));
    }
}
