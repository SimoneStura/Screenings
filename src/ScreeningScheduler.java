import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.*;

public class ScreeningScheduler extends Application {
	
	private Model model;
	private FFController fFController;
	
	private void setModel() {
		model = new Model();
	}
	
	private void setController() {
		fFController = new FFController(model);
	}
	
	private void initView(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("FFView.fxml"));
        loader.setController(fFController);
        stage.setScene(new Scene(loader.load()));
        stage.setOnCloseRequest((WindowEvent event) -> {fFController.handleExitAction();});
        stage.setTitle("ScreeSched");
        stage.show();

	}

	@Override
	public void start(Stage stage) throws Exception {
		setModel();
		setController();
		initView(stage);
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
