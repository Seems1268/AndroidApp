package com.dev.retailstore.adapters;

/**
 * This class implements custom ArrayAdapter for displaying checked out Items.
 * @author seemasavadi
 */

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.dev.retailstore.R;
import com.dev.retailstore.db.DatabaseManager;
import com.dev.retailstore.db.Product;

public class CheckoutItemsAdapter extends ArrayAdapter<Product>{

	private List<Product> items;
	private Context mContext;

	public CheckoutItemsAdapter(Activity activity, int resource,
			int textViewResourceId, List<Product> objects) {
		super(activity, resource, textViewResourceId, objects);

		items = objects;
		mContext = activity;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		final int item_position = position;

		if (null == convertView) {

			LayoutInflater layout_inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layout_inflater.inflate(R.layout.checkout_list_item,
					null);

			holder = new ViewHolder();
			holder.product_name = (TextView) convertView
					.findViewById(R.id.product_name);

			holder.dele_btn = (Button) convertView
					.findViewById(R.id.delete_btn);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();

		}

		if (null != items && !items.isEmpty()) {

			holder.product_name.setText(items.get(position).get_name());
		}

		holder.dele_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				DatabaseManager dbManager = DatabaseManager.getInstance(mContext);

				Product product = items.get(item_position);
				product.set_added_to_cart(false);
				
				int rowAffected = dbManager.updateProduct(product);
				
				items.remove(item_position);
				
				Log.d("UPDATE", "No of rows affected are: "+rowAffected);				
				notifyDataSetChanged();

			}
		});

		return convertView;
	}

	static class ViewHolder {
		TextView product_name;
		Button dele_btn;
	}

}
