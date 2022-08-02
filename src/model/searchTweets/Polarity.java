package model.searchTweets;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public enum Polarity {
	UNPOLARIZED(-1),
	NEGATIVE(0),
	NEUTRAL(2),
	POSITIVE(4);
	
	
	private int value;
	
	private Polarity(int pValue) {
		this.value = pValue;
	}

	/**
	 * @return the polarity value
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * create a Polarity instance from a value
	 * @param value
	 * @return an instance of Polarity with the specified value
	 * @throws IllegalArgumentException
	 */
	public static Polarity fromValue(int value) throws IllegalArgumentException {
		switch ( value ) {
			case -1:
				return UNPOLARIZED;
			case 0:
				return NEGATIVE;
			case 2:
				return NEUTRAL;
			case 4:
				return POSITIVE;			
			default:
				throw new IllegalArgumentException("Value has to be -1, 0, 1 or 2");
		}
	}
	
	public ImageView getSmileyFace() {
		String result = "images/";
		switch(this.value) {
			case 0:
				result += "sadFace.png";
				break;
			case 2: 
				result += "neutralFace.png";
				break;
			case 4: 
				result += "happyFace.png";
				break;
		}
		ImageView img = new ImageView(new Image(result));
		return img;	
	}
}
