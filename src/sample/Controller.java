package sample;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {


    @FXML
    private TextField addMoneyField;
    @FXML
    private ChoiceBox<Account> accountSelection;
    @FXML
    private TextField moneyTransferField;
    @FXML
    private TextField accountNameField;
    @FXML
    private TextField accountTransferField;
    @FXML
    private Label balanceLabel;
    @FXML
    private Label nameLabel;

    private List<Account> accounts = new ArrayList<>();
    /**Path where accounts are saved: C:/Users/user**/
    private final String path = System.getProperty("user.home") + "/accounts";
    Account currentlySelected = null;

    //checks whether 1) selected user exists 2) user has enough money and sends it over
    //also if money is empty then transfer all
    @FXML
    void handleTransfer(ActionEvent event) {
        for(Account a : accounts){
            if(a.getName().equals(accountTransferField.getText()) && !accountNameField.getText().equals(currentlySelected.getName())){
                if(moneyTransferField.getText().isEmpty()){
                    a.transferBalance(currentlySelected.getBalance());
                    currentlySelected.setBalance(0);
                }
                else if(currentlySelected.getBalance() >= Double.parseDouble(moneyTransferField.getText())) {
                    a.transferBalance(Double.parseDouble(moneyTransferField.getText()));
                    currentlySelected.transferBalance(-(Double.parseDouble(moneyTransferField.getText())));
                }
                else
                    System.out.println("Not enough money on account!");
            }
        }
        updateAccount();
    }

    //after Select button is clicked, selects it and updates screen
    @FXML
    private void handleUpdateScreen(ActionEvent event){
        currentlySelected = accountSelection.getValue();
        updateAccount();
    }

    //creates account and adds it to arraylist
    @FXML
    void handleCreate(ActionEvent event) {
        accounts.add(new Account(accountNameField.getText(), Account.numOfAccounts));
        accountSelection.setItems(FXCollections.observableArrayList(accounts));
    }

    //adds money to user's account
    @FXML
    void handleMoneyAdd(ActionEvent event) {
        currentlySelected.transferBalance(Double.parseDouble(addMoneyField.getText()));
        updateAccount();
    }
    //checks if user has enough money in his account and withdraws the amount selected
    @FXML
    private void handleMoneyWithdraw(ActionEvent event){
        if(currentlySelected.getBalance() >= Double.parseDouble(addMoneyField.getText())) {
            currentlySelected.transferBalance(-(Double.parseDouble(addMoneyField.getText())));
        }
        else{
            System.out.println("Not enough money!");
        }
        updateAccount();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadAccounts();
        Account.numOfAccounts = accounts.size();
        accountSelection.setItems(FXCollections.observableArrayList(accounts));
    }

    //serializes ArrayList accounts and saves it to file called "accounts"
    @FXML
    private void saveAccounts() {
        try{
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
            oos.writeObject(accounts);
            oos.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        //System.out.println("Successfully saved!");
    }

    //load accounts from file on startup
    @FXML
    private void loadAccounts(){
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
            accounts = (ArrayList)ois.readObject();
            ois.close();
        }
        catch(FileNotFoundException fnfe){
            System.out.println("File doesn't exist yet!");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    //utility method for updating screen
    private void updateAccount(){
        nameLabel.setText(currentlySelected.getName());
        balanceLabel.setText(currentlySelected.getBalance() + "CZK");
        saveAccounts();
    }
}
