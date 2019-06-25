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

    @Override
    public boolean check(String fileName, String time, String vehicleNum, String distance) {

        BufferedReader reader = null;
        try {
            FileReader fr = new FileReader(new File(fileName));
            reader = new BufferedReader(fr);

            String line;
            double criterionT = Double.parseDouble(time);
            double criterionD = Double.parseDouble(distance);
            int currentVechicleNum = 0;
            double currentT = 0;

            while ((line = reader.readLine()) != null) {

                StringTokenizer st = new StringTokenizer(line, "\t ");

                if (st.countTokens() == 8) {
                    st.nextToken();
                     currentT= Double.parseDouble(st.nextToken().trim());
                    if (criterionT == currentT) { // time
                        st.nextToken();
                        if (criterionD <= Double.parseDouble(st.nextToken().trim())) { //distance
                            currentVechicleNum++;
                        }
                    }
                }
                if (currentT>criterionT)
                    break;
            }

            reader.close();
            fr.close();

            if (currentVechicleNum >= criterionD) {
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

