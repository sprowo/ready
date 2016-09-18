package com.prowo.ymlchain.tree;

public interface ITree<T> {

    T climbe(Object... choices);

    ITree<T> addBranch(Object branchId, IBranch<T> branch);

    ITree<T> addLeaf(Object branchId, ILeaf<T> leaf);

    ITree<T> addTrunk(ITrunk<T> trunk);

}
