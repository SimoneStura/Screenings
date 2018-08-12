import java.io.IOException;
import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class AddScreeningController {
	@FXML private Label movieLabel;
	@FXML private DatePicker startDate;
	@FXML private TextField startHour, startMinute, extraMinutes, endHour, endMinute, theater;
	@FXML private ChoiceBox<Cinema> cinemaChoice;
	
	private FilmFestival ff;
	private Movie movie;
	
	public AddScreeningController(FilmFestival ff, Movie movie) {
		this.ff = ff;
		this.movie = movie;
	}
	
	@FXML
	private void handleCancel() {
		System.out.println("AddScreening->Cancel");
		
		((Stage) movieLabel.getScene().getWindow()).close();
	}
	
	@FXML
	private void handleAddCinema() {
		System.out.println("AddScreening->AddCinema");
		
	}
	
	@FXML
	private void handleConfirm() {
		System.out.println("AddScreening->Confirm");
		Screening s = makeScreening();
		if(s != null)
			ff.addScreen(s);
		((Stage) movieLabel.getScene().getWindow()).close();
	}
	
	private Screening makeScreening() {
		if(movie == null) return null;
		return null;
	}
	
	private void initView() {
		String ml = "Proiezione per ";
		movieLabel.setText(ml + movie);
		LocalDate date = ff.getFirstDay();
		if(date == null)
			startDate.setValue(LocalDate.now());
		else
			startDate.setValue(date);
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
