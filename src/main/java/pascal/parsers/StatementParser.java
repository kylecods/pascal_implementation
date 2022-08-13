package pascal.parsers;

import lib.ICodeFactory;
import lib.frontend.EOFToken;
import lib.frontend.Token;
import lib.frontend.TokenType;
import lib.intermediate.ICodeNode;
import lib.intermediate.implementation.ICodeKeyImpl;
import lib.intermediate.implementation.ICodeNodeTypeImpl;
import pascal.PascalErrorCode;
import pascal.PascalParserTD;
import pascal.PascalTokenType;

public class StatementParser extends PascalParserTD {
    public StatementParser(PascalParserTD parent) {
        super(parent);
    }

    public ICodeNode parse(Token token) throws Exception{
        ICodeNode statementNode = null;

        switch ((PascalTokenType) token.getType()){
            case BEGIN -> {
                var compoundStatement = new CompoundStatement(this);
                statementNode = compoundStatement.parse(token);
            }
            case IDENTIFIER -> {
                var assigmentStatement = new AssignmentParser(this);
                statementNode = assigmentStatement.parse(token);
            }
            default -> {
                statementNode = ICodeFactory.createICodeNode(ICodeNodeTypeImpl.NO_OP);
            }
        }
        setLineNumber(statementNode,token);
        return statementNode;
    }
    protected void setLineNumber(ICodeNode node, Token token){
        if(node != null){
            node.setAttribute(ICodeKeyImpl.LINE,token.getLineNum());
        }
    }

    protected void parseList(Token token, ICodeNode parentNode, PascalTokenType terminator, PascalErrorCode errorCode) throws Exception{
        while (!(token instanceof EOFToken) && (token.getType() != terminator)){
            ICodeNode statementNode = parse(token);
            parentNode.addChild(statementNode);

            token = currentToken();
            var tokenType = token.getType();

            if (tokenType == PascalTokenType.SEMICOLON) {
                token = nextToken();
            }else if (tokenType == PascalTokenType.IDENTIFIER) {
                errorHandler.flag(token,PascalErrorCode.MISSING_SEMICOLON,this);
            } else if (tokenType != terminator) {
                errorHandler.flag(token, PascalErrorCode.UNEXPECTED_TOKEN,this);
                token = nextToken();
            }

        }
        if (token.getType() == terminator){
            token = nextToken();
        }else {
            errorHandler.flag(token,errorCode,this);
        }
    }
}
