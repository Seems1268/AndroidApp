package com.dev.retailstore.fragments;

/**
 * This fragment implements ProductsListing.
 * @author seemasavadi
 */

import java.util.ArrayList;
import java.util.List;

import android.R;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dev.retailstore.db.Product;

public class ProductsListingFragment extends ListFragment {
	
	private List<String> mItems;
	private ArrayList<Product> mProducts;
	private FragmentManager fragManager = null;
	private FragmentTransaction fragTransaction = null;
	private ProductDetailsFragment detailFrag = null;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mProducts = getArguments().getParcelableArrayList("products");
		
		mItems = new ArrayList<String>();
		
		if (null != mProducts && !mProducts.isEmpty()) {

			for (Product product : mProducts) {

				mItems.add(product.get_name());

			}
		}
				
		setListAdapter(new ArrayAdapter<String>(getActivity(),R.layout.simple_list_item_1,mItems));
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		        
		fragManager = getFragmentManager();
		fragTransaction = fragManager.beginTransaction();

		detailFrag = ProductDetailsFragment.getNewInstance();
				
		detailFrag.setSelected_product(mProducts.get(position));
		
		detailFrag.show(fragTransaction, "Dialog");

	}

}
