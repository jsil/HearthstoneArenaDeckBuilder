package com.example.hearthstonearenadeckbuilder;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.ImageView;


class DownloadCardTask extends AsyncTask<String, Void, Bitmap> {
	protected Bitmap doInBackground(String... urls) {
		Bitmap cardImage = MainActivity.loadImageFromNetwork(urls[0]);
		return cardImage;
	}
	protected void onPostExecute(Bitmap result) {
		CardInspect.cardPic.setImageBitmap(result);
	}
}

public class CardInspect extends Activity {

	static final String STRING_MESSAGE = "com.example.hearthstonearenadeckbuilder.STRING";
	static ImageView cardPic;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card_inspect);
		
		
		cardPic = (ImageView) findViewById(R.id.card_big);
		Intent intent = getIntent();
		String cardURL = intent.getStringExtra(STRING_MESSAGE);
		
		
		new DownloadCardTask().execute(cardURL);
		
	  
	}
	

}