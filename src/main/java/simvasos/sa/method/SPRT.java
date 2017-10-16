package simvasos.sa.method;

import simvasos.sa.StatisticalAnalyzer;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Created by mgjin on 2017-06-21.
 */
public class SPRT extends StatisticalAnalyzer {
    private final static int MINIMUM_SAMPLES = 2;

    private BigDecimal alpha = new BigDecimal("0.05");
    private BigDecimal beta = new BigDecimal("0.05");
    private BigDecimal delta = new BigDecimal("0.01"); // 99% confidence interval

    private BigDecimal p0 = null; // theta + delta
    private BigDecimal p1 = null; // theta - delta

    private int totalSamples = 0;
    private int positiveSamples = 0; // dm

    private BigDecimal probRatioA = null;
    private BigDecimal probRatioB = null;

    private boolean h0decision = false;

    public void reset(double alpha, double beta, double delta, double theta) {
        this.alpha = new BigDecimal(String.valueOf(alpha));
        this.beta = new BigDecimal(String.valueOf(beta));
        this.delta = new BigDecimal(String.valueOf(delta));

        //
        boolean lessCheck = true; // for Robot
        //

        if (!lessCheck) {
            this.p0 = new BigDecimal(String.valueOf(theta)).add(this.delta).subtract(new BigDecimal(Double.toString(Double.MIN_VALUE)));
            this.p1 = new BigDecimal(String.valueOf(theta)).subtract(this.delta).add(new BigDecimal(Double.toString(Double.MIN_VALUE)));
        } else {
            this.p1 = new BigDecimal(String.valueOf(theta)).add(this.delta).subtract(new BigDecimal(Double.toString(Double.MIN_VALUE)));
            this.p0 = new BigDecimal(String.valueOf(theta)).subtract(this.delta).add(new BigDecimal(Double.toString(Double.MIN_VALUE)));
        }

        this.totalSamples = 0;
        this.positiveSamples = 0;

        this.probRatioA = new BigDecimal(String.valueOf(1)); // new BigDecimal("1");
        this.probRatioA = this.probRatioA.subtract(this.beta);
        this.probRatioA = this.probRatioA.divide(this.alpha, MathContext.DECIMAL32);

        this.probRatioB = new BigDecimal(String.valueOf(1)); // new BigDecimal("1");
        this.probRatioB = this.probRatioB.subtract(this.alpha);
        this.probRatioB = this.beta.divide(this.probRatioB, MathContext.DECIMAL32);

        this.h0decision = false;
    }

    public void addSample(boolean sample) {
        this.totalSamples++;

        if (sample)
            this.positiveSamples++;
    }

    public boolean isSampleNeeded() {
        if (this.totalSamples < SPRT.MINIMUM_SAMPLES)
            return true;

        BigDecimal p1m = null, p0m = null, v = null;

//        p1m = new BigDecimal("1");
//        p1m = p1m.subtract(this.p1);
//        p1m = p1m.pow(this.totalSamples - this.positiveSamples, MathContext.DECIMAL32);
//        p1m = p1m.multiply(this.p1.pow(this.positiveSamples, MathContext.DECIMAL32));
//
//        p0m = new BigDecimal("1");
//        p0m = p0m.subtract(this.p0);
//        p0m = p0m.pow(this.totalSamples - this.positiveSamples, MathContext.DECIMAL32);
//        p0m = p0m.multiply(this.p0.pow(this.positiveSamples, MathContext.DECIMAL32));

        BigDecimal p1m_before = new BigDecimal(String.valueOf(1));
        p1m_before = p1m_before.subtract(this.p1);
        p1m_before = p1m_before.pow(this.totalSamples-this.positiveSamples, MathContext.DECIMAL32);
        p1m = this.p1.pow(this.positiveSamples, MathContext.DECIMAL32);
        p1m = p1m.multiply(p1m_before, MathContext.DECIMAL32);

        BigDecimal p0m_before = new BigDecimal(String.valueOf(1));
        p0m_before = p0m_before.subtract(this.p0);
        p0m_before = p0m_before.pow(this.totalSamples - this.positiveSamples, MathContext.DECIMAL32);
        p0m = this.p0.pow(this.positiveSamples, MathContext.DECIMAL32);
        p0m = p0m.multiply(p0m_before, MathContext.DECIMAL32);

        v = p1m.divide(p0m, MathContext.DECIMAL32);

        if (v.compareTo(probRatioB) <= 0) {
            this.h0decision = true;
            return false;
        } else if (v.compareTo(probRatioA) >= 0) {
            this.h0decision = false;
            return false;
        } else
            return true;
    }

    public boolean getDecision() {
        return this.h0decision;
    }

    public int getSampleSize() {
        return this.totalSamples;
    }
}
