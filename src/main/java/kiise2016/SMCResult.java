package kiise2016;


public class SMCResult {

    private double prob;
    private int numOfSamples;
    private long executionTime;
    private int minTick;
    private int maxTick;
    private boolean result;

    public SMCResult(double prob, int numOfSamples, long executionTime, int minTick, int maxTick, boolean result){
        this.prob = prob;
        this.numOfSamples = numOfSamples;
        this.executionTime = executionTime;
        this.minTick = minTick;
        this.maxTick = maxTick;
        this.result = result;
    }

    public String toString(){
        String retStr = "";
        retStr += String.format("%.2f", this.prob);
        retStr += "," + numOfSamples;
        retStr += String.format("%.3f", (this.executionTime/1000.0));
        retStr += "," + this.minTick + "," + this.maxTick;
        retStr += "," + this.result;
        return retStr;
    }

    public String[] getArr(){
        return new String[] {String.format("%.2f", this.prob), Integer.toString(numOfSamples),
                String.format("%.3f", (this.executionTime/1000.0)), Integer.toString(this.minTick),
                Integer.toString(this.maxTick), Boolean.toString(this.result)};
    }

}
