/*************************************************************************************
 * Product: SFAndroid (Sales Force Mobile)                       		             *
 * This program is free software; you can redistribute it and/or modify it    		 *
 * under the terms version 2 of the GNU General Public License as published   		 *
 * by the Free Software Foundation. This program is distributed in the hope   		 *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied 		 *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           		 *
 * See the GNU General Public License for more details.                       		 *
 * You should have received a copy of the GNU General Public License along    		 *
 * with this program; if not, write to the Free Software Foundation, Inc.,    		 *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     		 *
 * For the text or an alternative of this public license, you may reach us    		 *
 * Copyright (C) 2012-2014 E.R.P. Consultores y Asociados, S.A. All Rights Reserved. *
 * Contributor(s): Yamel Senih www.erpconsultoresyasociados.com				  		 *
 *************************************************************************************/
package org.spinsuite.base;


import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;

import org.spinsuite.util.Env;
import org.spinsuite.util.KeyNamePair;
import org.spinsuite.util.LogM;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

/**
 * 
 * @author Yamel Senih
 *
 */
public class DB extends SQLiteOpenHelper {

	/**
	 * 
	 * *** Constructor ***
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 03/02/2014, 21:43:37
	 * @param ctx
	 * @param name
	 * @param factory
	 * @param version
	 * @param sqlCreate
	 */
	public DB(Context ctx, String name, CursorFactory factory,
			int version, String sqlCreate) {
		super(ctx, 	name, factory, version);
		this.sqlCreate = sqlCreate;
		this.ctx = ctx;
	}
	
	/**
	 * 
	 * *** Constructor ***
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 03/02/2014, 21:43:29
	 * @param ctx
	 */
	public DB(Context ctx){
		super(ctx, Env.getDB_PathName(ctx), null, DB_VERSION);
		this.ctx = ctx;
	}
	
	private String 				sqlCreate;
	private String 				sqlUpdate;
	private SQLiteDatabase 		db;
	private SQLiteStatement 	stm;
	public static final int 	READ_ONLY = 0;
	public static final int 	READ_WRITE = 1;
	public static final String 	DB_NAME = "SFAndroid";
	public static final String 	DB_DIRECTORY = "data";
	public static final int 	DB_VERSION = 1;
	private Context 			ctx;
	
	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase bd) {
		if(sqlCreate != null
				&& sqlCreate.length() > 0)
			bd.execSQL(sqlCreate);
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		if(sqlUpdate != null
				&& sqlUpdate.length() > 0)
		db.execSQL(sqlUpdate);
	}
	
	/**
	 * Open database in mode read or read write
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 03/02/2014, 21:43:49
	 * @param type
	 * @return
	 * @return SQLiteDatabase
	 */
	public SQLiteDatabase openDB(int type){
		if(type == READ_ONLY){
			db = getReadableDatabase();
		}else if(type == READ_WRITE){
			db = getWritableDatabase();
		}
		return db;
	}
	
	/**
	 * Get database
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 03/02/2014, 21:44:15
	 * @return
	 * @return SQLiteDatabase
	 */
	public SQLiteDatabase getBd() {
		return db;
	}

	/**
	 * Close DB
	 * @author Yamel Senih 03/12/2012, 01:47:20
	 * @param rs
	 * @return void
	 */
	public void closeDB(Cursor rs){
		if(rs != null && !rs.isClosed())
			rs.close();
		db.close();
		LogM.log(ctx, getClass(), Level.INFO, "Closed");
	}
	
	/**
	 * Verifi if is open database
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 03/02/2014, 21:44:37
	 * @return
	 * @return boolean
	 */
	public boolean isOpen(){
		boolean ok = false;
		if(db != null){
			ok = db.isOpen();
		}
		return ok;
	}
	
	/**
	 * End transaction
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 03/02/2014, 21:44:54
	 * @return void
	 */
	public void endTransaction(){
		db.endTransaction();
	}
	
	/**
	 * Begin transaction
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 03/02/2014, 21:45:07
	 * @return void
	 */
	public void beginTransaction(){
		db.beginTransaction();
	}
	
	/**
	 * Set Successful transaction
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 03/02/2014, 21:45:34
	 * @return void
	 */
	public void setTransactionSuccessful(){
		db.setTransactionSuccessful();
	}
	
	/**
	 * Execute SQL
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 03/02/2014, 21:46:02
	 * @param sql
	 * @return void
	 */
	public void executeSQL(String sql){
		LogM.log(ctx, getClass(), Level.FINE, "SQL=" + sql);
		db.execSQL(sql);
	}
	
	/**
	 * Execute SQL with parameters
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 03/02/2014, 21:46:22
	 * @param sql
	 * @param param
	 * @return void
	 */
	public void executeSQL(String sql, Object [] param){
		LogM.log(ctx, getClass(), Level.FINE, "SQL=" + sql);
		db.execSQL(sql, param);
	} 
	
	/**
	 * Insert on table
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 03/02/2014, 21:46:44
	 * @param table
	 * @param columnaNull
	 * @param values
	 * @return void
	 */
	public void insertSQL(String table, String columnaNull, ContentValues values){
		db.insert(table, columnaNull, values);
	}
	
	/**
	 * Update table
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 03/02/2014, 21:47:03
	 * @param table
	 * @param values
	 * @param where
	 * @param argmWhere
	 * @return void
	 */
	public void updateSQL(String table, ContentValues values, String where, String [] argmWhere){
		db.update(table, values, where, argmWhere);
	}
	
	/**
	 * Delete over table
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 03/02/2014, 21:47:14
	 * @param table
	 * @param where
	 * @param argmWhere
	 * @return void
	 */
	public void deleteSQL(String table, String where, String [] argmWhere){
		db.delete(table, where, argmWhere);
	}
	
	/**
	 * Get SQL with parameters
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 03/02/2014, 21:47:26
	 * @param sql
	 * @param values
	 * @return
	 * @return Cursor
	 */
	public Cursor querySQL(String sql, String [] values){
		LogM.log(ctx, getClass(), Level.FINE, "SQL=" + sql);
		return db.rawQuery(sql, values);
	}
	
	/**
	 * Get SQL wit all values
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 03/02/2014, 21:48:10
	 * @param table
	 * @param col
	 * @param where
	 * @param argsWhere
	 * @param group
	 * @param having
	 * @param orden
	 * @param limit
	 * @return
	 * @return Cursor
	 */
	public Cursor querySQL(String table, String [] col, 
			String where, String [] argsWhere, String group, 
			String having, String orden, String limit){
		return db.query(table, col, where, argsWhere, group, having, orden, limit);
	}
	
	/**
	 * Compile statement
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 03/02/2014, 21:48:23
	 * @param sql
	 * @return
	 * @return SQLiteStatement
	 */
	public SQLiteStatement compileSQL(String sql){
		stm = db.compileStatement(sql);
		return stm;
	}
	
	/**
	 * Get Statement
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 03/02/2014, 21:48:35
	 * @return
	 * @return SQLiteStatement
	 */
	public SQLiteStatement getStatement(){
		return stm;
	}

	/**
	 * Complete Path folder
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 03/02/2014, 21:48:45
	 * @return
	 * @return String
	 */
	public static String getDB_Path(){
		return File.separator + 
				Env.APP_DIRECTORY + 
				File.separator + 
				DB_DIRECTORY;
	}
	
	/**
	 * Get DB Path Name
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 03/02/2014, 21:49:05
	 * @return
	 * @return String
	 */
	public static String getDB_PathName(){
		return File.separator + 
				Env.APP_DIRECTORY + 
				File.separator + 
				DB_DIRECTORY + 
				File.separator + 
				DB_NAME;
	}
	
	/**
	 * Load a Connection
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 24/02/2014, 09:15:36
	 * @param conn
	 * @param type
	 * @return
	 */
	public static void loadConnection(DB conn, int type) {
		if(conn != null
				&& !conn.isOpen()){
			conn.openDB(type);
			if(type == READ_WRITE)
				conn.beginTransaction();
		}
    }
	
	/**
	 * Close a Connection
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 24/02/2014, 09:17:46
	 * @param conn
	 * @return void
	 */
	public static void closeConnection(DB conn){
		if(conn != null 
				&& conn.isOpen()){
			if(conn.getBd().inTransaction())
				conn.endTransaction();
			conn.close();
		}
    }
	
	/**
	 * Get Value from SQL
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 24/02/2014, 09:21:49
	 * @param ctx
	 * @param sql
	 * @param params
	 * @return
	 * @return String
	 */
	public static String getSQLValueStringEx(Context ctx, String sql, String... params){
		DB conn = new DB(ctx);
		loadConnection(conn, READ_ONLY);
		Cursor rs = null;
		rs = conn.querySQL(sql, params);
		String retValue = null;
		if(rs.moveToFirst()){
			retValue = rs.getString(0);
		}
		closeConnection(conn);
		//	Return
		return retValue;
	}
	
	/**
	 * Get Key Name Pairs
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 31/03/2014, 11:14:56
	 * @param ctx
	 * @param sql
	 * @param optional
	 * @param params
	 * @return
	 * @return KeyNamePair[]
	 */
	public static KeyNamePair[] getKeyNamePairsEx(Context ctx, String sql, boolean optional, String... params){
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
        //	If is Optional
		if (optional){
            list.add (new KeyNamePair(-1, ""));
        }
		//	
		DB conn = new DB(ctx);
		loadConnection(conn, READ_ONLY);
		Cursor rs = null;
		rs = conn.querySQL(sql, params);
		//	Add to List
		if(rs.moveToFirst()){
			do{
				list.add(new KeyNamePair(rs.getInt(0), rs.getString(1)));
			}while(rs.moveToNext());
		}
		closeConnection(conn);
		KeyNamePair[] retValue = new KeyNamePair[list.size()];
        list.toArray(retValue);
		//	Return
		return retValue;
	}
	
	/**
	 * Get Key Name Pairs
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 31/03/2014, 11:15:56
	 * @param ctx
	 * @param sql
	 * @param params
	 * @return
	 * @return KeyNamePair[]
	 */
	public static KeyNamePair[] getKeyNamePairs(Context ctx, String sql, String... params){
		KeyNamePair[] retValue = null;
		try{
			retValue = getKeyNamePairsEx(ctx, sql, false, params);
		} catch(Exception e){
			LogM.log(ctx, "DB", Level.SEVERE, "SQLError", e);
		}
		return retValue;
	}
	
	/**
	 * Get Value from SQL as int
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 25/03/2014, 14:24:00
	 * @param ctx
	 * @param sql
	 * @param params
	 * @return
	 * @return int
	 */
	public static int getSQLValueEx(Context ctx, String sql, String... params){
		DB conn = new DB(ctx);
		loadConnection(conn, READ_ONLY);
		Cursor rs = null;
		rs = conn.querySQL(sql, params);
		int retValue = -1;
		if(rs.moveToFirst()){
			retValue = rs.getInt(0);
		}
		closeConnection(conn);
		//	Return
		return retValue;
	}
	
	/**
	 * Get Value SQL as int
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 25/03/2014, 14:25:42
	 * @param ctx
	 * @param sql
	 * @return
	 * @return int
	 */
	public static int getSQLValueEx(Context ctx, String sql){
		return getSQLValueEx(ctx, sql, (String[])null);
	}
	
	/**
	 * Get Value without Exception
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 25/03/2014, 14:41:53
	 * @param ctx
	 * @param sql
	 * @return
	 * @return int
	 */
	public static int getSQLValue(Context ctx, String sql){
		int retValue = -1;
		try{
			retValue = getSQLValueEx(ctx, sql, (String[])null);
		} catch(Exception e){
			LogM.log(ctx, "DB", Level.SEVERE, "SQLError", e);
		}
		return retValue;
	}
	
	/**
	 * Get Value from SQL without parameter
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 24/02/2014, 09:25:04
	 * @param ctx
	 * @param sql
	 * @return
	 * @return String
	 */
	public static String getSQLValueStringEx(Context ctx, String sql){
		return getSQLValueStringEx(ctx, sql, (String[])null);
	}
	
	/**
	 * Get Value from sql without Exception
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 24/02/2014, 09:43:18
	 * @param ctx
	 * @param sql
	 * @return
	 * @return String
	 */
	public static String getSQLValueString(Context ctx, String sql){
		String retValue = null;
		try{
			retValue = getSQLValueStringEx(ctx, sql, (String[])null);
		} catch(Exception e){
			LogM.log(ctx, "DB", Level.SEVERE, "SQLError", e);
		}
		return retValue;
	}
	
	/**
	 * Get Value from sql without Exception, with parameters
	 * @author <a href="mailto:yamelsenih@gmail.com">Yamel Senih</a> 24/02/2014, 09:44:22
	 * @param ctx
	 * @param sql
	 * @param params
	 * @return
	 * @return String
	 */
	public static String getSQLValueString(Context ctx, String sql, String... params){
		String retValue = null;
		try{
			retValue = getSQLValueStringEx(ctx, sql, params);
		} catch(Exception e){
			LogM.log(ctx, "DB", Level.SEVERE, "SQLError", e);
		}
		return retValue;
	}
}
