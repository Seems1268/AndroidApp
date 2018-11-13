package com.dev.retailstore.db;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable{
	

	int _id;
	String _name;
	String _category;
	String _image_uri;
	String _price;
	boolean _added_to_cart;
	
	public Product(){
		
	}
	
	public Product(Parcel in){
		super(); 
        readFromParcel(in);
	}
	
	   public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
           public Product createFromParcel(Parcel in) {
               return new Product(in);
           }

           public Product[] newArray(int size) {

               return new Product[size];
           }

       };
	
	public Product(int id, String name, String category, String imageURI, String price, boolean isAddedToCart){
		
		_id = id;
		_name = name;
		_category = category;
		_image_uri = imageURI;
		_price = price;
		_added_to_cart = isAddedToCart;
	}
	
	public Product(String name, String category, String imageURI,
			String price, boolean isAddedToCart) {

		_name = name;
		_category = category;
		_image_uri = imageURI;
		_price = price;
		_added_to_cart = isAddedToCart;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String get_name() {
		return _name;
	}

	public void set_name(String _name) {
		this._name = _name;
	}

	public String get_category() {
		return _category;
	}

	public void set_category(String _category) {
		this._category = _category;
	}

	public String get_image_uri() {
		return _image_uri;
	}

	public void set_image_uri(String _image_uri) {
		this._image_uri = _image_uri;
	}

	public String get_price() {
		return _price;
	}

	public void set_price(String _price) {
		this._price = _price;
	}

	public boolean is_added_to_cart() {
		return _added_to_cart;
	}

	public void set_added_to_cart(boolean _added_to_cart) {
		this._added_to_cart = _added_to_cart;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
	}
	
	 public void readFromParcel(Parcel in) {

       }
	

}


