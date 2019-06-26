package new_simvasos.property;

import new_simvasos.log.Log;
import new_simvasos.log.Snapshot;

import java.io.*;
import java.util.StringTokenizer;

public class PlatooningAbssenceChecker extends PropertyChecker {

    @Override
    protected boolean evaluateState(Snapshot snapshot, Property verificationProperty) {
        return false;
    }

    @Override
    public boolean check(Log log, Property verificationProperty) {
        return false;
    }

    @Override
    public boolean check(Log log, Property verificationProperty, int until) {
        return false;
    }

    @Override
    public boolean check(Log log, Property verificationProperty, double prob, int T) {
        return false;
    }

    @Override
    public boolean check(Log log, Property verificationProperty, double prob, int t, int T) {
        return false;
    }

    @Override
    public boolean check(Log log, Property verificationProperty, int t, int T) {
        return false;
    }

    public double isNum (String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    @Override
    public boolean check(String fileName, String time, String vehicleNum, String distance) {

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(fileName)));

            String line;
            double criterionT = Double.parseDouble(time);
            double criterionD = Double.parseDouble(distance);
            int currentVehicleNum = 0;
            double currentT = 0;

            while ((line = reader.readLine()) != null) {

                StringTokenizer st = new StringTokenizer(line, "\t ");

                if (st.countTokens() == 8) {
                    st.nextToken();
                    String numCheck = st.nextToken();
                    if ((currentT=isNum(numCheck)) == -1)
                        continue;

                    if (criterionT == currentT) { // time
                        st.nextToken();
                        if (criterionD <= Double.parseDouble(st.nextToken().trim())) { //distance
                            currentVehicleNum++;
                        }
                    }
                }
                if (currentT > criterionT)
                    break;
            }

            reader.close();

            if (currentVehicleNum >= Double.parseDouble(vehicleNum)) {
                return true;
            } else {
                return false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

