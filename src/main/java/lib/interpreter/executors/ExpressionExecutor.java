package lib.interpreter.executors;

import lib.intermediate.ICodeNode;
import lib.intermediate.SymTabEntry;
import lib.intermediate.implementation.ICodeKeyImpl;
import lib.intermediate.implementation.ICodeNodeTypeImpl;
import lib.intermediate.implementation.SymTabKeyImpl;
import lib.interpreter.Executor;
import lib.interpreter.RuntimeErrorCode;


import java.util.EnumSet;

public class ExpressionExecutor extends StatementExecutor{

    public ExpressionExecutor(Executor parent) {
        super(parent);
    }

    public Object execute(ICodeNode node){
        var nodeType = (ICodeNodeTypeImpl) node.getType();

        switch (nodeType){
            case VARIABLE -> {
                var entry = (SymTabEntry)node.getAttribute(ICodeKeyImpl.ID);
                return entry.getAttribute(SymTabKeyImpl.DATA_VALUE);
            }
            case INTEGER_CONSTANT -> {
                return (Integer) node.getAttribute(ICodeKeyImpl.VALUE);
            }
            case REAL_CONSTANT -> {
                return (Float)node.getAttribute(ICodeKeyImpl.VALUE);
            }
            case STRING_CONSTANT -> {
                return (String)node.getAttribute(ICodeKeyImpl.VALUE);
            }
            case NEGATE -> {
                var children = node.getChildren();
                var expressionNode = children.get(0);

                var value = execute(expressionNode);
                if(value instanceof Integer){
                    return -((Integer) value);
                }
                return -((Float) value);
            }
            case NOT -> {
                var children = node.getChildren();
                var expressionNode = children.get(0);

                var value = (Boolean)execute(expressionNode);
                return !value;
            }
            default -> {
                return executeBinaryOperator(node,nodeType);
            }
        }
    }

    private static final EnumSet<ICodeNodeTypeImpl> ARITH_OPS = EnumSet.of(ICodeNodeTypeImpl.ADD,ICodeNodeTypeImpl.SUBTRACT,ICodeNodeTypeImpl.MULTIPLY,
            ICodeNodeTypeImpl.FLOAT_DIVIDE,ICodeNodeTypeImpl.INTEGER_DIVIDE);

    public Object executeBinaryOperator(ICodeNode node,ICodeNodeTypeImpl nodeType){
        var children = node.getChildren();

        var operandNode1 = children.get(0);
        var operandNode2 = children.get(1);

        var operand1 = execute(operandNode1);
        var operand2 = execute(operandNode2);

        boolean integerMode = (operand1 instanceof Integer) && (operand2 instanceof Integer);

        if(ARITH_OPS.contains(nodeType)){
            if (integerMode){
                int value1 = (Integer)operand1;
                int value2 = (Integer)operand2;
                switch (nodeType){
                    case ADD : return value1 + value2;
                    case SUBTRACT: return value1 - value2;
                    case MULTIPLY: return value1 * value2;
                    case FLOAT_DIVIDE:{
                        if(value2 != 0) return ((float) value1) /((float) value2);

                        errorHandler.flag(node, RuntimeErrorCode.DIVISION_BY_ZERO,this);
                        return 0;
                    }
                    case INTEGER_DIVIDE:{
                        if(value2 != 0){
                            return value1/value2;
                        }
                        errorHandler.flag(node,RuntimeErrorCode.DIVISION_BY_ZERO,this);
                        return 0;
                    }
                    case MOD:{
                        if (value2 != 0){
                            return value1 % value2;
                        }
                        errorHandler.flag(node,RuntimeErrorCode.DIVISION_BY_ZERO,this);
                        return 0;
                    }
                }
            }else {
                float value1 = operand1 instanceof Integer ? (int) operand1 : (float) operand1;
                float value2 = operand2 instanceof Integer ? (int) operand2 : (float) operand2;

                switch (nodeType){
                    case ADD : return value1 + value2;
                    case SUBTRACT: return value1 - value2;
                    case MULTIPLY: return value1 * value2;
                    case FLOAT_DIVIDE:{
                        if (value2 != 0.0f){
                            return value1 / value2;
                        }
                        errorHandler.flag(node,RuntimeErrorCode.DIVISION_BY_ZERO,this);
                        return 0.0f;
                    }
                }
            }
        } else if ((nodeType == ICodeNodeTypeImpl.AND) || (nodeType == ICodeNodeTypeImpl.OR)) {
            boolean value1 = (boolean) operand1;
            boolean value2 = (boolean) operand2;
            switch (nodeType){
                case AND: return value1 && value2;
                case OR : return value1 || value2;
            }
        }else if(integerMode){
            int value1 = (int) operand1;
            int value2 = (int) operand2;

            switch (nodeType){
                case EQ : return value1 == value2;
                case NE: return value1 != value2;
                case LT: return value1 < value2;
                case LE: return value1 <= value2;
                case GT: return value1 > value2;
                case GE: return value1 >= value2;
            }
        }

        return 0;
    }

    private Object executeOperator(){
        return null;
    }
}
