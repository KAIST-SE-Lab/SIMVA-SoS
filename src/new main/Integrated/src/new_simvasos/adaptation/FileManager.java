package new_simvasos.adaptation;

import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;

public class FileManager {
    public static ArrayList<Pair<String, String>> readConfiguration(String configFile){
        ArrayList<Pair<String, String>> configs =  new ArrayList<Pair<String, String>>();

        try{
            File file = new File(configFile);
            FileReader filereader = new FileReader(file);
            BufferedReader bufReader = new BufferedReader(filereader);
            String line = "";
            while((line = bufReader.readLine()) != null){
               String[] parts = line.split(":");
               configs.add(new Pair<String, String>(parts[0], parts[1]));
            }
            bufReader.close();
        }catch(IOException e){
            System.out.println(e);
        }

        //return a dictionary
        return configs;
    }

    public static String getValueFromConfigDictionary(ArrayList<Pair<String, String>> configs, String keyword){
        for(Pair<String, String> p : configs){
            if(p.getKey().equals(keyword))
                return p.getValue();
        }
        return null;
    }
}
