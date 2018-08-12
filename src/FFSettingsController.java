import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.TextField;
import javafx.stage.*;

public class FFSettingsController {
	private Model dm;
	private static FilmFestival ff = null;
	
	@FXML private TextField ffName, minutes;
	
	public FFSettingsController(Model dm) {
		this.dm = dm;
	}
	
	@FXML
	private void handleCancel() {
		System.out.println("FFSettings->Cancel");
		ff = null;
		((Stage) ffName.getScene().getWindow()).close();
	}
	
	@FXML
	private void handleConfirm() {
		System.out.println("FFSettings->Confirm");
        if(ffName.getText() == "") {
        	System.out.println("Devi scrivere qualcosa!");
        	return;
        }
        if(minutes.getText() == "") {
        	System.out.println("Devi scrivere qualcosa nei numeri!");
        	return;
        }
        ff = dm.newFilmFestival(ffName.getText(), Integer.parseInt(minutes.getText()));
        if(ff != null)
        	System.out.println("OK");
		((Stage) ffName.getScene().getWindow()).close();
	}
	
	public static FilmFestival display(String title, Model dm) {
        FFSettingsController controller = new FFSettingsController(dm);
        try {
        FXMLLoader loader = new FXMLLoader(controller.getClass().getResource("FFSettingsView.fxml"));
        loader.setController(controller);
        Stage window = new Stage();
        window.setScene(new Scene(loader.load()));
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setWidth(400);
        window.setHeight(225);
        window.setOnCloseRequest((WindowEvent event) -> {controller.handleCancel();});

        window.showAndWait();
        return ff;
        } catch(IOException e) {e.printStackTrace();}
        return null;
	}

}
