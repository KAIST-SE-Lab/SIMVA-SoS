package goalModelSlicer;

import java.util.Set;

public class GoalModelSlicer {
    private Set<Goal> slicedGoals;
    // slicing goal model recursively
    private void sliceGoalModel (Set<Goal> goals, Set<Goal> criterion) {
        slicedGoals = goals;
        for(Goal criterionGoal: criterion) {
            recursiveSlice(criterionGoal.name);
        }
    }
    // recurively slicing model in sliceGoalModel function
    private void recursiveSlice(String goalName) {

        Goal currentGoal = findGoalByName(goalName);

        if (currentGoal.isSliced)
            return;

        currentGoal.isSliced = true;

        for (String higherGoalName: currentGoal.higherGoals) {
            if(higherGoalName.equals("-"))
                return;
            else
                recursiveSlice(higherGoalName);
        }
    }

    public Set<Goal> getSlicedGoals () {
        return slicedGoals;
    }

    public Goal findGoalByName (String goalName) {
        for (Goal goal: slicedGoals) {
            if (goal.name.equals(goalName))
                return goal;
        }
        return null;
    }
}
