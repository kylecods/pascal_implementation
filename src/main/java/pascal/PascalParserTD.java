package pascal;

import lib.frontend.*;
import lib.message.Message;
import lib.message.MessageType;

import java.io.IOException;

public class PascalParserTD extends Parser {
    protected static PascalErrorHandler errorHandler = new PascalErrorHandler();

    public PascalParserTD(Scanner scanner) {
        super(scanner);
    }

    @Override
    public void parse() throws Exception {
        Token token;
        var startTime = System.currentTimeMillis();

        try {
            while (!((token = nextToken()) instanceof EOFToken)) {
                var tokenType = token.getType();

                if(tokenType == PascalTokenType.IDENTIFIER){
                    var name = token.getText().toLowerCase();

                    var entry = symTabStack.lookup(name);

                    if(entry == null) entry = symTabStack.enterLocal(name);

                    entry.appendLineNumber(token.getLineNum());
                }
                else if (tokenType == PascalTokenType.ERROR) {
                    errorHandler.flag(token, (PascalErrorCode) token.getValue(), this);
                }
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
}
