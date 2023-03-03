package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import application.classes.Cartes;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameHardController {
	
	private ObservableList<Cartes> cards = FXCollections.observableArrayList();
	private ObservableList<Cartes> choiceCards = FXCollections.observableArrayList();
	private List<ImageView> imgClicked = new ArrayList<>();
	private int nbPairs;
	private int duration_game = 15; // valeur par defaut
	private int scorePartie = 0;
	private Timeline timer;
	
	private final Image IMAGE_DOS = new Image("file:../../assets/cards.png");
	
	@FXML
	private ImageView carte1;
	
	@FXML
	private ImageView carte2;
	
	@FXML
	private ImageView carte3;
	
	@FXML
	private ImageView carte4;
	
	@FXML
	private ImageView carte5;
	
	@FXML
	private ImageView carte6;
	
	@FXML
	private ImageView carte7;
	
	@FXML
	private ImageView carte8;
	
	@FXML
	private ImageView carte9;
	
	@FXML
	private ImageView carte10;
	
	@FXML
	private ImageView carte11;
	
	@FXML
	private ImageView carte12;
	
	@FXML
	private Label time;
	
	@FXML
	private Label score;
	
	@FXML
	private Button hub;
	
	@FXML
	private Button restart;
	
	@FXML
	public void initialize() {
		Cartes cartes[] = 
			{new Cartes(1, "file:../../assets/carte1.png"), 
		     new Cartes(1, "file:../../assets/carte1.png"), 
		     new Cartes(2, "file:../../assets/carte2.png"), 
		     new Cartes(2, "file:../../assets/carte2.png"), 
		     new Cartes(3, "file:../../assets/carte3.png"), 
		     new Cartes(3, "file:../../assets/carte3.png"), 
		     new Cartes(4, "file:../../assets/carte4.png"), 
		     new Cartes(4, "file:../../assets/carte4.png"),
		     new Cartes(5, "file:../../assets/carte5.png"), 
		     new Cartes(5, "file:../../assets/carte5.png"), 
		     new Cartes(6, "file:../../assets/carte6.png"), 
		     new Cartes(6, "file:../../assets/carte6.png")};
		
		shuffle(cartes);
		cards.addAll(cartes);
		nbPairs = cards.size() / 2;
		score.setText("Score : 0");
		hub.setVisible(false);
		restart.setVisible(false);
		
		carte1.setImage(IMAGE_DOS);
		carte2.setImage(IMAGE_DOS);
		carte3.setImage(IMAGE_DOS);
		carte4.setImage(IMAGE_DOS);
		carte5.setImage(IMAGE_DOS);
		carte6.setImage(IMAGE_DOS);
		carte7.setImage(IMAGE_DOS);
		carte8.setImage(IMAGE_DOS);
		carte9.setImage(IMAGE_DOS);
		carte10.setImage(IMAGE_DOS);
		carte11.setImage(IMAGE_DOS);
		carte12.setImage(IMAGE_DOS);
		
		time.setText("00:" + duration_game);
		timer = new Timeline(new KeyFrame(Duration.millis(1000), ae -> showDurationGame()));
		timer.setCycleCount(duration_game);
		timer.play();
		
		choiceCards.addListener((ListChangeListener.Change<? extends Cartes> change) -> {
			if (choiceCards.size() == 2) {
				checkIfIsSameFamily(choiceCards.get(0), choiceCards.get(1));
			}
		});
	}
	
	private void showDurationGame() {
		duration_game--;
		String resultat = duration_game < 10 ? "0" + duration_game : "" + duration_game;
		time.setText("00:" + resultat);
		if (duration_game == 0) {
			estGagnant();
		}
	}
	
	@FXML
	public void choiceCard(MouseEvent event) {
		Main.soundClick();
		if (choiceCards.size() == 2) choiceCards.clear();
		Node source = (Node) event.getSource();
		String id = source.getId();
		String carteId = "" + id.charAt(id.length() - 2) + id.charAt(id.length() - 1);
		if (carteId.matches("e\\d")) {
			carteId = "" + carteId.charAt(carteId.length() - 1);
		}
		int index = Integer.parseInt(carteId);
		if (!(choiceCards.contains(cards.get(index - 1)))) {
			ImageView imgV = (ImageView) event.getSource();
			imgClicked.add(imgV);
			Image img = new Image((cards.get(index - 1)).getImage());
			imgV.setImage(img);
			choiceCards.add(cards.get(index - 1));
		}
	}
	
	private void checkIfIsSameFamily(Cartes c1, Cartes c2) {
		if (c1.getId() == c2.getId()) {
			imgClicked.get(0).setVisible(false);
			imgClicked.get(1).setVisible(false);
			nbPairs--;
			scorePartie += 20;
			score.setText("Score : " + scorePartie);
			estGagnant();
		} else {
			final ImageView im1 = imgClicked.get(0);
			final ImageView im2 = imgClicked.get(1);
			Timeline timer = new Timeline(new KeyFrame(Duration.millis(500), ae -> {
				im1.setImage(IMAGE_DOS);
				im2.setImage(IMAGE_DOS);
				Main.soundCartes();
			}));
			timer.setCycleCount(1);
			timer.play();
			scorePartie -= 5;
			score.setText("Score : " + scorePartie);
		}
		imgClicked.clear();
	}
	
	private void estGagnant() {
		if (nbPairs == 0 && duration_game > 0) {
			alert("OH !!!!!!!!!!!!!!!", "Ta gagné 1 M ... de canards en plastiques", "Au plaisir! Score : " + scorePartie);
			hub.setVisible(true);
			restart.setVisible(true);
			timer.stop();
		} else if(duration_game == 0) {
			alert("CHEH", "Vous avez perdu...C'est dur non ?", "Bon courage gros !");
			hub.setVisible(true);
			restart.setVisible(true);
			carte1.setVisible(false);
			carte2.setVisible(false);
			carte3.setVisible(false);
			carte4.setVisible(false);
			carte5.setVisible(false);
			carte6.setVisible(false);
			carte7.setVisible(false);
			carte8.setVisible(false);
			carte9.setVisible(false);
			carte10.setVisible(false);
			carte11.setVisible(false);
			carte12.setVisible(false);
		}
	}
	
	private void alert(String title, String header, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
	}
	
	@FXML
	public void goToHub(ActionEvent event) {
		Main.soundClick();
		((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
		try {
            Parent root;
            Stage rapport = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass()
                         .getResource("Memory.fxml"));
            
            root = loader.load();
            rapport.setScene(new Scene(root));
            rapport.setTitle("Memory Game");
            rapport.getIcons().add(new Image("file:assets/logo.jpg"));
            rapport.show();
        } catch (IOException e) {
            System.out.println("Problème lors de" 
                             + " la création de l'interface");
        }
	}
	
	@FXML
	public void restartGame(ActionEvent event) {
		Main.soundClick();
		((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
		try {
            Parent root;
            Stage rapport = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass()
                         .getResource("GameHard.fxml"));
            
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
	
	private static void shuffle(Cartes[] array) {
	    Random random = new Random();
	    for (int i = array.length - 1; i > 0; i--) {
	        int index = random.nextInt(i + 1);
	        Cartes temp = array[index];
	        array[index] = array[i];
	        array[i] = temp;
	    }
	}

}
