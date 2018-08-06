import java.io.File;
import java.net.URL;
import java.util.*;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
//import javafx.scene.control.*;
import javafx.stage.*;

public class Controller implements Initializable {
	
	private Model dm;
	//@FXML TableView screeningView;
	
	public Controller(Model dm) {
		this.dm = dm;
	}
	
	@FXML
	private void handleNewAction() {
		dm.newFilmFestival("Torino Film Festival");
		System.out.println("New!");
	}
	
	@FXML
	private void handleOpenAction() {
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose a file");
		File file = fc.showOpenDialog(new Stage());
		if(file != null) {
			dm.loadFilmFestival(file);
			System.out.println("Open " + dm.getFFName());
		}
	}
	
	@FXML
	private void handleSaveAction() {
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose a file");
		File file = fc.showSaveDialog(new Stage());
		if(file != null)
			dm.saveFilmFestival(file);
		System.out.println("Save!");
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
	}

}
