package not_decided;

import new_simvasos.not_decided.CS;
import new_simvasos.not_decided.FireFighter;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FireFighterTest {
    double probability = 0.5;
    int location = -1;
    int rescued = 0;
    FireFighter ff = new FireFighter("abc", probability);

    @Test
    public void act() {

    }

    @Test
    public void reset() {
        int rescue = 0;
        assertEquals(rescue, this.rescued);
    }


}