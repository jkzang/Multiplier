package com.etaluma;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class FileReader implements JavaDelegate {

    public void execute(DelegateExecution execution) {

        Properties prop = new Properties();
        InputStream input = null;

        try {

            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("challenge.properties");

            // load a properties file
            prop.load(is);

            String property1 = prop.getProperty("Collection.1");
            String property2 = prop.getProperty("Collection.2");

            execution.setVariable( "Collection.1", property1 );
            execution.setVariable( "Collection.2", property2 );

            // get the property value and print it out
            System.out.println("Collection.1=" + prop.getProperty("Collection.1"));
            System.out.println("Collection.2=" + prop.getProperty("Collection.2"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}

