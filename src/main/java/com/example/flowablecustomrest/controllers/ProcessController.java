package com.example.flowablecustomrest.controllers;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest-driven-process")
public class ProcessController {

    private final RuntimeService runtimeService;
    private final TaskService taskService;


    public ProcessController(RuntimeService runtimeService, TaskService taskService) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
    }

    @GetMapping("/start")
    public String startProcess() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("restDrivenProcess");

        return processInstance.getProcessInstanceId();
    }

    @GetMapping("/continue/{processInstanceId}")
    public String continueProcess(@PathVariable("processInstanceId") String processInstanceId) {
        List<Task> tasks = taskService.createTaskQuery().taskDefinitionKey("secondRequest").processInstanceId(processInstanceId).list();

        tasks.stream().forEach(task -> taskService.complete(task.getId()));

        return tasks.size() + "complete";
    }

    @GetMapping("/movingOn/{processInstanceId}")
    public List<String>  movingOn(@PathVariable("processInstanceId") String processInstanceId) {
        List<Task> tasks = taskService.createTaskQuery().taskDefinitionKey("otherTask").processInstanceId(processInstanceId).list();

        return tasks.stream().map(Task::getId).collect(Collectors.toList());
    }

}
