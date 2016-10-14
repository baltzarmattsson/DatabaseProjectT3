package app.database.fields.uppgift1;

public enum DbCourse implements DbInterface {
	
	TABLENAME ("Kurs"),
	
	CODE ("kod"),
	NAME ("namn"),
	POINTS ("po�ng");
	
	private final String name;
	
	private DbCourse(String s) {
		name = s;
	}
	
	public String toString() {
		return this.name;
	}
	
	public String getDbName() {
		return this.name;
	}

}
