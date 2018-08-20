package com.woaiqw.scm_compiler.model;

/**
 * Created by haoran on 2018/8/16.
 */

public class ActionMeta {

    private String name;
    private int priority;
    private String process;
    private String sourcePath;


    public ActionMeta(String name, int priority, String process, String act) {
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

    public static ActionMeta build(String name, int priority, String process, String sourcePath) {
        return new ActionMeta(name, priority, process, sourcePath);
    }

    public static ActionMeta build(String name, String sourcePath) {
        return new ActionMeta(name, 0, "", sourcePath);
    }

    @Override
    public String toString() {
        return "ActionMeta{" +
                "name='" + name + '\'' +
                ", priority=" + priority +
                ", process='" + process + '\'' +
                ", sourcePath=" + sourcePath +
                '}';
    }

}
