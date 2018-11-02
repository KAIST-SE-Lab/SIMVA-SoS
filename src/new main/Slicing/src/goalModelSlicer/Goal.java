package goalModelSlicer;

import java.util.HashSet;
import java.util.Set;

public class Goal {
    String name;
    String subject;
    String formalDescription;
    Set<String> higherGoals = new HashSet<String>();
    Set<String> subGoals = new HashSet<String>();
    String explanation;
    boolean isSliced = false;
}
