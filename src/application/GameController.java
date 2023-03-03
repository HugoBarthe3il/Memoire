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
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameController {
	
	private ObservableList<Cartes> cards = FXCollections.observableArrayList();
	private ObservableList<Cartes> choiceCards = FXCollections.observableArrayList();
	private List<ImageView> imgClicked = new ArrayList<>();
	private int nbPairs;
	private int duration_game = 20;
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
	private Label time;
	
	@FXML
	private Label score;
	
	@FXML
	private Button hub;
	
	@FXML
	public void initialize() {
		Cartes cartes[] = 
			{new Cartes(1, "file:../../assets/cartesReleves.jpg"), 
		     new Cartes(1, "file:../../assets/cartesReleves.jpg"), 
		     new Cartes(2, "file:../../assets/logo.jpg"), 
		     new Cartes(2, "file:../../assets/logo.jpg"), 
		     new Cartes(3, "file:../../assets/memoryGame.jpg"), 
		     new Cartes(3, "file:../../assets/memoryGame.jpg"), 
		     new Cartes(4, "file:../../assets/customCards.png"), 
		     new Cartes(4, "file:../../assets/customCards.png")};
		
		shuffle(cartes);
		cards.addAll(cartes);
		nbPairs = cards.size() / 2;
		time.setText("00:" + duration_game);
		score.setText("Score : 0");
		hub.setVisible(false);
		
		carte1.setImage(IMAGE_DOS);
		carte2.setImage(IMAGE_DOS);
		carte3.setImage(IMAGE_DOS);
		carte4.setImage(IMAGE_DOS);
		carte5.setImage(IMAGE_DOS);
		carte6.setImage(IMAGE_DOS);
		carte7.setImage(IMAGE_DOS);
		carte8.setImage(IMAGE_DOS);
		
		choiceCards.addListener((ListChangeListener.Change<? extends Cartes> change) -> {
			if (choiceCards.size() == 2) {
				checkIfIsSameFamily(choiceCards.get(0), choiceCards.get(1));
			}
		});
		
		timer = new Timeline(new KeyFrame(Duration.millis(1000), ae -> showDurationGame()));
		timer.setCycleCount(20);
		timer.play();
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
		if (choiceCards.size() == 2) choiceCards.clear();
		Node source = (Node) event.getSource();
		String id = source.getId();
		int index = Character.getNumericValue(id.charAt(id.length()-1));
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
			imgClicked.get(0).setImage(IMAGE_DOS);
			imgClicked.get(1).setImage(IMAGE_DOS);
			scorePartie -= 5;
			score.setText("Score : " + scorePartie);
		}
		imgClicked.clear();
	}
	
	private void estGagnant() {
		if (nbPairs == 0 && duration_game > 0) {
			alert("OH !!!!!!!!!!!!!!!", "Ta gagné 1 M ... de canards en plastiques", "Au plaisir! Score : " + scorePartie);
			hub.setVisible(true);
			timer.stop();
		} else if(duration_game == 0) {
			alert("CHEH", "Vous avez perdu...C'est dur non ?", "Bon courage gros !");
			hub.setVisible(true);
			carte1.setVisible(false);
			carte2.setVisible(false);
			carte3.setVisible(false);
			carte4.setVisible(false);
			carte5.setVisible(false);
			carte6.setVisible(false);
			carte7.setVisible(false);
			carte8.setVisible(false);
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
