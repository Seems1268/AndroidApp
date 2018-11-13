package com.dev.retailstore.main;

/**
 * Main Activity of the application which initializes DB and app.
 * @author seemasavadi
 */

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dev.retailstore.R;
import com.dev.retailstore.db.DatabaseManager;
import com.dev.retailstore.db.Product;
import com.dev.retailstore.fragments.CheckoutCartFragment;
import com.dev.retailstore.fragments.ProductsListingFragment;
import com.dev.retailstore.interfaces.DBSetUpListener;
import com.dev.retailstore.interfaces.NavigationDrawerCallback;
import com.dev.retailstore.util.AppConstants;
import com.dev.retailstore.util.Utils;

public class MainActivity extends Activity implements NavigationDrawerCallback, DBSetUpListener{
	
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    
    private String[] mProdutsCategories;
    private CharSequence mTitle,mDrawerTitle;
    private ArrayList<Product> productsList = new ArrayList<Product>();
    
    Fragment currentFragment;
    DatabaseManager dbManager;
    private NavigationDrawerCallback navigationDrawerCallbacks;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_main);
    	
        navigationDrawerCallbacks = (NavigationDrawerCallback) this;
        
        mProdutsCategories = getResources().getStringArray(R.array.navigation_drawer_array);
 
        Utils.showProgressDialog(this);

		initUI();
		initNavigationAdapter();
		initListeners();
		   
        DBSetUpTask dbSetupTask = new DBSetUpTask(this, this);
        
        dbSetupTask.execute(null,null,null);
 
    }

	@Override
	public void onDBInitilized() {
		
		dbManager = DatabaseManager.getInstance(this);

		selectItem(0);

	}
    
	private void initUI() {

		mTitle = mDrawerTitle = getTitle();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this,
		mDrawerLayout, 
		R.drawable.ic_drawer, 
		R.string.drawer_open, 
		R.string.drawer_close 
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}
		};
        mDrawerLayout.setDrawerListener(mDrawerToggle);

	}
	
	private void initListeners(){
		
		mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                selectItem(position);
            }
        });
	}
	
	private void initNavigationAdapter(){
		  mDrawerList.setAdapter(new ArrayAdapter<String>(this,
	                R.layout.drawer_list_item, mProdutsCategories));
		  
		  mDrawerList.setItemChecked(0, true);
	}
	
	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
        getActionBar().setTitle(mTitle);
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		if (null != mDrawerToggle)
			mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		if (null != mDrawerToggle)
			mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private void selectItem(int position) {

		if (mDrawerList != null) {
			mDrawerList.setItemChecked(position, true);
		}
		if (mDrawerLayout != null) {
			mDrawerLayout.closeDrawer(mDrawerList);
		}
		
		if (navigationDrawerCallbacks != null) {
			navigationDrawerCallbacks.onNavigationItemSelected(position);
		}

	}

	@Override
	public void onNavigationItemSelected(int position) {
		ListFragment fragment = null;

		fragment = new ProductsListingFragment();

		switch (position) {
		case AppConstants.ID_HOME_FRAGMENT: {

			productsList = (ArrayList<Product>) dbManager.getAllProductsList();
			Bundle bundle = new Bundle();
			bundle.putParcelableArrayList("products", productsList);
			fragment.setArguments(bundle);
		}
			break;
		case AppConstants.ID_ELECTRONICS_PRODCUTS_FRAGMENT: {

			productsList = (ArrayList<Product>) dbManager
					.getAllProdutsOfCategory("Electronics");
			Bundle bundle = new Bundle();
			bundle.putParcelableArrayList("products", productsList);
			fragment.setArguments(bundle);

		}
			break;
		case AppConstants.ID_FURNITURE_PRODUCTS_FRAGMENT: {

			productsList = (ArrayList<Product>) dbManager
					.getAllProdutsOfCategory("Furniture");
			Bundle bundle = new Bundle();
			bundle.putParcelableArrayList("products", productsList);
			fragment.setArguments(bundle);
		}
			break;
		}
		setCurrentFragment(fragment);
	}
	
	void setCurrentFragment(Fragment fragment) {
		currentFragment = fragment;
		getFragmentManager().popBackStack(null,
				FragmentManager.POP_BACK_STACK_INCLUSIVE);
		FragmentTransaction transaction = buildTransaction();
		transaction.replace(R.id.container, fragment, null);
		transaction.commitAllowingStateLoss();
	}

	private FragmentTransaction buildTransaction() {
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		return transaction;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_cart).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		
		 if (mDrawerToggle.onOptionsItemSelected(item)) {
	            return true;
	        }
		 
		 
        switch(item.getItemId()) {
        case R.id.action_cart:{
			CheckoutCartFragment cartFrag = new CheckoutCartFragment();

//			productsList = (ArrayList<Product>) dbManager
//					.getAllProductsInCart();
//			Bundle bundle = new Bundle();
//			bundle.putParcelableArrayList("checkedout_products", productsList);
//			cartFrag.setArguments(bundle);
			setCurrentFragment(cartFrag);
        }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
        
    }
	
	class DBSetUpTask extends AsyncTask<Void, Void, Void>{
		
		Context mContext;
		DBSetUpListener mListener; 
		
		public  DBSetUpTask(Context context, DBSetUpListener listener) {
			mContext = context;
			mListener = listener;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Utils.showProgressDialog(mContext);
		}

		@Override
		protected Void doInBackground(Void... params) {
			
			DatabaseManager dbManager = DatabaseManager.getInstance(mContext);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			Utils.hideProgressDialog();
			mListener.onDBInitilized();
		}
		
	}
	
	@Override
	public void onBackPressed() {
		
		if (currentFragment.getClass().getSimpleName()
				.equalsIgnoreCase(CheckoutCartFragment.class.getSimpleName())) {
			
			selectItem(0);
		}else{
			super.onBackPressed();
		}
	}

}
