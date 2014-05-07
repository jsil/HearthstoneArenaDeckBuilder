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
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.graphics.Typeface;

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

	static final String JSON_MESSAGE = "com.example.hearthstonearenadeckbuilder.JSON";
	static final String STRING_MESSAGE = "com.example.hearthstonearenadeckbuilder.STRING";
	static final String INT_MESSAGE = "com.example.hearthstonearenadeckbuilder.INT";
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
	static ImageView heroPic;
	String[] heroUrls;
	JSONArray deckArray;
	int deckNum;
	JSONObject card1;
	JSONObject card2;
	JSONObject card3;
	TextView cardCounter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		jsonString = loadJSONFromAsset();
		
		mImageView1 = (ImageView) findViewById(R.id.card1);
		mImageView2 = (ImageView) findViewById(R.id.card2);
		mImageView3 = (ImageView) findViewById(R.id.card3);
		
		cardCounter = (TextView) findViewById(R.id.deck_count);
		cardCounter.setText("0/30");
		
		heroPic = (ImageView) findViewById(R.id.hero_pic);
		
		Intent intent = getIntent();
		String heroName = intent.getStringExtra(STRING_MESSAGE);
		int heroNum = intent.getIntExtra(MainActivity.INT_MESSAGE,10);
		
		Resources res = getResources();
		heroUrls = res.getStringArray(R.array.hero_urls);
		
		new DownloadHeroTask().execute(heroUrls[heroNum]);
		
		//Set all fonts on page
		Button deckBtn = (Button) findViewById(R.id.deck_button);
	    Typeface face=Typeface.createFromAsset(getAssets(),
	                                          "fonts/Belwe.ttf");

	    deckBtn.setTypeface(face);
	    cardCounter.setTypeface(face);
	    
	    
	    //Set mana counter fonts
	    TextView mana0 = (TextView) findViewById(R.id.manaNum0);
	    TextView mana1 = (TextView) findViewById(R.id.manaNum1);
	    TextView mana2 = (TextView) findViewById(R.id.manaNum2);
	    TextView mana3 = (TextView) findViewById(R.id.manaNum3);
	    TextView mana4 = (TextView) findViewById(R.id.manaNum4);
	    TextView mana5 = (TextView) findViewById(R.id.manaNum5);
	    TextView mana6 = (TextView) findViewById(R.id.manaNum6);
	    TextView mana7 = (TextView) findViewById(R.id.manaNum7);
	    
	    mana0.setTypeface(face);
	    mana1.setTypeface(face);
	    mana2.setTypeface(face);
	    mana3.setTypeface(face);
	    mana4.setTypeface(face);
	    mana5.setTypeface(face);
	    mana6.setTypeface(face);
	    mana7.setTypeface(face);
		
		commonArray = new JSONArray();
		rareArray = new JSONArray();
		epicArray = new JSONArray();
		legendArray = new JSONArray();
		deckArray = new JSONArray();
		deckNum = 0;
		
		Log.v("JS",Integer.toString(heroNum));
		Log.v("JS",heroName);
		
		generatePool(heroName,heroNum);
	}
	
	@Override
	public void onBackPressed() {
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
	

	public void onClickBtn(View v) throws JSONException{
		generateCards();		
	}
	
	public void generatePool(String heroName, int heroNum) {
		try {
			cardObj = new JSONObject(jsonString);
			cardArray = cardObj.getJSONArray("cards");
			
			String hero = heroName;
			
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
			generateCards();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void cardLeftClick(View v) throws JSONException {
		boolean isNew = true;
		card1.put("quantity",1);
		for(int i=0;i<deckArray.length();i++) {
			if(deckArray.getJSONObject(i).get("name").equals(card1.get("name"))) {
				deckArray.getJSONObject(i).put("quantity", 1 + Integer.parseInt(deckArray.getJSONObject(i).get("quantity").toString()));	
				deckNum++;
				isNew = false;
			}
		}
		if(isNew) {
			deckArray.put(card1);
			deckNum++;
		}
		if(deckNum<30) {
			generateCards();
			String newText = "" + Integer.toString(deckNum) + "/30";
			cardCounter.setText(newText);
		}
		else
			loadCards();
	}
	
	public void cardCenterClick(View v) throws JSONException {
		boolean isNew = true;
		card2.put("quantity",1);
		for(int i=0;i<deckArray.length();i++) {
			if(deckArray.getJSONObject(i).get("name").equals(card2.get("name"))) {
				deckArray.getJSONObject(i).put("quantity", 1 + Integer.parseInt(deckArray.getJSONObject(i).get("quantity").toString()));	
				deckNum++;
				isNew = false;
			}
		}
		if(isNew) {
			deckArray.put(card1);
			deckNum++;
		}
		if(deckNum<30) {
			generateCards();
			String newText = "" + Integer.toString(deckNum) + "/30";
			cardCounter.setText(newText);
		}
		else
			loadCards();
	}
	
	public void cardRightClick(View v) throws JSONException {
		boolean isNew = true;
		card3.put("quantity",1);
		for(int i=0;i<deckArray.length();i++) {
			if(deckArray.getJSONObject(i).get("name").equals(card3.get("name"))) {
				deckArray.getJSONObject(i).put("quantity", 1 + Integer.parseInt(deckArray.getJSONObject(i).get("quantity").toString()));	
				deckNum++;
				isNew = false;
			}
		}
		if(isNew) {
			deckArray.put(card1);
			deckNum++;
		}
		if(deckNum<30) {
			generateCards();
			String newText = "" + Integer.toString(deckNum) + "/30";
			cardCounter.setText(newText);
		}
		else
			loadCards();
	}
	
	public void deckButtonClick(View v) {
//		Log.v("JS",deckArray.toString());
		loadCards();
	}
	
	
	private void loadCards() {
		Intent i = new Intent(this, Cards.class);
		
		i.putExtra(JSON_MESSAGE, deckArray.toString());
		startActivity(i);
	}
	
	public void generateCards() throws JSONException {
		JSONArray valueToDraw;
		int cardValue = 0 + (int)(Math.random()*100);
		if(cardValue <= 1)
			valueToDraw = legendArray;
		else if(cardValue > 1 && cardValue <= 4) 
			valueToDraw = epicArray;
		else if(cardValue > 4 && cardValue <= 20)
			valueToDraw = rareArray;
		else
			valueToDraw = commonArray;

		
		Log.v("JS",Integer.toString(cardValue));
		
		int randomCard1 = 0 + (int)(Math.random()*valueToDraw.length()); 
		int randomCard2 = 0 + (int)(Math.random()*valueToDraw.length());
		while(randomCard2 == randomCard1)
			randomCard2 = 0 + (int)(Math.random()*valueToDraw.length());
		int randomCard3 = 0 + (int)(Math.random()*valueToDraw.length());
		while(randomCard3 == randomCard1 || randomCard3 == randomCard2)
			randomCard3 = 0 + (int)(Math.random()*valueToDraw.length());
		
		int[] cards = {randomCard1,randomCard2,randomCard3};
		
		String URL1;
		String URL2;
		String URL3;
		
		
		card1 = valueToDraw.getJSONObject(cards[0]);
		card2 = valueToDraw.getJSONObject(cards[1]);
		card3 = valueToDraw.getJSONObject(cards[2]);
		URL1 = card1.getString("image_url");
		URL2 = card2.getString("image_url");
		URL3 = card3.getString("image_url");
		new Download3CardsTask().execute(URL1,URL2,URL3);
	}
}