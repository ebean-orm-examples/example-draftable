package org.example.draftable.domain;

import com.avaje.ebean.annotation.DraftDirty;
import com.avaje.ebean.annotation.DraftReset;
import com.avaje.ebean.annotation.Draftable;
import com.avaje.ebean.annotation.SoftDelete;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.sql.Timestamp;
import java.util.List;


@Draftable
@Entity
public class Link extends BaseDomain {

  @SoftDelete
  boolean deleted;

  String name;

  String location;

  /**
   * Draft reset to null on publish.
   */
  @DraftReset
  Timestamp whenPublish;

  /**
   * Draft reset to null on publish.
   */
  @DraftReset
  String comment;

  @DraftDirty
  boolean dirty;

  @ManyToMany(mappedBy = "links")
  List<Doc> docs;

  public Link(String name) {
    this.name = name;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public List<Doc> getDocs() {
    return docs;
  }

  public void setDocs(List<Doc> docs) {
    this.docs = docs;
  }

  public boolean isDirty() {
    return dirty;
  }

  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }

  public Timestamp getWhenPublish() {
    return whenPublish;
  }

  public void setWhenPublish(Timestamp whenPublish) {
    this.whenPublish = whenPublish;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }
}
