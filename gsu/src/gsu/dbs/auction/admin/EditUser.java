package gsu.dbs.auction.admin;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import gsu.dbs.auction.DBConnect;
import gsu.dbs.auction.Launcher;
import gsu.dbs.auction.TestConnection;
import gsu.dbs.auction.login.BrowsePage;
import gsu.dbs.auction.ui.Page;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
//
public class EditUser extends Page{
	 Connection c ;
   	 ObservableList<ObservableList> data;
   	 TableView tv;
	
	public void loadPage(Pane canvas)  {
		
		VBox mainPage = new VBox();
		mainPage.setFillWidth(true);
		canvas.getChildren().add(mainPage);
		Launcher.topBar(mainPage, "Edit Users");
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(15);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));    

		//Create Table and columns for users
		tv = new TableView();
		buildData("select * from User");
	    grid.add(tv, 1, 0);
       
        //Create Text Fields for adding new User info
        final TextField addAccID = new TextField();
        addAccID.setPromptText("AccountID");
        final TextField addUsername = new TextField();
        addUsername.setPromptText("Username");
        final TextField addPassword = new TextField();
        addPassword.setPromptText("Password");
        final TextField addDateCreated = new TextField();
        addDateCreated.setPromptText("Date Created");
        final TextField addAccessLevel = new TextField();
        addAccessLevel.setPromptText("Access Level");
        final TextField addEmail = new TextField();
        addEmail.setPromptText("Email");
        final TextField addAge = new TextField();
        addAge.setPromptText("Age");
        final TextField addLoginDate = new TextField();
        addLoginDate.setPromptText("Login Date");
        
        HBox hb = new HBox();
        hb.setPadding(new Insets(25,25,25,25));
        hb.setSpacing(15);
        hb.getChildren().addAll(addAccID, addUsername, addPassword, addDateCreated, addAccessLevel, addEmail, addAge, addLoginDate);
        
        grid.add(hb, 1, 1);

		//Back Button
		Button back = new Button("Back to browse page");
		Button addUser = new Button("Add User");
		HBox hbBack = new HBox();
		hbBack.setAlignment(Pos.BOTTOM_CENTER);
		hbBack.getChildren().addAll(back,addUser);
		hbBack.setSpacing(25);
		grid.add(hbBack, 1, 2);
		
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
		
		mainPage.getChildren().add(grid);
		
	}
	
	 public void buildData(String query){
         data = FXCollections.observableArrayList();
         try{
           c = DBConnect.connect();
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
                       return new SimpleStringProperty(s.getValue().get(j).toString());                        
                   }                    
               });

               tv.getColumns().addAll(col); 
               System.out.println("Column ["+i+"] ");
           }

        
           
           //Add Data to ObservableList 
           while(res.next()){
               ObservableList<String> row = FXCollections.observableArrayList();
               for(int i=1 ; i<=res.getMetaData().getColumnCount(); i++){
                   row.add(res.getString(i));
               }
               System.out.println("Row [1] added "+row );
               data.add(row);

           }

           //Add to tableview
           tv.setItems(data);
         }catch(Exception e){
             e.printStackTrace();
             System.out.println("Whoops... Something happened.");             
         }
     }
	
}
