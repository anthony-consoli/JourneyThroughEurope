/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JTE.game;

/**
 *
 * @author Anthony
 */

import java.util.HashMap;
import JTE.file.JTEFileLoader;
import java.io.File;


public class JTEGameData {
    
    //SCHEMA FILE NAME
    File schemaFile = new File("data/PathXLevelSchema.xsd");
    //CITY XML FILE
    File citiesFile = new File("data/Cali.xml");
    //FILE LOADER FOR CITIES
    //private JTEFileLoader fileLoader = new JTEFileLoader(schemaFile);
    //HASHMAP OF CITIES
    HashMap<String,City> cities;
    
    public JTEGameData()
    {
        //cities = fileLoader.loadCities(citiesFile);
    }
    
    public HashMap<String, City> getCities()
    {
        return cities;
    }        

    
}
