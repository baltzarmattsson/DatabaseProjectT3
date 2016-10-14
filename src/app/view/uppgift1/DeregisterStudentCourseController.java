package app.view.uppgift1;

import java.util.ArrayList;
import java.util.Optional;

import org.controlsfx.control.CheckComboBox;

import app.dal.uppgift1.DALStudent;
import app.model.uppgift1.StudiedEntry;
import app.model.uppgift1.StudyingEntry;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DeregisterStudentCourseController {

	@FXML
	private TextField studentNbrField;
	
	@FXML
	private CheckComboBox<StudyingEntry> studyingComboBox;
	@FXML
	private CheckComboBox<StudiedEntry> studiedComboBox;
	
	@FXML
	private Label statusLabel;
	
	private Stage dialogStage;
	private boolean okClicked = false;
	
	@FXML
	private void initialize() {
	}
	
	@FXML
	private void handleSearchButton() {
		
		studyingComboBox.getItems().clear();
		studiedComboBox.getItems().clear();
		
		if (studentNbrField != null && studentNbrField.getText().length() > 0) {
			String studentID = studentNbrField.getText();
				
			ArrayList<StudyingEntry> studyingValuesFromDatabase = DALStudent.findStudyingCoursesByStudentID(studentID);
			if (studyingValuesFromDatabase != null) {
				studyingComboBox.getItems().addAll(FXCollections.observableArrayList(studyingValuesFromDatabase));
			}

			ArrayList<StudiedEntry> studiedValuesFromDatabase = DALStudent.findStudiedCoursesByStudentID(studentID);
			if (studiedValuesFromDatabase != null)
				studiedComboBox.getItems().addAll(FXCollections.observableArrayList(studiedValuesFromDatabase));
		} else if (studentNbrField.getText() == null || studentNbrField.getText().length() == 0) {
			statusLabel.setText("Ange minst ett tecken i pnr");
		}
	}
	
	@FXML
	private void handleDeregisterButton() {
	
		if (studentNbrField.getText() != null && studentNbrField.getText().length() > 0) {
			
			String studentID = studentNbrField.getText();
			
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Säkerställande");
			alert.setHeaderText("Säkerställande");
			alert.setContentText("Är du säker på att du vill ta bort student " + studentNbrField.getText() + " på de valda kurserna?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				int numberOfRowsAffected = 0;
				for (StudiedEntry studiedChecked : studiedComboBox.getCheckModel().getCheckedItems()) {
					numberOfRowsAffected += DALStudent.deRegisterStudentFromStudiedCourse(studentID,
							studiedChecked.getCourseCode());
				}
				for (StudyingEntry studyingChecked : studyingComboBox.getCheckModel().getCheckedItems()) {
					numberOfRowsAffected += DALStudent.deRegisterStudentFromStudyingCourse(studentID,
							studyingChecked.getCourseCode());
				}
				String status = "";
				if (numberOfRowsAffected != 0)
					status = "OK, student avregistrerad/borttagen från " + numberOfRowsAffected + " kurser.";
				else if (numberOfRowsAffected == 0)
					status = "Ingen avregistrering är gjord";
				statusLabel.setText(status);
			}
		} else if (studentNbrField.getText() == null || studentNbrField.getText().length() == 0) {
			statusLabel.setText("Ange minst ett tecken i pnr");
		}
	}	
	
	@FXML
	private void handleClose() {
		dialogStage.close();
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public boolean isOkClicked() {
		return okClicked;
	}
	
}
