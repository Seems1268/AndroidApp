package com.dev.retailstore.db;

/**
 * This class is an interface between the Database and its users.
 * @author seemasavadi
 */

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.content.Context;

public class DatabaseManager {
	
	private static DatabaseHelper dbHelper = null;
	private static DatabaseManager dbManager = null;
	private static Context mContext = null;
	private static final String KEY_CATEGORY = "category";
	private static final String KEY_ADDEDTOCART = "isAddedToCart";

	
	public static DatabaseManager getInstance(Context context){
		mContext = context;
		
		if(null == dbManager)
			dbManager = new DatabaseManager();
		
		
		return dbManager;
	}
	
	private DatabaseManager() {
		
		if (null == dbHelper)
			dbHelper = new DatabaseHelper(mContext);

		File dbFile = mContext.getDatabasePath(DatabaseHelper.DB_NAME);
		
		if (!dbFile.exists()) {

			try {
				dbHelper.copyDataBase();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<Product> getAllProductsList(){
		
		return dbHelper.getAllProducts();
		
	}
	
	public List<Product> getAllProdutsOfCategory(String category){
		
		return dbHelper.getAllProducts(KEY_CATEGORY,category);
	}
	
	public List<Product> getAllProductsInCart(){
		return dbHelper.getAllProducts(KEY_ADDEDTOCART,"1");
	}
	
	public int updateProduct(Product product){
		
		return dbHelper.updateProduct(product);
	}

}
