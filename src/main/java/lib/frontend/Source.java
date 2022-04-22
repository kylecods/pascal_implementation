package lib.frontend;

import lib.message.Message;
import lib.message.MessageListener;
import lib.message.MessageProducer;

import java.io.BufferedReader;
import java.io.IOException;

import static lib.frontend.Parser.messageHandler;
import lib.message.MessageType;

public class Source implements MessageProducer {
    public static final char EOF = (char) 0;
    public static final char EOL = '\n';

    private final BufferedReader reader;
    private String line;
    private int lineNum;

    private int currentPos;

    public Source(BufferedReader reader) throws IOException{
        this.lineNum = 0;
        this.currentPos = -2;
        this.reader = reader;
    }

    public char currentChar() throws Exception{
        if(currentPos == -2) {
            readLine();
            return nextChar();
        }

        if(line == null) return EOF;

        if((currentPos == -1) || (currentPos == line.length())) return EOL;

        if(currentPos > line.length()) {
            readLine();
            return nextChar();
        }

        return line.charAt(currentPos);
    }

    /**
     * Consume current source character and return the next character.
     * @return the next source character
     * @throws Exception if an error occurred
     *
     **/
    public char nextChar() throws Exception{
        ++currentPos;
        return currentChar();
    }

    public char peekChar() throws Exception{
        currentChar();
        if(line == null) return EOF;

        int nextPos = currentPos + 1;

        return nextPos < line.length() ? line.charAt(nextPos) : EOL;
    }

    private void readLine() throws Exception{

        line = reader.readLine();
        currentPos = - 1;

        if (line != null) ++lineNum;

        if (line != null){
            sendMessage(new Message(MessageType.SOURCE_LINE, new Object[] {lineNum, line}));
        }

    }

    public void close() throws Exception {
        if( reader != null) {
            try {
                reader.close();
            }catch (IOException ex){
                ex.printStackTrace();
                throw ex;
            }
        }
    }

    public int getLineNum() {
        return lineNum;
    }

    public int getPosition() {
        return currentPos;
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
}
