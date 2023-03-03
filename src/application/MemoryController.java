package application;

import java.io.IOException;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class MemoryController {
	
	private ObservableList<String> levelstems = FXCollections.observableArrayList("Facile", "Moyen", "Difficile");
	
	@FXML
	private ChoiceBox<String> choiceDifficult;
	
	@FXML
	private Button closeBtn;
	
	@FXML
	private Button playBtn;
	
	@FXML
	public void initialize() {
		choiceDifficult.setItems(levelstems);
		choiceDifficult.getSelectionModel().selectFirst();
	}
	
	@FXML
	public void closeGame(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Fermeture du jeu");
		alert.setHeaderText("Voulez vous vraiment fermer le jeu ?");
		alert.setContentText("Memory Game");

		Optional<ButtonType> option = alert.showAndWait();
		if (option.get() == ButtonType.OK) {
			Stage stage = (Stage) closeBtn.getScene().getWindow();
			stage.close();
		}
	}
	
	@FXML
	public void startGame(ActionEvent event) {
		((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
		try {
            Parent root;
            Stage rapport = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass()
                         .getResource("Game.fxml"));
            
            root = loader.load();
            rapport.setScene(new Scene(root));
            rapport.setTitle("Partie");
            rapport.getIcons().add(new Image("file:assets/logo.jpg"));
            rapport.show();
        } catch (IOException e) {
            System.out.println("Problème lors de" 
                             + " la création de l'interface");
        }
	}
}
