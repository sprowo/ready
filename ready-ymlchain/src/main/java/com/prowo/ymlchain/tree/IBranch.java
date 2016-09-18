package com.prowo.ymlchain.tree;

public interface IBranch<T> {
    T climbe(Object... choices);

    IBranch<T> addBranch(Object branchId, IBranch<T> branch);

    IBranch<T> addLeaf(Object branchId, ILeaf<T> leaf);

    IBranch<T> addTrunk(ITrunk<T> trunk);

}
