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
package sqlite.feature.dynamic.err1;

import java.util.Date;
import java.util.List;

import com.abubusoft.kripton.android.annotation.BindDao;
import com.abubusoft.kripton.android.annotation.BindSqlInsert;
import com.abubusoft.kripton.android.annotation.BindSqlParam;
import com.abubusoft.kripton.android.annotation.BindSqlSelect;

import sqlite.feature.dynamic.Person;

import com.abubusoft.kripton.android.annotation.BindSqlDynamicWhere;

// TODO: Auto-generated Javadoc
/**
 * The Interface Err1DAO.
 */
@BindDao(Person.class)
public interface Err1DAO {
	
	/**
	 * Insert one.
	 *
	 * @param name the name
	 * @param surname the surname
	 * @param birthCity the birth city
	 * @param birthDay the birth day
	 */
	@BindSqlInsert
	void insertOne(String name, String surname, String birthCity, @BindSqlDynamicWhere Date birthDay);
	
	/**
	 * Select one.
	 *
	 * @param name the name
	 * @param nameValue the name value
	 * @return the list
	 */
	@BindSqlSelect(where="typeName like ${nameTemp} || '%' ")
	List<Person> selectOne(@BindSqlDynamicWhere String name, @BindSqlParam("nameTemp") String nameValue);
}