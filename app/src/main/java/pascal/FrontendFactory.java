package pascal;

import lib.frontend.Parser;
import lib.frontend.Source;


public class FrontendFactory {
    public static Parser createParser(String lang, String type, Source source) throws Exception{
        if(lang.equalsIgnoreCase("Pascal") && type.equalsIgnoreCase("top-down")) {
            var scanner = new PascalScanner(source);

            return new PascalParserTD(scanner);
        }

        if(!lang.equalsIgnoreCase("Pascal")) throw new Exception("Parser factory: Invalid language '" + lang + "'");

        throw new Exception("Parser Factory: Invalid type '"+ type + "'");
    }
}
