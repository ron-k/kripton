package sqlite.feature.javadoc.update.bean;

import com.abubusoft.kripton.android.annotation.BindContentProvider;
import com.abubusoft.kripton.android.annotation.BindDataSource;

@BindContentProvider(authority = "sqlite.feature.javadoc.bean")
@BindDataSource(daoSet={PersonDao.class}, fileName = "person.db")
public interface PersonDataSource {

}