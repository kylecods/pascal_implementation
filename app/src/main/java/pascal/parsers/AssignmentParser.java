package pascal.parsers;

import lib.ICodeFactory;
import lib.frontend.Token;
import lib.intermediate.ICodeNode;
import lib.intermediate.implementation.ICodeKeyImpl;
import lib.intermediate.implementation.ICodeNodeTypeImpl;
import pascal.PascalErrorCode;
import pascal.PascalParserTD;
import pascal.PascalTokenType;

import java.util.EnumSet;

public class AssignmentParser extends StatementParser {
    private static final EnumSet<PascalTokenType> COLON_EQUALS_SET = ExpressionParser.EXPR_START_SET.clone();

    static {
        COLON_EQUALS_SET.add(PascalTokenType.COLON_EQUALS);
        COLON_EQUALS_SET.addAll(StatementParser.STMT_FOLLOW_SET);
    }

    public AssignmentParser(PascalParserTD parent) {
        super(parent);
    }
    // name := 3 + 2;
    public ICodeNode parse(Token token) throws Exception{
        var assignNode = ICodeFactory.createICodeNode(ICodeNodeTypeImpl.ASSIGN);

        var targetName = token.getText().toLowerCase();
        var targetId = symTabStack.lookup(targetName);

        if(targetId == null) targetId = symTabStack.enterLocal(targetName);

        targetId.appendLineNumber(token.getLineNum());

        token = nextToken();

        var variableNode = ICodeFactory.createICodeNode(ICodeNodeTypeImpl.VARIABLE);
        variableNode.setAttribute(ICodeKeyImpl.ID,targetId);

        assignNode.addChild(variableNode);

        token = synchronize(COLON_EQUALS_SET);

        if(token.getType() == PascalTokenType.COLON_EQUALS){
            token = nextToken();
        }else {
            errorHandler.flag(token, PascalErrorCode.MISSING_COLON_EQUALS,this);
        }

        var expressionParser = new ExpressionParser(this);
        assignNode.addChild(expressionParser.parse(token));

        return assignNode;
    }
}
