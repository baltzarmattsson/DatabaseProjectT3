package app.view.uppgift1;

import java.sql.SQLException;
import java.util.Optional;

import app.dal.uppgift1.DALCourse;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CourseDeleteDialogController {

	@FXML
	private TextField codeField;
	
	@FXML
	private Label statusLabel;
	
	private Stage dialogStage;
	private boolean okClicked = false;
	
	@FXML
	private void initialize() {
	}
	
	@FXML
	private void handleOK() {
	
		if (codeField.getText() != null && codeField.getText().length() > 0) {
			
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Säkerställande");
			alert.setHeaderText("Säkerställande");
			alert.setContentText("Är du säker på att du vill ta bort kurs " + codeField.getText() + "?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				try {
					int numberOfCoursesAffecetd = DALCourse.deleteCourse(codeField.getText());
					String status = "";
					if (numberOfCoursesAffecetd != 0) status = "OK, " + numberOfCoursesAffecetd + " kurs borttagen.";			
					else if (numberOfCoursesAffecetd == 0) status = "Ingen kurs borttagen";
					statusLabel.setText(status);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else if (codeField.getText() == null || codeField.getText().length() == 0) {
			statusLabel.setText("Vänligen ange minst ett tecken för kurskod");
		}
	}	
	
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public boolean isOkClicked() {
		return okClicked;
	}
}
