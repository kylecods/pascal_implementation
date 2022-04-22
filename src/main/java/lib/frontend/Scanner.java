package lib.frontend;

public abstract class Scanner {
    protected Source source;
    private Token currentToken;

    public Scanner(Source source){
        this.source = source;
    }

    public Token currentToken(){
        return currentToken;
    }

    public Token nextToken() throws Exception {
        currentToken = extractToken();
        return currentToken;
    }

    /**
     * Do the actual work of extracting and returning the token from
     * the source. Implemented by scanner subclasses.
     * @return the next token
     * @throws Exception if an error occurred.
     */
    public abstract Token extractToken() throws Exception;

    public char currentChar() throws Exception{
        return source.currentChar();
    }

    public char nextChar() throws Exception{
        return source.nextChar();
    }
}
