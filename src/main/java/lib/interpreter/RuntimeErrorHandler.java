package lib.interpreter;

import lib.backend.Backend;
import lib.intermediate.ICodeNode;
import lib.intermediate.implementation.ICodeKeyImpl;
import lib.message.Message;
import lib.message.MessageType;

public class RuntimeErrorHandler {
    private static final int MAX_ERRORS = 5;
    private static int errorCount = 0;

    public void flag(ICodeNode node, RuntimeErrorCode errorCode, Backend backend){
        String lineNumber = null;

        while ((node != null) && (node.getAttribute(ICodeKeyImpl.LINE) == null)){
            node = node.getParent();
        }
        backend.sendMessage(new Message(MessageType.RUNTIME_ERROR,new Object[] {
                errorCode.toString(), (Integer)node.getAttribute(ICodeKeyImpl.LINE)
        }));

        if(++errorCount > MAX_ERRORS){
            System.out.println("***ABORTED AFTER TOO MANY RUNTIME ERRORS.");
            System.exit(-1);
        }
    }

    public static int getErrorCount() {
        return errorCount;
    }
}
