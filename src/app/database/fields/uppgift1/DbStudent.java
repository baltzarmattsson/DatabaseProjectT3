package app.database.fields.uppgift1;

public enum DbStudent implements DbInterface {

	TABLENAME ("Student"),
	
	PNUMBER ("pnr"),
	NAME ("namn"),
	ADDRESS ("adress"),
	PHONENBR ("telefon");
	
	private final String name;
	
	private DbStudent(String s) {
		name = s;
	}
	
	public String toString() {
		return this.name;
	}
	
	public String getDbName() {
		return this.name;
	}

}
