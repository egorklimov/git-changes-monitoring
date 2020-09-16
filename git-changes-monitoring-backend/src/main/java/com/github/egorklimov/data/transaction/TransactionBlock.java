package com.github.egorklimov.data.transaction;

@FunctionalInterface
public interface TransactionBlock {
    void apply() throws Exception;
}
