package app.view.uppgift1;

import java.io.IOException;

import app.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RootController {
	
	@FXML
	private void initialize() {
	}
	
	@FXML
	public void newCourseButton() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/uppgift1/CourseAddDialog.fxml"));
		AnchorPane page = null;
		try {
			page = (AnchorPane) loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Ny kurs");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(MainApp.getMainApp().getPrimaryStage());
		Scene scene = new Scene(page);
		dialogStage.setScene(scene);

		CourseAddDialogController controller = loader.getController();
		controller.setDialogStage(dialogStage);
		
		dialogStage.showAndWait();
	}
	
	@FXML
	public void newStudentButton() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/uppgift1/StudentAddDialog.fxml"));
		AnchorPane page = null;
		try {
			page = (AnchorPane) loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Ny student");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(MainApp.getMainApp().getPrimaryStage());
		Scene scene = new Scene(page);
		dialogStage.setScene(scene);

		StudentAddDialogController controller = loader.getController();
		controller.setDialogStage(dialogStage);
		
		dialogStage.showAndWait();
	
	}
	
	@FXML
	public void removeStudentButton() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/uppgift1/StudentDeleteDialog.fxml"));
		AnchorPane page = null;
		try {
			page = (AnchorPane) loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Ta bort student");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(MainApp.getMainApp().getPrimaryStage());
		Scene scene = new Scene(page);
		dialogStage.setScene(scene);

		StudentDeleteDialogController controller = loader.getController();
		controller.setDialogStage(dialogStage);
		
		dialogStage.showAndWait();
	}
	
	@FXML
	public void removeCourseButton() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/uppgift1/CourseDeleteDialog.fxml"));
		AnchorPane page = null;
		try {
			page = (AnchorPane) loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Ta bort kurs");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(MainApp.getMainApp().getPrimaryStage());
		Scene scene = new Scene(page);
		dialogStage.setScene(scene);

		CourseDeleteDialogController controller = loader.getController();
		controller.setDialogStage(dialogStage);
		
		dialogStage.showAndWait();
	}
	
	@FXML
	public void registerStudentOnCourseButton() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/uppgift1/RegisterStudentDialog.fxml"));
		AnchorPane page = null;
		try {
			page = (AnchorPane) loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Registrera kurser för student");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(MainApp.getMainApp().getPrimaryStage());
		Scene scene = new Scene(page);
		dialogStage.setScene(scene);

		RegisterStudentDialogController controller = loader.getController();
		controller.setDialogStage(dialogStage);
		
		dialogStage.showAndWait();
	
	}
	
	@FXML
	public void deregisterStudentOnCourseButton() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/uppgift1/DeregisterStudentCourse.fxml"));
		AnchorPane page = null;
		try {
			page = (AnchorPane) loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Avregistrera kurser för student");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(MainApp.getMainApp().getPrimaryStage());
		Scene scene = new Scene(page);
		dialogStage.setScene(scene);

		DeregisterStudentCourseController controller = loader.getController();
		controller.setDialogStage(dialogStage);
		
		dialogStage.showAndWait();
	}
}
