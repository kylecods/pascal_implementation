package pascal.tokens;

import lib.frontend.Source;
import pascal.PascalToken;
import pascal.PascalTokenType;
public class PascalWordToken extends PascalToken {

    public PascalWordToken(Source source) throws Exception {
        super(source);
    }
    protected void extract() throws Exception{
        var textBuffer = new StringBuilder();

        var currentChar = currentChar();

        while (Character.isLetterOrDigit(currentChar)){
            textBuffer.append(currentChar);
            currentChar = nextChar();
        }

        text = textBuffer.toString();

        type = (PascalTokenType.RESERVED_WORDS.contains(text.toLowerCase())) ? PascalTokenType.valueOf(text.toUpperCase()) : PascalTokenType.IDENTIFIER;
    }
}
