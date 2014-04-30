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
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

class Download3CardsTask extends AsyncTask<String, Void, Bitmap[]> {
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

class DownloadHeroTask extends AsyncTask<String, Void, Bitmap> {
	protected Bitmap doInBackground(String... urls) {
		Bitmap heroImage = MainActivity.loadImageFromNetwork(urls[0]);
		return heroImage;
	}
	protected void onPostExecute(Bitmap result) {
		MainActivity.heroPic.setImageBitmap(result);
	}
}

public class MainActivity extends Activity {

	static ImageView mImageView1;
	static ImageView mImageView2;
	static ImageView mImageView3;
	JSONObject cardObj;
	JSONArray cardArray;
	JSONArray commonArray;
	JSONArray rareArray;
	JSONArray epicArray;
	JSONArray legendArray;
	LinearLayout pictureArea;
	String jsonString;
	public int cardId;
	Spinner heroSpinner;
	static ImageView heroPic;
	String[] heroUrls;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		heroPic = (ImageView) findViewById(R.id.hero_pic);
		Resources res = getResources();
		heroUrls = res.getStringArray(R.array.hero_urls);
		Log.v("JS",heroUrls[0]);
		new DownloadHeroTask().execute(heroUrls[0]);

		heroSpinner = (Spinner) findViewById(R.id.hero_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.hero_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		heroSpinner.setAdapter(adapter);
		
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
		
		//	LinearLayout.LayoutParams pictureLayout = new LinearLayout.LayoutParams(
		//		    LinearLayout.LayoutParams.WRAP_CONTENT,
		//			    LinearLayout.LayoutParams.WRAP_CONTENT);
		//			pictureArea.setOrientation(LinearLayout.HORIZONTAL);
			
				JSONArray valueToDraw;
				int cardValue = (int)(Math.random()*100);
				if(cardValue <= 5)
					valueToDraw = legendArray;
				else if(cardValue > 5 && cardValue <= 20)
					valueToDraw = epicArray;
				else if(cardValue >20 && cardValue <= 55)
					valueToDraw = rareArray;
				else
					valueToDraw = commonArray;
				
				Log.v("JS",Integer.toString(cardValue));
		
				int[] cardIds = {R.id.card1,R.id.card2,R.id.card3};
				
				int randomCard1 = 0 + (int)(Math.random()*valueToDraw.length()); 
				int randomCard2 = 0 + (int)(Math.random()*valueToDraw.length());
				while(randomCard2 == randomCard1)
					randomCard2 = 0 + (int)(Math.random()*valueToDraw.length());
				int randomCard3 = 0 + (int)(Math.random()*valueToDraw.length());
				while(randomCard3 == randomCard1 || randomCard3 == randomCard2)
					randomCard3 = 0 + (int)(Math.random()*valueToDraw.length());
				
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
				card1 = valueToDraw.getJSONObject(cards[0]);
				card2 = valueToDraw.getJSONObject(cards[1]);
				card3 = valueToDraw.getJSONObject(cards[2]);
				URL1 = card1.getString("image_url");
				URL2 = card2.getString("image_url");
				URL3 = card3.getString("image_url");
				new Download3CardsTask().execute(URL1,URL2,URL3);
//				}
				
		}
	public void heroButtonClick(View v) {
		try {
			cardObj = new JSONObject(jsonString);
			cardArray = cardObj.getJSONArray("cards");
			commonArray = new JSONArray();
			rareArray = new JSONArray();
			epicArray = new JSONArray();
			legendArray = new JSONArray();
			String hero = heroSpinner.getSelectedItem().toString();
			int position = heroSpinner.getSelectedItemPosition();
			new DownloadHeroTask().execute(heroUrls[position]);
			
			
		int length = cardArray.length();
		for(int i=0;i<length;i++) {
			if(cardArray.getJSONObject(i).getString("hero").equals(hero) || cardArray.getJSONObject(i).getString("hero").equals("neutral")) {
				String quality = cardArray.getJSONObject(i).getString("quality");
				if(quality.equals("common") || quality.equals("free")) {
					commonArray.put(cardArray.getJSONObject(i));
				}
				else if(quality.equals("rare")) {
					rareArray.put(cardArray.getJSONObject(i));
				}
				else if(quality.equals("epic")) {
					epicArray.put(cardArray.getJSONObject(i));
				}
				else if(quality.equals("legendary")) {
					legendArray.put(cardArray.getJSONObject(i));
				}
			}
		}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}