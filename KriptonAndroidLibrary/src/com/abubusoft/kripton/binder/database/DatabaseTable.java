package com.abubusoft.kripton.binder.database;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import com.abubusoft.kripton.binder.schema.MappingSchema;

/**
 * Database table
 * 
 * @author xcesco
 *
 */
public class DatabaseTable {
	
	public Class<?> clazz;

	public ArrayList<DatabaseColumn> columns = new ArrayList<>();

	public LinkedHashMap<String, DatabaseColumn> field2column = new LinkedHashMap<>();

	public LinkedHashMap<String, Insert> inserts = new LinkedHashMap<>();

	public String name;

	public LinkedHashMap<String, Query> queries = new LinkedHashMap<>();

	public MappingSchema schema;

	public LinkedHashMap<String, Update> updates = new LinkedHashMap<>();

	public DatabaseColumn primaryKey;

	public LinkedHashMap<String, Delete> deletes = new LinkedHashMap<>();
	
	//protected boolean parsed=false;
	
	public Query lastQuery;
	
	public Insert lastInsert;
	
	public Update lastUpdate;
	
	public Delete lastDelete;

	public LinkedHashMap<DatabaseColumn, DatabaseColumn> foreignKeys=new LinkedHashMap<DatabaseColumn, DatabaseColumn>();
	
}
