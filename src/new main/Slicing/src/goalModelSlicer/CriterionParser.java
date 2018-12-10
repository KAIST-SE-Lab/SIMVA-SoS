package goalModelSlicer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class CriterionParser {
    //parsing xml file which contains criterion
    public Set<Goal> parseCriterion (File file) {
        Set<Goal> criteriaGoals = new HashSet<Goal>();
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(file));
            String line;

            while((line = reader.readLine()) != null) {
                Goal tempGoal = new Goal();
                tempGoal.name = line;
                criteriaGoals.add(tempGoal);
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return criteriaGoals;
    }
}
