package net.gg.myapplication.MyModule;

import java.util.Arrays;

public class Task {

    private String State;
    private String title;
    private String body;
    public enum  StateEnum{
         ASSIGNED,IN_PROGRESS,COMPLETE
    }

    public Task(String title, String body, StateEnum state) {
        this.title = title;
        this.body = body;
        this.State= state.toString();
        
        
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }
}
