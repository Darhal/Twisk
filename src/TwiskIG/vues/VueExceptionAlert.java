package TwiskIG.vues;

import TwiskIG.exceptions.TwiskException;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;

public class VueExceptionAlert
{
    private Alert alert;
    public VueExceptionAlert(TwiskException e){
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(e.getType());
        alert.setHeaderText(e.getMessage());

        Label label = new Label("The exception stacktrace was:");
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        TextArea textArea = new TextArea(sw.toString());

        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }

    public void close(){
        for ( ButtonType bt : alert.getDialogPane().getButtonTypes() )
        {
            if ( bt.getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE ) {
                Button cancelButton = ( Button ) alert.getDialogPane().lookupButton( bt );
                cancelButton.fire();
                break;
            }
        }
    }
}
