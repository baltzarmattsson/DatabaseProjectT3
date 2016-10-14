package app.view.uppgift1;

import java.sql.SQLException;

import app.dal.uppgift1.DALCourse;
import app.model.uppgift1.Course;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CourseAddDialogController {

	@FXML
	private TextField codeField;
	@FXML
	private TextField nameField;
	@FXML
	private TextField pointsField;
	
	@FXML
	private Label statusLabel;

	private Stage dialogStage;
	private boolean okClicked = false;

	@FXML
	private void initialize() {

	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleOK() {
		if (codeField.getText() != null && codeField.getText().length() > 0 
				&& pointsField.getText().length() > 0 && this.isInteger(pointsField.getText())) {
			
			try {
				Integer.parseInt(pointsField.getText());
			} catch (NumberFormatException e) {
				this.showAlert("Poäng är för stort", "Välj ett värde mellan 0 - " + Integer.MAX_VALUE);
				return;
			}
			
			Course c = new Course(codeField.getText(), nameField.getText(), Integer.parseInt(pointsField.getText()));
			try {
				DALCourse.addCourse(c);
				
				statusLabel.setText("Kurs tillagd!");
			} catch (SQLException e) {

				// PRIMARY KEY CONSTRAINT 1062 = MySQL, 2627 = MSSQL (primary key)
				if (e.getErrorCode() == 1062 || e.getErrorCode() == 2627) {

					this.showAlert("Värdet finns redan (ID: " + e.getErrorCode() + ")", "Kurskoden finns redan");

				// Data too long 1406 = MySQL (too long), 8152 = MsSQL (truncated)
				} else if (e.getErrorCode() == 1406 || e.getErrorCode() == 8152) {

					this.showAlert("Värde är för långt (ID: " + e.getErrorCode() + ")",
							"Ett värde är för långt, vänligen minska det");

				} else {
					e.printStackTrace();
				}
			}
		} else if (codeField.getText() == null || codeField.getText().length() == 0 ) {
			statusLabel.setText("Fältet kurskod kan inte vara tomt. Vänligen ange minst ett tecken");
		} else if (pointsField.getText() == null || pointsField.getText().length() == 0) {
			statusLabel.setText("Fältet kurspoäng kan inte vara tomt. Vänligen ange en siffra");
		}
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	private boolean isInteger(String toBeParsed) {
		char[] ca = toBeParsed.toCharArray();
		boolean isOk = true;
		for (char c : ca) {
			if (!Character.isDigit(c)) {
				isOk = false;
				break;
			}
		}
		if (!isOk) {
			Alert a = new Alert(AlertType.ERROR);
			a.initOwner(dialogStage);
			a.setTitle("Fel: Poäng är ej numerisk");
			a.setHeaderText("Poängtext är ej numerisk");
			a.setContentText("Poängtext är ej numerisk");

			a.showAndWait();
			return false;
		}
		return true;
	}
	
	private void showAlert(String header, String contentText) {
		Alert a = new Alert(AlertType.ERROR);
		a.initOwner(dialogStage);
		a.setTitle("Misslyckad inläggning");
		a.setHeaderText(header);
		a.setContentText(contentText);
		a.showAndWait();
	}

}
