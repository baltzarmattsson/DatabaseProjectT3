package app.model.uppgift2;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FetchedMetadataRow {

	private StringProperty TABLE_SCHEMA;
	private StringProperty TABLE_NAME;
	private StringProperty COLUMN_NAME;
	private IntegerProperty ORDINAL_POSITION;
	private StringProperty COLUMN_DEFAULT;
	private StringProperty IS_NULLABLE;
	private StringProperty DATA_TYPE;
	private IntegerProperty CHARACTER_MAXIMUM_LENGTH;
	private IntegerProperty CHARACTER_OCTET_LENGTH;
	
	public FetchedMetadataRow(String TABLE_SCHEMA, String TABLE_NAME, String COLUMN_NAME, int ORDINAL_POSITION, String COLUMN_DEFAULT, String IS_NULLABLE,
								String DATA_TYPE, int CHARACTER_MAXIMUM_LENGTH, int CHARACTER_OCTET_LENGTH) {
		this.TABLE_SCHEMA = new SimpleStringProperty(TABLE_SCHEMA);
		this.TABLE_NAME = new SimpleStringProperty(TABLE_NAME);
		this.COLUMN_NAME = new SimpleStringProperty(COLUMN_NAME);
		this.ORDINAL_POSITION = new SimpleIntegerProperty(ORDINAL_POSITION);
		this.COLUMN_DEFAULT = new SimpleStringProperty(COLUMN_DEFAULT);
		this.IS_NULLABLE = new SimpleStringProperty(IS_NULLABLE);
		this.DATA_TYPE = new SimpleStringProperty(DATA_TYPE);
		this.CHARACTER_MAXIMUM_LENGTH = new SimpleIntegerProperty(CHARACTER_MAXIMUM_LENGTH);
		this.CHARACTER_OCTET_LENGTH = new SimpleIntegerProperty(CHARACTER_OCTET_LENGTH);
	}
								
	public StringProperty getTABLE_SCHEMA() {
		return TABLE_SCHEMA;
	}
	public StringProperty getTABLE_NAME() {
		return TABLE_NAME;
	}
	public StringProperty getCOLUMN_NAME() {
		return COLUMN_NAME;
	}
	public StringProperty getORDINAL_POSITION() {
		return new SimpleStringProperty(ORDINAL_POSITION.get() + "");
	}
	public StringProperty getCOLUMN_DEFAULT() {
		return COLUMN_DEFAULT;
	}
	public StringProperty getIS_NULLABLE() {
		return IS_NULLABLE;
	}
	public StringProperty getDATA_TYPE() {
		return DATA_TYPE;
	}
	public StringProperty getCHARACTER_MAXIMUM_LENGTH() {
		return new SimpleStringProperty(CHARACTER_MAXIMUM_LENGTH.get() + "");
	}
	public StringProperty getCHARACTER_OCTET_LENGTH() {
		return new SimpleStringProperty(CHARACTER_OCTET_LENGTH.get() + "");
	}
	
	@Override
	public String toString() {
		return TABLE_SCHEMA.get() + " " + TABLE_NAME.get() + " " + COLUMN_NAME.get() + " " + ORDINAL_POSITION.get() + " " + COLUMN_DEFAULT.get() + " " + IS_NULLABLE.get() + " " + DATA_TYPE.get() + " " + CHARACTER_MAXIMUM_LENGTH.get() + " " + CHARACTER_OCTET_LENGTH.get();
	}
}
