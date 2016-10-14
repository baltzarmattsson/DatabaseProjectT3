package app;

import java.io.IOException;
import app.view.SuperRootController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage primaryStage;
    private AnchorPane rootLayout;
    
    private SuperRootController mainController;
    
    public MainApp() {
    }
    
    private static MainApp mainApp;
    
    public static MainApp getMainApp() {
    	return mainApp;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Uppgift 11 (Kursadmin, Cronus)");
        
        initRootLayout();
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/SuperRoot.fxml"));
            rootLayout = (AnchorPane) loader.load();

            mainController = loader.getController();
            mainApp = this;

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    public SuperRootController getRootController() {
    	return this.mainController;
    }
       
}