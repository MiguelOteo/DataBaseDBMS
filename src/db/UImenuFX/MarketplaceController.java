package db.UImenuFX;


import java.awt.Label;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import db.jdbc.SQLManager;
import db.pojos.Biomaterial;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

public class MarketplaceController implements Initializable {
	
	private static SQLManager manager_object;
	private static MarketplaceController marketplace_controller;
	
	
	
	@FXML
	private AnchorPane menu_window;
	@FXML
	private Pane menu_main_pane;
	@FXML
	private Pane iteminfopane;
	@FXML
	private Label prueba;
	@FXML
	private JFXTreeTableView<BiomaterialListObject> biomaterials_tree_view;
	@FXML
	private ImageView itemimg1;
	@FXML
	private ImageView itemimg2;
	@FXML
	private Label item_information;
	@FXML
	private final ObservableList<BiomaterialListObject> biomaterial_objects = FXCollections.observableArrayList();
	
	public static void setValues(SQLManager manager) {
		manager_object=manager;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
			

		// Biomaterials list columns creation

				JFXTreeTableColumn<BiomaterialListObject, String> product_name = new JFXTreeTableColumn<>("Product");
				product_name.setPrefWidth(140);
				product_name.setCellValueFactory(
						new Callback<TreeTableColumn.CellDataFeatures<BiomaterialListObject, String>, ObservableValue<String>>() {
							@Override
							public ObservableValue<String> call(CellDataFeatures<BiomaterialListObject, String> param) {
								return param.getValue().getValue().product_name;
							}
						});
				product_name.setResizable(false);

				JFXTreeTableColumn<BiomaterialListObject, String> available_units = new JFXTreeTableColumn<>("Available units");
				available_units.setPrefWidth(120);
				available_units.setCellValueFactory(
						new Callback<TreeTableColumn.CellDataFeatures<BiomaterialListObject, String>, ObservableValue<String>>() {
							@Override
							public ObservableValue<String> call(CellDataFeatures<BiomaterialListObject, String> param) {
								return param.getValue().getValue().available_units;
							}
						});
				available_units.setResizable(false);

				JFXTreeTableColumn<BiomaterialListObject, String> price = new JFXTreeTableColumn<>("Price / unit ($)");
				price.setPrefWidth(90);
				price.setCellValueFactory(
						new Callback<TreeTableColumn.CellDataFeatures<BiomaterialListObject, String>, ObservableValue<String>>() {
							@Override
							public ObservableValue<String> call(CellDataFeatures<BiomaterialListObject, String> param) {
								return param.getValue().getValue().price_unit;
							}
						});
				price.setResizable(false);

				JFXTreeTableColumn<BiomaterialListObject, String> exp_date = new JFXTreeTableColumn<>("Expiration date");
				exp_date.setPrefWidth(90);
				exp_date.setCellValueFactory(
						new Callback<TreeTableColumn.CellDataFeatures<BiomaterialListObject, String>, ObservableValue<String>>() {
							@Override
							public ObservableValue<String> call(CellDataFeatures<BiomaterialListObject, String> param) {
								return param.getValue().getValue().expiration_date;
							}
						});
				exp_date.setResizable(false);

				JFXTreeTableColumn<BiomaterialListObject, String> id = new JFXTreeTableColumn<>("id");
				id.setPrefWidth(40);
				id.setCellValueFactory(
						new Callback<TreeTableColumn.CellDataFeatures<BiomaterialListObject, String>, ObservableValue<String>>() {
							@Override
							public ObservableValue<String> call(CellDataFeatures<BiomaterialListObject, String> param) {
								return param.getValue().getValue().bio_id;
							}
						});
				id.setResizable(false);
				
				JFXTreeTableColumn<BiomaterialListObject, JFXButton> action = new JFXTreeTableColumn<>("Action");
				action.setPrefWidth(40);
				action.setCellValueFactory(
						new Callback<TreeTableColumn.CellDataFeatures<BiomaterialListObject, JFXButton>, ObservableValue<JFXButton>>() {
							@Override
							public ObservableValue<JFXButton> call(CellDataFeatures<BiomaterialListObject, JFXButton> param) {
								return param.getValue().getValue().button;
							}
						});
				product_name.setResizable(false);
				List<Biomaterial> biomaterial_list = manager_object.List_all_biomaterials();
				for(Biomaterial biomaterial: biomaterial_list) {
					biomaterial_objects.add(new BiomaterialListObject(biomaterial.getBiomaterial_id().toString(), biomaterial.getName_product(), biomaterial.getAvailable_units().toString()
							, biomaterial.getPrice_unit().toString(), biomaterial.getExpiration_date().toString()));
				}
				
				TreeItem<BiomaterialListObject> root = new RecursiveTreeItem<BiomaterialListObject>(biomaterial_objects, RecursiveTreeObject::getChildren);
				biomaterials_tree_view.getColumns().setAll(id, product_name, available_units, price, exp_date, action);
				biomaterials_tree_view.setRoot(root);
				biomaterials_tree_view.setShowRoot(false);

				
				//Ables the selection of several biomaterials of treeTable
				//next step: associate selection's id to a variable being read by Order product controller
				
			
	
}
    
	@FXML 
	private void iteminfo (MouseEvent event) throws IOException{
		TreeItem<BiomaterialListObject> biomaterial_object = biomaterials_tree_view.getSelectionModel().getSelectedItem();
	    System.out.println(Integer.parseInt(biomaterial_object.getValue().bio_id.getValue().toString()));
	    if(biomaterial_object!=null) {
	    	
	    }
	    
	}

// To insert columns into the list of biomaterials with all the information
class BiomaterialListObject extends RecursiveTreeObject<BiomaterialListObject> {
	StringProperty action;
	StringProperty product_name;
	StringProperty available_units;
	StringProperty price_unit;
	StringProperty expiration_date;
	StringProperty bio_id;
    ObjectProperty<JFXButton> button;
	@SuppressWarnings("unchecked")
	public BiomaterialListObject(String id, String product_name, String available_units, String price_unit,
			String expiration_date) {
		this.product_name = new SimpleStringProperty(product_name);
		this.available_units = new SimpleStringProperty(available_units);
		this.price_unit = new SimpleStringProperty(price_unit);
		this.expiration_date = new SimpleStringProperty(expiration_date);
		this.bio_id = new SimpleStringProperty(id);
		Button button = new JFXButton("smth");
		this.button=new SimpleObjectProperty(button);
		
	}
	
}}

