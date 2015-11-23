package org.example.draftable.domain;

import com.avaje.ebean.annotation.DraftDirty;
import com.avaje.ebean.annotation.Draftable;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;


@Draftable
@Entity
public class Link extends BaseDomain {

  String name;

  String location;

  @DraftDirty
  boolean dirty;

  @ManyToMany(mappedBy = "links")
  List<Doc> docs;

  public Link(String name) {
    this.name = name;
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
}
