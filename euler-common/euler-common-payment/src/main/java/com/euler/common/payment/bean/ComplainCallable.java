package com.euler.common.payment.bean;

public interface ComplainCallable<T> {


    T call(String json) throws Exception;

}
