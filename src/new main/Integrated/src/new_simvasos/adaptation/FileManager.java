package new_simvasos.adaptation;

import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import new_simvasos.log.Log;
import new_simvasos.log.Snapshot;

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

    public static void saveLogCSV(Log log, String filePath, String keyword){
        File file = new File(filePath);
        try {
            FileWriter fw = new FileWriter(file);

            HashMap<Integer, Snapshot> snapshotMap = log.getSnapshotMap();
            Iterator<Integer> keys = snapshotMap.keySet().iterator();
            while(keys.hasNext()) {
                Integer key = keys.next();

                String snapshotStr = snapshotMap.get(key).getSnapshotString();

                StringTokenizer st = new StringTokenizer(snapshotStr, " :");
                while(st.hasMoreTokens()) {
                    if(st.nextToken().equals(keyword))
                        break;
                }

                if(st.hasMoreTokens()){
                    Double keywordValue = Double.parseDouble(st.nextToken());
                    fw.write(keywordValue.toString() + "\n");
                }
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveAllLogCSV(Log log, String filePath){
        File file = new File(filePath);
        try {
            FileWriter fw = new FileWriter(file);

            HashMap<Integer, Snapshot> snapshotMap = log.getSnapshotMap();
            Iterator<Integer> keys = snapshotMap.keySet().iterator();
            while(keys.hasNext()) {
                Integer key = keys.next();

                String snapshotStr = snapshotMap.get(key).getSnapshotString();

                StringTokenizer st = new StringTokenizer(snapshotStr, " :");
                while(st.hasMoreTokens()) {
                    st.nextToken();
                    fw.write(st.nextToken() + ",");
                }
                fw.write("\n");
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveAllLogTXT(Log log, String filePath){
        File file = new File(filePath);
        try {
            FileWriter fw = new FileWriter(file);

            HashMap<Integer, Snapshot> snapshotMap = log.getSnapshotMap();
            Iterator<Integer> keys = snapshotMap.keySet().iterator();
            while(keys.hasNext()) {
                Integer key = keys.next();
                fw.write("TICK:" + key.toString() + " " + snapshotMap.get(key).getSnapshotString() + "\n");
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
