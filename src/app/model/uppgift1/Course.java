package app.model.uppgift1;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class Course {
	
	private StringProperty code;
	private StringProperty name;
	private IntegerProperty points;
	private DoubleProperty throughFlow;
	
	public Course(String code, String name, int points) {
		this.code = new SimpleStringProperty(code);
		this.name = new SimpleStringProperty(name);
		this.points = new SimpleIntegerProperty(points);
		this.throughFlow = new SimpleDoubleProperty(-1);
	}
	
	public Course(String code, String name, int points, double throughFlow) {
		this.code = new SimpleStringProperty(code);
		this.name = new SimpleStringProperty(name);
		this.points = new SimpleIntegerProperty(points);
		this.throughFlow = new SimpleDoubleProperty(throughFlow);
	}

	public StringProperty getCodeProperty() {
		return code;
	}

	public StringProperty getNameProperty() {
		return name;
	}

	public IntegerProperty getPointsProperty() {
		return points;
	}	
	
	public DoubleProperty getThroughFlowProperty() {
		return throughFlow;
	}
	
	public String getCode() {
		return this.code.get();
	}
	
	public String getName() {
		return name.get();
	}
	
	public int getPoints() {
		return this.points.get();
	}
	
	public Double getThroughFlow() {
		return throughFlow.get();
	}

	@Override
	public String toString() {
		return this.getCode() + " " + this.getName() + " " + this.getPoints() + "p.";
	}
}

