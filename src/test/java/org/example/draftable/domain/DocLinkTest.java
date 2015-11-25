package org.example.draftable.domain;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Query;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DocLinkTest {

  @Test
  public void testDelete_whenNotPublished() {

    Link link1 = new Link("Ld1");
    link1.save();

    Ebean.delete(link1);
  }

  @Test
  public void testDelete_whenPublished() {

    Link link1 = new Link("Ld2");
    link1.save();
    EbeanServer server = Ebean.getDefaultServer();
    server.publish(Link.class, link1.getId());

    link1 = Ebean.find(Link.class).setId(link1.getId()).asDraft().findUnique();
    link1.delete();

    Link live = Ebean.find(Link.class).setId(link1.getId()).findUnique();
    assertThat(live).isNull();
  }

  @Test
  public void testDirtyState() {

    Timestamp when = new Timestamp(System.currentTimeMillis());
    String comment = "Really interesting";

    Link link1 = new Link("Ls1");
    link1.setComment(comment);
    link1.setWhenPublish(when);
    link1.save();

    Link draft1 = Ebean.find(Link.class).setId(link1.getId()).asDraft().findUnique();
    assertThat(draft1.isDirty()).isTrue();

    EbeanServer server = Ebean.getDefaultServer();

    Link linkLive = server.publish(Link.class, link1.getId(), null);
    assertThat(linkLive.getComment()).isEqualTo(comment);
    assertThat(linkLive.getWhenPublish()).isEqualTo(when);

    Link draft1b = Ebean.find(Link.class).setId(link1.getId()).asDraft().findUnique();
    assertThat(draft1b.isDirty()).isFalse();
    assertThat(draft1b.getComment()).isNull();
    assertThat(draft1b.getWhenPublish()).isNull();

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

    // delete the draft and live beans (with associated children)
    draftDoc1.delete();
  }


  @Test
  public void testDraftRestore() {

    Link link1 = new Link("Ldr1");
    link1.setLocation("firstLocation");
    link1.save();

    EbeanServer server = Ebean.getDefaultServer();

    server.publish(Link.class, link1.getId(), null);

    Link draftLink = Ebean.find(Link.class)
        .setId(link1.getId())
        .asDraft()
        .findUnique();

    draftLink.setLocation("secondLocation");
    draftLink.save();

    server.draftRestore(Link.class, link1.getId(), null);

    draftLink = Ebean.find(Link.class)
        .setId(link1.getId())
        .asDraft()
        .findUnique();

    assertThat(draftLink.getLocation()).isEqualTo("firstLocation");

  }

  @Test
  public void testDraftRestoreViaQuery() {

    Link link1 = new Link("Ldr1");
    link1.setLocation("firstLocation");
    link1.setComment("Banana");
    link1.save();

    EbeanServer server = Ebean.getDefaultServer();

    server.publish(Link.class, link1.getId(), null);

    Link draftLink = Ebean.find(Link.class)
        .setId(link1.getId())
        .asDraft()
        .findUnique();

    draftLink.setLocation("secondLocation");
    draftLink.setComment("A good change");
    draftLink.save();

    Query<Link> query = server.find(Link.class).where().eq("id", link1.getId()).query();
    List<Link> links = server.draftRestore(query);

    assertThat(links).hasSize(1);
    assertThat(links.get(0).getLocation()).isEqualTo("firstLocation");
    assertThat(links.get(0).isDirty()).isEqualTo(false);
    assertThat(links.get(0).getComment()).isNull();

  }
}