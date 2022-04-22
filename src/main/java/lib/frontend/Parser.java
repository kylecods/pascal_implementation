package lib.frontend;

import lib.intermediate.ICode;
import lib.intermediate.SymTab;
import lib.message.Message;
import lib.message.MessageHandler;
import lib.message.MessageListener;
import lib.message.MessageProducer;

public abstract class Parser  implements MessageProducer {
    protected static SymTab symTab;
    protected static MessageHandler messageHandler;

    static {
        symTab = null;
        messageHandler = new MessageHandler();
    }

    protected Scanner scanner;
    protected ICode iCode;

    public Parser(Scanner scanner){
        this.scanner = scanner;
        this.iCode = null;
    }
    public abstract void parse() throws Exception;

    public abstract int getErrorCount();

    public Token currentToken(){
        return scanner.currentToken();
    }
    public Token nextToken() throws Exception{
        return scanner.nextToken();
    }

    @Override
    public void addMessageListener(MessageListener listener) {
        messageHandler.addListener(listener);
    }

    @Override
    public void removeMessageListener(MessageListener listener) {
        messageHandler.removeListener(listener);
    }

    @Override
    public void sendMessage(Message message) {
        messageHandler.sendMessage(message);
    }

    public static SymTab getSymTab() {
        return symTab;
    }

    public static MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public ICode getiCode() {
        return iCode;
    }
}
