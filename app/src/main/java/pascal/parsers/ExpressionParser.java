package pascal.parsers;

import factory.ICodeFactory;
import lib.frontend.Token;
import lib.frontend.TokenType;
import lib.intermediate.ICodeNode;
import lib.intermediate.ICodeNodeType;
import lib.intermediate.implementation.ICodeKeyImpl;
import lib.intermediate.implementation.ICodeNodeTypeImpl;
import pascal.PascalErrorCode;
import pascal.PascalParserTD;
import pascal.PascalTokenType;


import java.util.EnumSet;
import java.util.HashMap;


public class ExpressionParser extends StatementParser{
    static final EnumSet<PascalTokenType> EXPR_START_SET = EnumSet.of(PascalTokenType.PLUS,PascalTokenType.MINUS,PascalTokenType.IDENTIFIER,PascalTokenType.INTEGER,
            PascalTokenType.REAL,PascalTokenType.STRING,PascalTokenType.NOT,PascalTokenType.LEFT_PAREN);

    public ExpressionParser(PascalParserTD parent) {
        super(parent);
    }

    @Override
    public ICodeNode parse(Token token) throws Exception {
        return parseExpression(token);
    }

    private static final EnumSet<PascalTokenType> REL_OPS = EnumSet.of(PascalTokenType.EQUALS,PascalTokenType.NOT_EQUALS,PascalTokenType.LESS_THAN,PascalTokenType.LESS_EQUALS,
            PascalTokenType.GREATER_THAN,PascalTokenType.GREATER_EQUALS);
    private static final HashMap<PascalTokenType, ICodeNodeType> REL_OPS_MAP = new HashMap<>();

    static {
      REL_OPS_MAP.put(PascalTokenType.EQUALS, ICodeNodeTypeImpl.EQ);
      REL_OPS_MAP.put(PascalTokenType.NOT_EQUALS, ICodeNodeTypeImpl.NE);
      REL_OPS_MAP.put(PascalTokenType.LESS_THAN, ICodeNodeTypeImpl.LT);
      REL_OPS_MAP.put(PascalTokenType.LESS_EQUALS, ICodeNodeTypeImpl.LE);
      REL_OPS_MAP.put(PascalTokenType.GREATER_THAN, ICodeNodeTypeImpl.GT);
      REL_OPS_MAP.put(PascalTokenType.GREATER_EQUALS,ICodeNodeTypeImpl.GE);
    };

    private ICodeNode parseExpression(Token token) throws Exception{
        var rootNode = parseSimpleExpression(token);
        token = currentToken();

        var tokenType = token.getType();

        if(REL_OPS.contains(tokenType)){
            var nodeType = REL_OPS_MAP.get(tokenType);

            var opNode = ICodeFactory.createICodeNode(nodeType);

            opNode.addChild(rootNode);

            token = nextToken();

            opNode.addChild(parseSimpleExpression(token));
            rootNode = opNode;
        }
        return rootNode;
    }

    private static final EnumSet<PascalTokenType> ADD_OPS = EnumSet.of(PascalTokenType.PLUS,PascalTokenType.MINUS,PascalTokenType.OR);

    private static final HashMap<PascalTokenType,ICodeNodeTypeImpl> ADD_OPS_OPS_MAP = new HashMap<>();

    static {
        ADD_OPS_OPS_MAP.put(PascalTokenType.PLUS,ICodeNodeTypeImpl.ADD);
        ADD_OPS_OPS_MAP.put(PascalTokenType.MINUS,ICodeNodeTypeImpl.SUBTRACT);
        ADD_OPS_OPS_MAP.put(PascalTokenType.OR,ICodeNodeTypeImpl.OR);
    }

    private ICodeNode parseSimpleExpression(Token token) throws Exception{
        TokenType signType = null;

        var tokenType = token.getType();

        if((tokenType == PascalTokenType.PLUS) || (tokenType == PascalTokenType.MINUS)){
            signType = tokenType;
            token = nextToken();
        }
        var rootNode = parseTerm(token);

        if(signType == PascalTokenType.MINUS){
            var negateNode = ICodeFactory.createICodeNode(ICodeNodeTypeImpl.NEGATE);
            negateNode.addChild(rootNode);
            rootNode = negateNode;
        }

        token = currentToken();
        tokenType = token.getType();

        while (ADD_OPS.contains(tokenType)){
            var nodeType = ADD_OPS_OPS_MAP.get(tokenType);
            var opNode = ICodeFactory.createICodeNode(nodeType);
            opNode.addChild(rootNode);

            token = nextToken();

            opNode.addChild(parseTerm(token));

            rootNode = opNode;
            token = currentToken();
            tokenType = token.getType();
        }

        return rootNode;
    }

    private static final EnumSet<PascalTokenType> MULT_OPS = EnumSet.of(PascalTokenType.STAR,PascalTokenType.SLASH,PascalTokenType.DIV,PascalTokenType.MOD,PascalTokenType.AND);
    private static final HashMap<PascalTokenType,ICodeNodeType> MULT_OPS_OPS_MAP = new HashMap<>();

    static {
        MULT_OPS_OPS_MAP.put(PascalTokenType.STAR,ICodeNodeTypeImpl.MULTIPLY);
        MULT_OPS_OPS_MAP.put(PascalTokenType.SLASH,ICodeNodeTypeImpl.FLOAT_DIVIDE);
        MULT_OPS_OPS_MAP.put(PascalTokenType.DIV,ICodeNodeTypeImpl.INTEGER_DIVIDE);
        MULT_OPS_OPS_MAP.put(PascalTokenType.MOD,ICodeNodeTypeImpl.MOD);
        MULT_OPS_OPS_MAP.put(PascalTokenType.AND,ICodeNodeTypeImpl.AND);
    }

    private ICodeNode parseTerm(Token token) throws Exception{
        var rootNode = parseFactor(token);

        token = currentToken();
        var tokenType = token.getType();

        while (MULT_OPS.contains(tokenType)){
            var nodeType = MULT_OPS_OPS_MAP.get(tokenType);
            var opNode = ICodeFactory.createICodeNode(nodeType);
            opNode.addChild(rootNode);

            token = nextToken();
            opNode.addChild(parseFactor(token));

            rootNode = opNode;
            token = currentToken();
            tokenType = token.getType();
        }
        return rootNode;
    }

    private ICodeNode parseFactor(Token token) throws Exception{
        var tokenType = token.getType();
        ICodeNode rootNode = null;

        switch ((PascalTokenType) tokenType){
            case IDENTIFIER -> {
                var name = token.getText().toLowerCase();
                var id = symTabStack.lookup(name);
                if(id == null){
                    errorHandler.flag(token, PascalErrorCode.IDENTIFIER_UNDEFINED,this);
                    id = symTabStack.enterLocal(name);
                }
                rootNode = ICodeFactory.createICodeNode(ICodeNodeTypeImpl.VARIABLE);
                rootNode.setAttribute(ICodeKeyImpl.ID,id);
                id.appendLineNumber(token.getLineNum());

                token = nextToken();
            }
            case INTEGER -> {
                rootNode = ICodeFactory.createICodeNode(ICodeNodeTypeImpl.INTEGER_CONSTANT);
                rootNode.setAttribute(ICodeKeyImpl.VALUE,token.getValue());

                token = nextToken();
            }
            case REAL -> {
                rootNode = ICodeFactory.createICodeNode(ICodeNodeTypeImpl.REAL_CONSTANT);
                rootNode.setAttribute(ICodeKeyImpl.VALUE,token.getValue());

                token = nextToken();
            }
            case STRING -> {
                var value = (String) token.getValue();

                rootNode = ICodeFactory.createICodeNode(ICodeNodeTypeImpl.STRING_CONSTANT);
                rootNode.setAttribute(ICodeKeyImpl.VALUE,value);
                token = nextToken();
            }
            case NOT -> {
                token = nextToken();

                rootNode = ICodeFactory.createICodeNode(ICodeNodeTypeImpl.NOT);
                rootNode.addChild(parseFactor(token));
            }
            case LEFT_PAREN -> {
                token = nextToken(); //consume (

                rootNode = parseExpression(token);

                token = currentToken();
                if(token.getType() == PascalTokenType.RIGHT_PAREN){
                    token = nextToken();
                }else {
                    errorHandler.flag(token, PascalErrorCode.MISSING_RIGHT_PAREN,this);
                }
            }
            default -> errorHandler.flag(token,PascalErrorCode.UNEXPECTED_TOKEN,this);
        }
        return rootNode;
    }
}

