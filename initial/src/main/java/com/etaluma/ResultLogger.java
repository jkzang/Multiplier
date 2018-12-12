package com.etaluma;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class ResultLogger implements JavaDelegate {

    public void execute(DelegateExecution execution) {
        String result = execution.getVariable("result").toString();
        System.out.println( "Calulation yielded the following result: " + result );
    }

}
