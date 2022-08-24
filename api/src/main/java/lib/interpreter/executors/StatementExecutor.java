package lib.interpreter.executors;


import lib.intermediate.ICodeNode;
import lib.intermediate.implementation.ICodeKeyImpl;
import lib.intermediate.implementation.ICodeNodeTypeImpl;
import lib.interpreter.Executor;
import lib.interpreter.RuntimeErrorCode;
import lib.message.Message;
import lib.message.MessageType;

public class StatementExecutor extends Executor {
    public StatementExecutor(Executor parent){
        super(parent);
    }

    public Object execute(ICodeNode node)  {
        var nodeType = (ICodeNodeTypeImpl)node.getType();

        sendSourceLineMessage(node);

        switch (nodeType){
            case COMPOUND -> {
                var compoundExecutor = new CompoundExecutor(this);
                return compoundExecutor.execute(node);
            }
            case ASSIGN -> {
                var assignmentExecutor = new AssignmentExecutor(this);
                return assignmentExecutor.execute(node);
            }
            case NO_OP -> {
                return null;
            }
            default -> {
                errorHandler.flag(node, RuntimeErrorCode.UNIMPLEMENTED_FEATURE,this);
                return null;
            }
        }
    }
    private void sendSourceLineMessage(ICodeNode node){
        var lineNumber = node.getAttribute(ICodeKeyImpl.LINE);

        if(lineNumber != null){
            sendMessage(new Message(MessageType.SOURCE_LINE,lineNumber));
        }
    }
}
