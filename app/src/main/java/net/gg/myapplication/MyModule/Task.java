package net.gg.myapplication.MyModule;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Task {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String State;
    private String title;
    private String body;
//    public enum  StateEnum{
//         ASSIGNED,IN_PROGRESS,COMPLETE
//    }

    public Task(String title, String body, String state) {
        this.title = title;
        this.body = body;
        this.State= state;
        
        
    }

    public Task() {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
