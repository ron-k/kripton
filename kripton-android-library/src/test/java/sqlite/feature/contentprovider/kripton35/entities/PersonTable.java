package sqlite.feature.contentprovider.kripton35.entities;

import com.abubusoft.kripton.android.sqlite.SQLiteTable;

/**
 * <p>
 * Entity <code>Person</code> is associated to table <code>person</code>
 * This class represents table associated to entity.
 * </p>
 *  @see Person
 */
public class PersonTable implements SQLiteTable {
  /**
   * Costant represents typeName of table person
   */
  public static final String TABLE_NAME = "person";

  /**
   * <p>
   * DDL to create table person
   * </p>
   *
   * <pre>CREATE TABLE person (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, alias_parent_id INTEGER, birth_city TEXT, birth_day TEXT, city INTEGER, name TEXT, surname TEXT, value INTEGER, FOREIGN KEY(alias_parent_id) REFERENCES person(id), FOREIGN KEY(city) REFERENCES city(id));</pre>
   */
  public static final String CREATE_TABLE_SQL = "CREATE TABLE person (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, alias_parent_id INTEGER, birth_city TEXT, birth_day TEXT, city INTEGER, name TEXT, surname TEXT, value INTEGER, FOREIGN KEY(alias_parent_id) REFERENCES person(id), FOREIGN KEY(city) REFERENCES city(id));";

  /**
   * <p>
   * DDL to drop table person
   * </p>
   *
   * <pre>DROP TABLE IF EXISTS person;</pre>
   */
  public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS person;";

  /**
   * Entity's property <code>id</code> is associated to table column <code>id</code>. This costant represents column name.
   *
   *  @see Person#id
   */
  public static final String COLUMN_ID = "id";

  /**
   * Entity's property <code>parentId</code> is associated to table column <code>alias_parent_id</code>. This costant represents column name.
   *
   *  @see Person#parentId
   */
  public static final String COLUMN_PARENT_ID = "alias_parent_id";

  /**
   * Entity's property <code>birthCity</code> is associated to table column <code>birth_city</code>. This costant represents column name.
   *
   *  @see Person#birthCity
   */
  public static final String COLUMN_BIRTH_CITY = "birth_city";

  /**
   * Entity's property <code>birthDay</code> is associated to table column <code>birth_day</code>. This costant represents column name.
   *
   *  @see Person#birthDay
   */
  public static final String COLUMN_BIRTH_DAY = "birth_day";

  /**
   * Entity's property <code>city</code> is associated to table column <code>city</code>. This costant represents column name.
   *
   *  @see Person#city
   */
  public static final String COLUMN_CITY = "city";

  /**
   * Entity's property <code>name</code> is associated to table column <code>name</code>. This costant represents column name.
   *
   *  @see Person#name
   */
  public static final String COLUMN_NAME = "name";

  /**
   * Entity's property <code>surname</code> is associated to table column <code>surname</code>. This costant represents column name.
   *
   *  @see Person#surname
   */
  public static final String COLUMN_SURNAME = "surname";

  /**
   * Entity's property <code>value</code> is associated to table column <code>value</code>. This costant represents column name.
   *
   *  @see Person#value
   */
  public static final String COLUMN_VALUE = "value";

  /**
   * Columns array
   */
  private static final String[] COLUMNS = {COLUMN_ID, COLUMN_PARENT_ID, COLUMN_BIRTH_CITY, COLUMN_BIRTH_DAY, COLUMN_CITY, COLUMN_NAME, COLUMN_SURNAME, COLUMN_VALUE};

  /**
   * Columns array
   */
  @Override
  public String[] columns() {
    return COLUMNS;
  }

  /**
   * table name
   */
  @Override
  public String name() {
    return TABLE_NAME;
  }
}
