package app.view.uppgift1;

import java.sql.SQLException;
import java.util.Optional;

import app.dal.uppgift1.DALStudent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class StudentDeleteDialogController {

	@FXML
	private TextField studentNbrField;
	
	@FXML
	private Label statusLabel;
	
	private Stage dialogStage;
	private boolean okClicked = false;
	
	@FXML
	private void initialize() {
	}
	
	@FXML
	private void handleOK() {
	
		if (studentNbrField.getText() != null && studentNbrField.getText().length() > 0) {
			
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Säkerställande");
			alert.setHeaderText("Säkerställande");
			alert.setContentText("Är du säker på att du vill ta bort student " + studentNbrField.getText() + "?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				try {
					int numberOfRowsAffected = DALStudent.deleteStudent(studentNbrField.getText());
					String status = "";
					if (numberOfRowsAffected > 0) status = "OK, " + numberOfRowsAffected + " student borttagen.";			
					else if (numberOfRowsAffected == 0) status = "Ingen student borttagen";
					statusLabel.setText(status);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else if (studentNbrField.getText() == null || studentNbrField.getText().length() == 0) {
			statusLabel.setText("Vänligen ange minst ett tecken för pnr");
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
