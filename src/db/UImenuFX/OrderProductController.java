package db.UImenuFX;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import db.jdbc.SQLManager;
import db.pojos.Biomaterial;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

public class OrderProductController implements Initializable {

	// -----> CLASS ATRIBUTES <-----
	
	private static SQLManager manager_object;
	private static Biomaterial selected_items;
	
	
	// -----> FXML ATRIBUTES <-----
	
	 @FXML
	 private Pane order_pane;
	 @FXML
	 private JFXButton doOrder_button;
	 @FXML
	 private JFXTextField units_order;
	 @FXML
	 private JFXTreeTableView<SelectionListObject> selection_tree_view;
	 @FXML
	 private final ObservableList<SelectionListObject> biomaterial_objects = FXCollections.observableArrayList();

	// -----> GETTERS AND SETTERS <-----
	

	public JFXButton getDoOrder_button() {
		return doOrder_button;
	}

	public void setDoOrder_button(JFXButton doOrder_button) {
		this.doOrder_button = doOrder_button;
	}
	
		
	// -----> ESSENTIAL METHODS <-----
	
	public OrderProductController() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public static void setValues(SQLManager manager) {
		manager_object = manager;
	}
	
	public static void setItems(Biomaterial biomaterial) {
		selected_items = biomaterial;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		JFXTreeTableColumn<SelectionListObject, String> product_name = new JFXTreeTableColumn<>("Product");
		product_name.setPrefWidth(180);
		product_name.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<SelectionListObject, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<SelectionListObject, String> param) {
						return param.getValue().getValue().product_name;
					}
				});
		product_name.setResizable(false);
		
		JFXTreeTableColumn<SelectionListObject, String> units = new JFXTreeTableColumn<>("Available units");
		units.setPrefWidth(180);
		units.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<SelectionListObject, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<SelectionListObject, String> param) {
						return param.getValue().getValue().available_units;
					}
				});
		units.setResizable(false);
		
		
		List<Biomaterial> biomaterial_selection = new ArrayList<Biomaterial>();
		System.out.println(selected_items);
		
		//ADD ALL PARA CUANDO TENGAMOS LA LISTA
		biomaterial_selection.add(selected_items);
		
		for(Biomaterial biomaterial: biomaterial_selection) {
			biomaterial_objects.add(new SelectionListObject(biomaterial.getName_product(), biomaterial.getAvailable_units().toString()));
		}
		
		TreeItem<SelectionListObject> root = new RecursiveTreeItem<SelectionListObject>(biomaterial_objects, RecursiveTreeObject::getChildren);
		selection_tree_view.setPlaceholder(new Label("No selected products"));
		selection_tree_view.getColumns().setAll(product_name, units);
		selection_tree_view.setRoot(root);
		selection_tree_view.setShowRoot(false);
		
		
		
	}
	
	/*NO ACTUALZIA PORQUE BIOMATERIAL ES ESTATICO*/
	
	@FXML
	public void do_order_button(MouseEvent event) {
		System.out.println(selected_items);
		
		/*METODO PARA UNA LISTA DE SELECCIONADOS
		for(Biomaterial biomaterial: selected_items) {
			biomaterial.setAvailable_units(biomaterial.getAvailable_units() + Integer.parseInt(units_order.getText()));
			
			
			
			if (manager_object.Update_biomaterial_units(biomaterial) == true) {
				System.out.println("new units: " + biomaterial.getAvailable_units());
				System.out.println("Units updated");
				
			} else System.out.println("Error on units update");
		}
		
		 */

		Biomaterial biomaterial = selected_items;
		 biomaterial.setAvailable_units(biomaterial.getAvailable_units() + Integer.parseInt(units_order.getText()));
		
		if (manager_object.Update_biomaterial_units(biomaterial) == true) {
			System.out.println("Units updated");
			System.out.println(manager_object.Search_biomaterial_by_id(biomaterial.getBiomaterial_id()));
			
		} else System.out.println("Error on units update");
	}
	
	
	/*
	// -----> REFRESH BIOMATERIAL LIST VIEW <-----
	
		public void refreshSelectionListView(List<Biomaterial> selection) {
			biomaterial_objects.clear();
			List<Biomaterial> biomaterial_list = selection;
			for(Biomaterial biomaterial: biomaterial_list) {
				biomaterial_objects.add(new SelectionListObject(biomaterial.getName_product(), biomaterial.getAvailable_units().toString()));
			}
			TreeItem<SelectionListObject> root = new RecursiveTreeItem<SelectionListObject>(biomaterial_objects, RecursiveTreeObject::getChildren);
			selection_tree_view.refresh();
			}
*/	
}


//-----> SELECTION LIST CLASS <-----

//To insert columns into the list of selected biomaterials with all the information
class SelectionListObject extends RecursiveTreeObject<SelectionListObject> {
	StringProperty product_name;
	StringProperty available_units;

	public SelectionListObject(String product_name, String available_units) {
		this.product_name = new SimpleStringProperty(product_name);
		this.available_units = new SimpleStringProperty(available_units);
	}
}

