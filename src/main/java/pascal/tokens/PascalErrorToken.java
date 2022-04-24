package pascal.tokens;

import lib.frontend.Source;
import lib.frontend.TokenType;
import pascal.PascalErrorCode;
import pascal.PascalToken;
import pascal.PascalTokenType;

public class PascalErrorToken extends PascalToken {
    private final Source source;
    private final TokenType type;

    private final PascalErrorCode value;
    public PascalErrorToken(Source source, PascalErrorCode errorCode,String tokenText) throws Exception{
        super(source);

        this.source = source;
        this.type = PascalTokenType.ERROR;
        this.value = errorCode;
    }

    protected void extract()throws Exception{}

}
