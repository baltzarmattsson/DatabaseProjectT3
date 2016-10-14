package app.view.uppgift1;

import java.util.ArrayList;
import java.util.HashMap;

import org.controlsfx.control.table.TableFilter;

import app.dal.uppgift1.DALCourse;
import app.model.uppgift1.Course;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class MainOverviewController {

	@FXML
	private TableView<Course> overviewTable;
	@FXML
	private TableColumn<Course, String> courseCodeColumn;
	@FXML
	private TableColumn<Course, String> courseNameColumn;
	@FXML
	private TableColumn<Course, Double> courseThroughFlowColumn;
	
	private TableFilter<Course> filter;
	
	@FXML
	private Label avgThroughFlowAllCoursesLabel;
	@FXML
	private Label numberCoursesReadingAllLabel;
	@FXML
	private Label numberCoursesReadAllLabel;
	
	@FXML
	private BarChart<String, Number> courseStatisticsChart;
	
	@FXML
	private CategoryAxis xAxis;
	@FXML
	private NumberAxis yAxis;

	public MainOverviewController() {
	}

	@FXML
	private void initialize() {

		courseCodeColumn.setCellValueFactory(cellData -> cellData.getValue().getCodeProperty());
		courseNameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
		courseThroughFlowColumn.setCellValueFactory(
				cellData -> new SimpleDoubleProperty(cellData.getValue().getThroughFlow()).asObject());
		
        courseStatisticsChart.setTitle("Andel betyg");		
		
		this.updateLabelsAndChart();
		this.updateTable();
	}
	
	@FXML
	private void handleUpdateButton() {
		this.updateTable();
		this.updateLabelsAndChart();
	}

	public void updateTable() {
		overviewTable.setItems(null);
		
		ArrayList<Course> valuesFromDatabase = DALCourse.findAllCourses();
		overviewTable.setItems(FXCollections.observableArrayList(valuesFromDatabase));
		filter = new TableFilter<Course>(overviewTable);
				
	}

	public void updateLabelsAndChart() {
		
		courseStatisticsChart.getData().clear();

		HashMap<String, Object> valuesFromDatabase = DALCourse.findAllCourseStatistics();
		
		String throughFlow = valuesFromDatabase.get("throughFlow").toString().equals("-1.0") ? "Ingen data tillgänglig" : valuesFromDatabase.get("throughFlow").toString();
		
		avgThroughFlowAllCoursesLabel.setText(throughFlow);
		numberCoursesReadingAllLabel.setText(valuesFromDatabase.get("countStudying").toString());
		numberCoursesReadAllLabel.setText(valuesFromDatabase.get("countStudied").toString());

        XYChart.Series gradeSeries = new XYChart.Series();
        
        gradeSeries.getData().add(new XYChart.Data("Betyg F", isNull(valuesFromDatabase.get("grade0"))));
        gradeSeries.getData().add(new XYChart.Data("Betyg E", isNull(valuesFromDatabase.get("grade1"))));
        gradeSeries.getData().add(new XYChart.Data("Betyg D", isNull(valuesFromDatabase.get("grade2"))));
        gradeSeries.getData().add(new XYChart.Data("Betyg C", isNull(valuesFromDatabase.get("grade3"))));
        gradeSeries.getData().add(new XYChart.Data("Betyg B", isNull(valuesFromDatabase.get("grade4"))));
        gradeSeries.getData().add(new XYChart.Data("Betyg A", isNull(valuesFromDatabase.get("grade5"))));
        
        
        courseStatisticsChart.getData().addAll(gradeSeries);
	}
	
	private double isNull(Object o) {
		return (o == null) ? 0 : (double) o;
	}

}
