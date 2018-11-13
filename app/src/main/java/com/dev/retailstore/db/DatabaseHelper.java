package com.dev.retailstore.db;

/**
 * This class creates Database for application and manages it.
 * @author seemasavadi
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static  String dbPath ;
	
	public static final int DB_VERSION = 1;
	
	public static final String DB_NAME = "retail_products.db";
	
	private static final String TABLE_PRODUCTS = "products";
	
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_CATEGORY = "category";
	private static final String KEY_IMAGEURI = "image_uri";
	private static final String KEY_PRICE = "price";
	private static final String KEY_ADDEDTOCART = "isAddedToCart";
	
	private Context mContext;
	private SQLiteDatabase myDataBase;


	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		
		mContext = context;
		dbPath = "/data/data/"+mContext.getApplicationContext().getPackageName()+"/databases/"+DB_NAME;
		
		Log.d("DBNAME", "DB Path: "+dbPath);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS );

		onCreate(db);
	}

	public void addProduct(Product product){
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, product.get_name());
		values.put(KEY_CATEGORY, product.get_category());
		values.put(KEY_IMAGEURI, product.get_image_uri());
		values.put(KEY_PRICE, product.get_price());
		values.put(KEY_ADDEDTOCART, product.is_added_to_cart());

		db.insert(TABLE_PRODUCTS, null, values);
		db.close();
		
	}
	
	public Product getProduct(int id){
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_PRODUCTS, new String[] { KEY_ID,
				KEY_NAME, KEY_CATEGORY, KEY_IMAGEURI, KEY_PRICE, KEY_ADDEDTOCART }, KEY_ID
				+ "=?", new String[] { String.valueOf(id) }, null, null, null,
				null);
		
		if(null != cursor)
			cursor.moveToFirst();
		
		Product product = new Product(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2), cursor.getString(3),
				cursor.getString(4), Boolean.getBoolean(cursor.getString(5)));
		
		return product;
	}
	
	public List<Product> getAllProducts(){
		
		List<Product> productsList = new ArrayList<Product>();
		
		String selectQuery = "SELECT * FROM " +TABLE_PRODUCTS;
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			
			do{
				Product product = new Product();
				product.set_id(Integer.parseInt(cursor.getString(0)));
				product.set_name(cursor.getString(1));
				product.set_category(cursor.getString(2));
				product.set_image_uri(cursor.getString(3));
				product.set_price(cursor.getString(4));
				product.set_added_to_cart(Boolean.getBoolean(cursor.getString(5)));
				
				productsList.add(product);
			}while(cursor.moveToNext());
		}
		
		return productsList;
	}
	
	
	
	public List<Product> getAllProducts(String key, String value){
		
		List<Product> productsList = new ArrayList<Product>();

		String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS +" WHERE "+key+"= ?";
		
		Log.d("SELECT CATEGORY", selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, new String[]{ value });

		if (cursor.moveToFirst()) {

			do {
				Product product = new Product();
				product.set_id(Integer.parseInt(cursor.getString(0)));
				product.set_name(cursor.getString(1));
				product.set_category(cursor.getString(2));
				product.set_image_uri(cursor.getString(3));
				product.set_price(cursor.getString(4));
				product.set_added_to_cart(Boolean.getBoolean(cursor
						.getString(5)));

				productsList.add(product);
			} while (cursor.moveToNext());
		}

		return productsList;
	}
	
	public int updateProduct(Product product){
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, product.get_name());
		values.put(KEY_CATEGORY, product.get_category());
		values.put(KEY_IMAGEURI, product.get_image_uri());
		values.put(KEY_PRICE, product.get_price());
		values.put(KEY_ADDEDTOCART, product.is_added_to_cart());
		
		return db.update(TABLE_PRODUCTS, values, KEY_ID + "=?",
				new String[] { String.valueOf(product.get_id()) });
	}
	
	public int getProductsCount(){
		
		String countQuery = "SELECT * FROM " + TABLE_PRODUCTS;
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		
		return cursor.getCount();
	}
	
	
	public void copyDataBase() throws IOException {
		
		myDataBase = getWritableDatabase();

		File dbFile = mContext.getDatabasePath(DatabaseHelper.DB_NAME);

		if (dbFile.exists()) {

			InputStream myInput = mContext.getAssets().open(DB_NAME);

			OutputStream myOutput = new FileOutputStream(dbPath);

			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}

			myOutput.flush();
			myOutput.close();
			myInput.close();
		}

	}
	
	public void openDataBase() throws SQLException {
		String myPath = dbPath + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY);

	}

	@Override
	public synchronized void close() {
		if (myDataBase != null)
			myDataBase.close();

		super.close();

	}

	public SQLiteDatabase getDb() {
		return myDataBase;
	}

}

