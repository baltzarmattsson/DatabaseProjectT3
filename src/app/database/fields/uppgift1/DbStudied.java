package app.database.fields.uppgift1;

public enum DbStudied implements DbInterface {
	
	TABLENAME ("Läst"),
	
	PNUMBER ("pnr"),
	CODE ("kod"),
	GRADE ("betyg");
	
	private final String name;
	
	private DbStudied(String s) {
		name = s;
	}
	
	public String toString() {
		return this.name;
	}
	
	public String getDbName() {
		return this.name;
	}

}
