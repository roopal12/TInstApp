package com.example.sir.tinstapp;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;


public class AllMediaFiles extends Activity {
	//**private GridView gvAllImages;
	ImageView viewmoreimages;
	RecyclerView recyclerViewforimage;
	private GridLayoutManager lLayout;
	RecImageViewAdapter recImageViewAdapter;
	private HashMap<String, String> userInfo;
	private ArrayList<String>imageThumbList = new ArrayList<String>();
	private Context context;
	private int WHAT_FINALIZE = 0;
	private static int WHAT_ERROR = 1;
	private ProgressDialog pd;
	public static final String TAG_DATA = "data";
	public static final String TAG_IMAGES = "images";
	public static final String TAG_THUMBNAIL = "thumbnail";
	public static final String TAG_URL = "url";

	private String accessToken;

	private Handler handler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			if (pd != null && pd.isShowing())
				pd.dismiss();
			if (msg.what == WHAT_FINALIZE) {
				//**setImageGridAdapter();
				setImageViewAdapter();
			} else {
				Toast.makeText(context, "Check your network.",
						Toast.LENGTH_SHORT).show();
			}
			return false;
		}
	});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_media_list_files);
		//**gvAllImages = (GridView) findViewById(R.id.gvAllImages);
		viewmoreimages=(ImageView) findViewById(R.id.viewmoreimages);
		recyclerViewforimage=findViewById(R.id.allimagerecyclerview);
		userInfo = (HashMap<String, String>)getIntent().getSerializableExtra("userInfo");
		accessToken=getIntent().getStringExtra("access_token");
		Log.e("AllMediaFile ","User info "+userInfo.get(InstagramApp.TAG_USERNAME));
		context = AllMediaFiles.this;
		getAllMediaImages();
        viewmoreimages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://instagram.com/_.26rs/");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/_.26rs/")));
                }
            }
        });
	}
	private void setImageViewAdapter(){
		GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
		gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // set Horizontal Orientation
		recyclerViewforimage.setLayoutManager(gridLayoutManager);
		recImageViewAdapter = new RecImageViewAdapter(this, imageThumbList);
		recyclerViewforimage.setAdapter(recImageViewAdapter);
	}

//**	private void setImageGridAdapter() {
//	//	gvAllImages.setAdapter(new MyGridListAdapter(context,imageThumbList));
//	}

	private void getAllMediaImages() {
		pd = ProgressDialog.show(context, "", "Loading images...");
		new Thread(new Runnable() {

			@Override
			public void run() {
				int what = WHAT_FINALIZE;
				try {
					// URL url = new URL(mTokenUrl + "&code=" + code);
					JSONParser jsonParser = new JSONParser();
					JSONObject jsonObject = jsonParser
							.getJSONFromUrlByGet("https://api.instagram.com/v1/users/"
									+ userInfo.get(InstagramApp.TAG_ID)
									+ "/media/recent/?access_token="+accessToken);

//                                    client_id="
//                                    								+ AppCongif.CLIENT_ID
//                                    									+ "&count="
//                                    									+ userInfo.get(InstagramApp.TAG_COUNTS));

					Log.e("JSONObj",""+jsonObject.toString());

					JSONArray data = jsonObject.getJSONArray(TAG_DATA);

					Log.e("JSONArray",""+data.toString());
					for (int data_i = 0; data_i < data.length(); data_i++) {
						JSONObject data_obj = data.getJSONObject(data_i);

						JSONObject images_obj = data_obj
								.getJSONObject(TAG_IMAGES);

						JSONObject thumbnail_obj = images_obj
								.getJSONObject(TAG_THUMBNAIL);

						// String str_height =
						// thumbnail_obj.getString(TAG_HEIGHT);
						//
						// String str_width =
						// thumbnail_obj.getString(TAG_WIDTH);

						String str_url = thumbnail_obj.getString(TAG_URL);
						imageThumbList.add(str_url);
					}

					System.out.println("jsonObject::" + jsonObject);

				} catch (Exception exception) {
					exception.printStackTrace();
					what = WHAT_ERROR;
				}
				// pd.dismiss();
				handler.sendEmptyMessage(what);
			}
		}).start();
	}
}
