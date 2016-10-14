package app.view.uppgift1;

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

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.ws.soap.AddressingFeature.Responses;

import org.controlsfx.control.table.TableFilter;

import app.dal.uppgift1.DALCourse;
import app.dal.uppgift1.DALStudent;
import app.database.fields.uppgift1.DbCourse;
import app.model.uppgift1.Course;
import app.model.uppgift1.Student;

public class CourseOverviewController {

	@FXML
	private ComboBox<Course> courseComboBox;
	
	@FXML
	private TextField searchField;
	
	@FXML
	private TableView<Student> courseTable;
	@FXML
	private TableColumn<Student, String> studentNameColumn;
	@FXML
	private TableColumn<Student, String> studentPnrColumn;
	@FXML
	private TableColumn<Student, String> studentStatusColumn;
	@FXML
	private TableColumn<Student, String> studentPhoneNbrColumn;
	@FXML
	private TableColumn<Student, String> studentAdressColumn;
	
	private TableFilter<Student> filter;
	
	@FXML
	private Label courseCodeLabel;
	@FXML
	private Label courseNameLabel;
	@FXML
	private Label coursePointsLabel;
	@FXML
	private Label courseAverageGradeLabel;
	@FXML
	private Label coursePercentageHighestGradeLabel;
	@FXML
	private Label courseNumberStudyingStudentsLabel;
	@FXML
	private Label courseNumberStudiedStudentsLabel;
	@FXML
	private Label responseLabel;
	
	@FXML
	private BarChart<String, Number> gradeStatisticsChart;
	@FXML
	private CategoryAxis xAxis;
	@FXML
	private NumberAxis yAxis;
	
	private XYChart.Series gradeSeries;
	
	
	private boolean searchButtonWasUsedLastNotComboBox; 
	
	
	public CourseOverviewController() {
	}

	@FXML
	private void initialize() {
				
		studentNameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
		studentPnrColumn.setCellValueFactory(cellData -> cellData.getValue().getPersonNumberProperty());
		studentPhoneNbrColumn.setCellValueFactory(cellData -> cellData.getValue().getPhoneNumberProperty());
		studentAdressColumn.setCellValueFactory(cellData -> cellData.getValue().getAddressProperty());
		studentStatusColumn.setCellValueFactory(cellData -> cellData.getValue().getStatusProperty());
		
		courseComboBox.valueProperty().addListener(new ChangeListener<Course>() {
			@Override
			public void changed(ObservableValue<? extends Course> observable, Course oldValue, Course newValue) {
				if (courseComboBox.getSelectionModel().getSelectedItem() != null) {
					String courseID = courseComboBox.getSelectionModel().getSelectedItem().getCode();
					updateLabelsAndChart(courseID);
					updateTable(courseID);
					searchButtonWasUsedLastNotComboBox = false;
				}
			}
		});
		this.setComboBoxValeus();
	}
	
	@FXML
	private void handleUpdateButton() {
		
		String courseID = "";
		if (searchButtonWasUsedLastNotComboBox) {
			courseID = searchField.getText();
		} else {
			if (courseComboBox.getSelectionModel().getSelectedItem() != null) {
				courseID = courseComboBox.getSelectionModel().getSelectedItem().getCode();
			}
		}
		if (courseID.length() > 0) {
			this.updateTable(courseID);
			this.updateLabelsAndChart(courseID);
		}
	}
	
	@FXML
	private void handleSearchButton() {
		String courseID = searchField.getText();

		if (courseID.length() == 0) {
			searchField.setPromptText("Ange minst ett tecken");
		} else {

			updateLabelsAndChart(courseID);
			updateTable(courseID);

			searchButtonWasUsedLastNotComboBox = true;
		}
	}
	
	public void updateTable(String courseID) {
		courseTable.setItems(null);
		
		ArrayList<Student> valuesFromDatabase = DALStudent.findAllStudentsByCourseID(courseID);
		if (valuesFromDatabase != null) {
			courseTable.setItems(FXCollections.observableArrayList(valuesFromDatabase));
			filter = new TableFilter<Student>(courseTable);
		}
	}
	
	public void updateLabelsAndChart(String courseID) {
		
		responseLabel.setText("");
		gradeStatisticsChart.getData().clear();
		
		HashMap<String, Object> valuesFromDatabase = DALCourse.findCourseInfoByCourseID(courseID);
		if (valuesFromDatabase != null && valuesFromDatabase.get(DbCourse.CODE.getDbName()) != null) {

			courseCodeLabel.setText((String)valuesFromDatabase.get(DbCourse.CODE.getDbName()));
			courseNameLabel.setText((String)valuesFromDatabase.get(DbCourse.NAME.getDbName()));
			coursePointsLabel.setText((String)valuesFromDatabase.get(DbCourse.POINTS.getDbName()));
				
			double avgGrade = (double)(valuesFromDatabase.get("avgGrade"));
			String gradeEqv = "";
			
			if 		(avgGrade > 4.5) gradeEqv = "A";
			else if (avgGrade > 3.5) gradeEqv = "B";
			else if (avgGrade > 2.5) gradeEqv = "C";
			else if (avgGrade > 1.5) gradeEqv = "D";
			else if (avgGrade > 0.5) gradeEqv = "E";
			else if (avgGrade < 0.5 
				   && avgGrade >= 0) gradeEqv = "F";
			else if (avgGrade == -1) gradeEqv = "Ingen data tillgänglig"; // Query returns -1 if there is no value, i.e. no read courses to avg(grade)
		
			if (avgGrade > 0) gradeEqv += " (" + avgGrade + ")";
			
			courseAverageGradeLabel.setText(gradeEqv);
			coursePercentageHighestGradeLabel.setText(valuesFromDatabase.get("topGradePercentage").toString() + "%");
			courseNumberStudyingStudentsLabel.setText(valuesFromDatabase.get("countStudying").toString());
			courseNumberStudiedStudentsLabel.setText(valuesFromDatabase.get("countStudied").toString());

		} else {
			responseLabel.setText("Ingen kurs hittad");
			
			courseCodeLabel.setText("");
			courseNameLabel.setText("");
			coursePointsLabel.setText("");
			courseAverageGradeLabel.setText("");
			coursePercentageHighestGradeLabel.setText("");
			courseNumberStudiedStudentsLabel.setText("");
			courseNumberStudyingStudentsLabel.setText("");
		}
		
		gradeSeries = new XYChart.Series();
		
		gradeSeries.getData().add(new XYChart.Data("Betyg F", isNull(valuesFromDatabase.get("grade0"))));
        gradeSeries.getData().add(new XYChart.Data("Betyg E", isNull(valuesFromDatabase.get("grade1"))));
        gradeSeries.getData().add(new XYChart.Data("Betyg D", isNull(valuesFromDatabase.get("grade2"))));
        gradeSeries.getData().add(new XYChart.Data("Betyg C", isNull(valuesFromDatabase.get("grade3"))));
        gradeSeries.getData().add(new XYChart.Data("Betyg B", isNull(valuesFromDatabase.get("grade4"))));
        gradeSeries.getData().add(new XYChart.Data("Betyg A", isNull(valuesFromDatabase.get("grade5"))));
        
        gradeStatisticsChart.getData().addAll(gradeSeries);
	}
	
	@FXML
	public void setComboBoxValeus() {
		ArrayList<Course> valuesFromDatabase = DALCourse.findAllCourses();
		courseComboBox.setItems(FXCollections.observableArrayList(valuesFromDatabase));
	}
	
	private double isNull(Object o) {
		return (o == null) ? 0 : (double) o;
	}
}
