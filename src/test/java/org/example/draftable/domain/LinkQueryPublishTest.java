package org.example.draftable.domain;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Query;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LinkQueryPublishTest {

  @Test
  public void testPublishViaQuery() {

    Link link1 = new Link("L1");
    link1.save();

    Link link2 = new Link("L2");
    link2.save();

    Link link3 = new Link("L3");
    link3.save();

    EbeanServer server = Ebean.getDefaultServer();

    List<Object> ids = new ArrayList<Object>();
    ids.add(link1.getId());
    ids.add(link2.getId());
    ids.add(link3.getId());

    Query<Link> pubQuery = server.find(Link.class)
        .where().idIn(ids)
        .order().asc("id");


    List<Link> pubList = server.publish(pubQuery);

    assertThat(pubList).hasSize(3);
    assertThat(pubList).extracting("id").contains(link1.getId(), link2.getId(), link3.getId());

  }


}