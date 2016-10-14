package app.view.uppgift1;

import java.sql.SQLException;
import java.util.ArrayList;

import org.controlsfx.control.CheckComboBox;

import app.dal.uppgift1.DALCourse;
import app.dal.uppgift1.DALStudent;
import app.model.uppgift1.Course;
import app.model.uppgift1.Student;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class StudentAddDialogController {
	
	@FXML
	private TextField pnbrField;
	@FXML
	private TextField nameField;
	@FXML
	private TextField addressField;
	@FXML
	private TextField phoneNbrField;
	
	@FXML
	private Label statusLabel;
	
	@FXML
	private Label currentPoints;
	private int currentPointsNumber = 0;
	
	@FXML
	private CheckComboBox<Course> studyingComboBox;
	
	private Stage dialogStage;
	private boolean okClicked = false;
	
	@FXML
	private void initialize() {
		this.updateComboBoxes();
				
		studyingComboBox.getCheckModel().getCheckedItems().addListener(new ListChangeListener<Course>(){
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Course> c) {
				currentPointsNumber = 0;
				for (Course selectedCourse : studyingComboBox.getCheckModel().getCheckedItems()) {
					currentPointsNumber += selectedCourse.getPoints();
				}
			}
		});
	}
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public boolean isOkClicked() {
		return okClicked;
	}
	
    @FXML
    private void handleCancel() {
    	dialogStage.close();
    }
    
    @FXML
	private void handleOK() {
    	
		if (currentPointsNumber > 45) {
			this.showAlert("Överstigit antal",
					"Antal läsande kurser får max vara 45p, nuvarande sammanlagt värde för valda kurser är " + currentPointsNumber + "p");
		} else {

			if (pnbrField.getText() != null && pnbrField.getText().length() > 0) {
				Student s = new Student(pnbrField.getText(), nameField.getText(), addressField.getText(),
						phoneNbrField.getText());
				try {

					DALStudent.addStudent(s);

					ArrayList<String> courseCodes = new ArrayList<String>();
					for (Course c : studyingComboBox.getCheckModel().getCheckedItems())
						courseCodes.add(c.getCode());

					int rowsAffected = 0;
					if (courseCodes.isEmpty() == false)
						rowsAffected = DALStudent.addStudyingRelation(pnbrField.getText(), courseCodes);

					statusLabel.setText("Student tillagd, samt registrerad på " + rowsAffected + " kurser");

				} catch (SQLException e) {

					// FOREIGN KEY CONSTRAINT 1452 = Mysql, 547 = MSSQL (foreign key)
					if (e.getErrorCode() == 1452 || e.getErrorCode() == 547) {

						this.showAlert("Hittade ej kurs", "Kunde inte hitta en kurs, vänligen försök igen");

						// PRIMARY KEY CONSTRAINT 1062 = MySQL, 2627 = MSSQL (primary key)
					} else if (e.getErrorCode() == 1062 || e.getErrorCode() == 2627 ) {

						this.showAlert("Värdet finns redan", "Personnumret finns redan");

						// Data too long 1406 = MySQL (too long), 8152 = MsSQL (truncated)
					} else if (e.getErrorCode() == 1406 || e.getErrorCode() == 8152) {

						this.showAlert("Värde är för långt", "Ett värde är för långt, vänligen minska det");

					} else {
						System.out.println(e.getMessage());
						System.out.println(e.getErrorCode());
						e.printStackTrace();
					}
				}
			} else if (pnbrField.getText() == null || pnbrField.getText().length() == 0) {
				statusLabel.setText("Fältet personnummer kan inte vara tomt. Vänligen ange minst ett tecken");
			}
		}
    }
    
	public void updateComboBoxes() {
		ArrayList<Course> valuesFromDatabase = DALCourse.findAllCourses();
		studyingComboBox.getItems().addAll(valuesFromDatabase);
	}
	
	private void showAlert(String header, String contentText) {
		Alert a = new Alert(AlertType.ERROR);
		a.initOwner(dialogStage);
		a.setTitle("Fel");
		a.setHeaderText(header);
		a.setContentText(contentText);
		a.showAndWait();
	}
}