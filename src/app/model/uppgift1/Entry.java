package app.model.uppgift1;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Entry {

	private StringProperty courseCode;
	private StringProperty courseName;
	private IntegerProperty coursePoints;
	private StringProperty studentStatusOnCourse;
	
	public Entry(String courseCode, String courseName, int coursePoints, String studentStatusOnCourse) {
		this.courseCode = new SimpleStringProperty(courseCode);
		this.courseName = new SimpleStringProperty(courseName);
		this.coursePoints = new SimpleIntegerProperty(coursePoints);
		this.studentStatusOnCourse = new SimpleStringProperty(studentStatusOnCourse);
	}

	public StringProperty getCourseCodeProperty() {
		return courseCode;
	}

	public StringProperty getCourseNameProperty() {
		return courseName;
	}

	public IntegerProperty getCoursePointsProperty() {
		return coursePoints;
	}

	public StringProperty getStudentStatusOnCourseProperty() {
		return studentStatusOnCourse;
	}
	
	public String getCourseCode() {
		return courseCode.get();
	}

	public String getCourseName() {
		return courseName.get();
	}

	public int getCoursePoints() {
		return coursePoints.get();
	}

	public String getStudentStatusOnCourse() {
		return studentStatusOnCourse.get();
	}
	
	@Override
	public String toString() {
		return this.getCourseCode() + "\t" + this.getCourseName() + "\t" + this.getCoursePoints() + " p.\t" + this.getStudentStatusOnCourse();
	}
	
}
