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
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.graphics.Typeface;

class Download3CardsTask extends AsyncTask<String, Void, Bitmap[]> {
	protected Bitmap[] doInBackground(String... urls) {
		Bitmap[] cardImages = {MainActivity.loadImageFromNetwork(urls[0]),
				MainActivity.loadImageFromNetwork(urls[1]),
				MainActivity.loadImageFromNetwork(urls[2])};
		return cardImages;
	}
	protected void onPreExecute() {
		MainActivity.loadingBox.setVisibility(View.VISIBLE);
		MainActivity.loadSpin.setVisibility(View.VISIBLE);
		MainActivity.mImageView1.setVisibility(View.INVISIBLE);
		MainActivity.mImageView2.setVisibility(View.INVISIBLE);
		MainActivity.mImageView3.setVisibility(View.INVISIBLE);
	}
	protected void onPostExecute(Bitmap[] result) {
		MainActivity.loadingBox.setVisibility(View.GONE);
		MainActivity.loadSpin.setVisibility(View.GONE);
		MarginLayoutParams params = (MarginLayoutParams) MainActivity.loadSpin.getLayoutParams();
		params.topMargin = 0;
		   MainActivity.loadSpin.setLayoutParams(params);
		MainActivity.mImageView1.setVisibility(View.VISIBLE);
		MainActivity.mImageView2.setVisibility(View.VISIBLE);
		MainActivity.mImageView3.setVisibility(View.VISIBLE);
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
	
	ProgressBar[] mProgress;
	int[] manaCounts;
	int highestMana;
	
	static RelativeLayout loadingBox;
	static ProgressBar loadSpin;
	
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
		
		loadingBox = (RelativeLayout) findViewById(R.id.loading_box);
		loadSpin = (ProgressBar) findViewById(R.id.progress_spin);
		
		Intent intent = getIntent();
		String heroName = intent.getStringExtra(STRING_MESSAGE);
		int heroNum = intent.getIntExtra(MainActivity.INT_MESSAGE,10);
		
		Resources res = getResources();
		heroUrls = res.getStringArray(R.array.hero_urls);
		
		
		
		new DownloadHeroTask().execute(heroUrls[heroNum]);
		
		//Set all fonts on page
		Button heroReturnBtn = (Button) findViewById(R.id.hero_return_button);
		Button deckBtn = (Button) findViewById(R.id.deck_button);
		TextView chooseText = (TextView) findViewById(R.id.choose_a_card);
	    Typeface face=Typeface.createFromAsset(getAssets(),
	                                          "fonts/Belwe.ttf");

	    heroReturnBtn.setTypeface(face);
	    deckBtn.setTypeface(face);
	    chooseText.setTypeface(face);
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
	    
	    mProgress = new ProgressBar[8];
	    mProgress[0] = (ProgressBar) findViewById(R.id.mana0);
	    mProgress[1] = (ProgressBar) findViewById(R.id.mana1);
	    mProgress[2] = (ProgressBar) findViewById(R.id.mana2);
	    mProgress[3] = (ProgressBar) findViewById(R.id.mana3);
	    mProgress[4] = (ProgressBar) findViewById(R.id.mana4);
	    mProgress[5] = (ProgressBar) findViewById(R.id.mana5);
	    mProgress[6] = (ProgressBar) findViewById(R.id.mana6);
	    mProgress[7] = (ProgressBar) findViewById(R.id.mana7);
	    
	    highestMana = 0;
	    
	    manaCounts = new int[8];
	    for(int i=0;i<manaCounts.length;i++) {
	    	manaCounts[i] = 0;
	    }
		
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
		 moveTaskToBack(true);
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
			updateMana(card1.getInt("mana"));
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
			deckArray.put(card2);
			deckNum++;
		}
		if(deckNum<30) {
			updateMana(card2.getInt("mana"));
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
			deckArray.put(card3);
			deckNum++;
		}
		if(deckNum<30) {
			updateMana(card3.getInt("mana"));
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
	
	public void heroReturnClick(View v) {
		Intent i = new Intent(this, HeroPick.class);
		startActivity(i);
	}
	
	public void updateMana(int manaVal) {
		int count;
		if(manaVal >= 7) {
			manaCounts[7]++;
			count = manaCounts[7];
		}
		else {
			manaCounts[manaVal]++;
			count = manaCounts[manaVal];
		}
		
		if(count>highestMana)
			highestMana = count;
		
		Log.v("JS"," ");
		Log.v("JS",Integer.toString(highestMana));
		Log.v("JS",Integer.toString(count));
		
		Float valToSet = (float) 0;
		
		int equalToHigh = 0;
		for(int i=0;i<8;i++) {
			if(manaCounts[i] == highestMana)
				equalToHigh++;
		}
		for(int i=0;i<8;i++) {
			if(highestMana<=10) {
				valToSet = (float) (manaCounts[i]) * 10;
			}
			else {
				if(manaCounts[i] != highestMana)
					valToSet = (float) (manaCounts[i])*(100/highestMana);
				else
					valToSet = (float) 100/equalToHigh;
			}
			mProgress[i].setProgress(valToSet.intValue());
		}
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
		else if(cardValue > 1 && cardValue <= 5) 
			valueToDraw = epicArray;
		else if(cardValue > 5 && cardValue <= 20)
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