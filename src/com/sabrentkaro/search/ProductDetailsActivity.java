package com.sabrentkaro.search;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sabrentkaro.BaseActivity;
import com.sabrentkaro.InternalApp;
import com.sabrentkaro.R;
import com.sabrentkaro.login.LoginActivity;
import com.squareup.picasso.Picasso;
import com.utils.ApiUtils;
import com.utils.StaticUtils;
import com.utils.StorageClass;

public class ProductDetailsActivity extends BaseActivity {

	private ImageView mImageProduct, mImageRating;
	private TextView mtxtProductName, mtxtBrand, mtxtType, mtxtModel,
			mtxtCategory, mtxtLocation, mtxtDailyCost, mtxtSecurityDeposit,
			mtxtYearOfPurchase, mtxtMonthOfPurchase, mtxtCapacity,
			mtxtQuantity;
	private EditText mEditQuantity;
	private TextView mbtnRent;
	private String selectedProductAdId;

	private String mType, mModel, mBrand, mCapacity, mQuantity, mCategory,
			mProductCategory, mYearOfPurchase, mMonthOfPurchase,
			mPricePerMonth, locationValue, productConditionValue;

	private String mImageUrl;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContentLayout(R.layout.activity_product_details);
		getDetails();
		loadLayoutReferences();
		initProductDetailsApi();
	}

	private void getDetails() {
		if (getIntent() != null && getIntent().getExtras() != null) {
			Bundle mBundle = getIntent().getExtras();
			selectedProductAdId = mBundle.getString("selectedProductAdId");
		}
	}

	private void initProductDetailsApi() {
		showProgressLayout();

		JSONObject params = new JSONObject();

		JsonObjectRequest mObjReq = new JsonObjectRequest(
				ApiUtils.LOADADDETAILS, params, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						hideProgressLayout();
						responseForProductDetailsApi(response);
					}

				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						hideProgressLayout();
					}

				}) {

			@Override
			public byte[] getBody() {
				super.getBody();
				return selectedProductAdId.getBytes();
			}

		};

		RequestQueue mQueue = ((InternalApp) getApplication()).getQueue();
		mQueue.add(mObjReq);

	}

	private void loadLayoutReferences() {
		mtxtProductName = (TextView) findViewById(R.id.txtProductName);
		mtxtBrand = (TextView) findViewById(R.id.txtBrand);
		mtxtType = (TextView) findViewById(R.id.txtType);
		mtxtModel = (TextView) findViewById(R.id.txtModel);
		mtxtCategory = (TextView) findViewById(R.id.txtCategoryName);
		mtxtLocation = (TextView) findViewById(R.id.txtLocationName);
		mtxtDailyCost = (TextView) findViewById(R.id.txtDailyCost);
		mtxtSecurityDeposit = (TextView) findViewById(R.id.txtSecurityDeposit);
		mtxtYearOfPurchase = (TextView) findViewById(R.id.txtYearOfPurchase);
		mtxtMonthOfPurchase = (TextView) findViewById(R.id.txtMonthOfPurchase);
		mtxtCapacity = (TextView) findViewById(R.id.txtCapacity);
		mImageRating = (ImageView) findViewById(R.id.imgProductRating);
		mImageProduct = (ImageView) findViewById(R.id.itemProduct);
		mEditQuantity = (EditText) findViewById(R.id.editTextQuantity);
		mtxtQuantity = (TextView) findViewById(R.id.txtQuantity);
		mbtnRent = (TextView) findViewById(R.id.btnRentProduct);
		mbtnRent.setOnClickListener(this);
		StaticUtils.setEditTextHintFont(mEditQuantity, this);
	}

	private void responseForProductDetailsApi(JSONObject response) {
		if (response != null) {
			JSONArray productsArray = response.optJSONArray("Products");
			if (productsArray != null) {
				for (int i = 0; i < productsArray.length(); i++) {
					JSONObject mObj = productsArray.optJSONObject(i);
					if (mObj != null) {
						String mitemStr = mObj.optString("ItemDetails");
						JSONArray mItemDetailsArray = null;
						try {
							mItemDetailsArray = new JSONArray(mitemStr);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (mItemDetailsArray != null) {
							for (int j = 0; j < mItemDetailsArray.length(); j++) {
								JSONObject mItemObj = mItemDetailsArray
										.optJSONObject(j);
								if (mItemObj != null) {
									if (mItemObj.optString("Title")
											.equalsIgnoreCase("Brand")) {
										mBrand = mItemObj.optString("Value");
									} else if (mItemObj.optString("Title")
											.equalsIgnoreCase("Model")) {
										mModel = mItemObj.optString("Value");
									} else if (mItemObj.optString("Title")
											.equalsIgnoreCase("Type")) {
										mType = mItemObj.optString("Value");
									} else if (mItemObj.optString("Title")
											.equalsIgnoreCase("Storage Volume")) {
										mCapacity = mItemObj.optString("Value");
									}
								}
							}
						}
						mQuantity = mObj.optString("Quantity");
						mMonthOfPurchase = mObj.optString("MonthOfPurchase");
						mYearOfPurchase = mObj.optString("YearOfPurchase");
						JSONObject mCategoryObj = mObj
								.optJSONObject("Category");
						if (mCategoryObj != null) {
							mCategory = mCategoryObj.optString("Title");
						}

						JSONObject mProductCategoryObj = mObj
								.optJSONObject("ProductCategory");
						if (mProductCategoryObj != null) {
							mProductCategory = mProductCategoryObj
									.optString("Title");
						}

						productConditionValue = mObj.optJSONObject(
								"ProductCondition").optString("Code");

						JSONArray mMediaArray = mObj.optJSONArray("ItemMedia");
						if (mMediaArray != null) {
							for (int k = 0; k < mMediaArray.length(); k++) {
								JSONObject mMediaObj = mMediaArray
										.optJSONObject(k);
								if (mMediaObj != null) {
									mImageUrl = mMediaObj.optString("Filepath")
											.toString();
								}
							}
						}

						JSONArray mPricingArray = mObj.optJSONArray("Pricing");
						if (mPricingArray != null) {
							for (int l = 0; l < mPricingArray.length(); l++) {
								JSONObject mPriceObj = mPricingArray
										.optJSONObject(l);
								if (mPriceObj != null) {
									Double mPrice = mPriceObj
											.optDouble("Price");
									if (mPrice > 0) {
										mPricePerMonth = String.valueOf(mPrice);
									}
								}
							}
						}

						JSONArray adSettingsArray = response
								.optJSONArray("AdSettings");
						if (adSettingsArray != null) {
							for (int m = 0; m < adSettingsArray.length(); m++) {
								JSONObject mAdSettingsObj = adSettingsArray
										.optJSONObject(m);
								if (mAdSettingsObj != null) {
									if (mAdSettingsObj
											.optJSONObject(
													"ProductCategorySpecification")
											.optString("Code").toString()
											.equalsIgnoreCase("LOCATION")) {
										locationValue = mAdSettingsObj
												.optString("Value");
									}
								}
							}
						}

					}
				}

			}
		}
		setData();
	}

	private void setData() {
		mtxtProductName.setText(mProductCategory);
		mtxtCategory.setText(mCategory);
		mtxtYearOfPurchase.setText(mYearOfPurchase);
		mtxtMonthOfPurchase.setText(mMonthOfPurchase);
		mtxtQuantity.setText(mQuantity);
		mtxtBrand.setText(mBrand);
		mtxtModel.setText(mModel);
		mtxtType.setText(mType);
		mtxtCapacity.setText(mCapacity);
		mtxtLocation.setText(locationValue);
		mtxtSecurityDeposit.setText("0");

		if (TextUtils.isEmpty(productConditionValue)
				|| productConditionValue.equalsIgnoreCase("0")) {
			mImageRating.setImageResource(R.drawable.zerorating);
		} else {
			if (productConditionValue.equalsIgnoreCase("1")) {
				mImageRating.setImageResource(R.drawable.onerating);
			} else if (productConditionValue.equalsIgnoreCase("2")) {
				mImageRating.setImageResource(R.drawable.tworating);
			} else if (productConditionValue.equalsIgnoreCase("3")) {
				mImageRating.setImageResource(R.drawable.threerating);
			} else if (productConditionValue.equalsIgnoreCase("4")) {
				mImageRating.setImageResource(R.drawable.fourrating);
			} else {
				mImageRating.setImageResource(R.drawable.fiverating);
			}
		}

		Picasso.with(this).load(mImageUrl)
				.placeholder(R.drawable.default_loading)
				.error(R.drawable.default_loading).into(mImageProduct);

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.btnRentProduct) {
			btnRentProductClicked();
		}
	}

	private void btnRentProductClicked() {
		String mQuanty = mEditQuantity.getText().toString();
		if (TextUtils.isEmpty(mQuanty)) {
			showToast("Please enter a value for quantity");
		} else {
			if (Integer.parseInt(mQuanty) > 0) {
				if (Integer.parseInt(mQuanty) > Integer.parseInt(mQuantity)) {
					showToast("Please enter a value for quantity as per the available quantity");
				} else {
					if (TextUtils.isEmpty(StorageClass.getInstance(this)
							.getUserName())) {
						startLoginActivity();
					} else {
						startSelectDatesActivity();
					}
				}
			} else {
				showToast("Please enter a value for quantity greater than 0 to proceed.");
			}

		}
	}

	private void startSelectDatesActivity() {

	}

	private void startLoginActivity() {
		Intent mIntent = new Intent(this, LoginActivity.class);
		startActivity(mIntent);
	}

}