package application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import application.classes.Cartes;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class GameController {
	
	private ObservableList<Cartes> cards = FXCollections.observableArrayList();
	private ObservableList<Cartes> followCards = FXCollections.observableArrayList();
	private ObservableList<Cartes> choiceCards = FXCollections.observableArrayList();
	private List<ImageView> imgClicked = new ArrayList<>();
	
	private final Image IMAGE_DOS = new Image("C:\\3il\\Java\\workspace\\MemoryGame\\assets\\cards.png");
	
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
	public void initialize() {
		Cartes cartes[] = 
			{new Cartes(1, "C:\\3il\\Java\\workspace\\MemoryGame\\assets\\cartesReleves.jpg"), 
		     new Cartes(1, "C:\\3il\\Java\\workspace\\MemoryGame\\assets\\cartesReleves.jpg"), 
		     new Cartes(2, "C:\\3il\\Java\\workspace\\MemoryGame\\assets\\logo.jpg"), 
		     new Cartes(2, "C:\\3il\\Java\\workspace\\MemoryGame\\assets\\logo.jpg"), 
		     new Cartes(3, "C:\\3il\\Java\\workspace\\MemoryGame\\assets\\memoryGame.jpg"), 
		     new Cartes(3, "C:\\3il\\Java\\workspace\\MemoryGame\\assets\\memoryGame.jpg"), 
		     new Cartes(4, "C:\\3il\\Java\\workspace\\MemoryGame\\assets\\customCards.png"), 
		     new Cartes(4, "C:\\3il\\Java\\workspace\\MemoryGame\\assets\\customCards.png")};
		
		shuffle(cartes);
		cards.addAll(cartes);
		followCards.addAll(cartes);
		
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
	}
	
	@FXML
	public void choiceCard(MouseEvent event) {
		if (choiceCards.size() == 2) choiceCards.clear();
		ImageView imgV = (ImageView) event.getSource();
		imgClicked.add(imgV);
		Node source = (Node) event.getSource();
		String id = source.getId();
		int index = Character.getNumericValue(id.charAt(id.length()-1));
		if (!(choiceCards.contains(cards.get(index - 1)))) {
			choiceCards.add(cards.get(index - 1));
			Image img = new Image((cards.get(index - 1)).getImage());
			imgV.setImage(img);
		}
	}
	
	private void checkIfIsSameFamily(Cartes c1, Cartes c2) {
		if (c1.getId() == c2.getId()) {
			imgClicked.get(0).setVisible(false);
			imgClicked.get(1).setVisible(false);
			followCards.remove(c1);
			followCards.remove(c2);
			estGagnant();
		} else {
			imgClicked.get(0).setImage(IMAGE_DOS);
			imgClicked.get(1).setImage(IMAGE_DOS);
		}
		imgClicked.clear();
	}
	
	private void estGagnant() {
		if (followCards.size() == 0) {
			System.out.println("vous avez gagné");
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
