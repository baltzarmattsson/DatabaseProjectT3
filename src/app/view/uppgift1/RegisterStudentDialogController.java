package app.view.uppgift1;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;


import app.dal.uppgift1.DALCourse;
import app.dal.uppgift1.DALStudent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class RegisterStudentDialogController {

	@FXML
	private TextField pnbrField;
	@FXML
	private TextField codeField;
	@FXML
	private TextField gradeField;
	
	@FXML
	private Label statusLabel;

	@FXML
	private RadioButton studiedRdbtn;
	@FXML
	private RadioButton studyingRdbtn;
	
	private ToggleGroup rdbtnGroup = new ToggleGroup();
	
	private Stage dialogStage;
	private boolean okClicked = false;
	
	@FXML
	private void initialize() {

		studiedRdbtn.setToggleGroup(rdbtnGroup);
		studyingRdbtn.setToggleGroup(rdbtnGroup);

		rdbtnGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				RadioButton check = (RadioButton) newValue.getToggleGroup().getSelectedToggle();

				if (check.getText().equals(studyingRdbtn.getText())) {
					gradeField.setDisable(true);
				} else if (check.getText().equals(studiedRdbtn.getText())) {
					gradeField.setDisable(false);
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
	private void handleAddButton() {

		String pnbr = pnbrField.getText();
		String code = codeField.getText();
		int gradeParsed = 0;

		if (pnbr != null && pnbr.length() > 0 && code != null && code.length() > 0) {

			try {
				
				if (studyingRdbtn.isSelected()) {

					if (DALStudent.findCurrentlyReadingPoints(pnbr) + DALCourse.findCoursePoints(code) <= 45) {

						ArrayList<String> list = new ArrayList<String>();
						list.add(code);

						DALStudent.addStudyingRelation(pnbr, list);

						statusLabel.setText("Lyckad registrering!");
						
					} else {
						this.showAlert("�verstigit antal", "Antal po�ng l�sta denna termin plus vald kurs �r �ver gr�nsen (45)");
					}
					
				} 

				else if (studiedRdbtn.isSelected()) {
					
					String grade = gradeField.getText();
					
					if (grade != null && grade.length() > 0) {
						
						if (this.isInteger(grade)) {
							try {
								gradeParsed = Integer.parseInt(grade);
								
								if (gradeParsed > 5 || gradeParsed < 0) {
									statusLabel.setText("Ogiltigt tal, betyg m�ste vara mellan 0 och 5");
								} else {
									String response = "";
									if (DALStudent.isStudentStudyingOnCourse(pnbr, code)) {
										
										Alert alert = new Alert(AlertType.CONFIRMATION);
										alert.setTitle("Studenten l�ser kursen");
										alert.setHeaderText("");
										alert.setContentText(
												"Studenten l�ser just nu denna kurs. Tryck OK ifall du vill avregistrera studenten fr�n kursen samt registrera betyg "
														+ gradeParsed + ", "
														+ "eller Avbryt f�r att registrera betyget och beh�lla studenten som l�sande.");

										Optional<ButtonType> buttonClicked = alert.showAndWait();
										if (buttonClicked.get() == ButtonType.OK) {
											DALStudent.deRegisterStudentFromStudyingCourse(pnbr, code);
											response += ", avregistrerad som l�sande";
										} else {
											response += ", ingen avregistrering gjort";
										}
										
									} 
									if (DALStudent.hasStudentStudiedOnCourse(pnbr, code)) {
											Alert alert2 = new Alert(AlertType.CONFIRMATION);
											alert2.setTitle("V�rdet finns redan");
											alert2.setHeaderText("");
											alert2.setContentText(
													"Kombinationen personnummer och kurskod finns redan med ett betyg, "
													+ "vill du uppdatera betyget till " + gradeField.getText() + "?");

											Optional<ButtonType> buttonClickedUpdateGrade = alert2.showAndWait();
											if (buttonClickedUpdateGrade.get() == ButtonType.OK) {
												int numberOfRowsAffected = DALStudent.updateStudentGradeOnCourse(pnbr, code, gradeParsed);
												if (numberOfRowsAffected > 0)
													response += ", betyg uppdaterat till " + gradeParsed;
												else if (numberOfRowsAffected == 0)
													response += ", inget betyg uppdaterat";
											} else if (buttonClickedUpdateGrade.get() == ButtonType.CANCEL) {
												response += ", inget betyg uppdaterat";
											}
									} else {
										DALStudent.addStudiedRelation(pnbr, code, gradeParsed);
										statusLabel.setText("Lyckad registrering p� kurs" + response);
									}
									statusLabel.setText("Status" + response);
								}
							} catch (NumberFormatException e) {
								statusLabel.setText("Ogiltigt tal, betyg m�ste vara mellan 0 och 5");
							}
						}
					} else if (grade == null || grade.length() == 0) {
						statusLabel.setText("F�ltet betyg kan inte vara tomt. V�nligen ange en siffra");
					}
				}

			} catch (SQLException e) {

				// FOREIGN KEY CONSTRAINT 1452 = Mysql, 547 = MSSQL (foreign key)
				if (e.getErrorCode() == 1452 || e.getErrorCode() == 547) { 
										
					this.showAlert("Hittade ej kurs/student", "Kunde inte hitta kurs och/eller student, v�nligen f�rs�k igen");

					// PRIMARY KEY CONSTRAINT 1062 = MySQL, 2627 = MSSQL (primary key)
				} else if (e.getErrorCode() == 1062 || e.getErrorCode() == 2627) {
					
					if (studiedRdbtn.isSelected()) {

						Alert alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("V�rdet finns redan");
						alert.setHeaderText("");
						alert.setContentText(
								"Kombinationen personnummer och kurskod finns redan med ett betyg, "
								+ "vill du uppdatera betyget till " + gradeField.getText() + "?");

						Optional<ButtonType> result = alert.showAndWait();
						if (result.get() == ButtonType.OK) {
							int numberOfRowsAffected = DALStudent.updateStudentGradeOnCourse(pnbr, code, gradeParsed);
							String status = "";
							if (numberOfRowsAffected > 0)
								status = "OK, betyg uppdaterat till " + gradeParsed;
							else if (numberOfRowsAffected == 0)
								status = "Inget betyg uppdaterat";
							statusLabel.setText(status);

						}
					} else if (studyingRdbtn.isSelected()) {
						this.showAlert("Kombinationen finns redan", "Studenten �r redan registrerad p� denna kurs");
					}
					
					// Data too long 1406 = MySQL (too long), 8152 = MsSQL (truncated)
				} else if (e.getErrorCode() == 1406 || e.getErrorCode() == 8152) {
					
					this.showAlert("V�rde �r f�r l�ngt", "Ett v�rde �r f�r l�ngt, v�nligen minska det");
					
				
				} else {
					System.out.println(e.getMessage());
					System.out.println(e.getErrorCode());
					e.printStackTrace();
				}
			}

		} else { 
			statusLabel.setText(("V�nligen ange minst ett tecken i pnr och kurskod"));
		}
	}

	@FXML
	private void handleCloseButton() {
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
			statusLabel.setText("Betyg-text �r ej numerisk, eller �r negativt");
			return false;
		}
		return true;
	}
	
	private void showAlert(String header, String contentText) {
		Alert a = new Alert(AlertType.ERROR);
		a.initOwner(dialogStage);
		a.setTitle("Misslyckad uppdatering");
		a.setHeaderText(header);
		a.setContentText(contentText);
		a.showAndWait();
	}
}
