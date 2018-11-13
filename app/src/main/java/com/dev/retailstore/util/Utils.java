package com.dev.retailstore.util;

/**
 * Utility package for App.
 * @author seemasavadi
 */

import android.app.ProgressDialog;
import android.content.Context;

public class Utils {

	private static ProgressDialog progressDialog;

	public static void showProgressDialog(Context context) {
		if (null == progressDialog) {
			progressDialog = ProgressDialog.show(context, "Retail Store",
					"Loading...");
		} else
			progressDialog.show();
	}

	public static void hideProgressDialog() {

		if (null != progressDialog && progressDialog.isShowing()) {
			progressDialog.cancel();
			progressDialog = null;
		}
	}

}
