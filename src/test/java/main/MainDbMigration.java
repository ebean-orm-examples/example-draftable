package main;

import com.avaje.ebean.config.dbplatform.DbPlatformName;
import com.avaje.ebean.dbmigration.DbMigration;

import java.io.IOException;

/**
 * Run the DB Migration.
 *
 * Update the version prior to running.
 */
public class MainDbMigration {

  public static void main(String[] args) throws IOException {

    DbMigration dbMigration = new DbMigration();

    // set path that the migration xml and ddl is located
    // this defaults to standard maven src/main/resources
    //dbMigration.setPathToResources("src/main/resources");

    // Postgres as the target DB
    dbMigration.setPlatform(DbPlatformName.POSTGRES);

    System.setProperty("ddl.migration.version", "1.1");

    // generate the migration xml and ddl
    dbMigration.generateMigration();
  }
}