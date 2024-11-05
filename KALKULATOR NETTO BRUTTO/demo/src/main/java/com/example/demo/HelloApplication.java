package com.example.demo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    private RadioButton nettoToBrutto;
    private RadioButton bruttoToNetto;
    private RadioButton fitToVatAmount;
    private TextField wartoscBazowa;
    private ChoiceBox<String> vatChoice;
    private Label nettoLabel;
    private Label vatLabel;
    private Label bruttoLabel;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Kalkulator VAT netto-brutto");


        ToggleGroup methodGroup = new ToggleGroup();
        nettoToBrutto = new RadioButton("Od netto do brutto");
        nettoToBrutto.setToggleGroup(methodGroup);
        nettoToBrutto.setSelected(true);

        bruttoToNetto = new RadioButton("Od brutto do netto");
        bruttoToNetto.setToggleGroup(methodGroup);

        fitToVatAmount = new RadioButton("Dopasuj do kwoty VAT");
        fitToVatAmount.setToggleGroup(methodGroup);

        VBox methodBox = new VBox(5, nettoToBrutto, bruttoToNetto, fitToVatAmount);
        methodBox.setPadding(new Insets(10));
        TitledPane methodPane = new TitledPane("Metoda Obliczeń", methodBox);
        methodPane.setCollapsible(false);

        // Data Input Section
        wartoscBazowa = new TextField();
        wartoscBazowa.setPromptText("Wartość bazowa: ");

        vatChoice = new ChoiceBox<>();
        vatChoice.getItems().addAll("0%", "5%", "8%", "23%");
        vatChoice.setValue("23%");

        GridPane dataGrid = new GridPane();
        dataGrid.setPadding(new Insets(10));
        dataGrid.setVgap(8);
        dataGrid.setHgap(10);
        dataGrid.add(new Label("Wartość bazowa:"), 0, 0);
        dataGrid.add(wartoscBazowa, 1, 0);
        dataGrid.add(new Label("Stawka VAT:"), 0, 1);
        dataGrid.add(vatChoice, 1, 1);

        TitledPane dataPane = new TitledPane("Dane: ", dataGrid);
        dataPane.setCollapsible(false);

        // Results Section
        nettoLabel = new Label("Netto: ");
        vatLabel = new Label("VAT: ");
        bruttoLabel = new Label("Brutto: ");

        VBox resultsBox = new VBox(5, nettoLabel, vatLabel, bruttoLabel);
        resultsBox.setPadding(new Insets(10));
        TitledPane resultsPane = new TitledPane("Wyniki: ", resultsBox);
        resultsPane.setCollapsible(false);

        // Calculate Button
        Button calculateButton = new Button("Oblicz");
        calculateButton.setOnAction(e -> calculateVat());

        // Main Layout
        VBox mainLayout = new VBox(10, methodPane, dataPane, calculateButton, resultsPane);
        mainLayout.setPadding(new Insets(10));

        Scene scene = new Scene(mainLayout, 300, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void calculateVat() {
        try {
            double baseValue = Double.parseDouble(wartoscBazowa.getText());
            double vatRate = Double.parseDouble(vatChoice.getValue().replace("%", "")) / 100;

            double netto, vatAmount, brutto;

            if (nettoToBrutto.isSelected()) {

                netto = baseValue;
                vatAmount = netto * vatRate;
                brutto = netto + vatAmount;
            } else if (bruttoToNetto.isSelected()) {

                brutto = baseValue;
                vatAmount = brutto * vatRate / (1 + vatRate);
                netto = brutto - vatAmount;
            } else {

                vatAmount = baseValue;
                netto = vatAmount / vatRate;
                brutto = netto + vatAmount;
            }

            nettoLabel.setText(String.format("Netto: %.2f", netto));
            vatLabel.setText(String.format("VAT: %.2f @ %.0f%%", vatAmount, vatRate * 100));
            bruttoLabel.setText(String.format("Brutto: %.2f", brutto));
        } catch (NumberFormatException e) {
            nettoLabel.setText("Netto: Błąd wprowadzania");
            vatLabel.setText("VAT: Błąd wprowadzania");
            bruttoLabel.setText("Brutto: Błąd wprowadzania");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
