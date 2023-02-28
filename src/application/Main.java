package application;
	
import java.io.File;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	@Override
	public void init() throws Exception {
		System.out.println("[Lancement du jeu...]");
		super.init();
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			String path = "sounds/accueil.mp3";
			Media media = new Media(new File(path).toURI().toString());
			MediaPlayer media_player = new MediaPlayer(media);
			
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("Memory.fxml"));
			Scene scene = new Scene(root,800,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Memory Game");
			primaryStage.show();
			primaryStage.getIcons().add(new Image("file:assets/logo.jpg"));
			primaryStage.setResizable(false);
			
			
			media_player.setCycleCount(MediaPlayer.INDEFINITE);
			media_player.play();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void stop() throws Exception {
		System.out.println("[Fermeture du jeu...]");
		super.stop();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
