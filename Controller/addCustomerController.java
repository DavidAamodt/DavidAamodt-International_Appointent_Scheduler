package project.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.w3c.dom.Text;
import project.Model.Customer;
import project.Utility.Queries;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Objects;
import java.util.ResourceBundle;
/** This class controls the add customer form. */
public class addCustomerController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private ChoiceBox<String> countryChoiceBox;
    @FXML
    private ChoiceBox<String> stateProvinceChoiceBox;
    @FXML
    private TextField nameText;
    @FXML
    private TextField addressText;
    @FXML
    private TextField postalCodeText;
    @FXML
    private TextField phoneText;

    private final String[] countries = {"United States", "United Kingdom", "Canada"};
    private final String[] countryNotSelected = {"Country not selected"};
    private final String[] unitedStates = {"Alabama", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "District of Columbia", "Florida", "Georgia", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming", "Hawaii", "Alaska"};
    private final String[] unitedKingdom = {"England", "Wales", "Scotland", "Northern Ireland"};
    private final String[] canada = {"Northwest Territories", "Alberta", "British Columbia", "Manitoba", "New Brunswick", "Nova Scotia", "Prince Edward Island", "Ontario", "Québec", "Saskatchewan", "Nunavut", "Yukon", "Newfoundland and Labrador"};

    /** Initializes the controller.
     * Sets countryChoiceBox to display countries.
     * Sets StateProvinceChoiceBox to display states or provinces once country is selected.
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryChoiceBox.getItems().addAll(countries);
        stateProvinceChoiceBox.getItems().addAll(countryNotSelected);
        countryChoiceBox.setOnAction(this::countrySelected);
    }
    /** Sets the array of states or provinces once a country is selected in the country ChoiceBox.
     * @param countryEvent This method is triggered by an action event.
     * */
    public void countrySelected(ActionEvent countryEvent) {
        String countrySelected = countryChoiceBox.getValue();
        if(Objects.equals(countrySelected, "United States")){
            stateProvinceChoiceBox.getItems().clear();
            stateProvinceChoiceBox.getItems().addAll(unitedStates);
        } else if (Objects.equals(countrySelected, "United Kingdom")) {
            stateProvinceChoiceBox.getItems().clear();
            stateProvinceChoiceBox.getItems().addAll(unitedKingdom);
        } else if (Objects.equals(countrySelected, "Canada")) {
            stateProvinceChoiceBox.getItems().clear();
            stateProvinceChoiceBox.getItems().addAll(canada);
        }
    }
    /** Redirects back to the customer form.
     * @param actionEvent This method is triggered by an action event.
     * */
    public void exitPressed(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/project/view/customers.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /** Adds a customer to the customer table in the database with the information entered in the form.
     * Errors are displayed if fields are left or incorrect data is entered.
     * @param actionEvent This method is triggered by an action event.
     * */
    public void addCustomerClicked(ActionEvent actionEvent) throws SQLException, IOException {
        try {
            String name = nameText.getText();
            String address = addressText.getText();
            String postalCode = postalCodeText.getText();
            String phone = phoneText.getText();
            String country = countryChoiceBox.getValue();
            String stateProvince = stateProvinceChoiceBox.getValue();

            if(Objects.equals(name, "") || Objects.equals(address, "") || Objects.equals(postalCode, "") || Objects.equals(phone, "")){
                throw new RuntimeException();
            }

            int rowsAffected = Queries.insertCustomer(name, address, postalCode, phone, stateProvince);
            if (rowsAffected > 0) {
                System.out.println("Insert Successful");
            } else {
                System.out.println("Insert Failed");
            }

            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/project/view/customers.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }catch (RuntimeException | SQLIntegrityConstraintViolationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid data entered");
            alert.setContentText("Please be sure all fields are filled out and in the correct format and try again!");
            alert.showAndWait();
        }
    }
}
