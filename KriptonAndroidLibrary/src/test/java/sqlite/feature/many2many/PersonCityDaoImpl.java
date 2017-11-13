package sqlite.feature.many2many;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.abubusoft.kripton.android.Logger;
import com.abubusoft.kripton.android.sqlite.AbstractDao;
import com.abubusoft.kripton.android.sqlite.KriptonContentValues;
import com.abubusoft.kripton.android.sqlite.KriptonDatabaseWrapper;
import com.abubusoft.kripton.android.sqlite.SQLContext;
import com.abubusoft.kripton.common.StringUtils;
import com.abubusoft.kripton.common.Triple;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * DAO implementation for entity <code>PersonCity</code>, based on interface <code>GeneratedPersonCityDao</code>
 * </p>
 *
 *  @see PersonCity
 *  @see GeneratedPersonCityDao
 *  @see PersonCityTable
 */
public class PersonCityDaoImpl extends AbstractDao implements GeneratedPersonCityDao {
  private static final String SELECT_BY_ID_SQL5 = "SELECT id, person_id, city_id FROM person_city WHERE id=?";

  private static final String SELECT_BY_PERSON_ID_SQL6 = "SELECT id, person_id, city_id FROM person_city WHERE person_id=?";

  private static final String SELECT_BY_CITY_ID_SQL7 = "SELECT id, person_id, city_id FROM person_city WHERE city_id=?";

  private static SQLiteStatement deleteByIdPreparedStatement0;

  private static SQLiteStatement deleteByPersonIdPreparedStatement1;

  private static SQLiteStatement deleteByCityIdPreparedStatement2;

  private static SQLiteStatement insertPreparedStatement3;

  public PersonCityDaoImpl(SQLContext context) {
    super(context);
  }

  /**
   * <h2>Select SQL:</h2>
   *
   * <pre>SELECT id, person_id, city_id FROM person_city WHERE id=${id}</pre>
   *
   * <h2>Projected columns:</h2>
   * <dl>
   * 	<dt>id</dt><dd>is associated to bean's property <strong>id</strong></dd>
   * 	<dt>person_id</dt><dd>is associated to bean's property <strong>personId</strong></dd>
   * 	<dt>city_id</dt><dd>is associated to bean's property <strong>cityId</strong></dd>
   * </dl>
   *
   * <h2>Query's parameters:</h2>
   * <dl>
   * 	<dt>${id}</dt><dd>is binded to method's parameter <strong>id</strong></dd>
   * </dl>
   *
   * @param id
   * 	is binded to <code>${id}</code>
   * @return selected bean or <code>null</code>.
   */
  @Override
  public PersonCity selectById(long id) {
    KriptonContentValues _contentValues=contentValues();
    // query SQL is statically defined
    String _sql=SELECT_BY_ID_SQL5;
    // add where arguments
    _contentValues.addWhereArgs(String.valueOf(id));
    String[] _sqlArgs=_contentValues.whereArgsAsArray();
    // log section BEGIN
    if (_context.isLogEnabled()) {
      // manage log
      Logger.info(_sql);

      // log for where parameters -- BEGIN
      int _whereParamCounter=0;
      for (String _whereParamItem: _contentValues.whereArgs()) {
        Logger.info("==> param%s: '%s'",(_whereParamCounter++), StringUtils.checkSize(_whereParamItem));
      }
      // log for where parameters -- END
    }
    // log section END
    try (Cursor cursor = database().rawQuery(_sql, _sqlArgs)) {
      // log section BEGIN
      if (_context.isLogEnabled()) {
        Logger.info("Rows found: %s",cursor.getCount());
      }
      // log section END

      PersonCity resultBean=null;

      if (cursor.moveToFirst()) {

        int index0=cursor.getColumnIndex("id");
        int index1=cursor.getColumnIndex("person_id");
        int index2=cursor.getColumnIndex("city_id");

        resultBean=new PersonCity();

        resultBean.id=cursor.getLong(index0);
        if (!cursor.isNull(index1)) { resultBean.personId=cursor.getLong(index1); }
        if (!cursor.isNull(index2)) { resultBean.cityId=cursor.getLong(index2); }

      }
      return resultBean;
    }
  }

  /**
   * <h2>Select SQL:</h2>
   *
   * <pre>SELECT id, person_id, city_id FROM person_city WHERE person_id=${personId}</pre>
   *
   * <h2>Projected columns:</h2>
   * <dl>
   * 	<dt>id</dt><dd>is associated to bean's property <strong>id</strong></dd>
   * 	<dt>person_id</dt><dd>is associated to bean's property <strong>personId</strong></dd>
   * 	<dt>city_id</dt><dd>is associated to bean's property <strong>cityId</strong></dd>
   * </dl>
   *
   * <h2>Query's parameters:</h2>
   * <dl>
   * 	<dt>${personId}</dt><dd>is binded to method's parameter <strong>personId</strong></dd>
   * </dl>
   *
   * @param personId
   * 	is binded to <code>${personId}</code>
   * @return collection of bean or empty collection.
   */
  @Override
  public List<PersonCity> selectByPersonId(long personId) {
    KriptonContentValues _contentValues=contentValues();
    // query SQL is statically defined
    String _sql=SELECT_BY_PERSON_ID_SQL6;
    // add where arguments
    _contentValues.addWhereArgs(String.valueOf(personId));
    String[] _sqlArgs=_contentValues.whereArgsAsArray();
    // log section BEGIN
    if (_context.isLogEnabled()) {
      // manage log
      Logger.info(_sql);

      // log for where parameters -- BEGIN
      int _whereParamCounter=0;
      for (String _whereParamItem: _contentValues.whereArgs()) {
        Logger.info("==> param%s: '%s'",(_whereParamCounter++), StringUtils.checkSize(_whereParamItem));
      }
      // log for where parameters -- END
    }
    // log section END
    try (Cursor cursor = database().rawQuery(_sql, _sqlArgs)) {
      // log section BEGIN
      if (_context.isLogEnabled()) {
        Logger.info("Rows found: %s",cursor.getCount());
      }
      // log section END

      ArrayList<PersonCity> resultList=new ArrayList<PersonCity>(cursor.getCount());
      PersonCity resultBean=null;

      if (cursor.moveToFirst()) {

        int index0=cursor.getColumnIndex("id");
        int index1=cursor.getColumnIndex("person_id");
        int index2=cursor.getColumnIndex("city_id");

        do
         {
          resultBean=new PersonCity();

          resultBean.id=cursor.getLong(index0);
          if (!cursor.isNull(index1)) { resultBean.personId=cursor.getLong(index1); }
          if (!cursor.isNull(index2)) { resultBean.cityId=cursor.getLong(index2); }

          resultList.add(resultBean);
        } while (cursor.moveToNext());
      }

      return resultList;
    }
  }

  /**
   * <h2>Select SQL:</h2>
   *
   * <pre>SELECT id, person_id, city_id FROM person_city WHERE city_id=${cityId}</pre>
   *
   * <h2>Projected columns:</h2>
   * <dl>
   * 	<dt>id</dt><dd>is associated to bean's property <strong>id</strong></dd>
   * 	<dt>person_id</dt><dd>is associated to bean's property <strong>personId</strong></dd>
   * 	<dt>city_id</dt><dd>is associated to bean's property <strong>cityId</strong></dd>
   * </dl>
   *
   * <h2>Query's parameters:</h2>
   * <dl>
   * 	<dt>${cityId}</dt><dd>is binded to method's parameter <strong>cityId</strong></dd>
   * </dl>
   *
   * @param cityId
   * 	is binded to <code>${cityId}</code>
   * @return collection of bean or empty collection.
   */
  @Override
  public List<PersonCity> selectByCityId(long cityId) {
    KriptonContentValues _contentValues=contentValues();
    // query SQL is statically defined
    String _sql=SELECT_BY_CITY_ID_SQL7;
    // add where arguments
    _contentValues.addWhereArgs(String.valueOf(cityId));
    String[] _sqlArgs=_contentValues.whereArgsAsArray();
    // log section BEGIN
    if (_context.isLogEnabled()) {
      // manage log
      Logger.info(_sql);

      // log for where parameters -- BEGIN
      int _whereParamCounter=0;
      for (String _whereParamItem: _contentValues.whereArgs()) {
        Logger.info("==> param%s: '%s'",(_whereParamCounter++), StringUtils.checkSize(_whereParamItem));
      }
      // log for where parameters -- END
    }
    // log section END
    try (Cursor cursor = database().rawQuery(_sql, _sqlArgs)) {
      // log section BEGIN
      if (_context.isLogEnabled()) {
        Logger.info("Rows found: %s",cursor.getCount());
      }
      // log section END

      ArrayList<PersonCity> resultList=new ArrayList<PersonCity>(cursor.getCount());
      PersonCity resultBean=null;

      if (cursor.moveToFirst()) {

        int index0=cursor.getColumnIndex("id");
        int index1=cursor.getColumnIndex("person_id");
        int index2=cursor.getColumnIndex("city_id");

        do
         {
          resultBean=new PersonCity();

          resultBean.id=cursor.getLong(index0);
          if (!cursor.isNull(index1)) { resultBean.personId=cursor.getLong(index1); }
          if (!cursor.isNull(index2)) { resultBean.cityId=cursor.getLong(index2); }

          resultList.add(resultBean);
        } while (cursor.moveToNext());
      }

      return resultList;
    }
  }

  /**
   * <h2>SQL delete</h2>
   * <pre>DELETE FROM person_city WHERE id=${id}</pre>
   *
   *
   * <h2>Where parameters:</h2>
   * <dl>
   * 	<dt>${id}</dt><dd>is mapped to method's parameter <strong>id</strong></dd>
   * </dl>
   *
   * @param id
   * 	is used as where parameter <strong>${id}</strong>
   *
   * @return number of deleted records
   */
  @Override
  public int deleteById(long id) {
    KriptonContentValues _contentValues=contentValuesForUpdate();
    _contentValues.addWhereArgs(String.valueOf(id));

    // generation CODE_001 -- BEGIN
    // generation CODE_001 -- END
    if (deleteByIdPreparedStatement0==null) {
      StringBuilder _sqlBuilder=sqlBuilder();

      // manage WHERE arguments -- BEGIN

      // manage WHERE statement
      String _sqlWhereStatement=" id=?";
      _sqlBuilder.append(_sqlWhereStatement);

      // manage WHERE arguments -- END

      // generate sql
      String _sql="DELETE FROM person_city WHERE id=?";
      deleteByIdPreparedStatement0 = KriptonDatabaseWrapper.compile(_context, _sql);
    }
    // log section BEGIN
    if (_context.isLogEnabled()) {

      // display log
      Logger.info("DELETE FROM person_city WHERE id=?");

      // log for where parameters -- BEGIN
      int _whereParamCounter=0;
      for (String _whereParamItem: _contentValues.whereArgs()) {
        Logger.info("==> param%s: '%s'",(_whereParamCounter++), StringUtils.checkSize(_whereParamItem));
      }
      // log for where parameters -- END
    }
    // log section END
    int result = KriptonDatabaseWrapper.updateDelete(_context, deleteByIdPreparedStatement0, _contentValues);
    return result;
  }

  /**
   * <h2>SQL delete</h2>
   * <pre>DELETE FROM person_city WHERE person_id=${personId}</pre>
   *
   *
   * <h2>Where parameters:</h2>
   * <dl>
   * 	<dt>${personId}</dt><dd>is mapped to method's parameter <strong>personId</strong></dd>
   * </dl>
   *
   * @param personId
   * 	is used as where parameter <strong>${personId}</strong>
   *
   * @return number of deleted records
   */
  @Override
  public int deleteByPersonId(long personId) {
    KriptonContentValues _contentValues=contentValuesForUpdate();
    _contentValues.addWhereArgs(String.valueOf(personId));

    // generation CODE_001 -- BEGIN
    // generation CODE_001 -- END
    if (deleteByPersonIdPreparedStatement1==null) {
      StringBuilder _sqlBuilder=sqlBuilder();

      // manage WHERE arguments -- BEGIN

      // manage WHERE statement
      String _sqlWhereStatement=" person_id=?";
      _sqlBuilder.append(_sqlWhereStatement);

      // manage WHERE arguments -- END

      // generate sql
      String _sql="DELETE FROM person_city WHERE person_id=?";
      deleteByPersonIdPreparedStatement1 = KriptonDatabaseWrapper.compile(_context, _sql);
    }
    // log section BEGIN
    if (_context.isLogEnabled()) {

      // display log
      Logger.info("DELETE FROM person_city WHERE person_id=?");

      // log for where parameters -- BEGIN
      int _whereParamCounter=0;
      for (String _whereParamItem: _contentValues.whereArgs()) {
        Logger.info("==> param%s: '%s'",(_whereParamCounter++), StringUtils.checkSize(_whereParamItem));
      }
      // log for where parameters -- END
    }
    // log section END
    int result = KriptonDatabaseWrapper.updateDelete(_context, deleteByPersonIdPreparedStatement1, _contentValues);
    return result;
  }

  /**
   * <h2>SQL delete</h2>
   * <pre>DELETE FROM person_city WHERE city_id=${cityId}</pre>
   *
   *
   * <h2>Where parameters:</h2>
   * <dl>
   * 	<dt>${cityId}</dt><dd>is mapped to method's parameter <strong>cityId</strong></dd>
   * </dl>
   *
   * @param cityId
   * 	is used as where parameter <strong>${cityId}</strong>
   *
   * @return number of deleted records
   */
  @Override
  public int deleteByCityId(long cityId) {
    KriptonContentValues _contentValues=contentValuesForUpdate();
    _contentValues.addWhereArgs(String.valueOf(cityId));

    // generation CODE_001 -- BEGIN
    // generation CODE_001 -- END
    if (deleteByCityIdPreparedStatement2==null) {
      StringBuilder _sqlBuilder=sqlBuilder();

      // manage WHERE arguments -- BEGIN

      // manage WHERE statement
      String _sqlWhereStatement=" city_id=?";
      _sqlBuilder.append(_sqlWhereStatement);

      // manage WHERE arguments -- END

      // generate sql
      String _sql="DELETE FROM person_city WHERE city_id=?";
      deleteByCityIdPreparedStatement2 = KriptonDatabaseWrapper.compile(_context, _sql);
    }
    // log section BEGIN
    if (_context.isLogEnabled()) {

      // display log
      Logger.info("DELETE FROM person_city WHERE city_id=?");

      // log for where parameters -- BEGIN
      int _whereParamCounter=0;
      for (String _whereParamItem: _contentValues.whereArgs()) {
        Logger.info("==> param%s: '%s'",(_whereParamCounter++), StringUtils.checkSize(_whereParamItem));
      }
      // log for where parameters -- END
    }
    // log section END
    int result = KriptonDatabaseWrapper.updateDelete(_context, deleteByCityIdPreparedStatement2, _contentValues);
    return result;
  }

  /**
   * <p>SQL insert:</p>
   * <pre>INSERT INTO person_city (person_id, city_id) VALUES (${bean.personId}, ${bean.cityId})</pre>
   *
   * <p><code>bean.id</code> is automatically updated because it is the primary key</p>
   *
   * <p><strong>Inserted columns:</strong></p>
   * <dl>
   * 	<dt>person_id</dt><dd>is mapped to <strong>${bean.personId}</strong></dd>
   * 	<dt>city_id</dt><dd>is mapped to <strong>${bean.cityId}</strong></dd>
   * </dl>
   *
   * @param bean
   * 	is mapped to parameter <strong>bean</strong>
   *
   * @return <strong>id</strong> of inserted record
   */
  @Override
  public int insert(PersonCity bean) {
    KriptonContentValues _contentValues=contentValuesForUpdate();
    _contentValues.put("person_id", bean.personId);
    _contentValues.put("city_id", bean.cityId);

    // log section BEGIN
    if (_context.isLogEnabled()) {
      // log for insert -- BEGIN 
      StringBuffer _columnNameBuffer=new StringBuffer();
      StringBuffer _columnValueBuffer=new StringBuffer();
      String _columnSeparator="";
      for (String columnName:_contentValues.keys()) {
        _columnNameBuffer.append(_columnSeparator+columnName);
        _columnValueBuffer.append(_columnSeparator+":"+columnName);
        _columnSeparator=", ";
      }
      Logger.info("INSERT INTO person_city (%s) VALUES (%s)", _columnNameBuffer.toString(), _columnValueBuffer.toString());

      // log for content values -- BEGIN
      Triple<String, Object, KriptonContentValues.ParamType> _contentValue;
      for (int i = 0; i < _contentValues.size(); i++) {
        _contentValue = _contentValues.get(i);
        if (_contentValue.value1==null) {
          Logger.info("==> :%s = <null>", _contentValue.value0);
        } else {
          Logger.info("==> :%s = '%s' (%s)", _contentValue.value0, StringUtils.checkSize(_contentValue.value1), _contentValue.value1.getClass().getCanonicalName());
        }
      }
      // log for content values -- END
      // log for insert -- END 

    }
    // log section END
    // insert operation
    if (insertPreparedStatement3==null) {
      // generate SQL for insert
      String _sql=String.format("INSERT INTO person_city (%s) VALUES (%s)", _contentValues.keyList(), _contentValues.keyValueList());
      insertPreparedStatement3 = KriptonDatabaseWrapper.compile(_context, _sql);
    }
    long result = KriptonDatabaseWrapper.insert(_context, insertPreparedStatement3, _contentValues);
    bean.id=result;

    return (int)result;
  }

  public static void clearCompiledStatements() {
    if (deleteByIdPreparedStatement0!=null) {
      deleteByIdPreparedStatement0.close();
      deleteByIdPreparedStatement0=null;
    }
    if (deleteByPersonIdPreparedStatement1!=null) {
      deleteByPersonIdPreparedStatement1.close();
      deleteByPersonIdPreparedStatement1=null;
    }
    if (deleteByCityIdPreparedStatement2!=null) {
      deleteByCityIdPreparedStatement2.close();
      deleteByCityIdPreparedStatement2=null;
    }
    if (insertPreparedStatement3!=null) {
      insertPreparedStatement3.close();
      insertPreparedStatement3=null;
    }
  }
}
