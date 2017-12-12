/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.controllers;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;
import librarymanagementsystem.LibraryManagementSystem;

/**
 * FXML Controller class
 *
 * @author danml
 */
public class DashboardController implements Initializable {

    @FXML
    private Label currentUserName;

    @FXML
    private Hyperlink logoutLink;

    @FXML
    private JFXButton borrowerBtn;

    @FXML
    private JFXButton bookBtn;

    @FXML
    private JFXButton scanBtn;

    @FXML
    private JFXButton settingsBtn;

    @FXML
    private AnchorPane dashboardContentArea;

    @FXML
    void logOut(ActionEvent event) {
        // close current view
        LibraryManagementSystem.showView("user.login");
        // set current user to null
    }

    @FXML
    void openBookRecord(ActionEvent event) {
        LibraryManagementSystem.showView("book.manage");
    }

    @FXML
    void openBorrower(ActionEvent event) {
        LibraryManagementSystem.showView("borrower.manage");
    }

    @FXML
    void openSystemUser(ActionEvent event) {
        LibraryManagementSystem.showView("user.manage");
    }

    @FXML
    void openScanPanel(ActionEvent event) {
        LibraryManagementSystem.showView("scanner.manage");
    }

    @FXML
    void openSettings(ActionEvent event) {
        LibraryManagementSystem.showView("settings.manage");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // set the current user logged in's name
        // set the current user logged in's role
        // load number of borrowed books
        // load number of books with issue and its borrower

    }

    @FXML
    void maximize(MouseEvent event) {
        LibraryManagementSystem.APP_ROOT_PANE.setMaximized(true);
    }

    @FXML
    void compress(MouseEvent event) {
        LibraryManagementSystem.APP_ROOT_PANE.setMaximized(false);
    }

    @FXML
    void viewAllActiveBooks(ActionEvent event) {

    }

    @FXML
    void viewAllBorrowedBooks(ActionEvent event) {

    }

    @FXML
    void viewAllOverDueBooks(ActionEvent event) {

    }

    @FXML
    void openBorrowBookView(ActionEvent event) {
        LibraryManagementSystem.showView("book.borrow");
    }

    @FXML
    void openReturnBookView(ActionEvent event) {
        LibraryManagementSystem.showView("book.return");
    }

    @FXML
    void openPenaltyReportView(ActionEvent event) {
        LibraryManagementSystem.showView("book.penalty");
    }

}
