package com.tschanz.v_bro.common.testing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class SpyHelper<E extends Throwable> {
    private final List<MethodCall> methodCalls = new ArrayList<>();
    private E throwException;


    public void setThrowException(E exception) {
        this.throwException = exception;
    }


    public void checkThrowException() throws E {
        if (this.throwException != null) {
            throw this.throwException;
        }
    }


    public void reportMethodCall(String methodName, Object... arguments) {
        this.methodCalls.add(new MethodCall(methodName, Arrays.asList(arguments)));
    }


    public int getMethodCallCount() {
        return this.methodCalls.size();
    }


    public int getMethodCallCount(String methodName) {
        return this.findMethodCalls(methodName).size();
    }


    public boolean isMethodCalled(String methodName) {
        return this.getMethodCallCount(methodName) > 0;
    }


    public Object getMethodArgument(String methodName, int argumentIndex) {
        return this.getMethodArgument(methodName, 0, argumentIndex);
    }


    public Object getMethodArgument(String methodName, int callIndex, int argumentIndex) {
        List<MethodCall> methodCallMatches = this.findMethodCalls(methodName);
        if (methodCallMatches.size() == 0 || methodCallMatches.size() <= callIndex || methodCallMatches.get(callIndex).arguments.size() <= argumentIndex) {
            return null;
        } else {
            return methodCallMatches.get(callIndex).arguments.get(argumentIndex);
        }
    }


    private List<MethodCall> findMethodCalls(String methodName) {
        return this.methodCalls
            .stream()
            .filter(calls -> calls.methodName.equals(methodName))
            .collect(Collectors.toList());
    }


    public class MethodCall {
        public String methodName;
        public List<Object> arguments;


        public MethodCall(String methodName, List<Object> arguments) {
            this.methodName = methodName;
            this.arguments = arguments != null ? arguments : new ArrayList<>();
        }
    }
}
