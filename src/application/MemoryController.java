package application;

import java.io.File;
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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class MemoryController {
	
	private ObservableList<String> levelsItems = FXCollections.observableArrayList("Facile", "Moyen", "Difficile");
	
	@FXML
	private ChoiceBox<String> choiceDifficult;
	
	@FXML
	private Button closeBtn;
	
	@FXML
	private Button playBtn;
	
	@FXML
	public void initialize() {
		choiceDifficult.setItems(levelsItems);
		choiceDifficult.getSelectionModel().selectFirst();
	}
	
	@FXML
	public void closeGame(ActionEvent event) {
		Main.soundClick();
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
		Main.soundClick();
		((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
		if (choiceDifficult.getValue() == "Difficile") {
			try {
	            Parent root;
	            Stage niveauHard = new Stage();
	            FXMLLoader loader = new FXMLLoader(getClass()
	                         .getResource("GameHard.fxml"));
	            
	            root = loader.load();
	            niveauHard.setScene(new Scene(root));
	            niveauHard.setTitle("Partie");
	            niveauHard.getIcons().add(new Image("file:assets/logo.jpg"));
	            niveauHard.show();
	            niveauHard.setResizable(false);
	        } catch (IOException e) {
	            System.out.println("Problème lors de" 
	                             + " la création de l'interface");
	        }
		} else {
			try {
	            Parent root;
	            Stage niveau = new Stage();
	            FXMLLoader loader = new FXMLLoader(getClass()
	                         .getResource("Game.fxml"));
	            
	            root = loader.load();
	            GameController gameController = loader.getController();
	            gameController.level(choiceDifficult.getValue());
	            niveau.setScene(new Scene(root));
	            niveau.setTitle("Partie");
	            niveau.getIcons().add(new Image("file:assets/logo.jpg"));
	            niveau.show();
	            niveau.setResizable(false);
	        } catch (IOException e) {
	            System.out.println("Problème lors de" 
	                             + " la création de l'interface");
	        }
		}
	}
}
