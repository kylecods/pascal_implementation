package factory;

import lib.intermediate.*;
import lib.intermediate.implementation.ICodeImpl;
import lib.intermediate.implementation.ICodeNodeImpl;


public class ICodeFactory {
    public static ICode createICode(){
        return new ICodeImpl();
    }
    public static ICodeNode createICodeNode(ICodeNodeType type){
        return  new ICodeNodeImpl(type);
    }
}
