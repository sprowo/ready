package com.prowo.ymlchain.tree.impl;

import com.prowo.ymlchain.tree.IBranch;
import com.prowo.ymlchain.tree.ILeaf;
import com.prowo.ymlchain.tree.ITree;
import com.prowo.ymlchain.tree.ITrunk;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tree<T> implements ITree<T> {
    Map<Object, IBranch<T>> branchs = new HashMap<Object, IBranch<T>>();
    Map<Object, ILeaf<T>> leafs = new HashMap<Object, ILeaf<T>>();
    ITrunk<T> trunk;

    @Override
    public Tree<T> addBranch(Object branchId, IBranch<T> branch) {
        branchs.put(branchId, branch);
        return this;
    }

    @Override
    public Tree<T> addLeaf(Object leafId, ILeaf<T> leaf) {
        leafs.put(leafId, leaf);
        return this;
    }

    @Override
    public T climbe(Object... choices) {
        // first choice branch by id ,next choice leaf
        if (choices == null) {
            return trunk.fly();
        }
        List<Object> list = Arrays.asList(choices);
        for (Object choice : list) {
            if (branchs.get(choice) != null) {
                return branchs.get(choice).climbe(list.subList(1, list.size()).toArray());
            }
            if (leafs.get(choice) != null) {
                return leafs.get(choice).fly();
            }
        }
        return trunk.fly();

    }

    @Override
    public ITree<T> addTrunk(ITrunk<T> trunk) {
        this.trunk = trunk;
        return this;
    }
}
