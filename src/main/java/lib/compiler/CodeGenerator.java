package lib.compiler;

import lib.backend.Backend;
import lib.intermediate.ICode;
import lib.intermediate.SymTab;
import lib.message.Message;
import lib.message.MessageType;

public class CodeGenerator extends Backend {
    @Override
    public void process(ICode iCode, SymTab symTab) throws Exception {
        var startTime = System.currentTimeMillis();
        var elapsedTime = (System.currentTimeMillis() - startTime) /1000f;

        int instructionCount = 0;

        sendMessage(new Message(MessageType.COMPILER_SUMMARY,
                new Number[] {
                        instructionCount,
                        elapsedTime
                }));
    }
}
