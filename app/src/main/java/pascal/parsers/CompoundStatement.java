package pascal.parsers;

import factory.ICodeFactory;
import lib.frontend.Token;
import lib.intermediate.ICodeNode;
import lib.intermediate.implementation.ICodeNodeTypeImpl;
import pascal.PascalErrorCode;
import pascal.PascalParserTD;
import pascal.PascalTokenType;

public class CompoundStatement extends StatementParser {
    public CompoundStatement(PascalParserTD parent) {
        super(parent);
    }
    public ICodeNode parse(Token token) throws Exception{
        token = nextToken();

        var compoundNode = ICodeFactory.createICodeNode(ICodeNodeTypeImpl.COMPOUND);

        var statementParser = new StatementParser(this);
        statementParser.parseList(token,compoundNode, PascalTokenType.END, PascalErrorCode.MISSING_END);
        return compoundNode;
    }
}
