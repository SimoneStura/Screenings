import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.*;

public class ScreeningScheduler extends Application {
	
	private Model model;
	private Controller controller;
	
	private void setModel() {
		model = new Model();
	}
	
	private void setController() {
		controller = new Controller(model);
	}
	
	private void initView(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("View.fxml"));
        loader.setController(controller);
        stage.setScene(new Scene(loader.load()));
        stage.setOnCloseRequest((WindowEvent event) -> {controller.handleExitAction();});
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
