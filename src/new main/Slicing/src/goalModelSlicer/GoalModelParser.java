package goalModelSlicer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class GoalModelParser {

    private Set<Goal> goals = new HashSet<Goal>();;

    void parseGoalModel(File file) {
        String cvsSplit = ",";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] goalItems = line.split(cvsSplit);
                Goal goal = new Goal();
                goal.name = goalItems[0];
                goal.subject = goalItems[1];
                goal.formalDescription = goalItems[2];
                StringTokenizer st = new StringTokenizer(goalItems[3], ";");
                while (st.hasMoreTokens()) {
                    goal.higherGoals.add(st.nextToken());
                }

                st = new StringTokenizer(goalItems[4], ";");
                while (st.hasMoreTokens()) {
                    goal.subGoals.add(st.nextToken());
                }

                goal.explanation = goalItems[5];

                goals.add(goal);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    Set<Goal> getGoals(){
        return goals;
    }

    public String getGoalLog () {
        String goalLog = "";
        for(Goal goal: goals) {
            goalLog += ("[" + goal.name + "]\r\n");
            goalLog += ("\t- subject: " + goal.subject + "\r\n");
            goalLog += ("\t- formal description: " + goal.formalDescription + "\r\n");
            goalLog += ("\t- higher goals: ");
            for(String higherGoal: goal.higherGoals) {
                goalLog += (higherGoal + ", ");
            }
            goalLog += "\r\n";
            goalLog += ("\t- subgoals: ");
            for(String subGoal: goal.subGoals) {
                goalLog += (subGoal + ", ");
            }
            goalLog += "\r\n";
            goalLog += ("\t- explanation: " + goal.explanation + "\r\n");
        }
        return goalLog;
    }

}
