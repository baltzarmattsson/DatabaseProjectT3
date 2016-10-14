package app.model.uppgift1;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Student {

	private StringProperty personNumber;
	private StringProperty name;
	private StringProperty address;
	private StringProperty phoneNumber;
	private StringProperty status;
	
	public Student(String personNumber,
			String name,
			String address,
			String phoneNumber,
			String status) {
		this.personNumber = new SimpleStringProperty(personNumber);
		this.name = new SimpleStringProperty(name);
		this.address = new SimpleStringProperty(address);
		this.phoneNumber = new SimpleStringProperty(phoneNumber);
		this.status = new SimpleStringProperty(status);
	}
	
	public Student(String personNumber,
			String name,
			String address,
			String phoneNumber) {
		this(personNumber, name, address, phoneNumber, "");
	}

	public StringProperty getPersonNumberProperty() {
		return personNumber;
	}

	public StringProperty getNameProperty() {
		return name;
	}

	public StringProperty getAddressProperty() {
		return address;
	}

	public StringProperty getPhoneNumberProperty() {
		return phoneNumber;
	}
	
	public StringProperty getStatusProperty() {
		return status;
	}
	
	public String getPersonNumber() {
		return personNumber.get();
	}
	
	public String getName() {
		return name.get();
	}
	
	public String getAddress() {
		return address.get();
	}
	
	public String getPhoneNumber() {
		return phoneNumber.get();
	}
	
	public String getStatus() {
		return status.get();
	}
	
	@Override
	public String toString() {
		return this.getPersonNumber() + " " + this.getName() + " " + this.getAddress() + " " + this.getPhoneNumber();
	}
	
}