package pascal;

import lib.frontend.EOFToken;
import lib.frontend.Parser;
import lib.frontend.Scanner;
import lib.frontend.Token;
import lib.message.Message;
import lib.message.MessageType;

public class PascalParserTD extends Parser {

    public PascalParserTD(Scanner scanner) {
        super(scanner);
    }

    @Override
    public void parse() throws Exception {
        Token token;
        var startTime = System.currentTimeMillis();
        while (!((token = nextToken()) instanceof EOFToken)) {}

        var elapsedTime = (System.currentTimeMillis() - startTime) /1000f;

        sendMessage(new Message(MessageType.PARSER_SUMMARY, new Number[] {
                token.getLineNum(),
                getErrorCount(),
                elapsedTime
        }));
    }

    @Override
    public int getErrorCount() {
        return 0;
    }
}
