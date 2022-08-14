package lib.interpreter.executors;

import lib.intermediate.ICodeNode;
import lib.intermediate.SymTabEntry;
import lib.intermediate.SymTabKey;
import lib.intermediate.implementation.ICodeKeyImpl;
import lib.intermediate.implementation.SymTabKeyImpl;
import lib.interpreter.Executor;
import lib.message.Message;
import lib.message.MessageType;

public class AssignmentExecutor extends StatementExecutor{
    public AssignmentExecutor(Executor parent) {
        super(parent);
    }

    public Object execute(ICodeNode node){
        var children = node.getChildren();
        var variableNode = children.get(0);
        var expressionNode = children.get(1);

        var expressionExecutor = new ExpressionExecutor(this);

        var value = expressionExecutor.execute(expressionNode);
        var variableId = (SymTabEntry)variableNode.getAttribute(ICodeKeyImpl.ID);
        variableId.setAttribute(SymTabKeyImpl.DATA_VALUE,value);

        sendMessage(node,variableId.getName(),value);
        ++executionCount;
        return null;
    }

    private void sendMessage(ICodeNode node, String variableName, Object value){
        var lineNumber = node.getAttribute(ICodeKeyImpl.LINE);

        if(lineNumber != null){
            sendMessage(new Message(MessageType.ASSIGN, new Object[]{
                    lineNumber,
                    variableName,
                    value
            }));
        }
    }
}
