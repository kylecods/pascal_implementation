package lib.interpreter;

import lib.backend.Backend;
import lib.intermediate.ICode;
import lib.intermediate.SymTab;
import lib.message.Message;
import lib.message.MessageType;

public class Executor extends Backend {
    @Override
    public void process(ICode iCode, SymTab symTab) throws Exception {
        var startTime  = System.currentTimeMillis();

        var elapsedTime = (System.currentTimeMillis() - startTime) /1000f;

        int executionCount = 0;
        int runtimeErrors = 0;

        sendMessage(new Message(MessageType.INTERPRETER_SUMMARY, new Number[]{
                executionCount,
                runtimeErrors,
                elapsedTime
        }));
    }
}
