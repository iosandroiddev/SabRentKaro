package com.android.jsonclasses;

import java.io.File;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.examples.toolbox.MultipartRequestVideo;
import com.android.volley.examples.toolbox.MyVolley;
import com.android.volley.toolbox.JsonObjectRequest;

@SuppressWarnings("unused")
public class JSONRequestResponseVideo {

	public JSONRequestResponseVideo(Context cntx) {
		mContext = cntx;
	}

	private final Context mContext;
	private int reqCode;
	private IObjectParseListener listner;

	private boolean isFile = false;
	private String file_path = "", key = "";

	public void getResponse(String url, final int requestCode,
			IObjectParseListener mParseListener) {
		getResponse(url, requestCode, mParseListener, null);
	}

	public void getResponse(String url, final int requestCode,
			IObjectParseListener mParseListener, Bundle params) {
		this.listner = mParseListener;
		this.reqCode = requestCode;

		Response.Listener<JSONObject> sListener = new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				if (listner != null) {
					listner.SuccessResponse(response, reqCode);
				}
			}
		};

		Response.ErrorListener eListener = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if (listner != null) {
					listner.ErrorResponse(error, reqCode);
				}
			}
		};

		if (!isFile) {
			JsonObjectRequest jsObjRequest = new JsonObjectRequest(
					Request.Method.GET, url, null, sListener, eListener);
			MyVolley.getRequestQueue().add(jsObjRequest);
		} else {
			if (file_path != null) {
				File mFile = new File(file_path);
				MultipartRequestVideo multipartRequest = new MultipartRequestVideo(
						url, eListener, sListener, key, mFile, params);
				MyVolley.getRequestQueue().add(multipartRequest);
			} else {
				throw new NullPointerException("File path is null");
			}
		}
	}

	/**
	 * @return the isFile
	 */
	public boolean isFile() {
		return isFile;
	}

	/**
	 * @param isFile
	 *            the File to set
	 */
	public void setFile(String param, String path) {
		if (path != null && param != null) {
			key = param;
			file_path = path;
			this.isFile = true;
		}
	}

}
