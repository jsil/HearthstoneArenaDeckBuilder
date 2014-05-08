package com.example.hearthstonearenadeckbuilder;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

//class DownloadImagesTask extends AsyncTask<String, Integer, Bitmap[]> {
//	protected Bitmap[] doInBackground(String... urls) {
//		
//		Bitmap[] loadedImages = new Bitmap[urls.length];
//		for(int i=0;i<urls.length;i++) {
//		loadedImages[i] = Characters.loadImageFromNetwork(urls[i]);
////		publishProgress(i*(100/urls.length));
//		Log.v("JS",Integer.toString(i*(100/urls.length)));
//		MainActivity.setProgressPercent(i*(100/urls.length));
//		}
//		return loadedImages;
//	}
	
//	  BUG: was not getting called:
//    protected void onProgressUpdate(Integer progress) {
//    	Log.v("JSA",Integer.toString(progress));
//        MainActivity.setProgressPercent(progress);
//    }
	
//	protected void onPostExecute(Bitmap[] result) {
//		for(int i=0;i<Characters.characters.length;i++) {
//			Characters.characters[i].mImg = result[i];
//		}
//	}
//}


public class Cards extends ListActivity {
	static final String STRING_MESSAGE = "com.example.hearthstonearenadeckbuilder.STRING";
	private CardAdapter adapter;
	JSONObject mJSON;
	JSONArray mArray;
	static Card[] cards = null;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cards = null;
		
		try {
			Intent intent = getIntent();
			String jsonString = intent.getStringExtra(MainActivity.JSON_MESSAGE);

//			mJSON = new JSONObject(jsonString);
//			mArray = (mJSON).getJSONArray("caricatures");
			mArray = new JSONArray(jsonString);
			cards = new Card[mArray.length()];
			for(int i=0;i<mArray.length();i++) {
				Card card = new Card();
				card.mName = mArray.getJSONObject(i).getString("name");
				card.mQuantity = Integer.parseInt(mArray.getJSONObject(i).getString("quantity"));
				card.mImagePath = mArray.getJSONObject(i).getString("image_url");
	//			card.mOccupation = mArray.getJSONObject(i).getString("occupation");
	//			card.mIsGenderFemale = mArray.getJSONObject(i).getBoolean("isGenderFemale");
	//			card.mAge = mArray.getJSONObject(i).getInt("age");
	//			mImgLinks[i] = mArray.getJSONObject(i).getString("img_link");
				cards[i] = card;
			}
//			AsyncTask<String, Integer, Bitmap[]> task = new DownloadImagesTask().execute(mImgLinks);
//			task.get(1000, TimeUnit.MILLISECONDS);//Should force images to load before List is created
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.v("JS","it broke");
			e.printStackTrace();
		}
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (TimeoutException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		adapter = new CardAdapter(this, cards);
		setListAdapter(adapter);
	}
	
	public class CardAdapter extends ArrayAdapter<Card> {
		private final Context context;
		public CardAdapter(Context context, Card[] cards) {
			super(context, R.layout.card,
					R.id.text, cards);//R.id.label,
			this.context = context;
			mCards = cards;
			Log.v("JS","Cards adapter adapted");
		}
		Card[] mCards;
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 
			View v = inflater.inflate(R.layout.card, parent, false);
			
			RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.card_layout);
			
			layout.setOnClickListener(openCard);
			
//			
			TextView nameTextView = (TextView) v.findViewById(R.id.text);
			nameTextView.setText(mCards[position].mName);
			TextView quantityTextView = (TextView) v.findViewById(R.id.quantity);
			quantityTextView.setText(Integer.toString(mCards[position].mQuantity));
			
			TextView urlText = (TextView) v.findViewById(R.id.url);
			urlText.setText(mCards[position].mImagePath);
			
			//Set all fonts on page
		    Typeface face = Typeface.createFromAsset(getAssets(),
		                                          "fonts/Belwe.ttf");
		    nameTextView.setTypeface(face);
		    quantityTextView.setTypeface(face);
		    
//			TextView occupationTextView = (TextView) v.findViewById(R.id.occupation);
//			occupationTextView.setText(mCharacters[position].mOccupation);
//			TextView genderTextView = (TextView) v.findViewById(R.id.gender);
//			if(mCharacters[position].mIsGenderFemale == true)
//				genderTextView.setText("Female");
//			else
//				genderTextView.setText("Male");
//			TextView ageTextView = (TextView) v.findViewById(R.id.age);
//			ageTextView.setText(Integer.toString(mCharacters[position].mAge));
//			ImageView picImageView = (ImageView) v.findViewById(R.id.pic);
//			picImageView.setImageBitmap(mCharacters[position].mImg);

			return v;
		}
		
		View.OnClickListener openCard = new View.OnClickListener() {
		    public void onClick(View v) {
		      TextView url = (TextView) v.findViewById(R.id.url);
		      String urlText = (String) url.getText();
		      
		      Intent i = new Intent(v.getContext(), CardInspect.class);
				
		      Log.v("JS",urlText);
		      i.putExtra(STRING_MESSAGE, urlText);
		      startActivity(i);
		      
		    }
		  };
	}
	
	public static class Card {
		String mName;
		int mQuantity;
		String mImagePath;
		
		public Card() {}
		
		public Card(String name, int quantity, String imagePath) {
		mName = name;
		mQuantity = quantity;
		mImagePath = imagePath;
		}
		
		@Override
		public String toString() {
			return mName;
		}
	}
	
//	public static Bitmap loadImageFromNetwork (String imgUrl) {
//		Bitmap img = null;
//		URL url;
//		try {
//		url = new URL(imgUrl);
//		img = BitmapFactory.decodeStream(url.openStream());
//		} catch (MalformedURLException e) {
//		Log.e("JS", "URL is bad");
//		e.printStackTrace();
//		} catch (IOException e) {
//		Log.e("JS", "Failed to decode Bitmap");
//		e.printStackTrace();
//		}
//		return img;
//		}
}