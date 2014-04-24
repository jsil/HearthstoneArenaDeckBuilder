package com.example.hearthstonearenadeckbuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

class DownloadImageTask extends AsyncTask<String, Void, Bitmap[]> {
	protected Bitmap[] doInBackground(String... urls) {
		Bitmap[] cardImages = {MainActivity.loadImageFromNetwork(urls[0]),
				MainActivity.loadImageFromNetwork(urls[1]),
				MainActivity.loadImageFromNetwork(urls[2])};
		return cardImages;
	}
	protected void onPostExecute(Bitmap[] result) {
		MainActivity.mImageView1.setImageBitmap(result[0]);
		MainActivity.mImageView2.setImageBitmap(result[1]);
		MainActivity.mImageView3.setImageBitmap(result[2]);
	}
}

public class MainActivity extends Activity {

	static ImageView mImageView1;
	static ImageView mImageView2;
	static ImageView mImageView3;
	JSONObject obj;
	JSONArray array;
	JSONArray commonArray;
	JSONArray rareArray;
	JSONArray epicArray;
	JSONArray legendArray;
	LinearLayout pictureArea;
	String jsonString;
	public int cardId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//		pictureArea = (LinearLayout) findViewById(R.id.pictureArea);
		jsonString = loadJSONFromAsset();
		
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("all-cards.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
	public static Bitmap loadImageFromNetwork (String imgUrl) {
		Bitmap img = null;
		URL url;
		try {
		url = new URL(imgUrl);
		img = BitmapFactory.decodeStream(url.openStream());
		} catch (MalformedURLException e) {
		Log.e("JS", "URL is bad");
		e.printStackTrace();
		} catch (IOException e) {
		Log.e("JS", "Failed to decode Bitmap");
		e.printStackTrace();
		}
		return img;
		}
	

	public void onClickBtn(View v) throws JSONException
		{
		try {
			obj = new JSONObject(jsonString);
			array = (obj).getJSONArray("cards");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		//	LinearLayout.LayoutParams pictureLayout = new LinearLayout.LayoutParams(
		//		    LinearLayout.LayoutParams.WRAP_CONTENT,
		//			    LinearLayout.LayoutParams.WRAP_CONTENT);
		//			pictureArea.setOrientation(LinearLayout.HORIZONTAL);
			
				int[] cardIds = {R.id.card1,R.id.card2,R.id.card3};
				
				int randomCard1 = 0 + (int)(Math.random()*array.length()); 
				int randomCard2 = 0 + (int)(Math.random()*array.length());
				int randomCard3 = 0 + (int)(Math.random()*array.length());
				
				int[] cards = {randomCard1,randomCard2,randomCard3};
				
				JSONObject card1;
				JSONObject card2;
				JSONObject card3;
				String URL1;
				String URL2;
				String URL3;
				
//				for(int i=0;i<cardIds.length;i++) {
				mImageView1 = (ImageView) findViewById(cardIds[0]);
				mImageView2 = (ImageView) findViewById(cardIds[1]);
				mImageView3 = (ImageView) findViewById(cardIds[2]);
				card1 = array.getJSONObject(cards[0]);
				card2 = array.getJSONObject(cards[1]);
				card3 = array.getJSONObject(cards[2]);
				URL1 = card1.getString("image_url");
				URL2 = card2.getString("image_url");
				URL3 = card3.getString("image_url");
				new DownloadImageTask().execute(URL1,URL2,URL3);
//				}
				
		}
}