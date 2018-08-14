import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class AddScreeningController {
	@FXML private Label movieLabel, endTime;
	@FXML private DatePicker startDate;
	@FXML private TextField startHour, startMinute, extraMinutes, theater;
	@FXML private TextArea notes;
	@FXML private ChoiceBox<Integer> priorityBox;
	@FXML private ChoiceBox<Cinema> cinemaChoice;
	
	private FilmFestival ff;
	private Movie movie;
	private static DateTimeFormatter hour = DateTimeFormatter.ofPattern("HH : mm");
	
	public AddScreeningController(FilmFestival ff, Movie movie) {
		this.ff = ff;
		this.movie = movie;
	}
	
	@FXML
	private void updateTime(Event event) {
		try {
			LocalTime startTime = LocalTime.of(Integer.parseInt(startHour.getText()), 
					Integer.parseInt(startMinute.getText()));
			endTime.setText(startTime.plusMinutes(movie.getRuntime()).format(hour));
			System.out.println(event.getEventType());
		} catch(NumberFormatException | DateTimeException e) {}
	}
	
	@FXML
	private void handleCancel() {
		System.out.println("AddScreening->Cancel");
		
		((Stage) movieLabel.getScene().getWindow()).close();
	}
	
	@FXML
	private void handleAddCinema() {
		System.out.println("AddScreening->AddCinema");
		AddCinemaController.display("Nuovo Cinema", ff);
		cinemaChoice.getItems().setAll(ff.getCinemas());
	}
	
	@FXML
	private void handleConfirm() {
		System.out.println("AddScreening->Confirm");
		Screening s = makeScreening();
		if(s == null) return;
		ff.addScreen(s);
		((Stage) movieLabel.getScene().getWindow()).close();
	}
	
	private Screening makeScreening() {
		if(movie == null) return null;
		Screening s = null;
		LocalDate date = startDate.getValue();
		try {
			LocalTime time = LocalTime.of(Integer.parseInt(startHour.getText()), 
					Integer.parseInt(startMinute.getText()));
			LocalDateTime start = LocalDateTime.of(date, time);
			s = new Screening(movie, start);
		} catch(NumberFormatException | DateTimeException e) {
			System.err.println("ORA INSERITA NON VALIDA");
		}
		try {
			if(s != null) {
				s.setMinutesToWait(Integer.parseInt(extraMinutes.getText()));
				s.setMinimumToWait(ff.getMinimumToWait());
			}
		} catch(NumberFormatException e) {
			s = null;
			System.err.println("INSERISCI UN NUMERO");
		}
		try {
			if(s != null && cinemaChoice.getValue() != null)
				s.setCinema(cinemaChoice.getValue(), Integer.parseInt(theater.getText()));
		} catch(NumberFormatException e) {
			s = null;
			System.err.println("INSERISCI UN NUMERO");
		}
		if(s != null) {
			s.setPriority(priorityBox.getValue());
			s.setNotes(notes.getText());
		}
		return s;
	}
	
	private void initView() {
		String ml = "Proiezione per ";
		movieLabel.setText(ml + movie);
		LocalDate date = ff.getFirstDay();
		if(date == null)
			startDate.setValue(LocalDate.now());
		else
			startDate.setValue(date);
		for(int i = 0; i <= ff.getPriorityClasses(); i++)
			priorityBox.getItems().add(i);
		priorityBox.getItems().remove(0);
		priorityBox.setValue(1);
		cinemaChoice.getItems().addAll(ff.getCinemas());
	}
	
	public static void display(String title, FilmFestival ff, Movie m) {
		AddScreeningController controller = new AddScreeningController(ff, m);
		if(ff == null || m == null) return;
        try {
        FXMLLoader loader = new FXMLLoader(controller.getClass().getResource("AddScreeningView.fxml"));
        loader.setController(controller);
        Stage window = new Stage();
        window.setScene(new Scene(loader.load()));
        window.initModality(Modality.WINDOW_MODAL);
        window.setTitle(title);
        window.setWidth(800);
        window.setHeight(600);
        window.setOnCloseRequest((WindowEvent event) -> {controller.handleCancel();});
        
        controller.initView();

        window.show();
        } catch(IOException e) {e.printStackTrace();}
	}
}
