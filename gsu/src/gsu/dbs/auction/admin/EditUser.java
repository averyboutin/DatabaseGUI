package gsu.dbs.auction.admin;


import java.sql.Connection;
import java.sql.ResultSet;
import gsu.dbs.auction.DBConnect;
import gsu.dbs.auction.Launcher;
import gsu.dbs.auction.login.BrowsePage;
import gsu.dbs.auction.newuser.NewUserPage;
import gsu.dbs.auction.ui.Page;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.TableColumn.CellDataFeatures;

public class EditUser extends Page{
	Connection c;
	ObservableList<ObservableList> data;
	ObservableList<ObservableList> fieldData;
	TableView tv;
	HBox hb; //Text Fields
	
	public void loadPage(Pane canvas)  {


		VBox mainPage = new VBox();
		mainPage.setFillWidth(true);
		canvas.getChildren().add(mainPage);

		Launcher.topBar(mainPage, "Administrator");

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(15);
		grid.setVgap(10);
		grid.setPadding(new Insets(50, 50, 50, 50));

		//Vbox for combo box and custom query
		VBox queries = new VBox(25);
	

		//Dropdown menu for admin to pick what to edit
		ObservableList<String> options = FXCollections.observableArrayList(
				"Edit Admins",
				"Edit Access Levels",
				"Edit Bidding Items",
				"Edit Bid History",
				"Edit Customer Reviews",
				"Edit Customers",
				"Edit Invoices",
				"Edit Products",
				"Edit Product Types",
				"Edit Shipments",
				"Edit Shipping Addresses",
				"Edit Sold Items",
				"Edit Status",
				"Edit Stored Items",
				"Edit Users",
				"Edit Vendors"
		);
		final ComboBox<String> comboBox = new ComboBox<String>(options); 
		final Button submitquery = new Button("New Query");
	
		queries.getChildren().addAll(comboBox, submitquery);
		submitquery.setOnAction(new EventHandler<ActionEvent> () {

			@Override
			public void handle(ActionEvent event) {
				queryBox(grid);
			}
		});
		
		
		grid.add(queries, 0, 0);
		
		// Default data (EMPTY)
		build(grid,null);
		
		//Create Table and columns for users
		comboBox.setOnAction((event)->{
			String selected = comboBox.getSelectionModel().getSelectedItem();

			if(selected.contains("User")){
				build(grid, "select * from User");
			}
			if(selected.contains("Admins")) {
				build(grid, "select u.*"
						+ "from User u join Administrator a on u.AccountID = a.AdminID "
						+ "where u.AccessLevel >= 3 ");
			}
			if(selected.contains("Customers")){
				build(grid, "select u.*"
						+ "from User u join Customer c on u.AccountID = c.CustomerID "
						+ "where AccessLevel >= 1 ");
			}    
			if(selected.contains("Vendors")) {				//add in customer review info as well
				build(grid, "select u.* from User u join Vendor v on u.AccountID = v.VendorID "
						+ "where AccessLevel >= 2");
			}
			if(selected.contains("Products")) {
				build(grid, "select * from Products");
			}
			if(selected.contains("Product Type")) {
				build(grid, "select * from Product_Type");
			}
			if(selected.contains("Access Level")) {
				build(grid,"select * from Access_Level");
			}
			if(selected.contains("Customer Reviews")) {
				build(grid,"select * from Customer_Review");
			}
			if(selected.contains("Stored Items")) {
				build(grid, "select * from Stored_Items");
			}
			if(selected.contains("Bidding Items")) {
				build(grid, "select BI.*, P.ProductName, P.ImageURL "
						+ "	from Bidding_Items BI left join Products P "
						+ "ON BI.BiddingItemID = P.ProductID");
			}
			if(selected.contains("Bid History")) {
				build(grid, "select u.Username combo.*"
						+ " from User u left join "
						+ "(select BI.BidNumber, BI.CustomerID, BI.BidPrice, BI.SaleStatus,"
						+ " p.ProductName, p.ImageURL, p.StartingPrice"
						+ " Bid_History BI left join Products p "
						+ "	ON BI.BiddingItemID = p.ProductID) AS c"
						+ "	ON c.CustomerID = u.AccountID");
			}
			if(selected.contains("Status")) {
				build(grid, "select * from Status");
			}
			if(selected.contains("Sold Items")) {
				build(grid, "select * from Sold_Items");
			}
			if(selected.contains("Invoices")) {
				build(grid, "select * from Invoices");
			}
			if(selected.contains("Shipments")) {
				build(grid, "select * from Shipment");
			}
			if(selected.contains("Shipping Addresses")) {
				build(grid, "select * from Shipping_Address");
			}
			if(selected.contains("Shipping Companies")) {
				build(grid, "select * from Shipping_Company");
			}
		});

		//Back Button
		Button back = new Button("Back to browse page");
		Button addUser = new Button("Add User");
		Hyperlink cantfind = new Hyperlink("Can't find what you're looking for?");
		
		HBox hbBack = new HBox();
		hbBack.setAlignment(Pos.BOTTOM_CENTER);
		hbBack.getChildren().addAll(back,addUser);
		hbBack.setSpacing(25);
		grid.add(hbBack, 0, 3);

		back.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				Launcher.loadPage(new BrowsePage());

			}
		});

		addUser.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				// Insert code to add to database here

			}
		});
		
		
		mainPage.getChildren().addAll(grid);

	}
	
	private void queryBox(GridPane grid) {
        final Stage dialog = Launcher.popup();
        StackPane pane = new StackPane();
        pane.setPadding(new Insets(8,8,8,8));
        Scene dialogScene = new Scene(pane, 500, 200);
        dialog.setScene(dialogScene);
        dialog.show();
        
        VBox dialogVbox = new VBox(4);
        pane.getChildren().add(dialogVbox);
        dialogVbox.getChildren().add(new Text("Type your query below:"));
        
        TextArea text = new TextArea();
        dialogVbox.getChildren().add(text);
        
        Button submit = new Button("Submit");
        dialogVbox.getChildren().add(submit);
        
        submit.setOnAction(event -> {
        		String SQL = text.getText();
        		dialog.close();
        		build(grid, SQL);
        });
	}

	private void build(GridPane grid, String string) {
		if ( hb != null ) {
			hb.getChildren().clear();
		}
		buildData(string);
		buildFields(tv);
		grid.add(tv, 0, 1);
		grid.add(hb, 0, 2);
	}

	private void buildData(String query){
		data = FXCollections.observableArrayList();
		tv = new TableView();
		tv.setPrefWidth(Integer.MAX_VALUE);
		
		try{
			if ( query != null ) {
				c = DBConnect.getConnection();
				//Query for SQL
				String SQL = query;
				//Result
				ResultSet res = c.createStatement().executeQuery(SQL);
				//Adds the columns to the Table
				for(int i=0 ; i<res.getMetaData().getColumnCount(); i++){
					final int j = i;                
					TableColumn col = new TableColumn(res.getMetaData().getColumnName(i+1));
	
					col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
						public ObservableValue<String> call(CellDataFeatures<ObservableList, String> s) {
							ObservableList cell = s.getValue();
							Object t = cell.get(j);
							String str = "";
							if ( t != null ) {
								str = t.toString();
							}
							SimpleStringProperty prop = new SimpleStringProperty(str);
							return prop;                        
						}
					});
	
					tv.getColumns().add(col); 
					System.out.println("Column ["+i+"] ");
				}
	
	
	
				//Add Data to ObservableList 
				while(res.next()){
					ObservableList<String> row = FXCollections.observableArrayList();
					for(int i=1 ; i<=res.getMetaData().getColumnCount(); i++){
						row.add(res.getString(i));
					}
					System.out.println("Row [1] added " + row );
					data.add(row);
	
				}
			}

			//Add to tableview
			tv.setItems(data);

		}catch(Exception e){
			Launcher.error(e.getMessage());
			//e.printStackTrace();
			//System.out.println("Whoops... Something happened.");             
		}

	}

	private void buildFields(TableView view) {
		fieldData = FXCollections.observableArrayList();
		hb = new HBox();
		hb.setPadding(new Insets(25,25,25,25));
		hb.setSpacing(15);
		
		ObservableList columns = view.getColumns();
		for (int i = 0; i < columns.size(); i++) {
			TableColumn col = (TableColumn) columns.get(i);
			String colName = col.getText();
			
			final TextField field = new TextField();
			field.setPromptText(colName);
			hb.getChildren().add(field);
		}
	}
}
