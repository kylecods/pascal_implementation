package pascal;

import lib.frontend.*;

import lib.frontend.Source;
import pascal.tokens.*;


public class PascalScanner extends Scanner {

    public PascalScanner(Source source){
        super(source);
    }

    @Override
    protected Token extractToken() throws Exception {
        skipWhiteSpace();

        Token token;
        var currentChar = currentChar();

        if(currentChar == Source.EOF) {
            token = new EOFToken(source, PascalTokenType.END_OF_FILE);
        } else if (Character.isLetter(currentChar)) {
            token = new PascalWordToken(source);
        } else if (Character.isDigit(currentChar)) {
            token = new PascalNumberToken(source);
        } else if (currentChar == '\'') {
            token = new PascalStringToken(source);
        } else if (PascalTokenType.SPECIAL_SYMBOLS.containsKey(Character.toString(currentChar))) {
            token = new PascalSpecialSymbolToken(source);
        } else {
            token = new PascalErrorToken(source,PascalErrorCode.INVALID_CHARACTER, Character.toString(currentChar));
            nextChar();
        }

        return token;
    }

    private void skipWhiteSpace() throws Exception{
        var currentChar = currentChar();

        while (Character.isWhitespace(currentChar) || (currentChar == '{')){
            if(currentChar == '{'){
                do {
                    currentChar = nextChar(); //consume characters
                }while ((currentChar != '}') && (currentChar != Source.EOF));

                if(currentChar == '}') currentChar = nextChar();
            }else {
                currentChar = nextChar();
            }

        }

    }


}
