import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.stage.*;

public class Controller implements Initializable {
	
	@FXML private Label festivalName, firstDay, lastDay, minTimeLabel, minTime;
	@FXML private Button addMovie, addScreening, remove, schedule, viewScheduling;
	@FXML private TableView<Movie> moviesView;
	@FXML private TableColumn<Movie, String> columnTitle;
	@FXML private TableColumn<Movie, Integer> columnYear, columnRuntime, columnNumScreens;
	
	private Model dm;
	private FilmFestival ff;
	private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEE dd/MM/yyyy");
	
	public Controller(Model dm) {
		this.dm = dm;
	}
	
	@FXML
	private void handleNewAction() {
		dm.newFilmFestival("Torino Film Festival", 10);
		ff = dm.getFilmFestival();
		refreshLabels();
		setMoviesView();
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
			setMoviesView();
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
	
	@FXML
	public void handleExitAction() {
		System.out.println("ABRAKADABRA VIA DI QUA");
		Platform.exit();
	}
	
	//BUTTONS HANDLERS
	
	@FXML
	private void handleAddMovie() {
		ff.addScreen(new Screening(new Movie("Lo Lo Lond", 2016, 120), LocalDateTime.of(2015,10,2,14,0)));
		refreshLabels();
		System.out.println("add movie");
	}
	
	@FXML
	private void handleAddScreening() {
		System.out.println("add screening");
	}
	
	@FXML
	private void handleRemove() {
		System.out.println("remove movie");
	}
	
	@FXML
	private void handleSchedule() {
		System.out.println("schedule");
	}
	
	@FXML
	private void handleViewScheduling() {
		System.out.println("view scheduling");
	}
	
	public void refreshView() {
		refreshLabels();
	}
	
	private void refreshLabels() {
		festivalName.setText(ff.getName());
		LocalDate first = ff.getFirstDay();
		if(first == null)
			firstDay.setText("Inizio:           ");
		else
			firstDay.setText("Inizio: " + first.format(dateFormat));
		LocalDate last = ff.getLastDay();
		if(last == null)
			lastDay.setText("Fine:           ");
		else
			lastDay.setText("Fine: " + last.format(dateFormat));
		minTime.setText(Integer.toString(ff.getMinimumToWait()) + " minuti");
		festivalName.setVisible(true);
		firstDay.setVisible(true);
		lastDay.setVisible(true);
		minTimeLabel.setVisible(true);
		minTime.setVisible(true);
	}
	
	private void setMoviesView() {
		columnTitle.setCellValueFactory(cellData -> cellData.getValue().getTitleProperty());
		columnYear.setCellValueFactory(new PropertyValueFactory<>("year"));
		columnRuntime.setCellValueFactory(new PropertyValueFactory<>("runtime"));
		
		moviesView.setItems(ff.getMovies());
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
