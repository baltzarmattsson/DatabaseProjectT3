package app.view.uppgift1;

import java.util.ArrayList;
import java.util.HashMap;
import org.controlsfx.control.table.TableFilter;
import app.dal.uppgift1.DALStudent;
import app.database.fields.uppgift1.DbStudent;
import app.model.uppgift1.Entry;
import app.model.uppgift1.Student;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class StudentOverviewController {

	@FXML
	private ComboBox<Student> studentComboBox;
	
	@FXML
	private TextField searchField;

	@FXML
	private TableView<Entry> studentTable;
	@FXML
	private TableColumn<Entry, String> courseCodeColumn;
	@FXML
	private TableColumn<Entry, String> courseNameColumn;
	@FXML
	private TableColumn<Entry, Number> coursePointsColumn;
	@FXML
	private TableColumn<Entry, String> studentStatusColumn;
	
	private TableFilter<Entry> filter;
	
	@FXML
	private Label studentPersonNumberLabel;
	@FXML
	private Label studentNameLabel;
	@FXML
	private Label studentAverageGradeLabel;
	@FXML
	private Label studentNumberCoursesReading;
	@FXML
	private Label studentNumberCoursesRead;
	@FXML
	private Label studentAdressLabel;
	@FXML
	private Label studentPhoneNumberLabel;
	@FXML
	private Label responseLabel;
	@FXML
	private Label currentlyReadingPoints;
	
	@FXML
	private BarChart<String, Number> gradeStatisticsChart;
	
	@FXML
	private CategoryAxis xAxis;
	@FXML
	private NumberAxis yAxis;
	
	private boolean searchWasUsedLastNotComboBox; 
	
	public StudentOverviewController() {
	}

	@FXML
	private void initialize() {
		courseCodeColumn.setCellValueFactory(cellData -> cellData.getValue().getCourseCodeProperty());
		courseNameColumn.setCellValueFactory(cellData -> cellData.getValue().getCourseNameProperty());
		coursePointsColumn.setCellValueFactory(cellData -> cellData.getValue().getCoursePointsProperty());
		studentStatusColumn.setCellValueFactory(cellData -> cellData.getValue().getStudentStatusOnCourseProperty());

		studentComboBox.valueProperty().addListener(new ChangeListener<Student>() {

			@Override
			public void changed(ObservableValue<? extends Student> observable, Student oldValue, Student newValue) {
				if (studentComboBox.getSelectionModel().getSelectedItem() != null) {
					
					String studentID = studentComboBox.getSelectionModel().getSelectedItem().getPersonNumber();
					updateLabelsAndChart(studentID);
					updateTable(studentID);
					searchWasUsedLastNotComboBox = false;
				}
			}
		});
			
		this.setComboBoxValues();
	}
	
	@FXML
	private void handleUpdateButton() {
		String studentID = "";
		if (searchWasUsedLastNotComboBox) {
			studentID = searchField.getText();
		} else {
			if (studentComboBox.getSelectionModel().getSelectedItem() != null) {
				studentID = studentComboBox.getSelectionModel().getSelectedItem().getPersonNumber();
			}
		}
		if (studentID.length() > 0) {
			this.updateTable(studentID);
			this.updateLabelsAndChart(studentID);
		}
	}
	
	@FXML
	private void handleSearchButton() {
		String studentID = searchField.getText();
		
		if (studentID.length() == 0) {
			searchField.setPromptText("Ange minst ett tecken");
		} else {
			this.updateLabelsAndChart(studentID);
			this.updateTable(studentID);
			searchWasUsedLastNotComboBox = true;
		}
		
	}
	
	public void updateTable(String studentID) {
		studentTable.setItems(null);
		
		ArrayList<Entry> valuesFromDatabase = DALStudent.findAllCourseRelationsByStudentID(studentID);
		if (valuesFromDatabase != null && valuesFromDatabase.size() > 0) {
			studentTable.setItems(FXCollections.observableArrayList(valuesFromDatabase));
			filter = new TableFilter<Entry>(studentTable);
		}
	}

	public void updateLabelsAndChart(String studentID) {
		
		responseLabel.setText("");
		gradeStatisticsChart.getData().clear();
		
		HashMap<String, Object> valuesFromDatabase = DALStudent.findStudentInfoByStudentID(studentID);
		
		if (valuesFromDatabase != null && valuesFromDatabase.get(DbStudent.PNUMBER.getDbName()) != null) {
			
			studentPersonNumberLabel.setText((String) valuesFromDatabase.get(DbStudent.PNUMBER.getDbName()));
			studentNameLabel.setText((String)valuesFromDatabase.get(DbStudent.NAME.getDbName()));
			studentAdressLabel.setText((String)valuesFromDatabase.get(DbStudent.ADDRESS.getDbName()));
			studentPhoneNumberLabel.setText((String)valuesFromDatabase.get(DbStudent.PHONENBR.getDbName()));

			double avgGrade = (double) valuesFromDatabase.get("avgGrade");
			String gradeEqv = "";
			
			if 		(avgGrade > 4.5) gradeEqv = "A";
			else if (avgGrade > 3.5) gradeEqv = "B";
			else if (avgGrade > 2.5) gradeEqv = "C";
			else if (avgGrade > 1.5) gradeEqv = "D";
			else if (avgGrade > 0.5) gradeEqv = "E";
			else if (avgGrade < 0.5 && avgGrade >= 0) gradeEqv = "F";
			else if (avgGrade == -1) gradeEqv = "Ingen data tillgänglig"; // Query returns -1 if there is no value, i.e. no read courses to avg(grade)
			
			if (avgGrade >= 0) gradeEqv += " (" + avgGrade + ")";
			
			studentAverageGradeLabel.setText(gradeEqv);
			studentNumberCoursesReading.setText(valuesFromDatabase.get("countStudying").toString());
			studentNumberCoursesRead.setText(valuesFromDatabase.get("countStudied").toString());
			
			int pointsCurrentlyReading = DALStudent.findCurrentlyReadingPoints(studentID);
			currentlyReadingPoints.setText(pointsCurrentlyReading + "");
		} else {
			responseLabel.setText("Ingen student hittad");
			studentPersonNumberLabel.setText("");
			studentNameLabel.setText("");
			studentAdressLabel.setText("");
			studentPhoneNumberLabel.setText("");
			studentAverageGradeLabel.setText("");
			studentNumberCoursesReading.setText("");
			studentNumberCoursesRead.setText("");
			currentlyReadingPoints.setText("");
		}
		
		XYChart.Series gradeSeries = new XYChart.Series();
		
		gradeSeries.getData().add(new XYChart.Data("Betyg F", isNull(valuesFromDatabase.get("grade0"))));
        gradeSeries.getData().add(new XYChart.Data("Betyg E", isNull(valuesFromDatabase.get("grade1"))));
        gradeSeries.getData().add(new XYChart.Data("Betyg D", isNull(valuesFromDatabase.get("grade2"))));
        gradeSeries.getData().add(new XYChart.Data("Betyg C", isNull(valuesFromDatabase.get("grade3"))));
        gradeSeries.getData().add(new XYChart.Data("Betyg B", isNull(valuesFromDatabase.get("grade4"))));
        gradeSeries.getData().add(new XYChart.Data("Betyg A", isNull(valuesFromDatabase.get("grade5"))));
        
        gradeStatisticsChart.getData().addAll(gradeSeries);
	}
	
	@FXML
	public void setComboBoxValues() {
		ArrayList<Student> valuesFromDatabase = DALStudent.findAllStudents();
		studentComboBox.setItems(FXCollections.observableArrayList(valuesFromDatabase));
	}
	
	private double isNull(Object o) {
		return (o == null) ? 0 : (double) o;
	}
}
