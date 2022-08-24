package lib.intermediate.implementation;

import lib.intermediate.ICode;
import lib.intermediate.ICodeNode;

public class ICodeImpl implements ICode {

    private ICodeNode root;

    @Override
    public ICodeNode setRoot(ICodeNode node) {
        root = node;
        return root;
    }

    @Override
    public ICodeNode getRoot() {
        return root;
    }
}
