package factory;

import lib.backend.Backend;
import lib.compiler.CodeGenerator;
import lib.interpreter.Executor;

public class BackendFactory {

    public static Backend createBackend(String operation) throws Exception{
        if (operation.equalsIgnoreCase("compile")) return new CodeGenerator();

        if (operation.equalsIgnoreCase("execute")) return new Executor();

        throw new Exception("Backend Factory: Invalid Operation '" + operation + "'");
    }
}
