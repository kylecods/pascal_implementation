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

import java.util.EnumSet;

public class StatementParser extends PascalParserTD {
    protected static final EnumSet<PascalTokenType> STMT_START_SET = EnumSet.of(PascalTokenType.BEGIN,PascalTokenType.CASE,PascalTokenType.FOR,PascalTokenType.IF,PascalTokenType.REPEAT,PascalTokenType.WHILE,
            PascalTokenType.IDENTIFIER,PascalTokenType.SEMICOLON);

    protected static final EnumSet<PascalTokenType> STMT_FOLLOW_SET = EnumSet.of(PascalTokenType.SEMICOLON,PascalTokenType.END,
            PascalTokenType.ELSE,PascalTokenType.UNTIL,PascalTokenType.DOT);

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
            case WHILE -> {
                var whileStatement = new WhileParser(this);
                statementNode = whileStatement.parse(token);
            }
            case REPEAT -> {
                var repeatStatement = new RepeatParser(this);
                statementNode = repeatStatement.parse(token);
            }
            case FOR ->{
                var forStatement = new ForParser(this);
                statementNode = forStatement.parse(token);
            }
            case IF -> {
                var ifStatement = new IfParser(this);
                statementNode = ifStatement.parse(token);
            }
            case CASE -> {
                var caseStatement = new CaseParser(this);
                statementNode = caseStatement.parse(token);
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
        var terminatorSet = STMT_START_SET.clone();
        terminatorSet.add(terminator);

        while (!(token instanceof EOFToken) && (token.getType() != terminator)){
            ICodeNode statementNode = parse(token);
            parentNode.addChild(statementNode);

            token = currentToken();
            var tokenType = token.getType();

            if (tokenType == PascalTokenType.SEMICOLON) {
                token = nextToken();
            }else if (STMT_START_SET.contains(tokenType)) {
                errorHandler.flag(token,PascalErrorCode.MISSING_SEMICOLON,this);
            }

            token = synchronize(terminatorSet);

        }
        if (token.getType() == terminator){
            token = nextToken();
        }else {
            errorHandler.flag(token,errorCode,this);
        }
    }
}
