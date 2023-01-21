package com.malfaang.e_culture_tool_a.listeners;

/*
 *
 *  @author adrianodelre
 */
@FunctionalInterface
public interface FailureListener {
    void onFail(String errorMsg);
}
