package com.example.hearthstonearenadeckbuilder;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;


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

			mArray = new JSONArray(jsonString);
			cards = new Card[mArray.length()];
			for(int i=0;i<mArray.length();i++) {
				Card card = new Card();
				card.mName = mArray.getJSONObject(i).getString("name");
				card.mQuantity = Integer.parseInt(mArray.getJSONObject(i).getString("quantity"));
				card.mImagePath = mArray.getJSONObject(i).getString("image_url");
				cards[i] = card;
			}
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		adapter = new CardAdapter(this, cards);
		setListAdapter(adapter);
	}
	
	public class CardAdapter extends ArrayAdapter<Card> {
		private final Context context;
		public CardAdapter(Context context, Card[] cards) {
			super(context, R.layout.card,
					R.id.text, cards);
			this.context = context;
			mCards = cards;
		}
		Card[] mCards;
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 
			View v = inflater.inflate(R.layout.card, parent, false);
			
			RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.card_layout);
			
			layout.setOnClickListener(openCard);
			
			
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
		    

			return v;
		}
		
		//On click, opens CardInspect activity for full screen view of card
		View.OnClickListener openCard = new View.OnClickListener() {
		    public void onClick(View v) {
		      TextView url = (TextView) v.findViewById(R.id.url);
		      String urlText = (String) url.getText();
		      
		      Intent i = new Intent(v.getContext(), CardInspect.class);
				
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
}