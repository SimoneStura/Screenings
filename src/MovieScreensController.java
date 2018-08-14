import java.io.IOException;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MovieScreensController {
	private Movie m;
	private FilmFestival ff;
	private ObservableList<Screening> screens;
	
	@FXML private Label movieInfo;
	@FXML private TableView<Screening> screensView;
	@FXML private TableColumn<Screening, String> dateColumn, startTimeColumn, endTimeColumn;
	@FXML private TableColumn<Screening, Integer> priorityColumn, extraMinutesColumn;
	@FXML private TableColumn<Screening, String> cinemaColumn, notesColumn;
	
	private static DateTimeFormatter date = DateTimeFormatter.ofPattern("EEE dd/MM/yyyy");
	private static DateTimeFormatter hour = DateTimeFormatter.ofPattern("HH:mm");
	
	public MovieScreensController(FilmFestival ff, Movie m) {
		this.m = m;
		this.ff = ff;
		screens = ff.getScreens(m);
	}
	
	@FXML
	private void handleBack() {
		System.out.println("MovieScreens->Back");
		
		((Stage) movieInfo.getScene().getWindow()).close();
	}
	
	@FXML
	private void handleAddScreening() {
		System.out.println("MovieScreens->AddScreening");
		AddScreeningController.display("Nuova Proiezione", ff, m);
	}
	
	@FXML
	private void handleModifyMovie() {
		System.out.println("MovieScreens->ModifyMovie");
		
	}
	
	public void initView() {
		String info = m.toString();
		info += " - Durata: " + m.getRuntime() + "'";
		movieInfo.setText(info);
		setScreensView();
	}
	
	private void setScreensView() {
		dateColumn.setCellValueFactory(cellData -> {
			Screening s = cellData.getValue();
			return new SimpleStringProperty(s.getStartTime().format(date));
		});
		startTimeColumn.setCellValueFactory(cellData -> {
			Screening s = cellData.getValue();
			return new SimpleStringProperty(s.getStartTime().format(hour));
		});
		endTimeColumn.setCellValueFactory(cellData -> {
			Screening s = cellData.getValue();
			return new SimpleStringProperty(s.getEndTime().format(hour));
		});
		cinemaColumn.setCellValueFactory(cellData -> {
			Screening s = cellData.getValue();
			if(s.getCinema() == null) return new SimpleStringProperty();
			return new SimpleStringProperty(s.getCinema() + " " + s.getTheater());
		});
		priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
		extraMinutesColumn.setCellValueFactory(new PropertyValueFactory<>("minutesToWait"));
		notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));
		
		screensView.setItems(screens);
	}
	
	public static void display(FilmFestival ff, Movie m) {
		MovieScreensController controller = new MovieScreensController(ff,m);
		if(ff == null || m == null) return;
        try {
        FXMLLoader loader = new FXMLLoader(controller.getClass().getResource("MovieScreensView.fxml"));
        loader.setController(controller);
        Stage window = new Stage();
        window.setScene(new Scene(loader.load()));
        window.initModality(Modality.WINDOW_MODAL);
        window.setTitle("Proiezioni per il Film");
        window.setWidth(800);
        window.setHeight(600);
        window.setOnCloseRequest((WindowEvent event) -> {controller.handleBack();});
        
        controller.initView();

        window.show();
        } catch(IOException e) {e.printStackTrace();}
	}
}
