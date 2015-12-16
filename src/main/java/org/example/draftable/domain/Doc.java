package org.example.draftable.domain;

import com.avaje.ebean.annotation.DbJson;
import com.avaje.ebean.annotation.Draftable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;
import java.util.Map;


@Draftable
@Entity
public class Doc extends BaseDomain {

  String name;

  @ManyToMany(cascade = CascadeType.ALL)
  List<Link> links;

  @DbJson
  Map<String,Object> content;

  public Doc(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Link> getLinks() {
    return links;
  }

  public void setLinks(List<Link> links) {
    this.links = links;
  }

  public Map<String, Object> getContent() {
    return content;
  }

  public void setContent(Map<String, Object> content) {
    this.content = content;
  }
}
