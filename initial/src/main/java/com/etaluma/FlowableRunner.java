package com.etaluma;

import java.util.Scanner;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.flowable.task.api.Task;
import org.flowable.engine.TaskService;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;

import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;


import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class FlowableRunner {

    public static void main( String[] args ) {
        SpringApplication.run( FlowableRunner.class, args );
    }

    @Bean
    public CommandLineRunner multipier( ApplicationContext ctx ) {
         return args -> {

                //Create a process engine
                ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                        .setJdbcUrl("jdbc:h2:mem:flowable;DB_CLOSE_DELAY=-1")
                        .setJdbcUsername("sa")
                        .setJdbcPassword("")
                        .setJdbcDriver("org.h2.Driver")
                        .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

                ProcessEngine processEngine = cfg.buildProcessEngine();

                //Deploy process definition
                RepositoryService repositoryService = processEngine.getRepositoryService();
                Deployment deployment = repositoryService.createDeployment()
                        .addClasspathResource( "Multiplier.bpmn20.xml" )
                        .deploy();

                //Get process definition name
                ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                        .deploymentId(deployment.getId())
                        .singleResult();

                //Get a runtime service
                RuntimeService runtime = processEngine.getRuntimeService();

                //Start A Process Instance
                runtime.startProcessInstanceByKey("etaluma-challenge");

                TaskService taskService = processEngine.getTaskService();
                List<Task> firstTaskList = taskService.createTaskQuery().taskCandidateGroup("user").list();

                //First User Task
                Task firstTask = firstTaskList.get(0);
                System.out.println(firstTask.getName());

                Scanner scanner = new Scanner(System.in);
                String chosenProperty = scanner.nextLine();

                Map<String, Object> processVariables1 = new HashMap<String, Object>();
                processVariables1.put("chosenProperty", chosenProperty);

                taskService.complete(firstTask.getId(), processVariables1);

                //Second User Task
                List<Task> secondTaskList = taskService.createTaskQuery().taskCandidateGroup("user").list();

                Task secondTask = secondTaskList.get(0);
                System.out.println(secondTask.getName());

                String multiplier = scanner.nextLine();

                Map<String, Object> processVariables2 = new HashMap<String, Object>();
                processVariables2.put("multiplier", multiplier);

                taskService.complete(secondTask.getId(), processVariables2);

                //Third User Task
                List<Task> thirdTaskList = taskService.createTaskQuery().taskCandidateGroup("user").list();
                Task thirdTask = thirdTaskList.get(0);

                Boolean stop = scanner.nextLine().toLowerCase().equals("stop");

                Map<String, Object> processVariables3 = new HashMap<String, Object>();
                processVariables3.put("stop", stop);

                taskService.complete(thirdTask.getId(), processVariables3);

                //once finished
                scanner.close();


         };
    }

}