package pascal.tokens;

import lib.frontend.Source;
import pascal.PascalErrorCode;
import pascal.PascalToken;
import pascal.PascalTokenType;

public class PascalNumberToken extends PascalToken {
    public PascalNumberToken(Source source) throws Exception {
        super(source);
    }

    protected void extract() throws Exception{
        var textBuffer = new StringBuilder();
        extractNumber(textBuffer);

        text = textBuffer.toString();
    }

    protected void extractNumber(StringBuilder textBuffer) throws Exception{
        String wholeDigits = null;
        String fractionDigits = null;
        String exponentDigits = null;
        var exponentSign = '+';
        boolean sawDotDot = false;
        char currentChar;

        type = PascalTokenType.INTEGER;
        wholeDigits = unsignedIntegerDigits(textBuffer);

        if(type == PascalTokenType.ERROR) return;

        currentChar = currentChar();
        if(currentChar == '.') {
            if(peekChar() == '.'){
                sawDotDot = true;
            }else {
                type = PascalTokenType.REAL;

                textBuffer.append(currentChar);
                currentChar = nextChar();

                fractionDigits = unsignedIntegerDigits(textBuffer);
                if(type == PascalTokenType.ERROR) return;
            }
        }

        currentChar = currentChar();
        if(!sawDotDot && ((currentChar == 'E') || (currentChar == 'e'))){
            type = PascalTokenType.REAL;
            textBuffer.append(currentChar);
            currentChar = nextChar();
            if((currentChar == '+')){
                textBuffer.append(currentChar);
                exponentSign = currentChar;
                currentChar = nextChar();
            }
            exponentDigits = unsignedIntegerDigits(textBuffer);
        }

        if(type == PascalTokenType.INTEGER){
            int integerValue = computeIntegerValue(wholeDigits);

            if(type != PascalTokenType.ERROR) {
                value = integerValue;
            }
        }else if (type == PascalTokenType.REAL){
            float floatValue = computeFloatValue(wholeDigits,fractionDigits,exponentDigits,exponentSign);
            if(type != PascalTokenType.ERROR){
                value = floatValue;
            }
        }
    }

    private String unsignedIntegerDigits(StringBuilder textBuffer) throws Exception{
        var currentChar = currentChar();
        if(!Character.isDigit(currentChar)){
            type = PascalTokenType.ERROR;
            value = PascalErrorCode.INVALID_NUMBER;
            return null;
        }

        StringBuilder digits = new StringBuilder();

        while (Character.isDigit(currentChar)){
            textBuffer.append(currentChar);
            digits.append(currentChar);
            currentChar = nextChar();
        }

        return digits.toString();
    }

    private int computeIntegerValue(String digits){
        if (digits == null) return 0;

        int integerValue = 0;
        int prevValue = -1;
        int index = 0;

        while ((index < digits.length()) && (integerValue >= prevValue)){
            prevValue = integerValue;
            integerValue = 10* integerValue + Character.getNumericValue(digits.charAt(index++));
        }

        if (integerValue >= prevValue) return integerValue;

        type = PascalTokenType.ERROR;
        value = PascalErrorCode.RANGE_INTEGER;
        return 0;

    }

    private float computeFloatValue(String wholeDigits, String fractionDigits, String exponentDigits, char exponentSign){
        double floatValue = 0.0;
        int exponentValue = computeIntegerValue(exponentDigits);

        String digits = wholeDigits;

        if(exponentSign == '-'){
            exponentValue = -exponentValue;
        }
        if(fractionDigits != null){
            exponentValue -= fractionDigits.length();
            digits += fractionDigits;
        }

        if(Math.abs(exponentValue + wholeDigits.length()) > Float.MAX_EXPONENT){
            type = PascalTokenType.ERROR;
            value = PascalErrorCode.RANGE_REAL;
            return 0.0f;
        }

        int index = 0;
        while (index < digits.length()){
            floatValue = 10 * floatValue + Character.getNumericValue(digits.charAt(index++));
        }

        if(exponentValue !=0) {
            floatValue *= Math.pow(10, exponentValue);
        }
        return (float) floatValue;
    }
}
