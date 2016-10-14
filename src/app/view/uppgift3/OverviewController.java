package app.view.uppgift3;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class OverviewController {
	
	private final String DIRECTORY = "\\F:\\WindowsInstalls\\workspace\\KursAdmin\\excelaccessfiles\\";
	private final String ACCESS_FILE_PATH = DIRECTORY + "cronusaccess.accdb";
	
	@FXML
	private ComboBox<String> excelComboBox;
	@FXML
	private ComboBox<String> allCustomersComboBox;
	@FXML
	private ComboBox<String> allEmployeesComboBox;
	
	@FXML
	private void initialize() {
		excelComboBox.getItems().addAll(
				"1NOK", "2MestForSEK", "3FotografernaAB", "4Sjuk", "5Slaktingar", "6Andreas", "7Bankkonto");
		allCustomersComboBox.getItems().addAll(
				"Word", "Excel", "Access");
		allEmployeesComboBox.getItems().addAll(
				"Word", "Excel", "Access");
	}
	
	@FXML
	private void handleOpenExcel() {
		String chosenFileName = "";
		if (excelComboBox.getSelectionModel().getSelectedItem() != null) {

			chosenFileName = excelComboBox.getSelectionModel().getSelectedItem();
			File file = new File(DIRECTORY + chosenFileName + ".xlsx");
			try {
				Desktop.getDesktop().open(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@FXML
	private void handleOpenAccess() {
		try {
			Desktop.getDesktop().open(new File(ACCESS_FILE_PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void handleOpenCustomers() {
		String chosenApplication = "";
		if (allCustomersComboBox.getSelectionModel().getSelectedItem() != null) {

			chosenApplication = allCustomersComboBox.getSelectionModel().getSelectedItem();

			File file = null;

			switch (chosenApplication) {
			case "Excel":
				file = new File(DIRECTORY + "Report of All customers.xls");
				break;
			case "Word":
				file = new File(DIRECTORY + "Report of All customers.docx");
				break;
			case "Access":
				file = new File(ACCESS_FILE_PATH);
				break;
			}
			try {
				Desktop.getDesktop().open(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@FXML
	private void handleOpenEmployees() {
		
		String chosenApplication = "";
		if (allEmployeesComboBox.getSelectionModel().getSelectedItem() != null) {

			chosenApplication = allEmployeesComboBox.getSelectionModel().getSelectedItem();

			File file = null;

			switch (chosenApplication) {
			case "Excel":
				file = new File(DIRECTORY + "Report of All employees.xls");
				break;
			case "Word":
				file = new File(DIRECTORY + "Report of All employees.docx");
				break;
			case "Access":
				file = new File(ACCESS_FILE_PATH);
				break;
			}
			try {
				Desktop.getDesktop().open(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
