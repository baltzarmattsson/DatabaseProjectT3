package app.view.uppgift2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import app.dal.uppgift2.DALCronus;
import app.model.uppgift2.FetchedMetadataRow;
import app.model.uppgift2.FetchedRow;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class OverviewController {
	
	@FXML
	private TableView<FetchedRow> contentDataTable;
	@FXML
	private ComboBox<String> contentComboBox;
	@FXML
	private ComboBox<String> contentAllTablesComboBox;
	
	@FXML
	private TableView<FetchedMetadataRow> metaDataTable;
	@FXML
	private ComboBox<String> metadataComboBox;
	@FXML
	private ComboBox<String> metadataAllTablesComboBox;
	
	@FXML
	private TableView<FetchedRow> functionsDataTable;
	@FXML
	private ComboBox<String> functionsComboBox;
	
	@FXML
	private void initialize() {
		
		TableColumn<FetchedMetadataRow, String> column = null;
		
		column = new TableColumn<FetchedMetadataRow, String>("TABLE_SCHEMA");
		column.setCellValueFactory(cellData -> cellData.getValue().getTABLE_SCHEMA());
		column.setPrefWidth(50);
		column.setMinWidth(50);
		metaDataTable.getColumns().add(0, column);
		
		column = new TableColumn<FetchedMetadataRow, String>("TABLE_NAME");
		column.setCellValueFactory(cellData -> cellData.getValue().getTABLE_NAME());
		column.setPrefWidth(50);
		column.setMinWidth(50);
		metaDataTable.getColumns().add(0, column);
		
		column = new TableColumn<FetchedMetadataRow, String>("COLUMN_NAME");
		column.setCellValueFactory(cellData -> cellData.getValue().getCOLUMN_NAME());
		column.setPrefWidth(50);
		column.setMinWidth(50);
		metaDataTable.getColumns().add(0, column);
		
		column = new TableColumn<FetchedMetadataRow, String>("ORDINAL_POSITION");
		column.setCellValueFactory(cellData -> cellData.getValue().getORDINAL_POSITION());
		column.setPrefWidth(50);
		column.setMinWidth(50);
		metaDataTable.getColumns().add(0, column);
		
		column = new TableColumn<FetchedMetadataRow, String>("COLUMN_DEFAULT");
		column.setCellValueFactory(cellData -> cellData.getValue().getCOLUMN_DEFAULT());
		column.setPrefWidth(50);
		column.setMinWidth(50);
		metaDataTable.getColumns().add(0, column);
		
		column = new TableColumn<FetchedMetadataRow, String>("IS_NULLABLE");
		column.setCellValueFactory(cellData -> cellData.getValue().getIS_NULLABLE());
		column.setPrefWidth(50);
		column.setMinWidth(50);
		metaDataTable.getColumns().add(0, column);
		
		column = new TableColumn<FetchedMetadataRow, String>("DATA_TYPE");
		column.setCellValueFactory(cellData -> cellData.getValue().getDATA_TYPE());
		column.setPrefWidth(50);
		column.setMinWidth(50);
		metaDataTable.getColumns().add(0, column);
		
		column = new TableColumn<FetchedMetadataRow, String>("CAHRACTER_MAXIMUM_LENGTH");
		column.setCellValueFactory(cellData -> cellData.getValue().getCHARACTER_MAXIMUM_LENGTH());
		column.setPrefWidth(50);
		column.setMinWidth(50);
		metaDataTable.getColumns().add(0, column);
		
		column = new TableColumn<FetchedMetadataRow, String>("CHARACTER_OCTET_LENGTH");
		column.setCellValueFactory(cellData -> cellData.getValue().getCHARACTER_OCTET_LENGTH());
		column.setPrefWidth(50);
		column.setMinWidth(50);
		metaDataTable.getColumns().add(0, column);
				
		metaDataTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		this.updateComboBoxes();
		
		functionsComboBox.getItems().addAll("Nycklar", 
				"Indexes", "Table constraints", "Samtliga tabeller",
				"Samtliga kolumner i Employee", "Tabell med flest rader");
		
		functionsComboBox.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (functionsComboBox.getSelectionModel().getSelectedItem() != null) {
					updateFunctionsTable();
				}
			}
		});
		
		contentComboBox.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (contentComboBox.getSelectionModel().getSelectedItem() != null) {
					
					contentAllTablesComboBox.getSelectionModel().clearSelection();

					String tableName = contentComboBox.getSelectionModel().getSelectedItem();
					updateContentTable(tableName);
				}
			}
		});
		
		contentAllTablesComboBox.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (contentAllTablesComboBox.getSelectionModel().getSelectedItem() == null)
					return;
				
				contentComboBox.getSelectionModel().clearSelection();
				
				String tableName = contentAllTablesComboBox.getSelectionModel().getSelectedItem();
				updateContentTable(tableName);
			}
		});
		
		metadataComboBox.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (metadataComboBox.getSelectionModel().getSelectedItem() != null) {
					
					metadataAllTablesComboBox.getSelectionModel().clearSelection();
					
					String tableName = metadataComboBox.getSelectionModel().getSelectedItem();
					updateMetaDataTable(tableName);
				}
			}
		});
		
		metadataAllTablesComboBox.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (metadataAllTablesComboBox.getSelectionModel().getSelectedItem() != null) {
					
					metadataComboBox.getSelectionModel().clearSelection();
				
					String tableName = metadataAllTablesComboBox.getSelectionModel().getSelectedItem();
					updateMetaDataTable(tableName);
				}
			}
		});
	}
	
	private void updateComboBoxes() {
		ArrayList<String> valuesFromDatabase = DALCronus.findAllTables(true);
		contentComboBox.getItems().addAll(valuesFromDatabase);
		metadataComboBox.getItems().addAll(valuesFromDatabase);
		
		valuesFromDatabase = DALCronus.findAllTables(false);
		contentAllTablesComboBox.getItems().addAll(valuesFromDatabase);
		metadataAllTablesComboBox.getItems().addAll(valuesFromDatabase);
		
	}

	private void updateContentTable(String tableName) {
		
		contentDataTable.getColumns().clear();
		contentDataTable.getItems().clear();

		HashMap<Integer, String> columnInfo = null;
		ArrayList<FetchedRow> fetchedRows = null;

		if (tableName.length() > 0) {
			
			columnInfo = DALCronus.findColumnNamesFromTable(tableName);
			
			TableColumn<FetchedRow, String> column = null;
			int indexCounter = 0;
			
			outerloop:
			for (Entry<Integer, String> columnIndexAndName : columnInfo.entrySet()) {
				
				String columnName = columnIndexAndName.getValue();
				final int columnIndex = columnIndexAndName.getKey();
				
				column = new TableColumn<FetchedRow, String>(columnName);
				column.setPrefWidth(50);
				column.setMinWidth(50);
				
				switch (columnIndex) {
				case 1:
					column.setCellValueFactory(cellData -> cellData.getValue().getAttribute1());
					break;
				case 2:
					column.setCellValueFactory(cellData -> cellData.getValue().getAttribute2());
					break;
				case 3:
					column.setCellValueFactory(cellData -> cellData.getValue().getAttribute3());
					break;
				case 4:
					column.setCellValueFactory(cellData -> cellData.getValue().getAttribute4());
					break;
				case 5:
					column.setCellValueFactory(cellData -> cellData.getValue().getAttribute5());
					break;
				default:
					break outerloop;
				}	
				contentDataTable.getColumns().add(indexCounter++, column);
				contentDataTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			}
			
			fetchedRows = DALCronus.findAllContentFromTable(tableName);
			ObservableList<FetchedRow> data = FXCollections.observableArrayList(fetchedRows);
			contentDataTable.getItems().addAll(data);
		}
	}
	
	private void updateMetaDataTable(String tableName) {
		metaDataTable.getItems().clear();
		
		ArrayList<FetchedMetadataRow> fetchedRows = DALCronus.findTableMetaDataRowsFromTable(tableName);
		ObservableList<FetchedMetadataRow> data = FXCollections.observableArrayList(fetchedRows);
		metaDataTable.getItems().addAll(data);
	}
	
	private void updateFunctionsTable() {
		
		functionsDataTable.getColumns().clear();
		functionsDataTable.getItems().clear();
		
		String chosenFunction = functionsComboBox.getSelectionModel().getSelectedItem();
		
		HashMap<Integer, String> columnInfo = DALCronus.findColumnNamesByFunction(chosenFunction);
		TableColumn<FetchedRow, String> column = null;
		int indexCounter = 0;
		
		outerloop:
		for (Entry<Integer, String> columnIndexAndName : columnInfo.entrySet()) {
			
			String columnName = columnIndexAndName.getValue();
			final int columnIndex = columnIndexAndName.getKey();
			
			column = new TableColumn<FetchedRow, String>(columnName);
			column.setPrefWidth(50);
			column.setMinWidth(50);
			
			switch (columnIndex) {
			case 1:
				column.setCellValueFactory(cellData -> cellData.getValue().getAttribute1());
				break;
			case 2:
				column.setCellValueFactory(cellData -> cellData.getValue().getAttribute2());
				break;
			case 3:
				column.setCellValueFactory(cellData -> cellData.getValue().getAttribute3());
				break;
			case 4:
				column.setCellValueFactory(cellData -> cellData.getValue().getAttribute4());
				break;
			case 5:
				column.setCellValueFactory(cellData -> cellData.getValue().getAttribute5());
				break;
			case 6:
				column.setCellValueFactory(cellData -> cellData.getValue().getAttribute6());
				break;
			case 7:
				column.setCellValueFactory(cellData -> cellData.getValue().getAttribute7());
				break;
			case 8:
				column.setCellValueFactory(cellData -> cellData.getValue().getAttribute8());
				break;
			case 9:
				column.setCellValueFactory(cellData -> cellData.getValue().getAttribute9());
				break;
			case 10:
				column.setCellValueFactory(cellData -> cellData.getValue().getAttribute10());
				break;
			default:
				break outerloop;
			}	
			functionsDataTable.getColumns().add(indexCounter++, column);
			functionsDataTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		}
		
		ArrayList<FetchedRow> fetchedRows = null;
		fetchedRows = DALCronus.findAllContentByFunction(chosenFunction);
		ObservableList<FetchedRow> data = FXCollections.observableArrayList(fetchedRows);
		functionsDataTable.getItems().addAll(data);
	}
	
}
