package simvasos.simulation.component;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ActionTest {
    @Test
    public void isImmediateTest() throws Exception {
        Action action_0 = Action.getNullAction(0, "Null action");
        Action action_1 = Action.getNullAction(1, "Null action");

        assertEquals(true, action_0.isImmediate());
        assertEquals(false, action_1.isImmediate());
    }

    @Test
    public void getNullActionTest() throws Exception {
        String actionName = "This is the action name";

        Action action = Action.getNullAction(0, actionName);

        assertEquals(actionName, action.getName());
    }
}