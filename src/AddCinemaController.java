import java.io.IOException;
import java.util.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.*;

public class AddCinemaController {
	private FilmFestival ff;
	private List<Cinema> cinemaList;
	
	@FXML private TextField cinemaName;
	@FXML private Label explainLabel;
	@FXML private GridPane distanceGrid;
	private Label otherCinema[];
	private TextField distance[];
	
	public AddCinemaController(FilmFestival ff) {
		this.ff = ff;
		cinemaList = ff.getCinemas();
	}
	
	private void initGrid() {
		if(cinemaList.size() == 0) {
			explainLabel.setVisible(false);
			return;
		}
		otherCinema = new Label[cinemaList.size()];
		distance = new TextField[cinemaList.size()];
		for(int i = 0; i < cinemaList.size(); i++) {
			otherCinema[i] = new Label(cinemaList.get(i).getName());
			distance[i] = new TextField("0");
			distance[i].setPrefWidth(40.0);
			HBox left = new HBox();
			left.getChildren().add(otherCinema[i]);
			left.setAlignment(Pos.CENTER);
			HBox right = new HBox();
			right.getChildren().addAll(distance[i], new Label("minuti"));
			right.setAlignment(Pos.CENTER);
			distanceGrid.addRow(i, left, right);
		}
	}
	
	@FXML
	private void handleCancel() {
		System.out.println("AddCinema->Cancel");
		
		((Stage) cinemaName.getScene().getWindow()).close();
	}
	
	@FXML
	private void handleConfirm() {
		System.out.println("AddCinema->Confirm");
		
		Cinema c = makeCinema();
		if(c == null) return;
        ff.addCinema(c);
        
		((Stage) cinemaName.getScene().getWindow()).close();
	}
	
	private Cinema makeCinema() {
        if(cinemaName.getText() == "") {
        	System.out.println("Devi scrivere qualcosa!");
        	return null;
        }
        Cinema c = new Cinema(cinemaName.getText());
        try {
        	for(int i = 0; i < cinemaList.size(); i++) {
        		Cinema other = cinemaList.get(i);
        		int dist = Integer.parseInt(distance[i].getText());
        		c.setDistance(other, dist);
        		other.setDistance(c, dist);
        	}
        } catch(NumberFormatException e) {
        	System.err.println("Inserisci un numero");
        	return null;
        }
		return c;
	}
	
	public static void display(String title, FilmFestival ff) {
        AddCinemaController controller = new AddCinemaController(ff);
        try {
        FXMLLoader loader = new FXMLLoader(controller.getClass().getResource("AddCinemaView.fxml"));
        loader.setController(controller);
        Stage window = new Stage();
        window.setScene(new Scene(loader.load()));
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setOnCloseRequest((WindowEvent event) -> {controller.handleCancel();});

        controller.initGrid();
        
        window.showAndWait();
        } catch(IOException e) {e.printStackTrace();}
	}

}
