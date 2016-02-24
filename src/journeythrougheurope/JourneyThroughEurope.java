/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope;

import properties_manager.PropertiesManager;
import JTE.ui.JTEUI;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Anthony
 */
public class JourneyThroughEurope extends Application {
    
    
    static String PROPERTY_TYPES_LIST = "property_types.txt";
    static String UI_PROPERTIES_FILE_NAME = "properties.xml";
    static String PROPERTIES_SCHEMA_FILE_NAME = "properties_schema.xsd";
    static String DATA_PATH = "./data/";
    
    @Override
    public void start(Stage primaryStage) {
        try {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            props.addProperty(JTEPropertyType.UI_PROPERTIES_FILE_NAME, UI_PROPERTIES_FILE_NAME);
            props.addProperty(JTEPropertyType.PROPERTIES_SCHEMA_FILE_NAME,
                    PROPERTIES_SCHEMA_FILE_NAME);
            props.addProperty(JTEPropertyType.DATA_PATH.toString(),
                    DATA_PATH);
            props.loadProperties(UI_PROPERTIES_FILE_NAME,
                    PROPERTIES_SCHEMA_FILE_NAME);

            // GET THE LOADED TITLE AND SET IT IN THE FRAME
            String title = props.getProperty(JTEPropertyType.SPLASH_SCREEN_TITLE_TEXT);
            primaryStage.setTitle(title);

            JTEUI root = new JTEUI();
            BorderPane mainPane = root.GetMainPane();
            root.SetStage(primaryStage);

            // SET THE WINDOW ICON
            String mainPaneIconFile = props.getProperty(JTEPropertyType.WINDOW_ICON);
            Image mainPaneIcon = root.loadImage(mainPaneIconFile);
            primaryStage.getIcons().add(mainPaneIcon);

            Scene scene = new Scene(mainPane, mainPane.getWidth(), mainPane.getHeight());
            scene.getStylesheets().add(JTEUI.class.getResource("JTESplash.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public enum JTEPropertyType {
        /* SETUP FILE NAMES */

        UI_PROPERTIES_FILE_NAME, PROPERTIES_SCHEMA_FILE_NAME,NEIGHBORS_SCHEMA_NAME, CITIES_SCHEMA_NAME,
        /* DIRECTORIES FOR FILE LOADING */
        DATA_PATH, IMG_PATH,
        /* WINDOW DIMENSIONS */
        WINDOW_WIDTH, WINDOW_HEIGHT,
        /* GAME TEXT */
        SPLASH_SCREEN_TITLE_TEXT, GAME_TITLE_TEXT, GAME_SUBHEADER_TEXT, WIN_DISPLAY_TEXT, LOSE_DISPLAY_TEXT, GAME_RESULTS_TEXT, GUESS_LABEL, EXIT_REQUEST_TEXT, YES_TEXT, NO_TEXT, DEFAULT_YES_TEXT, DEFAULT_NO_TEXT, DEFAULT_EXIT_TEXT,
        /* IMAGE FILE NAMES */
        WINDOW_ICON, SPLASH_SCREEN_IMAGE_NAME, GAME_IMG_NAME, DIE1_IMG_NAME, DIE2_IMG_NAME, DIE3_IMG_NAME, DIE4_IMG_NAME, DIE5_IMG_NAME, DIE6_IMG_NAME, PLAYER_BLACK_IMG_NAME, PLAYER_YELLOW_IMG_NAME, PLAYER_BLUE_IMG_NAME, PLAYER_RED_IMG_NAME, FLAG_RED_IMG_NAME, FLAG_GREEN_IMG_NAME, FLAG_WHITE_IMG_NAME, QUAD_1_IMG_NAME, QUAD_3_IMG_NAME, QUAD_2_IMG_NAME, QUAD_4_IMG_NAME,
        /* DATA FILE STUFF */
        NEIGHBORS_FILE_NAME, CITIES_FILE_NAME, SRS_TEXT_FILE_NAME, SAVE_FILE_NAME, FLIGHT_FILE_NAME

    }
    
}
