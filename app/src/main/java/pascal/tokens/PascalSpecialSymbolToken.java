package pascal.tokens;

import lib.frontend.Source;
import pascal.PascalErrorCode;
import pascal.PascalToken;
import pascal.PascalTokenType;

public class PascalSpecialSymbolToken extends PascalToken {
    public PascalSpecialSymbolToken(Source source) throws Exception {
        super(source);
    }

    protected void extract() throws Exception{
        var currentChar = currentChar();
        text = Character.toString(currentChar);
        type = null;
        switch (currentChar) {
            case '+', '-', '*', '/', ',', ';', '\'', '=', '(', ')', '[', ']', '{', '}', '^' -> {
                nextChar();
            }
            case ':'->{
                currentChar = nextChar();
                if(currentChar == '='){
                    text += currentChar;
                    nextChar();
                }
            }
            case '<'->{
                currentChar = nextChar();

                if(currentChar == '='){
                    text += currentChar;
                    nextChar();
                } else if (currentChar == '>') {
                    text += currentChar;
                    nextChar();
                }
            }
            case '.' ->{
                currentChar = nextChar();
                if(currentChar == '.'){
                    text += currentChar;
                    nextChar();
                }
            }
            default -> {
                nextChar();
                type = PascalTokenType.ERROR;
                value = PascalErrorCode.INVALID_CHARACTER;
            }
        }

        if(type == null){
            type = PascalTokenType.SPECIAL_SYMBOLS.get(text);
        }
    }
}
