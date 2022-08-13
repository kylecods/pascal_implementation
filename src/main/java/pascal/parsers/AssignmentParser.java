package pascal.parsers;

import lib.ICodeFactory;
import lib.frontend.Token;
import lib.intermediate.ICodeNode;
import lib.intermediate.implementation.ICodeKeyImpl;
import lib.intermediate.implementation.ICodeNodeTypeImpl;
import pascal.PascalErrorCode;
import pascal.PascalParserTD;
import pascal.PascalTokenType;

public class AssignmentParser extends StatementParser {
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
