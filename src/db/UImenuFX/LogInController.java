package db.UImenuFX;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class LogInController implements Initializable {
	
	// -----> CLASS ATRIBUTES <-----
	
	private ChargingScreenController charging_controller;

	// -----> FXML ATRIBUTES <-----

	@FXML
	private AnchorPane startUpMenu;
	@FXML
	private Pane logInPane;
	@FXML
	private JFXButton logInButtom;
	@FXML
	private JFXButton signUpButtom;
	@FXML
	private JFXTextField userNameField;
	@FXML
	private JFXPasswordField passwordField;

	// -----> ESSENTIAL METHODS <-----

	public LogInController() {
		// Default contructor
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		logInButtom.setOnAction((ActionEvent event) -> {
			try {
				String user_name = userNameField.getText();
				String password = passwordField.getText();
				// Code to open charging window
				if (!(user_name.equals("") | password.equals(""))) {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("ChargingScreenView.fxml"));
					Parent root = (Parent) loader.load();
					this.charging_controller = new ChargingScreenController();
					this.charging_controller = loader.getController();
					Stage stage = new Stage();
					stage.setOnShowing((event_handler) -> this.charging_controller.searching_create_account(user_name, password, null));
					stage.initStyle(StageStyle.UNDECORATED);
					stage.setScene(new Scene(root));
					stage.show();
					
					PauseTransition wait = new PauseTransition(Duration.seconds(2));
					wait.setOnFinished(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent arg0) {
							if(charging_controller.getClientController() != null | charging_controller.getDirectorController() != null
									| charging_controller.getWorkerController() != null) {
								charging_controller.removeBlur();
							}
							stage.close();
						}
			        });
					wait.play();

					passwordField.clear();
					userNameField.clear();
				}
			} catch (IOException charging_screen_error) {
				charging_screen_error.printStackTrace();
			}
		});
	}

	// -----> BUTTOM METHODS <-----

	@FXML
	private void open_registration(MouseEvent event) throws IOException {
		Pane registration_pane_fxml = FXMLLoader.load(getClass().getResource("RegistrationView.fxml"));
		logInPane.getChildren().removeAll();
		logInPane.getChildren().setAll(registration_pane_fxml);
	}

	@FXML // It is triggered when "extitCross.png" is pressed
	private void close_app(MouseEvent event) throws IOException {
		System.exit(0);
	}
}
