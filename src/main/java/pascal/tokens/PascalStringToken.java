package pascal.tokens;

import lib.frontend.Source;
import pascal.PascalErrorCode;
import pascal.PascalToken;
import pascal.PascalTokenType;

public class PascalStringToken extends PascalToken {
    public PascalStringToken(Source source) throws Exception {
        super(source);
    }

    protected void extract()throws Exception{
        var textBuffer = new StringBuilder();
        var valueBuffer = new StringBuilder();

        var currentChar =  nextChar();
        textBuffer.append('\'');

        do {
            if(Character.isWhitespace(currentChar)){
                currentChar = ' ';
            }

            if ((currentChar != '\'') && (currentChar != Source.EOF)){
                textBuffer.append(currentChar);
                valueBuffer.append(currentChar);
                currentChar = nextChar();
            }
            if(currentChar == '\''){
                while ((currentChar == '\'') && (peekChar() == '\'')){
                    textBuffer.append("''");
                    valueBuffer.append(currentChar);
                    currentChar = nextChar();
                    currentChar = nextChar();
                }
            }
        }while ((currentChar != '\'') && (currentChar != Source.EOF));

        if(currentChar == '\''){
            nextChar();
            textBuffer.append('\'');
            type = PascalTokenType.STRING;
            value = valueBuffer.toString();
        }else {
            type = PascalTokenType.ERROR;
            value = PascalErrorCode.UNEXPECTED_EOF;
        }
        text = textBuffer.toString();
    }
}
