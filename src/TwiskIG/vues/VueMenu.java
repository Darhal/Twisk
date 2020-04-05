package TwiskIG.vues;

import TwiskIG.MainTwisk;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import TwiskIG.mondeIG.MondeIG;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import twisk.monde.Entree;

import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

public class VueMenu extends MenuBar implements Observer
{
    private Menu m_Fichier, m_Edition, m_Monde, m_Parametres;
    private MondeIG monde;

    public VueMenu(MondeIG m){
        this.monde = m;

        m_Fichier = new Menu("Fichier");
        MenuItem mNouveau = new MenuItem("Nouveau");
        MenuItem mOpen = new MenuItem("Ouvrir");
        MenuItem mSave = new MenuItem("Enregistrer Sous");
        MenuItem mQuitter = new MenuItem("Quitter");
        mNouveau.setOnAction(event -> monde.clearWorld());
        mQuitter.setOnAction(event -> this.QuitterApplication());
        mSave.setOnAction(event -> this.EnregistrerSous());
        mOpen.setOnAction(event -> this.Ouvrir());
        m_Fichier.getItems().add(mNouveau);
        m_Fichier.getItems().add(mOpen);
        m_Fichier.getItems().add(mSave);
        m_Fichier.getItems().add(mQuitter);

        m_Edition = new Menu("Édition");
        MenuItem supSelection = new MenuItem("Supprimer la sélection");
        MenuItem renomSelection = new MenuItem("Renommer");
        MenuItem effacerSelection = new MenuItem("Effacer la sélection");
        supSelection.setOnAction(event -> m.supprimerLaSelectionner());
        renomSelection.setOnAction(event -> m.renommerEtapeSelectionner());
        effacerSelection.setOnAction(event -> m.effacerLaSelection());
        m_Edition.getItems().add(supSelection);
        m_Edition.getItems().add(renomSelection);
        m_Edition.getItems().add(effacerSelection);

        m_Monde = new Menu("Monde");
        MenuItem entree = new MenuItem("Entrée");
        MenuItem sortie = new MenuItem("Sortie");
        MenuItem loiEntree = new MenuItem("Loi d'entrée");
        MenuItem nbClients = new MenuItem("Modifier Nb Clients");
        entree.setOnAction(event -> m.toggleCommeEntree());
        sortie.setOnAction(event -> m.toggleCommeSortie());
        loiEntree.setOnAction(event -> chooseLoi());
        nbClients.setOnAction(event -> modifierNbClient());
        m_Monde.getItems().add(entree);
        m_Monde.getItems().add(sortie);
        m_Monde.getItems().add(loiEntree);
        m_Monde.getItems().add(nbClients);

        m_Parametres = new Menu("Paramètres");
        MenuItem modif_delai = new MenuItem("Modifier delai");
        MenuItem modif_ecart_temp = new MenuItem("Modifier ecart temp");
        MenuItem modif_nb_jetons = new MenuItem("Modifier Le Nb de jetons");
        modif_delai.setOnAction(event -> m.attemptModifierDelai());
        modif_ecart_temp.setOnAction(event -> m.attemptModifierEcartTemp());
        modif_nb_jetons.setOnAction(event -> m.attemptModifierNbJetons());
        m_Parametres.getItems().add(modif_delai);
        m_Parametres.getItems().add(modif_ecart_temp);
        m_Parametres.getItems().add(modif_nb_jetons);

        this.getMenus().add(m_Fichier);
        this.getMenus().add(m_Edition);
        this.getMenus().add(m_Monde);
        this.getMenus().add(m_Parametres);

        monde.addObserver(this);
    }

    private void modifierNbClient()
    {
        TextInputDialog nbClientDialog = new TextInputDialog();
        nbClientDialog.setTitle("Modifier le nombre du clients");
        nbClientDialog.setHeaderText("Entrez la nouvelle valeur");
        nbClientDialog.setContentText("Veuillez entrer la nouvelle valeur de Nb Clients:");
        Optional<String> nbClient = nbClientDialog.showAndWait();
        if (nbClient.isPresent()){
            try {
                int nb = Integer.parseInt(nbClient.get());
                System.out.println(nb);
                monde.modifierNbCLients(nb);
            }catch (Exception e){

            }
        }
    }

    private void Ouvrir()
    {
        FileChooser fileChooser = new FileChooser();
        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TWISK Files (*.twisk)", "*.twisk");
        fileChooser.getExtensionFilters().add(extFilter);
        //Show open file dialog
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            try {
                this.monde.open(file.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void EnregistrerSous()
    {
        FileChooser fileChooser = new FileChooser();
        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TWISK Files (*.twisk)", "*.twisk");
        fileChooser.getExtensionFilters().add(extFilter);
        //Show save file dialog
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            try {
                this.monde.save(file.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void chooseLoi()
    {
        BorderPane bp = new BorderPane();
        VBox vbox = new VBox();
        vbox.setSpacing(15.f);
        bp.setLeft(vbox);
        vbox.setAlignment(Pos.BASELINE_CENTER);

        String lois[] = { "Loi Uniforme", "Loi Gaussien", "Loi Exponentiel" };

        // Create a combo box
        HBox h0 = new HBox();
        ComboBox loiComboBox = new ComboBox(FXCollections.observableArrayList(lois));
        HBox h1 = new HBox();
        Label l1 = new Label("Field 1 : ");
        TextField f1 = new TextField("");
        h1.getChildren().addAll(l1, f1);
        h0.getChildren().addAll(new Label("Lois : "), loiComboBox);

        HBox h2 = new HBox();
        Label l2 = new Label("Field 1 : ");
        TextField f2 = new TextField("");

        HBox h3 = new HBox();
        Button confirm = new Button("Confirmer");
        Button cancel = new Button("Cancel");
        h3.getChildren().addAll(confirm, cancel);

        h2.getChildren().addAll(l2, f2);

        h2.setVisible(false);
        h1.setVisible(false);

        vbox.getChildren().addAll(h0, h1, h2, h3);
        Scene loiScene = new Scene(bp, 300, 160);
        Stage loiWindow = new Stage();
        loiWindow.setTitle("Choix du loi d'entrée");
        loiWindow.setScene(loiScene);
        loiWindow.initStyle(StageStyle.DECORATED);
        loiWindow.initModality(Modality.NONE);
        loiWindow.show();

        loiComboBox.valueProperty().addListener((obs, oldItem, newItem) -> {
            h2.setVisible(false);
            h1.setVisible(false);
            if(loiComboBox.getValue().equals(lois[0])){ // Uniforme
                h2.setVisible(true);
                h1.setVisible(true);
                l1.setText("Temps : ");
                l2.setText("Ecart Temps : ");
            }else if(loiComboBox.getValue().equals(lois[1])){ // Gaussian
                h2.setVisible(true);
                h1.setVisible(true);
                l1.setText("Moyenne : ");
                l2.setText("Ecart Type : ");
            }else if(loiComboBox.getValue().equals(lois[2])){ // Exponential
                h1.setVisible(true);
                l1.setText("Exponential : ");
            }
        });
        loiComboBox.getSelectionModel().selectFirst();

        EventHandler<ActionEvent> confirmEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                float field1 = 1.f, field2 = 2.f;
                Entree.PROBA_TYPE type = Entree.PROBA_TYPE.UNIFORM;
                if (h1.isVisible()){
                    String f1Str = f1.getText();
                    try{
                        field1 = Float.parseFloat(f1Str);
                    }catch (Exception e){
                        return;
                    }
                }
                if (h2.isVisible()){
                    String f2Str = f2.getText();
                    try{
                        field2 = Float.parseFloat(f2Str);
                    }catch (Exception e){
                        return;
                    }
                }
                if(loiComboBox.getValue().equals(lois[1])){ // Gaussian
                    type = Entree.PROBA_TYPE.GAUSSIAN;
                }else if(loiComboBox.getValue().equals(lois[2])){ // Exponential
                    type = Entree.PROBA_TYPE.EXPONENTIAL;
                }
                monde.setLoiEntree(type, field1, field2);
                loiWindow.close();
            }
        };
        confirm.setOnAction(confirmEvent);

        EventHandler<ActionEvent> cancelEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loiWindow.close();
            }
        };
        cancel.setOnAction(cancelEvent);
    }

    public void QuitterApplication()
    {
        MainTwisk.QuitterApplication();
    }

    @Override
    public void update(Observable o, Object arg) {
        this.setDisable(monde.IsSimulationActive());
    }
}
