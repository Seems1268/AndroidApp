package com.dev.retailstore.fragments;

/**
 * This fragment implements Products Details Dialog.
 * @author seemasavadi
 */

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.dev.retailstore.R;
import com.dev.retailstore.db.DatabaseManager;
import com.dev.retailstore.db.Product;

public class ProductDetailsFragment extends DialogFragment implements OnClickListener{
	
	private static ProductDetailsFragment productDetailsFragment = null;
	private View view;
	private TextView product_name, product_price;
	private Button addToCartBtn;
	
	private Product selected_product = null;
	private DatabaseManager dbManager = null;
	
	public static ProductDetailsFragment getNewInstance(){
		if(null == productDetailsFragment)
			productDetailsFragment = new ProductDetailsFragment();
		
		return productDetailsFragment;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
				
		view = inflater.inflate(R.layout.product_details_view, container, false);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		
		initialiseView();
		
		return view;
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
				
		super.onCreate(savedInstanceState);

		setRetainInstance(true);
		
		dbManager = DatabaseManager.getInstance(getActivity());
		
	}
	
	private void initialiseView() {

		product_name = (TextView) view.findViewById(R.id.product_name);
		product_price = (TextView) view.findViewById(R.id.product_price);

		addToCartBtn = (Button) view.findViewById(R.id.add_to_cart_btn);

		addToCartBtn.setOnClickListener(this);
		
		int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
		int height = getResources().getDimensionPixelSize(R.dimen.popup_height);        
		getDialog().getWindow().setLayout(width, height);

		
		if(selected_product != null){
			
			product_name.setText(selected_product.get_name());
			product_price.setText(selected_product.get_price());
		}
	}
			
	
	@Override
	public int show(FragmentTransaction transaction, String tag) {
		return showDialog(transaction, tag, false);
	}
	
	private int showDialog(FragmentTransaction transaction, String tag, boolean allowStateLoss){
		transaction.remove(this);
		transaction.add(this,null);
				
		int  mBackStackId = allowStateLoss ? transaction.commitAllowingStateLoss() : transaction.commit();
        return mBackStackId;
	}
	
	 @Override
	 public void onDestroyView() {
	     if (getDialog() != null && getRetainInstance())
	         getDialog().setDismissMessage(null);
	     super.onDestroyView();
	 }


	@Override
	public void onClick(View v) {

		if(v.getId() == R.id.add_to_cart_btn)
			addProductToCart();
	}
	
	private void addProductToCart(){
		
		selected_product.set_added_to_cart(true);
		
		int rowAffected = dbManager.updateProduct(selected_product);
		
		Log.d("UPDATE", "No of rows affected are: "+rowAffected);
		
		this.dismiss();
	}


	public void setSelected_product(Product selected_product) {
		this.selected_product = selected_product;
	}

}
