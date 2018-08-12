
import java.io.File;
//import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javafx.application.Platform;
import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
//import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.*;
import javafx.stage.FileChooser.ExtensionFilter;

public class FFController implements Initializable {
	
	@FXML private Label festivalName, firstDay, lastDay, minTimeLabel, minTime;
	@FXML private Button addMovie, addScreening, remove, schedule, viewScheduling;
	@FXML private TableView<Movie> moviesView;
	@FXML private TableColumn<Movie, String> columnTitle;
	@FXML private TableColumn<Movie, Integer> columnYear, columnRuntime, columnNumScreens;
	
	private Model dm;
	private FilmFestival ff;
	private File saveFile;
	private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEE dd/MM/yyyy");
	
	public FFController(Model dm) {
		this.dm = dm;
	}
	
	@FXML
	private void handleNewAction() {
		ff = FFSettingsController.display("Nuovo Film Festival", dm);
		if(ff == null) return;
		refreshLabels();
		setMoviesView();
		addMovie.setDisable(false);
		System.out.println("New!");
	}
	
	@FXML
	private void handleOpenAction() {
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose a file");
		fc.getExtensionFilters().add(new ExtensionFilter("Festival files", "*.fes"));
		File file = fc.showOpenDialog(new Stage());
		if(file != null) {
			saveFile = file;
			dm.loadFilmFestival(saveFile);
			ff = dm.getFilmFestival();
			refreshLabels();
			setMoviesView();
			addMovie.setDisable(false);
			System.out.println("Open " + ff.getName());
		}
	}
	
	@FXML
	private void handleSaveAction() {
		if(dm.saveFilmFestival()) return;
		handleSaveAsAction();
	}
	
	@FXML
	private void handleSaveAsAction() {
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose a file");
		fc.getExtensionFilters().add(new ExtensionFilter("Festival files", "*.fes"));
		File file = fc.showSaveDialog(new Stage());
		if(file != null)
			dm.saveFilmFestival(file);
	}
	
	@FXML
	public void handleExitAction() {
		System.out.println("ABRAKADABRA VIA DI QUA");
		Platform.exit();
	}
	
	//BUTTONS HANDLERS
	
	@FXML
	private void handleAddMovie() {
		AddMovieController.display("Nuovo Film", ff);
		System.out.println("add movie");
	}
	
	@FXML
	private void handleAddScreening() {
		System.out.println("add screening");
		Movie selected = moviesView.getSelectionModel().getSelectedItem();
		AddScreeningController.display("Nuova Proiezione", ff, selected);
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
		columnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
		columnYear.setCellValueFactory(new PropertyValueFactory<>("year"));
		columnRuntime.setCellValueFactory(new PropertyValueFactory<>("runtime"));
		columnNumScreens.setCellValueFactory(new PropertyValueFactory<>("numScreens"));
		
		moviesView.setItems(ff.getMovies());
	}
	
	private void selectionListener() {
        moviesView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null) {
        		addScreening.setDisable(true);
        		remove.setDisable(true);
            } else {
        		addScreening.setDisable(false);
        		remove.setDisable(false);
            }
        });
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
		
		selectionListener();
	}

}
