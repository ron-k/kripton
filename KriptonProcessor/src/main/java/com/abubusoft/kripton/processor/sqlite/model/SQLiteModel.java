/*******************************************************************************
 * Copyright 2015, 2016 Francesco Benincasa.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.abubusoft.kripton.processor.sqlite.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.abubusoft.kripton.processor.element.GeneratedTypeElement;

public class SQLiteModel {

	protected List<SQLiteDatabaseSchema> schemas = new ArrayList<SQLiteDatabaseSchema>();
	
	//public Set<GeneratedTypeElement> generatedEntities;

	//public Set<GeneratedTypeElement> generatedDaos;

	public void schemaAdd(SQLiteDatabaseSchema schema) {
		schemas.add(schema);
	}

	public List<SQLiteDatabaseSchema> getSchemas() {
		return schemas;
	}

	public void schemaClear() {
		schemas.clear();
	}

	public int schemaCount() {
		return schemas.size();
	}
}
