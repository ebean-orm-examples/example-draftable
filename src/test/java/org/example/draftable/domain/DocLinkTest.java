package org.example.draftable.domain;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DocLinkTest {

  @Test
  public void testDelete() {

    Link link1 = new Link("Ld1");
    link1.save();

    Ebean.delete(link1);
  }

  @Test
  public void testDirtyState() {

    Link link1 = new Link("Ls1");
    link1.save();

    Link draft1 = Ebean.find(Link.class).setId(link1.getId()).asDraft().findUnique();
    assertThat(draft1.isDirty()).isTrue();

    EbeanServer server = Ebean.getDefaultServer();
    server.publish(Link.class, link1.getId(), null);

    Link draft1b = Ebean.find(Link.class).setId(link1.getId()).asDraft().findUnique();
    assertThat(draft1b.isDirty()).isFalse();

  }

  @Test
  public void testSave() {

    Link link1 = new Link("LinkOne");
    link1.save();

    Link link2 = new Link("LinkTwo");
    link2.save();

    Link link3 = new Link("LinkThree");
    link3.save();

    EbeanServer server = Ebean.getDefaultServer();
    server.publish(Link.class, link1.getId(), null);
    server.publish(Link.class, link2.getId(), null);
    server.publish(Link.class, link3.getId(), null);

    Doc doc1 = new Doc("DocOne");
    doc1.getLinks().add(link1);
    doc1.getLinks().add(link2);
    doc1.save();

    Doc draftDoc1 = server.find(Doc.class)
        .setId(doc1.getId())
        .asDraft()
        .findUnique();

    assertThat(draftDoc1.getLinks()).hasSize(2);

    Doc liveDoc1 = server.publish(Doc.class, doc1.getId(), null);

    assertThat(liveDoc1.getLinks()).hasSize(2);
    assertThat(liveDoc1.getLinks()).extracting("id").contains(link1.getId(), link2.getId());


    draftDoc1.getLinks().remove(0);
    draftDoc1.getLinks().add(link3);

    draftDoc1.save();

    // publish with insert and delete of Links M2M relationship
    Doc liveDoc2 = server.publish(Doc.class, doc1.getId(), null);
    assertThat(liveDoc2.getLinks()).hasSize(2);
    assertThat(liveDoc2.getLinks()).extracting("id").contains(link2.getId(), link3.getId());

  }


}