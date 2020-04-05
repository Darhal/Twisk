package TwiskIG;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import TwiskIG.mondeIG.MondeIG;
import TwiskIG.vues.VueDessin;
import TwiskIG.vues.VueMenu;
import TwiskIG.vues.VueOutils;
import twisk.outils.ThreadsManager;

public class MainTwisk extends Application {
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 900;

    @Override
    public void start(Stage primaryStage) throws Exception{
        BorderPane root = new BorderPane();
        primaryStage.setTitle("Twisk");

        MondeIG m = new MondeIG();
        VueOutils vo = new VueOutils(m);
        VueDessin vd = new VueDessin(m);
        VueMenu vm = new VueMenu(m);

        root.setTop(vm);
        root.setBottom(vo);
        root.setCenter(vd);
        
        m.chargerMonde("DefaultMonde/default.twisk");

        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        //primaryStage.setMaximized(true);
        primaryStage.show();
    }

    @Override
    public void stop(){
        QuitterApplication();
    }

    public static void QuitterApplication()
    {
        ThreadsManager.getInstance().detruireTout();
        Platform.exit();
    }

    public static void main(String[] args){
        launch(args);
    }
}
