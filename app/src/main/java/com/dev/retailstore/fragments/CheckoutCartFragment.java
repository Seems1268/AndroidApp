package com.dev.retailstore.fragments;

/**
 * This fragment implements checked out Cart.
 * @author seemasavadi
 */

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.dev.retailstore.R;
import com.dev.retailstore.adapters.CheckoutItemsAdapter;
import com.dev.retailstore.db.DatabaseManager;
import com.dev.retailstore.db.Product;

public class CheckoutCartFragment extends Fragment {

	private View view;
	private TextView product_total;
	private ListView listView;
	private CheckoutItemsAdapter adapter = null;
	private FragmentManager fragManager = null;
	private FragmentTransaction fragTransaction = null;
	private ProductDetailsFragment detailFrag = null;

	private ArrayList<Product> mProducts;
	private long totalAmt = 0;
	
	private DatabaseManager dbManager = null;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	public CheckoutCartFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.checkout_cart, container, false);

		initialiseView();

		return view;
	}

	private void initialiseView() {

		listView = (ListView) view.findViewById(R.id.products_list);

		product_total = (TextView) view
				.findViewById(R.id.product_grand_total_txt);

//		mProducts = getArguments()
//				.getParcelableArrayList("checkedout_products");
//		
//		for (Product product : mProducts) {
//			
//			totalAmt = totalAmt + Long.valueOf(product.get_price());
//		}
		
		getTotalItemsInCart();
		
		product_total.setText(String.valueOf(totalAmt));

		populateList();

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {

				fragManager = getFragmentManager();
				fragTransaction = fragManager.beginTransaction();

				detailFrag = ProductDetailsFragment.getNewInstance();
				
				detailFrag.setSelected_product(mProducts.get(position));

				detailFrag.show(fragTransaction, "Dialog");

			}
		});
	}

	private void populateList() {
		if (null != mProducts && !mProducts.isEmpty()) {
			if (null == adapter) {
				adapter = new CheckoutItemsAdapter(getActivity(), 0, 0,
						mProducts);
				listView.setAdapter(adapter);
			} else {
				listView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
		}
	}
	
	private void getTotalItemsInCart(){
		
		dbManager = DatabaseManager.getInstance(getActivity());
		
		mProducts = (ArrayList<Product>) dbManager
				.getAllProductsInCart();
		
		for (Product product : mProducts) {

			totalAmt = totalAmt + Long.valueOf(product.get_price());
		}

		product_total.setText(String.valueOf(totalAmt));
	}
	

}
