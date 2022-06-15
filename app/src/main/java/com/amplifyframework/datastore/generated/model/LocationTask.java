package com.amplifyframework.datastore.generated.model;

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

/** This is an auto generated class representing the LocationTask type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "LocationTasks")
public final class LocationTask implements Model {
  public static final QueryField ID = field("LocationTask", "id");
  public static final QueryField LONGITUDE = field("LocationTask", "Longitude");
  public static final QueryField LATITUDE = field("LocationTask", "Latitude");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String") String Longitude;
  private final @ModelField(targetType="String") String Latitude;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getLongitude() {
      return Longitude;
  }
  
  public String getLatitude() {
      return Latitude;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private LocationTask(String id, String Longitude, String Latitude) {
    this.id = id;
    this.Longitude = Longitude;
    this.Latitude = Latitude;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      LocationTask locationTask = (LocationTask) obj;
      return ObjectsCompat.equals(getId(), locationTask.getId()) &&
              ObjectsCompat.equals(getLongitude(), locationTask.getLongitude()) &&
              ObjectsCompat.equals(getLatitude(), locationTask.getLatitude()) &&
              ObjectsCompat.equals(getCreatedAt(), locationTask.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), locationTask.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getLongitude())
      .append(getLatitude())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("LocationTask {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("Longitude=" + String.valueOf(getLongitude()) + ", ")
      .append("Latitude=" + String.valueOf(getLatitude()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
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
  public static LocationTask justId(String id) {
    return new LocationTask(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      Longitude,
      Latitude);
  }
  public interface BuildStep {
    LocationTask build();
    BuildStep id(String id);
    BuildStep longitude(String longitude);
    BuildStep latitude(String latitude);
  }
  

  public static class Builder implements BuildStep {
    private String id;
    private String Longitude;
    private String Latitude;
    @Override
     public LocationTask build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new LocationTask(
          id,
          Longitude,
          Latitude);
    }
    
    @Override
     public BuildStep longitude(String longitude) {
        this.Longitude = longitude;
        return this;
    }
    
    @Override
     public BuildStep latitude(String latitude) {
        this.Latitude = latitude;
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
    private CopyOfBuilder(String id, String longitude, String latitude) {
      super.id(id);
      super.longitude(longitude)
        .latitude(latitude);
    }
    
    @Override
     public CopyOfBuilder longitude(String longitude) {
      return (CopyOfBuilder) super.longitude(longitude);
    }
    
    @Override
     public CopyOfBuilder latitude(String latitude) {
      return (CopyOfBuilder) super.latitude(latitude);
    }
  }
  
}
