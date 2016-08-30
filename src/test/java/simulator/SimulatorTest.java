package simulator;

import kr.ac.kaist.se.simulator.Simulator;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;

public class SimulatorTest {

    @Test
    public void testConstructor(){
        Constituent[] CSs = {};

        Simulator sim = new Simulator(CSs, null, null);
        assertNotNull(sim);
    }
}
