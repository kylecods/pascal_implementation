package pascal.parsers;

import factory.ICodeFactory;
import lib.frontend.Token;
import lib.intermediate.ICodeNode;
import lib.intermediate.implementation.ICodeNodeTypeImpl;
import pascal.PascalErrorCode;
import pascal.PascalParserTD;
import pascal.PascalTokenType;

public class RepeatParser extends StatementParser{
    public RepeatParser(PascalParserTD parent) {
        super(parent);
    }

    public ICodeNode parse(Token token) throws Exception{
        token = nextToken();

        var loopNode = ICodeFactory.createICodeNode(ICodeNodeTypeImpl.LOOP);
        var conditionNode = ICodeFactory.createICodeNode(ICodeNodeTypeImpl.TEST);
        var statementParser = new StatementParser(this);
        statementParser.parseList(token,loopNode, PascalTokenType.UNTIL, PascalErrorCode.MISSING_UNTIL);

        token = currentToken();
        var expressionParser = new ExpressionParser(this);
        conditionNode.addChild(expressionParser.parse(token));
        loopNode.addChild(conditionNode);
        return loopNode;
    }
}
