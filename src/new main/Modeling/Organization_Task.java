import java.util.ArrayList;

public class Organization_Task {
    int taskId;
    String taskName;
    int taskTargetGoalId;
    float taskProgress;
    ArrayList<Organization_Task> precedentTasks;
    //ArrayList<Organization_Policy> taskPolicies;

    public Organization_Task() {
        taskId = -1;
        taskName = "noName";
        taskProgress = -1;
        precedentTasks = new ArrayList<Organization_Task>();
        //taskPolicies = new ArrayList<Organization_Policy>();
    }
}
