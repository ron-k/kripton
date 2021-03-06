/*******************************************************************************
 * Copyright 2016-2019 Francesco Benincasa (info@abubusoft.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package sqlite.feature.typeadapter.kripton180;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import com.abubusoft.kripton.android.sqlite.TransactionResult;

import base.BaseAndroidTest;
import sqlite.feature.typeadapter.kripton180.bean.BindKripton180BeanDaoFactory;
import sqlite.feature.typeadapter.kripton180.bean.BindKripton180BeanDataSource;
import sqlite.feature.typeadapter.kripton180.bean.EmployeeBeanDaoImpl;

// TODO: Auto-generated Javadoc
/**
 * The Class TestKripton180Runtime.
 *
 * @author xcesco
 */
@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class TestKripton180Runtime extends BaseAndroidTest {

	/**
	 * Test run.
	 */
	@Test
	public void testRun() {
		final Employee bean = new Employee();
		bean.birthDate = new java.sql.Date((new java.util.Date()).getTime());
		bean.fieldBoolean = "true";
		bean.fieldByte = "42";
		bean.fieldByteArray = "42";
		bean.fieldCharacter = "a";
		bean.fieldDouble = "120";
		bean.fieldFloat = "120";
		bean.fieldInteger = "11";
		bean.fieldLong = "13";
		bean.fieldShort = "2";
		bean.fieldString = "a";
		bean.hireDate = new java.sql.Date((new java.util.Date()).getTime());

		BindKripton180BeanDataSource dataSource = BindKripton180BeanDataSource.getInstance();

		dataSource.execute(new BindKripton180BeanDataSource.Transaction() {

			@Override
			public TransactionResult onExecute(BindKripton180BeanDaoFactory daoFactory) {
				EmployeeBeanDaoImpl dao = daoFactory.getEmployeeBeanDao();

				dao.insert(bean);
				Assert.assertTrue(bean.id > 0);

				Employee readResult = dao.selectById(bean);
				Assert.assertTrue(readResult.fieldBoolean.equals(bean.fieldBoolean));
				Assert.assertTrue(readResult.birthDate.toString().equals(bean.birthDate.toString()));

				dao.update(bean);
				readResult = dao.selectById(bean);
				Assert.assertTrue(Double.valueOf(readResult.fieldDouble).equals(Double.valueOf(bean.fieldDouble)));
				Assert.assertTrue(readResult.fieldString.equals(bean.fieldString));

				long result = dao.deleteById(bean);
				Assert.assertTrue(result == 1);
				// ReflectionAssert.assertReflectionEquals("Bean is not valid"
				// (type.toString(), list, list2,
				// ReflectionComparatorMode.LENIENT_ORDER);

				return TransactionResult.ROLLBACK;
			}
		});

	}

	/**
	 * Test run JQL.
	 */
	@Test
	public void testRunJQL() {
		final Employee bean = new Employee();
		bean.birthDate = new java.sql.Date((new java.util.Date()).getTime());
		bean.fieldBoolean = "true";
		bean.fieldByte = "42";
		bean.fieldByteArray = "42";
		bean.fieldCharacter = "a";
		bean.fieldDouble = "120";
		bean.fieldFloat = "120";
		bean.fieldInteger = "11";
		bean.fieldLong = "13";
		bean.fieldShort = "2";
		bean.fieldString = "a";
		bean.hireDate = new java.sql.Date((new java.util.Date()).getTime());

		BindKripton180BeanDataSource dataSource = BindKripton180BeanDataSource.getInstance();

		dataSource.execute(new BindKripton180BeanDataSource.Transaction() {

			@Override
			public TransactionResult onExecute(BindKripton180BeanDaoFactory daoFactory) {
				EmployeeBeanDaoImpl dao = daoFactory.getEmployeeBeanDao();

				dao.insertJQL(bean);
				Assert.assertTrue(bean.id > 0);

				Employee readResult = dao.selectByIdJQL(bean);
				Assert.assertTrue(readResult.fieldBoolean.equals(bean.fieldBoolean));
				Assert.assertTrue(readResult.fieldLong.toString().equals(bean.fieldLong.toString()));

				dao.update(bean);
				readResult = dao.selectByIdJQL(bean);
				Assert.assertTrue(Double.valueOf(readResult.fieldDouble).equals(Double.valueOf(bean.fieldDouble)));
				Assert.assertTrue(readResult.fieldString.equals(bean.fieldString));

				long result = dao.deleteById(bean);
				Assert.assertTrue(result == 1);
				// ReflectionAssert.assertReflectionEquals("Bean is not valid"
				// (type.toString(), list, list2,
				// ReflectionComparatorMode.LENIENT_ORDER);

				return TransactionResult.ROLLBACK;
			}
		});

	}
}
