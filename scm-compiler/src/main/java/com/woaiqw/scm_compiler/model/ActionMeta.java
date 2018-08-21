package com.woaiqw.scm_compiler.model;

/**
 * Created by haoran on 2018/8/16.
 */

public class ActionMeta {

    private String name;
    private String module;
    private int priority;
    private String process;
    private String sourcePath;


    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public ActionMeta(String name, String module, int priority, String process, String act) {
        this.module = module;

        this.name = name;
        this.priority = priority;
        this.process = process;
        this.sourcePath = act;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getSourcePath() {
        return sourcePath;
    }


    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public static ActionMeta build(String name, String module, int priority, String process, String sourcePath) {
        return new ActionMeta(name, module, priority, process, sourcePath);
    }

    public static ActionMeta build(String name, String module, String sourcePath) {
        return new ActionMeta(name, module, 0, "", sourcePath);
    }

    @Override
    public String toString() {
        return "ActionMeta{" +
                "name='" + name + '\'' +
                ", module='" + module + '\'' +
                ", priority=" + priority +
                ", process='" + process + '\'' +
                ", sourcePath='" + sourcePath + '\'' +
                '}';
    }


}
