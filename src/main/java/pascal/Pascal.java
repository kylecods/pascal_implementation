package pascal;

import lib.BackendFactory;
import lib.FrontendFactory;
import lib.backend.Backend;
import lib.frontend.Parser;
import lib.frontend.Source;
import lib.frontend.TokenType;
import lib.intermediate.ICode;
import lib.intermediate.SymTab;
import lib.message.Message;
import lib.message.MessageListener;

import java.io.BufferedReader;
import java.io.FileReader;

public class Pascal {
    private Parser parser;
    private Source source;
    private ICode iCode;
    private SymTab symTab;
    private Backend backend;

    public Pascal(String operation, String filePath, String flags){
        try{
            var intermediate = flags.indexOf('i') > -1;
            var xref = flags.indexOf('x') > -1;

            source = new Source(new BufferedReader(new FileReader(filePath)));
            source.addMessageListener(new SourceMessageListener());
            parser = FrontendFactory.createParser("Pascal","top-down", source);
            parser.addMessageListener(new ParserMessageListener());

            backend = BackendFactory.createBackend(operation);
            backend.addMessageListener(new BackendMessageListener());

            parser.parse();
            source.close();

            iCode = parser.getiCode();
            symTab = parser.getSymTab();

            backend.process(iCode,symTab);
        }catch (Exception ex){
            System.out.println("***** Internal translator error ******");

            ex.printStackTrace();
        }
    }

    private static final String FLAGS = "[-ix]";
    private static final String USAGE =
            "Usage: Pascal execute | compile " + FLAGS + " <source file path>";

    public static void main(String[] args){
        try {
            var operation = args[0];

            if(!(operation.equalsIgnoreCase("compile") || operation.equalsIgnoreCase("execute"))) throw new Exception();

            int i = 0;
            StringBuilder flags = new StringBuilder();

             while ((++ i < args.length) && (args[i].charAt(0) == '-')){
                 flags.append(args[i].substring(1));
             }
             if (i < args.length) {
                 var path = args[i];

                 new Pascal(operation,path, flags.toString());
             }else throw new Exception();
        }catch (Exception ex){
            System.out.println(USAGE);
        }
    }

    private static final String SOURCE_LINE_FORMAT = "%03d %s";

    private class SourceMessageListener implements MessageListener{
        public void messageReceived(Message message){
            var messageType = message.getType();

            var body = (Object[])message.getBody();

            switch (messageType){
                case SOURCE_LINE -> {
                    int lineNumber = (int) body[0];
                    String lineText = (String) body[1];

                    System.out.println(String.format(SOURCE_LINE_FORMAT, lineNumber,lineText));
                }
            }
        }
    }
    private static final String PARSER_SUMMARY_FORMAT =
            "\n%,20d source lines."+
             "\n%,20d syntax errors." +
             "\n%,20.2f seconds total parsing time\n";

    private static final String TOKEN_FORMAT =
            ">>> %-15s line=%03d, pos=%2d, text=\"%s\" \n";

    private static final String VALUE_FORMAT =
            ">>>                value=%s\n";

    private static final int PREFIX_WIDTH = 5;


    private class ParserMessageListener implements MessageListener{

        @Override
        public void messageReceived(Message message) {
            var messageType = message.getType();

            switch (messageType){
                case TOKEN -> {
                    var body = (Object[]) message.getBody();
                    int line = (int) body[0];
                    int position = (int) body[1];
                    var tokenType = (TokenType) body[2];
                    var tokenText = (String) body[3];
                    var tokenValue = body[4];
                    System.out.printf(TOKEN_FORMAT,
                            tokenType,
                            line,
                            position,
                            tokenText);

                    if (tokenValue != null){
                        if(tokenType == PascalTokenType.STRING){
                            tokenValue ="\"" + tokenValue + "\"";
                        }

                        System.out.println(String.format(VALUE_FORMAT,tokenValue));
                    }
                }
                case SYNTAX_ERROR -> {
                    var body = (Object[])message.getBody();
                    int lineNumber = (int) body[0];
                    int position = (int) body[1];
                    var tokenText = (String) body[2];
                    var errorMessage = (String) body[3];

                    int spaceCount = PREFIX_WIDTH + position;

                    var flagBuffer = new StringBuilder();

                    for (int i = 1; i < spaceCount; ++i){
                        flagBuffer.append(' ');
                    }

                    flagBuffer.append("^\n*** ").append(errorMessage);

                    if(tokenText != null){
                        flagBuffer.append(" [at \"").append(tokenText).append("\"]");
                    }
                    System.out.println(flagBuffer.toString());
                }
                case PARSER_SUMMARY -> {
                    var body = (Number[])message.getBody();
                    int statementCount = (int)body[0];
                    int syntaxErrors = (int)body[1];
                    float elapsedTime = (float) body[2];

                    System.out.println(String.format(PARSER_SUMMARY_FORMAT,statementCount,syntaxErrors,elapsedTime));
                }
            }
        }
    }

    private static final String INTERPRETER_SUMMARY_FORMAT =
            "\n%,20d statements executed."+
            "\n%,20d runtime errors." +
            "\n%,20.2f seconds total execution time \n";

    private static final String COMPILER_SUMMARY_FORMAT =
            "\n%,20d instructions generated."+
            "\n%20.2f seconds total code generation time \n";

    private class BackendMessageListener implements MessageListener{

        @Override
        public void messageReceived(Message message) {
            var messageType = message.getType();

            switch (messageType){
                case INTERPRETER_SUMMARY -> {
                    var body = (Number[])message.getBody();
                    int executionCount = (int) body[0];
                    int runtimeErrors = (int) body[1];
                    float elapsedTime = (float) body[2];

                    System.out.printf(INTERPRETER_SUMMARY_FORMAT, executionCount,runtimeErrors,elapsedTime);
                }
                case COMPILER_SUMMARY -> {
                    var body = (Number[]) message.getBody();

                    int instructionCount = (int) body[0];
                    float elapsedTime = (float) body[1];

                    System.out.printf(COMPILER_SUMMARY_FORMAT, instructionCount,elapsedTime);
                }
            }
        }
    }
}
