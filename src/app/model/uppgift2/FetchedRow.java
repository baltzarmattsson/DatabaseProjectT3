package app.model.uppgift2;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FetchedRow {

	private StringProperty attribute1;
	private StringProperty attribute2;
	private StringProperty attribute3;
	private StringProperty attribute4;
	private StringProperty attribute5;
	private StringProperty attribute6;
	private StringProperty attribute7;
	private StringProperty attribute8;
	private StringProperty attribute9;
	private StringProperty attribute10;
	
	private int numberOfAttributes;
	
	public FetchedRow(String attribute1, String attribute2, String attribute3, String attribute4, String attribute5, 
			String attribute6, String attribute7, String attribute8, String attribute9, String attribute10) {
		this.attribute1 = new SimpleStringProperty(attribute1);
		this.attribute2 = new SimpleStringProperty(attribute2);
		this.attribute3 = new SimpleStringProperty(attribute3);
		this.attribute4 = new SimpleStringProperty(attribute4);
		this.attribute5 = new SimpleStringProperty(attribute5);
		this.attribute6 = new SimpleStringProperty(attribute6);
		this.attribute7 = new SimpleStringProperty(attribute7);
		this.attribute8 = new SimpleStringProperty(attribute8);
		this.attribute9 = new SimpleStringProperty(attribute9);
		this.attribute10 = new SimpleStringProperty(attribute10);
		
	}
	
	public FetchedRow(String attribute1, String attribute2, String attribute3, String attribute4, String attribute5) {
		this(attribute1, attribute2, attribute3, attribute4, attribute5, "", "", "", "", "");
	}
	
	public int getNumberOfAttributes() {
		return this.numberOfAttributes;
	}
	
	public StringProperty getAttribute1() {
		return attribute1;
	}
	public StringProperty getAttribute2() {
		return attribute2;
	}
	public StringProperty getAttribute3() {
		return attribute3;
	}
	public StringProperty getAttribute4() {
		return attribute4;
	}
	public StringProperty getAttribute5() {
		return attribute5;
	}
	public StringProperty getAttribute6() {
		return attribute6;
	}
	public StringProperty getAttribute7() {
		return attribute7;
	}
	public StringProperty getAttribute8() {
		return attribute8;
	}
	public StringProperty getAttribute9() {
		return attribute9;
	}
	public StringProperty getAttribute10() {
		return attribute10;
	}

	@Override
	public String toString() {
		return this.attribute1.get() + " " + this.attribute2.get() + " " + this.attribute3.get() + " " + this.attribute4.get() 
		+ " " + this.attribute5.get() + " " + this.attribute6.get() + " " + this.attribute6.get() + " " + this.attribute7.get() 
		+ " " + this.attribute8.get() + " " + this.attribute9.get() + " " + this.attribute10.get();
	}
}
