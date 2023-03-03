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

public class GameController {
	
	private ObservableList<Cartes> cards = FXCollections.observableArrayList(); // stocke les cartes
	private ObservableList<Cartes> choiceCards = FXCollections.observableArrayList(); // stocke les cartes choisies
	private List<ImageView> imgClicked = new ArrayList<>(); // stocke les images des cartes choisies
	private int nbPairs; // nombre de paire de la partie
	private int duration_game = 20; // valeur par defaut
	private int scorePartie = 0; // score au debut
	private Timeline timer;
	
	private final Image IMAGE_DOS = new Image("file:../../assets/cards.png"); // Image de dos des images
	
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
	private Button restart;
	
	/**
	 * Cette méthode permet de créer les cartes sur le plateau
	 * en affichant ses cartes retournés
	 */
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
		     new Cartes(4, "file:../../assets/carte4.png")};
		
		shuffle(cartes);
		cards.addAll(cartes);
		nbPairs = cards.size() / 2;
		score.setText("Score : 0");
		hub.setVisible(false);
		restart.setVisible(false);
		
		/* Initialise les images des cartes avec le dos de la carte */
		carte1.setImage(IMAGE_DOS);
		carte2.setImage(IMAGE_DOS);
		carte3.setImage(IMAGE_DOS);
		carte4.setImage(IMAGE_DOS);
		carte5.setImage(IMAGE_DOS);
		carte6.setImage(IMAGE_DOS);
		carte7.setImage(IMAGE_DOS);
		carte8.setImage(IMAGE_DOS);
		
		/* Ajoute un listener sur notre liste */
		choiceCards.addListener((ListChangeListener.Change<? extends Cartes> change) -> {
			if (choiceCards.size() == 2) {
				checkIfIsSameFamily(choiceCards.get(0), choiceCards.get(1));
			}
		});
	}
	
	/**
	 * Cette méthode permet de décrémenter le timer
	 */
	private void showDurationGame() {
		duration_game--;
		String resultat = duration_game < 10 ? "0" + duration_game : "" + duration_game;
		time.setText("00:" + resultat);
		if (duration_game == 0) {
			estGagnant();
		}
	}
	
	/**
	 * Permet de communiquer entre les controlers.
	 * De la page d'accueil à la page de partie
	 * @param level correspondant à la difficulté
	 */
	public void level(String level) {
		if (level.equals("Moyen")) {
			duration_game = 20;
		} else {
			duration_game = 40;
		}
		time.setText("00:" + duration_game);
		timer = new Timeline(new KeyFrame(Duration.millis(1000), ae -> showDurationGame()));
		timer.setCycleCount(duration_game);
		timer.play();
	}
	
	/**
	 * Méthode permettant d'ajouter la carte chosie dans notre liste
	 * @param event au clic de la carte
	 */
	@FXML
	public void choiceCard(MouseEvent event) {
		Main.soundClick();
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
	
	/**
	 * Cette méthode regarde si les 2 cartes sont de la meme famille
	 * @param c1 carte à comparer
	 * @param c2 carte à comparer
	 */
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
	
	/**
	 * Permet de savoir si le niveau est fini
	 */
	private void estGagnant() {
		if (nbPairs == 0 && duration_game > 0) {
			alert("OH !!!!!!!!!!!!!!!", "Ta gagné 1 M ... de canards en plastiques", "Au plaisir! Score : " + scorePartie);
			timer.stop();
			hub.setVisible(true);
			restart.setVisible(true);
		} else if(duration_game == 0) {
			alert("CHEH", "Vous avez perdu...C'est dur non ?", "Bon courage gros !");
			carte1.setVisible(false);
			carte2.setVisible(false);
			carte3.setVisible(false);
			carte4.setVisible(false);
			carte5.setVisible(false);
			carte6.setVisible(false);
			carte7.setVisible(false);
			carte8.setVisible(false);
			hub.setVisible(true);
			restart.setVisible(true);
		}
	}
	
	/**
	 * Méthode permettant de créer une pop up
	 * @param title
	 * @param header
	 * @param content
	 */
	private void alert(String title, String header, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
	}
	
	/**
	 * Methode permettant de retourner à la page d'accueil
	 * @param event
	 */
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
	
	/**
	 * Methode permettant de relancer la partie
	 * @param event
	 */
	@FXML
	public void restartGame(ActionEvent event) {
		Main.soundClick();
		((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
		try {
            Parent root;
            Stage rapport = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass()
                         .getResource("Game.fxml"));
            
            root = loader.load();
            GameController gameController = loader.getController();
            gameController.level("Facile");
            rapport.setScene(new Scene(root));
            rapport.setTitle("Partie");
            rapport.getIcons().add(new Image("file:assets/logo.jpg"));
            rapport.show();
        } catch (IOException e) {
            System.out.println("Problème lors de" 
                             + " la création de l'interface");
        }
	}
	
	/**
	 * Permet de mélanger un tableau aléatoirement
	 * @param array
	 */
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
