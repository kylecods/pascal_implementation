package lib.frontend;

public class Token {
    protected TokenType type;
    protected String text;
    protected Object value;

    protected Source source;
    protected int lineNum;
    protected int position;

    public Token(Source source) throws Exception{
        this.source = source;
        this.lineNum = source.getLineNum();
        this.position = source.getPosition();

        extract();
    }
    /**
     * Default method to extract only one-character tokens from the source.
     * Subclasses can override this method to construct language-specific
     * token. After extracting the token, the current source line position
     * will be one beyond the last token character.
     * @throws Exception if an error occurred.
     * */
    protected void extract() throws Exception{
        text = Character.toString(currentChar());
        value = null;
        nextChar();
    }

    protected char currentChar() throws Exception{
        return source.currentChar();
    }

    protected char nextChar() throws Exception{
        return source.nextChar();
    }
    protected char peekChar() throws Exception{
        return source.peekChar();
    }

    public TokenType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public Object getValue() {
        return value;
    }

    public Source getSource() {
        return source;
    }

    public int getLineNum() {
        return lineNum;
    }

    public int getPosition() {
        return position;
    }
}
