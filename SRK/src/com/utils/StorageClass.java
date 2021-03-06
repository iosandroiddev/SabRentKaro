package com.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.provider.Settings;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.models.CityModel;

public class StorageClass {

	public static final String PREF_NAME = "user_data";
	public static String EMAIL = "email";
	public static String PASSWORD = "password";

	public static String REMEMBERME = "is_remember";
	public static String USER_ID = "user_id";
	public static String USER_TYPE = "user_type";
	public static String BRAND_NAME = "brand_name";
	public static String AUTOLOGIND = "auto_login";

	protected static final String UTF8 = "utf-8";
	private static final char[] SEKRIT = { 11, 75, 81, 90, 39, 10, 57, 62, 17,
			114, 15, 19, 75, 15, 112, 64 };

	private static StorageClass instance;
	private SharedPreferences sh;
	private Editor edit;
	private boolean isViewed = false;
	private Context mContext;

	private StorageClass(Context mContext) {
		this.mContext = mContext;
		sh = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
		edit = sh.edit();
	}

	public static synchronized StorageClass getInstance(Context mContext) {
		if (instance == null) {
			instance = new StorageClass(mContext);
		}
		return instance;
	}

	public void setUserName(String membertype) {
		edit.putString("username", membertype).commit();
	}

	public String getUserName() {
		return sh.getString("username", "");
	}

	public void setBrandName(String membertype) {
		edit.putString("brand_name", membertype).commit();
	}

	public String getBrandName() {
		return sh.getString("brand_name", "");
	}

	public void setUserType(String membertype) {
		edit.putString("user_type", membertype).commit();
	}

	public String getUserType() {
		return sh.getString("user_type", "");
	}

	public void setUserSignedIn(String membertype) {
		edit.putString("user_signed", membertype).commit();
	}

	public String getUserSignedIn() {
		return sh.getString("user_signed", "no");
	}

	public void setAutoLogin(boolean membertype) {
		edit.putBoolean("auto_login", membertype).commit();
	}

	public void setAccessToken(String AuthToken) {
		edit.putString("Auth_Token", AuthToken).commit();
	}

	public String getAccessToken() {
		return sh.getString("Auth_Token", "");
	}

	public boolean getAutoLogin() {
		return sh.getBoolean("auto_login", false);
	}

	public int getUserId() {
		return sh.getInt("userid", -1);
	}

	public void setUserId(int userid) {
		edit.putInt("userid", userid).commit();
	}

	public void clear() {
		edit.clear().commit();
	}

	public void setRemeber(boolean isflag) {
		edit.putBoolean("remember", isflag).commit();
	}

	public boolean isRemember() {
		return sh.getBoolean("remember", false);
	}

	public String getUserEmail() {
		return sh.getString("useremail", "");
	}

	public void setUserEmail(String useremail) {
		edit.putString("useremail", useremail).commit();
	}

	public String getPassword() {
		return sh.getString("password", "");
	}

	public void setPassword(String password) {
		edit.putString("password", password).commit();
	}

	public boolean isViewed() {
		return isViewed;
	}

	public void setViewed(boolean isViewed) {
		this.isViewed = isViewed;
	}

	public String getFirstName() {
		return sh.getString("first_name", "");
	}

	public void setFirstName(String first_name) {
		edit.putString("first_name", first_name).commit();
	}

	public String getLastName() {
		return sh.getString("last_name", "");
	}

	public void setLastName(String last_name) {
		edit.putString("last_name", last_name).commit();
	}

	public String getStatus() {
		return sh.getString("status", "");
	}

	public void setStatus(String status) {
		edit.putString("status", status).commit();
	}

	public String getTotalCredits() {
		return sh.getString("total_credits", "");
	}

	public void setTotalCredits(String total_credits) {
		edit.putString("total_credits", total_credits).commit();
	}

	public int getScreenIndex() {
		return sh.getInt("screenIndex", -1);
	}

	public void setScreenIndex(int index) {
		edit.putInt("screenIndex", index).commit();
	}

	public void setDeviceToken(String device_token) {
		edit.putString("device_token", device_token).commit();
	}

	public String getDeviceToken() {
		return sh.getString("device_token", "");
	}

	public void setPayPerJobPrice(String mPrice) {
		edit.putString("pay_per_job_price", mPrice).commit();
	}

	public String getPayPerJobPrice() {
		return sh.getString("pay_per_job_price", "");
	}

	public void setAddShadowPrice(String mPrice) {
		edit.putString("add_shadow_price", mPrice).commit();
	}

	public String getAddShadowPrice() {
		return sh.getString("add_shadow_price", "");
	}

	public void setAddPathPrice(String mPrice) {
		edit.putString("add_path_price", mPrice).commit();
	}

	public String getAddPathPrice() {
		return sh.getString("add_path_price", "");
	}

	public void setShowPopup(boolean showPop) {
		edit.putBoolean("show_login_pop", showPop).commit();
	}

	public boolean isShowPopup() {
		return sh.getBoolean("show_login_pop", false);
	}

	public void setIsCamera(boolean showPop) {
		edit.putBoolean("is_camera", showPop).commit();
	}

	public boolean isCamera() {
		return sh.getBoolean("is_camera", false);
	}

	public void setCityList(ArrayList<CityModel> mCity) {
		Gson gson = new Gson();
		String json = gson.toJson(mCity);
		edit.putString("city_list", json).commit();
	}

	public ArrayList<CityModel> getCityList() {
		ArrayList<CityModel> mCityArray = new ArrayList<CityModel>();
		Gson gson = new Gson();
		String json = sh.getString("city_list", "");
		if (json.isEmpty()) {
			mCityArray = new ArrayList<CityModel>();
		} else {
			Type type = new TypeToken<List<CityModel>>() {
			}.getType();
			mCityArray = gson.fromJson(json, type);
		}
		return mCityArray;

	}

	public String getCity() {
		return sh.getString("user_selected_city", "");
	}

	public void setCity(String cityName) {
		edit.putString("user_selected_city", cityName).commit();
	}

	public String getCityValue() {
		return sh.getString("user_selected_city_value", "");
	}

	public void setCityValue(String cityName) {
		edit.putString("user_selected_city_value", cityName).commit();
	}

	public String getUserCity() {
		return sh.getString("user_city", "");
	}

	public void setUserCity(String cityName) {
		edit.putString("user_city", cityName).commit();
	}

	public String getUserState() {
		return sh.getString("user_state", "");
	}

	public void setUserState(String userState) {
		edit.putString("user_state", userState).commit();
	}

	public String getUserCountry() {
		return sh.getString("user_country", "");
	}

	public void setUserCountry(String userState) {
		edit.putString("user_country", userState).commit();
	}

	public String getPinCode() {
		return sh.getString("pin_code", "");
	}

	public void setPinCode(String userState) {
		edit.putString("pin_code", userState).commit();
	}

	public String getMobileNumber() {
		return sh.getString("mobile_no", "");
	}

	public void setMobileNumber(String userState) {
		edit.putString("mobile_no", userState).commit();
	}

	public String getAddress() {
		return sh.getString("address", "");
	}

	public void setAddress(String userState) {
		edit.putString("address", userState).commit();
	}

	public String getAuthHeader() {
		return sh.getString("auth_header", "");
	}

	public void setAuthHeader(String userState) {
		edit.putString("auth_header", userState).commit();
	}

	public String getServiceTitle() {
		return sh.getString("service_title", "");
	}

	public void setServiceTitle(String title) {
		edit.putString("service_title", title).commit();
	}

	public String getServiceValue() {
		return sh.getString("service_value", "");
	}

	public void setServiceValue(String value) {
		edit.putString("service_value", value).commit();
	}

	public String getJSONArrayCertificates() {
		String value = sh.getString("certificate_json_arry", "");
		return value != null && value.length() != 0 ? decrypt(value) : value;
	}

	public void setJSONArrayCertificates(JSONArray mArray) {
		edit.putString("certificate_json_arry", encrypt(mArray.toString()))
				.commit();
	}

	protected String encrypt(String value) {

		try {
			final byte[] bytes = value != null ? value.getBytes(UTF8)
					: new byte[0];
			SecretKeyFactory keyFactory = SecretKeyFactory
					.getInstance("PBEWithMD5AndDES");
			SecretKey key = keyFactory.generateSecret(new PBEKeySpec(SEKRIT));
			Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
			pbeCipher.init(
					Cipher.ENCRYPT_MODE,
					key,
					new PBEParameterSpec(Settings.Secure.getString(
							mContext.getContentResolver(),
							Settings.System.ANDROID_ID).getBytes(UTF8), 20));
			return new String(Base64.encode(pbeCipher.doFinal(bytes),
					Base64.NO_WRAP), UTF8);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	protected String decrypt(String value) {
		try {
			final byte[] bytes = value != null ? Base64.decode(value,
					Base64.DEFAULT) : new byte[0];
			SecretKeyFactory keyFactory = SecretKeyFactory
					.getInstance("PBEWithMD5AndDES");
			SecretKey key = keyFactory.generateSecret(new PBEKeySpec(SEKRIT));
			Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
			pbeCipher.init(
					Cipher.DECRYPT_MODE,
					key,
					new PBEParameterSpec(Settings.Secure.getString(
							mContext.getContentResolver(),
							Settings.System.ANDROID_ID).getBytes(UTF8), 20));
			return new String(pbeCipher.doFinal(bytes), UTF8);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
