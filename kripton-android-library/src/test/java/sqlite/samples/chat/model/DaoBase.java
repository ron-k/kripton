package sqlite.samples.chat.model;

import com.abubusoft.kripton.android.annotation.BindSqlInsert;

public interface DaoBase<E> {
    @BindSqlInsert
    void insert(E bean);
}
