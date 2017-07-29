package sqlite.kripton64;

import android.database.sqlite.SQLiteDatabase;
import com.abubusoft.kripton.android.Logger;
import com.abubusoft.kripton.android.sqlite.AbstractDataSource;
import com.abubusoft.kripton.android.sqlite.DataSourceOptions;
import com.abubusoft.kripton.exception.KriptonRuntimeException;
import java.lang.Override;
import java.lang.Throwable;

/**
 * <p>
 * Rapresents implementation of datasource Bean64ADataSource.
 * This class expose database interface through Dao attribute.
 * </p>
 *
 * @see Bean64ADataSource
 * @see BindBean64ADaoFactory
 * @see Bean64ADao
 * @see Bean64ADaoImpl
 * @see Bean64A
 */
public class BindBean64ADataSource extends AbstractDataSource implements BindBean64ADaoFactory, Bean64ADataSource {
  /**
   * <p>datasource singleton</p>
   */
  private static BindBean64ADataSource instance;

  /**
   * <p>dao instance</p>
   */
  protected Bean64ADaoImpl bean64ADao = new Bean64ADaoImpl(this);

  protected BindBean64ADataSource() {
    this(null);
  }

  protected BindBean64ADataSource(DataSourceOptions options) {
    super("dummy", 1, null);
  }

  @Override
  public Bean64ADaoImpl getBean64ADao() {
    return bean64ADao;
  }

  /**
   * <p>Executes a transaction. This method <strong>is thread safe</strong> to avoid concurrent problems. Thedrawback is only one transaction at time can be executed. The database will be open in write mode.</p>
   *
   * @param transaction
   * 	transaction to execute
   */
  public void execute(Transaction transaction) {
    SQLiteDatabase connection=openWritableDatabase();
    try {
      connection.beginTransaction();
      if (transaction!=null && transaction.onExecute(this)) {
        connection.setTransactionSuccessful();
      }
    } catch(Throwable e) {
      Logger.error(e.getMessage());
      e.printStackTrace();
      if (transaction!=null) transaction.onError(e);
    } finally {
      try {
        connection.endTransaction();
      } catch (Throwable e) {
        Logger.warn("error closing transaction %s", e.getMessage());
      }
      close();
    }
  }

  /**
   * instance
   */
  public static BindBean64ADataSource instance() {
    if (instance==null) {
      instance=new BindBean64ADataSource();
    }
    return instance;
  }

  /**
   * Retrieve data source instance and open it.
   * @return opened dataSource instance.
   */
  public static BindBean64ADataSource open() {
    if (instance==null) {
      instance=new BindBean64ADataSource();
    }
    instance.openWritableDatabase();
    return instance;
  }

  /**
   * Retrieve data source instance and open it in read only mode.
   * @return opened dataSource instance.
   */
  public static BindBean64ADataSource openReadOnly() {
    if (instance==null) {
      instance=new BindBean64ADataSource();
    }
    instance.openReadOnlyDatabase();
    return instance;
  }

  /**
   * onCreate
   */
  @Override
  public void onCreate(SQLiteDatabase database) {
    // generate tables
    Logger.info("DDL: %s",Bean64ATable.CREATE_TABLE_SQL);
    database.execSQL(Bean64ATable.CREATE_TABLE_SQL);
    if (options.databaseLifecycleHandler != null) {
      options.databaseLifecycleHandler.onCreate(database);
    }
  }

  /**
   * onUpgrade
   */
  @Override
  public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    if (options.databaseLifecycleHandler != null) {
      options.databaseLifecycleHandler.onUpdate(database, oldVersion, newVersion, true);
    } else {
      // drop tables
      Logger.info("DDL: %s",Bean64ATable.DROP_TABLE_SQL);
      database.execSQL(Bean64ATable.DROP_TABLE_SQL);

      // generate tables
      Logger.info("DDL: %s",Bean64ATable.CREATE_TABLE_SQL);
      database.execSQL(Bean64ATable.CREATE_TABLE_SQL);
    }
  }

  /**
   * onConfigure
   */
  @Override
  public void onConfigure(SQLiteDatabase database) {
    // configure database
    if (options.databaseLifecycleHandler != null) {
      options.databaseLifecycleHandler.onConfigure(database);
    }
  }

  /**
   * Build instance.
   * @return dataSource instance.
   */
  public static Bean64ADataSource build(DataSourceOptions options) {
    if (instance==null) {
      instance=new BindBean64ADataSource(options);
    }
    instance.openWritableDatabase();
    return instance;
  }

  /**
   * interface to define transactions
   */
  public interface Transaction extends AbstractTransaction<BindBean64ADaoFactory> {
  }

  /**
   * Simple class implements interface to define transactions
   */
  public abstract static class SimpleTransaction implements Transaction {
    @Override
    public void onError(Throwable e) {
      throw(new KriptonRuntimeException(e));
    }
  }
}
