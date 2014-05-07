package com.example.hearthstonearenadeckbuilder;





import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

class Download3HeroesTask extends AsyncTask<String, Void, Bitmap[]> {
	protected Bitmap[] doInBackground(String... urls) {
		Bitmap[] cardImages = {MainActivity.loadImageFromNetwork(urls[0]),
				MainActivity.loadImageFromNetwork(urls[1]),
				MainActivity.loadImageFromNetwork(urls[2])};
		return cardImages;
	}
	protected void onPostExecute(Bitmap[] result) {
		HeroPick.mHero1.setImageBitmap(result[0]);
		HeroPick.mHero2.setImageBitmap(result[1]);
		HeroPick.mHero3.setImageBitmap(result[2]);
	}
}

public class HeroPick extends Activity {
	static final String STRING_MESSAGE = "com.example.hearthstonearenadeckbuilder.STRING";
	static final String INT_MESSAGE = "com.example.hearthstonearenadeckbuilder.INT";
	static ImageView mHero1;
	static ImageView mHero2;
	static ImageView mHero3;
	int num1;
	int num2;
	int num3;
	
	String[] heroNames;
	String[] heroUrls;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hero_pick);
		
		//Set all fonts on page
		TextView heroText = (TextView) findViewById(R.id.hero_text);
	    Typeface face = Typeface.createFromAsset(getAssets(),
	                                          "fonts/Belwe.ttf");

	    heroText.setTypeface(face);
		
		mHero1 = (ImageView) findViewById(R.id.hero1);
		mHero2 = (ImageView) findViewById(R.id.hero2);
		mHero3 = (ImageView) findViewById(R.id.hero3);
		
		Resources res = getResources();
		heroNames = res.getStringArray(R.array.hero_array);
		heroUrls = res.getStringArray(R.array.hero_urls);
		

		num1 =  0 + (int)(Math.random()*9);
		num2 = 0 + (int)(Math.random()*9);
		while(num2 == num1)
			num2 = 0+ (int)(Math.random()*9);
		num3 = 0 + (int)(Math.random()*9);
		while(num3 == num1 || num3 == num2)
			num3 = 0 + (int)(Math.random()*9);
		new Download3HeroesTask().execute(heroUrls[num1],heroUrls[num2],heroUrls[num3]);
	}
	
	
	public void heroLeftClick(View v) {
		Log.v("JS","left");
		goToMain(num1);
	}
	
	public void heroCenterClick(View v) {
		Log.v("JS","center");
		goToMain(num2);
	}
	
	public void heroRightClick(View v) {
		Log.v("JS","right");
		goToMain(num3);
	}
	
	public void goToMain(int heroNum) {
		Intent i = new Intent(this, MainActivity.class);
		
		i.putExtra(STRING_MESSAGE, heroNames[heroNum]);
		i.putExtra(INT_MESSAGE, heroNum);
		startActivity(i);
	}
	
	
}