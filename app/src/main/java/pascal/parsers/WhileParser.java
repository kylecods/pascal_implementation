package pascal.parsers;

import lib.ICodeFactory;
import lib.frontend.Token;
import lib.intermediate.ICodeNode;
import lib.intermediate.implementation.ICodeNodeTypeImpl;
import pascal.PascalErrorCode;
import pascal.PascalParserTD;
import pascal.PascalTokenType;

public class WhileParser extends StatementParser{
    public WhileParser(PascalParserTD parent) {
        super(parent);
    }

    public ICodeNode parse(Token token) throws Exception{
        var loopNode = ICodeFactory.createICodeNode(ICodeNodeTypeImpl.LOOP);
        var conditionNode = ICodeFactory.createICodeNode(ICodeNodeTypeImpl.TEST);

        var statementParser = new StatementParser(this);
        statementParser.parseList(token,loopNode, PascalTokenType.DO, PascalErrorCode.MISSING_DO);

        var expressionNode = new ExpressionParser(this);

        conditionNode.addChild( expressionNode.parse(token));

        loopNode.addChild(conditionNode);
        return loopNode;
    }
}
