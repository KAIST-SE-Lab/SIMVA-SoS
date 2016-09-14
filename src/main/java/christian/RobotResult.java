package christian;


public class RobotResult{
    private boolean h0;
    private int numSample;
    private double prob;
    public RobotResult(boolean h0, int numSample, double prob){
        this.h0 = h0;
        this.numSample = numSample;
        this.prob = prob;
    }

    public String[] getArr(){
        return new String[] {String.format("%.2f", this.prob), Integer.toString(numSample),
                Boolean.toString(this.h0)};
    }

    public void updateResult(int numSample){
        this.numSample = (this.numSample + numSample)/2;
    }
}