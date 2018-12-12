package com.etaluma;

import java.util.stream.Collectors;
import java.util.List;
import java.util.Properties;
import java.util.Arrays;


import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class Multiplier implements JavaDelegate {

    public void execute(DelegateExecution execution) {

        String chosenProperty = execution.getVariable("chosenProperty").toString();
        Integer multiplier = Integer.valueOf(execution.getVariable("multiplier").toString());

        String chosenPropertyValues = execution.getVariable(chosenProperty).toString();

        List<String> list = Arrays.asList(chosenPropertyValues.split(","));

        Integer sum = list.stream()
                .map( i -> Integer.parseInt(i) * multiplier )
                .reduce( 0, (a, b) -> a + b );

        execution.setVariable( "result", sum );

    }

}
