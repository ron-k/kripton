/*******************************************************************************
 * Copyright 2015, 2016 Francesco Benincasa (info@abubusoft.com).
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
package shared;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import shared.kripton45.Test45Suite;
import shared.kripton46.Test46Compile;
import shared.kripton47.Test47Suite;
import shared.kripton50.TestKripton50;
import sqlite.kripton62.Test62Compile;
import sqlite.kripton63.Test63Compile;

@RunWith(Suite.class)
// @formatter:off
@Suite.SuiteClasses({ 
	Test45Suite.class, 
	Test46Compile.class,
	Test47Suite.class, 
	TestKripton50.class, 
	Test62Compile.class, 
	Test63Compile.class 
	 })
// @formatter:on
public class SharedCompileTestSuite {

}
