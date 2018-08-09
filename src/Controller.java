import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.*;

public class Controller implements Initializable {
	
	@FXML private Label festivalName, firstDay, lastDay, minTimeLabel, minTime;
	@FXML private Button addMovie, addScreening, remove, schedule, viewScheduling;
	@FXML private TableView<Movie> moviesView;
	@FXML private TableColumn<Movie, String> columnTitle;
	@FXML private TableColumn<Movie, Integer> columnYear, columnRuntime, columnNumScreens;
	
	private Model dm;
	private FilmFestival ff;
	private SimpleDateFormat dateForm = new SimpleDateFormat("EEE dd/MM/yyyy");
	
	public Controller(Model dm) {
		this.dm = dm;
	}
	
	@FXML
	private void handleNewAction() {
		dm.newFilmFestival("Torino Film Festival", 10);
		ff = dm.getFilmFestival();
		refreshLabels();
		addMovie.setDisable(false);
		System.out.println("New!");
	}
	
	@FXML
	private void handleOpenAction() {
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose a file");
		File file = fc.showOpenDialog(new Stage());
		if(file != null) {
			dm.loadFilmFestival(file);
			ff = dm.getFilmFestival();
			refreshLabels();
			addMovie.setDisable(false);
			System.out.println("Open " + ff.getName());
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
	
	//BUTTONS HANDLERS
	
	@FXML
	private void handleAddMovie() {
		
	}
	
	@FXML
	private void handleAddScreening() {
		
	}
	
	@FXML
	private void handleRemove() {
		
	}
	
	@FXML
	private void handleSchedule() {
		
	}
	
	@FXML
	private void handleViewScheduling() {
		
	}
	
	public void refreshView() {
		refreshLabels();
	}
	
	private void refreshLabels() {
		festivalName.setText(ff.getName());
		Date first = ff.getFirstDay();
		if(first == null)
			firstDay.setText("Inizio:           ");
		else
			firstDay.setText("Inizio: " + dateForm.format(first));
		Date last = ff.getLastDay();
		if(last == null)
			lastDay.setText("Fine:           ");
		else
			lastDay.setText("Fine: " + dateForm.format(first));
		minTime.setText(Integer.toString(ff.getMinimumToWait()) + " minuti");
		festivalName.setVisible(true);
		firstDay.setVisible(true);
		lastDay.setVisible(true);
		minTimeLabel.setVisible(true);
		minTime.setVisible(true);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		festivalName.setVisible(false);
		firstDay.setVisible(false);
		lastDay.setVisible(false);
		minTimeLabel.setVisible(false);
		minTimeLabel.setText("Intervallo minimo tra i film:");
		minTime.setVisible(false);
		
		addMovie.setDisable(true);
		addScreening.setDisable(true);
		remove.setDisable(true);
		schedule.setDisable(true);
		viewScheduling.setDisable(true);
	}

}
