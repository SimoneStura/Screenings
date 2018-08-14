import java.io.IOException;
import java.util.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.*;

public class AllCinemaController {
	private FilmFestival ff;
	private List<Cinema> cinemaList;
	private static final int buttonsInARow = 3;
	
	@FXML private Label explainLabel;
	@FXML private GridPane distanceGrid;
	@FXML private VBox cinemaBox;
	private Button cinemaModify[];
	
	public AllCinemaController(FilmFestival ff) {
		this.ff = ff;
		cinemaList = ff.getCinemas();
	}
	
	private void initGrids() {
		if(cinemaList.size() == 0) {
			explainLabel.setVisible(false);
			return;
		}
		if(cinemaList.size() > 3) {
			((Stage) explainLabel.getScene().getWindow()).setWidth(600);
			((Stage) explainLabel.getScene().getWindow()).setHeight(450);
		}
		if(cinemaList.size() > 9) {
			((Stage) explainLabel.getScene().getWindow()).setWidth(800);
			((Stage) explainLabel.getScene().getWindow()).setHeight(600);
		}
		cinemaModify = new Button[cinemaList.size()];
		for(int i = 0; i < cinemaList.size(); i++) {
			Cinema c = cinemaList.get(i);
			addCinemaButton(i, c);
			if(i > 0 || cinemaList.size() > 1)
				addGridRow(i, c);
		}
	}
	
	private void addCinemaButton(int index, Cinema c) {
		HBox hb = new HBox(20);
		if(index % buttonsInARow == 0) {
			hb.setAlignment(Pos.CENTER);
			cinemaBox.getChildren().add(hb);
		} else
			hb = (HBox) cinemaBox.getChildren().get(cinemaBox.getChildren().size() - 1);
		cinemaModify[index] = new Button(c.getName());
		cinemaModify[index].setMinWidth(70.);
		hb.getChildren().add(cinemaModify[index]);
	}
	
	private void addGridRow(int rowIndex, Cinema c) {
		if(rowIndex == 0) {
			distanceGrid.add(new HBox(), rowIndex, 0);
			int columnIndex = 1;
			for(Cinema other : cinemaList.subList(1, cinemaList.size())) {
				HBox hb = new HBox(new Label(other.getName()));
				hb.setAlignment(Pos.CENTER);
				distanceGrid.add(hb, rowIndex, columnIndex++);
			}
		}
		if(rowIndex == cinemaList.size() - 1)
			return;
		HBox firstColumn = new HBox(new Label(c.getName()));
		firstColumn.setAlignment(Pos.CENTER);
		distanceGrid.add(firstColumn, rowIndex+1, 0);
		int columnIndex = 1;
		for(Cinema other : cinemaList.subList(1, cinemaList.size())) {
			HBox hb = new HBox(new Label(Integer.toString(c.getDistance(other))));
			hb.setAlignment(Pos.CENTER);
			hb.setPadding(new Insets(5.0));
			distanceGrid.add(hb, rowIndex+1, columnIndex++);
		}
	}
	
	@FXML
	private void handleCancel() {
		System.out.println("AllCinema->Cancel");
		
		((Stage) explainLabel.getScene().getWindow()).close();
	}
	
	@FXML
	private void handleAddCinema() {
		System.out.println("AllCinema->AddCinema");
        
		AddCinemaController.display("Nuovo Cinema", ff);
		AllCinemaController.display(ff);
		((Stage) explainLabel.getScene().getWindow()).close();
	}
	
	public static void display(FilmFestival ff) {
        AllCinemaController controller = new AllCinemaController(ff);
        try {
        FXMLLoader loader = new FXMLLoader(controller.getClass().getResource("AllCinemaView.fxml"));
        loader.setController(controller);
        Stage window = new Stage();
        window.setScene(new Scene(loader.load()));
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Cinema del Film Festival");
        window.setOnCloseRequest((WindowEvent event) -> {controller.handleCancel();});

        controller.initGrids();
        
        window.show();
        } catch(IOException e) {e.printStackTrace();}
	}

}
