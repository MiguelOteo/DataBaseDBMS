package db.UImenuFX;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import db.jdbc.SQLManager;
import db.pojos.Client;
import db.pojos.Director;
import db.pojos.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ChargingScreenController implements Initializable {

	// -----> CLASS ATRIBUTES <-----
	
	private String user_name;
	private String password;
	private String user_type;
	private SQLManager manager;
	
	private DirectorMenuController director_controller;
	private ClientMenuController client_controller;
	private WorkerMenuController worker_controller;

	// -----> FXML ATRIBUTES <-----

	@FXML
	public static AnchorPane charging_main_pane;

	// -----> ESSENTIAL METHODS <-----

	public ChargingScreenController() {
		// Default constructor
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO - if its needed
	}
	
	// Next algorithm checks if the user account already exist when you create a new one or in 
	// case you access, if the account exist to charge it in all the user's tables (Client, Director, Worker)
	public void searching_create_account(String user_name, String password, String user_type) {
		try {
			this.user_name = user_name;
			this.password = password;
			this.user_type = user_type;
			
			// The following code charges all the database info and tables
			manager = new SQLManager();
			manager.Stablish_connection();
			boolean tables_exist = manager.Check_if_tables_exist();
			if (tables_exist == false) {
				manager.Create_tables();
			}

			// List all clients in order to find if he exist to access it
			List<Client> clients_list = manager.List_all_clients();
			Client client_account = null;
			for (Client client : clients_list) {
				if ((client.getName().equals(this.user_name)) && (client.getPassword().equals(this.password))) {
					client_account = client;
					break;
				}
			}
			if (client_account != null) {
				if (this.user_type == null) {
					charge_client_main_menu(client_account);
					LaunchApplication.stage.hide();
				} else {
					System.out.println("El cliente ya existe");
				}
			} else {
				// List all directors in order to find if he exist to access it
				List<Director> directors_list = manager.List_all_directors();
				Director director_account = null;
				for (Director director : directors_list) {
					if ((director.getDirector_name().equals(this.user_name)) && (director.getPassword().equals(this.password))) {
						director_account = director;
						break;
					}
				}
				if (director_account != null) {
					if (this.user_type == null) {
						charge_director_main_menu(director_account);
						LaunchApplication.stage.hide();
					} else {
						System.out.println("El director ya existe");
					}
				} else {
					// List all workers in order to find if he exist to access it
					List<Worker> workers_list = manager.List_all_workers();
					Worker worker_account = null;
					for (Worker worker : workers_list) {
						if ((worker.getWorker_name().equals(this.user_name)) && (worker.getPassword().equals(this.password))) {
							worker_account = worker;
							break;
						}
					}
					if (worker_account != null) {
						if (this.user_type == null) {
							// TODO - charge worker menu
							LaunchApplication.stage.hide();
							System.out.println("Cargando trabajador");
						} else {
							System.out.println("El trabajador ya existe");
						}
					} else {
						if (this.user_type == null) {
							System.out.println("No existe ese usuario");
						} else {
							if (this.user_type.equals("Client")) {
								boolean insertion_ok = manager.Insert_new_client_account(this.user_name, this.password);
								charge_client_main_menu(new Client(this.user_name, this.password));
								LaunchApplication.stage.hide();
							} else {
								if (this.user_type.equals("Director")) {
									boolean insertion_ok = manager.Insert_new_director(this.user_name, this.password);
									charge_director_main_menu(new Director(this.user_name, this.password));
									LaunchApplication.stage.hide();
								} else {
									if (this.user_type.equals("Worker")) {
										boolean insertion_ok = manager.Insert_new_worker(this.user_name, this.password);
										LaunchApplication.stage.hide();
										System.out.println("Creando trabajador");
									}
								}
							}
						}
					}
				}
			}
			manager.Close_connection();
		} catch (Exception error_occur) {
			error_occur.printStackTrace();
			manager.Close_connection();
		}
	}
	
	// -----> GET AND SET METHODS <-----
	
	public ClientMenuController getClientController() {
		return this.client_controller;
	}
	
	public DirectorMenuController getDirectorController() {
	    return this.director_controller;	
	}
	
	public WorkerMenuController getWorkerController() {
		return this.worker_controller;
	}
	
	// -----> OTHER METHODS <-----

	public void charge_client_main_menu(Client client) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientMenuView.fxml"));
			Parent root = (Parent) loader.load();
			this.client_controller = new ClientMenuController(this.manager, client);
			this.client_controller = loader.getController();
			this.client_controller.getAnchorPane().setEffect(new BoxBlur(4,4,4));
			Stage stage = new Stage();
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setScene(new Scene(root));
			stage.show();
		} catch (IOException client_menu_error) {
			client_menu_error.printStackTrace();
			System.exit(0);
		}
	}

	public void charge_director_main_menu(Director director) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("DirectorMenuView.fxml"));
			Parent root = (Parent) loader.load();
			this.director_controller = new DirectorMenuController(this.manager, director);
			this.director_controller = loader.getController();
			this.director_controller.setDirectorName(director.getDirector_name());
			this.director_controller.setDirectorEmail(director.getEmail());
			this.director_controller.setDirectorTelephone(director.getTelephone());
			this.director_controller.getAnchorPane().setEffect(new BoxBlur(4,4,4));
			Stage stage = new Stage();
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setScene(new Scene(root));
			stage.show();
		} catch (IOException director_menu_error) {
			director_menu_error.printStackTrace();
			System.exit(0);
		}
	}

	public void charge_worker_main_menu(Worker worker) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("WorkerMenuView.fxml"));
			Parent root = (Parent) loader.load();
			this.worker_controller = new WorkerMenuController(this.manager, worker);
			this.worker_controller = loader.getController();
			this.worker_controller.getAnchorPane().setEffect(new BoxBlur(4,4,4));
			Stage stage = new Stage();
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setScene(new Scene(root));
			stage.show();
		} catch (IOException director_menu_error) {
			director_menu_error.printStackTrace();
			System.exit(0);
		}
	}
	
	public void removeBlur() {
		if(this.client_controller != null) {
			this.client_controller.getAnchorPane().setEffect(null);
		} else {
			if(this.director_controller != null) {
				this.director_controller.getAnchorPane().setEffect(null);
			} else {
				this.worker_controller.getAnchorPane().setEffect(null);
			}
		}
	}
}
