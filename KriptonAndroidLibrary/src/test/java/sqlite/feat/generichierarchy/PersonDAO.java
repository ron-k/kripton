/*******************************************************************************
 * Copyright 2015, 2017 Francesco Benincasa (info@abubusoft.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package sqlite.feat.generichierarchy;

import com.abubusoft.kripton.android.annotation.BindDao;
import com.abubusoft.kripton.android.annotation.BindSqlInsert;
import com.abubusoft.kripton.android.annotation.BindSqlSelect;
import com.abubusoft.kripton.annotation.BindTypeVariables;

import sqlite.feat.generichierarchy.BaseDAO;
import sqlite.feat.generichierarchy.Person;

@BindTypeVariables(value="E", typeParameters=Person.class)
@BindDao(Person.class)
public interface PersonDAO extends BaseDAO<Person> {
//

//	
//	@BindSqlInsert
//	public void insertThread2(Person bean);
//	
//	@BindSqlSelect
//	public Person selectThread1();
//	
//	@BindSqlSelect
//	public Person selectThread2();
}