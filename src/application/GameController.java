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

public class GameController {
	
	private ObservableList<Cartes> cards = FXCollections.observableArrayList();
	private ObservableList<Cartes> followCards = FXCollections.observableArrayList();
	private ObservableList<Cartes> choiceCards = FXCollections.observableArrayList();
	private List<Button> btnClicked = new ArrayList<>();
	
	@FXML
	private Button carte1;
	
	@FXML
	private Button carte2;
	
	@FXML
	private Button carte3;
	
	@FXML
	private Button carte4;
	
	@FXML
	private Button carte5;
	
	@FXML
	private Button carte6;
	
	@FXML
	private Button carte7;
	
	@FXML
	private Button carte8;
	
	@FXML
	public void initialize() {
		Cartes cartes[] = {new Cartes(1), new Cartes(1), new Cartes(2), new Cartes(2), new Cartes(3), new Cartes(3), new Cartes(4), new Cartes(4)};
		shuffle(cartes);
		cards.addAll(cartes);
		followCards.addAll(cartes);
		choiceCards.addListener((ListChangeListener.Change<? extends Cartes> change) -> {
			if (choiceCards.size() == 2) {
				checkIfIsSameFamily(choiceCards.get(0), choiceCards.get(1));
			}
		});
	}
	
	@FXML
	public void choiceCard(ActionEvent event) {
		if (choiceCards.size() == 2) choiceCards.clear();
		Button btn = (Button) event.getSource();
		btnClicked.add(btn);
		Node source = (Node) event.getSource();
		String id = source.getId();
		int index = Character.getNumericValue(id.charAt(id.length()-1));
		choiceCards.add(cards.get(index - 1));
	}
	
	private void checkIfIsSameFamily(Cartes c1, Cartes c2) {
		if (c1.getId() == c2.getId()) {
			btnClicked.get(0).setDisable(true);
			btnClicked.get(1).setDisable(true);
			followCards.remove(c1);
			followCards.remove(c2);
			estGagnant();
		}
		btnClicked.clear();
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
