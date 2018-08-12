import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class AddMovieController {
	@FXML private TextField title, year, runtime, directors, section;
	
	private FilmFestival ff;
	
	public AddMovieController(FilmFestival ff) {
		this.ff = ff;
	}
	
	@FXML
	private void handleCancel() {
		System.out.println("AddMovie->Cancel");
		
		((Stage) title.getScene().getWindow()).close();
	}
	
	@FXML
	private void handleAddScreening() {
		System.out.println("AddMovie->AddScreening");
		Movie m = makeMovie();
		ff.addMovie(m);
		AddScreeningController.display("Nuova Proiezione", ff, m);
		((Stage) title.getScene().getWindow()).close();
	}
	
	@FXML
	private void handleConfirm() {
		System.out.println("AddMovie->Confirm");
		Movie m = makeMovie();
		ff.addMovie(m);
		((Stage) title.getScene().getWindow()).close();
	}
	
	private Movie makeMovie() {
		String mTitle = title.getText();
		int mYear = Integer.parseInt(year.getText());
		int mRuntime = Integer.parseInt(runtime.getText());
		Movie m = new Movie(mTitle, mYear, mRuntime);
		
		m.setDirectedBy(directors.getText());
		m.setSection(section.getText());
		
		return m;
	}
	
	public static void display(String title, FilmFestival ff) {
		AddMovieController controller = new AddMovieController(ff);
        try {
        FXMLLoader loader = new FXMLLoader(controller.getClass().getResource("AddMovieView.fxml"));
        loader.setController(controller);
        Stage window = new Stage();
        window.setScene(new Scene(loader.load()));
        window.initModality(Modality.WINDOW_MODAL);
        window.setTitle(title);
        window.setWidth(640);
        window.setHeight(360);
        window.setOnCloseRequest((WindowEvent event) -> {controller.handleCancel();});

        window.show();
        } catch(IOException e) {e.printStackTrace();}
	}
}
