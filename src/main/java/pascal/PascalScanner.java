package pascal;

import lib.frontend.*;

import lib.frontend.Source;

public class PascalScanner extends Scanner {

    public PascalScanner(Source source){
        super(source);
    }

    @Override
    public Token extractToken() throws Exception {
        Token token;
        var currentChar = currentChar();

        if(currentChar == Source.EOF) {
            token = new EOFToken(source);
        }else {
            token = new Token(source);
        }

        return token;
    }
}
