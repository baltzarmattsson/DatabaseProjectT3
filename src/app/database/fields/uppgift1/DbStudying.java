package app.database.fields.uppgift1;

public enum DbStudying implements DbInterface {
	
	TABLENAME ("Läser"),
	
	PNUMBER ("pnr"),
	CODE ("kod");
	
	private final String name;
	
	private DbStudying(String s) {
		name = s;
	}
	
	public String toString() {
		return this.name;
	}
	
	public String getDbName() {
		return this.name;
	}

}
