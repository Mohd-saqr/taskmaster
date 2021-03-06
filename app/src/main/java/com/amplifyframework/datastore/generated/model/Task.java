package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.HasOne;
import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Task type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Tasks")
public final class Task implements Model {
  public static final QueryField ID = field("Task", "id");
  public static final QueryField TITLE = field("Task", "title");
  public static final QueryField BODY = field("Task", "body");
  public static final QueryField STATE = field("Task", "state");
  public static final QueryField TASK_LOCATION_ID = field("Task", "taskLocationId");
  public static final QueryField TEAM_TASKS_ID = field("Task", "teamTasksId");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String") String title;
  private final @ModelField(targetType="String") String body;
  private final @ModelField(targetType="String") String state;
  private final @ModelField(targetType="LocationTask") @HasOne(associatedWith = "id", type = LocationTask.class) LocationTask location = null;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  private final @ModelField(targetType="ID") String taskLocationId;
  private final @ModelField(targetType="ID") String teamTasksId;
  public String getId() {
      return id;
  }
  
  public String getTitle() {
      return title;
  }
  
  public String getBody() {
      return body;
  }
  
  public String getState() {
      return state;
  }
  
  public LocationTask getLocation() {
      return location;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  public String getTaskLocationId() {
      return taskLocationId;
  }
  
  public String getTeamTasksId() {
      return teamTasksId;
  }
  
  private Task(String id, String title, String body, String state, String taskLocationId, String teamTasksId) {
    this.id = id;
    this.title = title;
    this.body = body;
    this.state = state;
    this.taskLocationId = taskLocationId;
    this.teamTasksId = teamTasksId;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Task task = (Task) obj;
      return ObjectsCompat.equals(getId(), task.getId()) &&
              ObjectsCompat.equals(getTitle(), task.getTitle()) &&
              ObjectsCompat.equals(getBody(), task.getBody()) &&
              ObjectsCompat.equals(getState(), task.getState()) &&
              ObjectsCompat.equals(getCreatedAt(), task.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), task.getUpdatedAt()) &&
              ObjectsCompat.equals(getTaskLocationId(), task.getTaskLocationId()) &&
              ObjectsCompat.equals(getTeamTasksId(), task.getTeamTasksId());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getTitle())
      .append(getBody())
      .append(getState())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .append(getTaskLocationId())
      .append(getTeamTasksId())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Task {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("title=" + String.valueOf(getTitle()) + ", ")
      .append("body=" + String.valueOf(getBody()) + ", ")
      .append("state=" + String.valueOf(getState()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()) + ", ")
      .append("taskLocationId=" + String.valueOf(getTaskLocationId()) + ", ")
      .append("teamTasksId=" + String.valueOf(getTeamTasksId()))
      .append("}")
      .toString();
  }
  
  public static BuildStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static Task justId(String id) {
    return new Task(
      id,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      title,
      body,
      state,
      taskLocationId,
      teamTasksId);
  }
  public interface BuildStep {
    Task build();
    BuildStep id(String id);
    BuildStep title(String title);
    BuildStep body(String body);
    BuildStep state(String state);
    BuildStep taskLocationId(String taskLocationId);
    BuildStep teamTasksId(String teamTasksId);
  }
  

  public static class Builder implements BuildStep {
    private String id;
    private String title;
    private String body;
    private String state;
    private String taskLocationId;
    private String teamTasksId;
    @Override
     public Task build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Task(
          id,
          title,
          body,
          state,
          taskLocationId,
          teamTasksId);
    }
    
    @Override
     public BuildStep title(String title) {
        this.title = title;
        return this;
    }
    
    @Override
     public BuildStep body(String body) {
        this.body = body;
        return this;
    }
    
    @Override
     public BuildStep state(String state) {
        this.state = state;
        return this;
    }
    
    @Override
     public BuildStep taskLocationId(String taskLocationId) {
        this.taskLocationId = taskLocationId;
        return this;
    }
    
    @Override
     public BuildStep teamTasksId(String teamTasksId) {
        this.teamTasksId = teamTasksId;
        return this;
    }
    
    /** 
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String title, String body, String state, String taskLocationId, String teamTasksId) {
      super.id(id);
      super.title(title)
        .body(body)
        .state(state)
        .taskLocationId(taskLocationId)
        .teamTasksId(teamTasksId);
    }
    
    @Override
     public CopyOfBuilder title(String title) {
      return (CopyOfBuilder) super.title(title);
    }
    
    @Override
     public CopyOfBuilder body(String body) {
      return (CopyOfBuilder) super.body(body);
    }
    
    @Override
     public CopyOfBuilder state(String state) {
      return (CopyOfBuilder) super.state(state);
    }
    
    @Override
     public CopyOfBuilder taskLocationId(String taskLocationId) {
      return (CopyOfBuilder) super.taskLocationId(taskLocationId);
    }
    
    @Override
     public CopyOfBuilder teamTasksId(String teamTasksId) {
      return (CopyOfBuilder) super.teamTasksId(teamTasksId);
    }
  }
  
}
