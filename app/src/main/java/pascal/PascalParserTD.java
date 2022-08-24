package pascal;

import factory.ICodeFactory;
import lib.frontend.*;
import lib.intermediate.ICodeNode;
import lib.message.Message;
import lib.message.MessageType;
import pascal.parsers.StatementParser;

import java.io.IOException;
import java.util.EnumSet;


public class PascalParserTD extends Parser {
    protected static PascalErrorHandler errorHandler = new PascalErrorHandler();

    public PascalParserTD(PascalScanner scanner) {
        super(scanner);
    }

    public PascalParserTD(PascalParserTD parent) {
        super(parent.getScanner());
    }

    @Override
    public void parse() throws Exception {

        var startTime = System.currentTimeMillis();
        iCode = ICodeFactory.createICode();

        try {
            Token token = nextToken();
            ICodeNode rootNode = null;

            if(token.getType() == PascalTokenType.BEGIN){
                StatementParser statementParser = new StatementParser(this);
                rootNode = statementParser.parse(token);
                token = currentToken();
            }else {
                errorHandler.flag(token, PascalErrorCode.UNEXPECTED_TOKEN,this);
            }

            if(token.getType() != PascalTokenType.DOT){
                errorHandler.flag(token, PascalErrorCode.MISSING_PERIOD, this);
            }
            token = currentToken();

            if(rootNode != null){
                iCode.setRoot(rootNode);
            }

            var elapsedTime = (System.currentTimeMillis() - startTime) / 1000f;

            sendMessage(new Message(MessageType.PARSER_SUMMARY, new Number[]{
                    token.getLineNum(),
                    getErrorCount(),
                    elapsedTime
            }));
        }catch (IOException ex){
            errorHandler.abortTranslation(PascalErrorCode.IO_ERROR,this);
        }
    }

    @Override
    protected int getErrorCount() {
        return errorHandler.getErrorCount();
    }

    public Token synchronize(EnumSet syncSet) throws Exception{
        var token = currentToken();

        if(!syncSet.contains(token.getType())){
            errorHandler.flag(token,PascalErrorCode.UNEXPECTED_TOKEN,this);

            do {
                token = nextToken();
            }while (!(token instanceof EOFToken) && !syncSet.contains(token.getType()));
        }
        return token;
    }
}
