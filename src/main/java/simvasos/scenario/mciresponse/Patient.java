package simvasos.scenario.mciresponse;

import simvasos.simulation.analysis.HasName;
import simvasos.simulation.util.*;

import java.util.Random;

public class Patient implements HasName {
    private final Random random;

    public enum Status {Trapped, Pulledout, OnTransport, Hospitalized, Cured, Dead}
    public enum Severity {Delayed, Immediate}

    private final String name;
    private Status status;
    private Severity severity;
    private int lifePoint;
    private Location location;

    public Patient(Random random, String name) {
        this.random = random;
        this.name = name;

        this.reset();
    }

    public void reset() {
        this.status = Status.Trapped;
        this.severity = this.random.nextInt(2) == 1 ? Severity.Immediate : Severity.Delayed;
        this.lifePoint = 50;
        this.location = new Location(0, 0);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Severity getSeverity() {
        return severity;
    }

    public int getLifePoint() {
        return lifePoint;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void bleed() {
        switch (this.status) {
            case Dead:
                // This patient is already dead
            case Cured:
                // This patient is cured and left the hospital;
            case Hospitalized:
                // This patient is under treatment
            case Trapped:
                // This patient is not yet pulled out by fire fighters
                break;

            case Pulledout:
            case OnTransport:
                // Once the patient is pulled out, it bleeds and loose its life points
                int bleedPoint = 0;
                if (this.severity == Severity.Immediate)
                    bleedPoint = 10;
                else
                    bleedPoint = 5;

//                this.lifePoint = Math.max(0, this.lifePoint - bleedPoint);

                if (this.lifePoint == 0)
                    this.status = Status.Dead;

                break;
        }
    }

    public String getName() {
        String stat = "";
        switch (this.status) {
            case Trapped:
                stat = "Trapped";
                break;
            case Pulledout:
                stat = "Pulledout";
                break;
            case OnTransport:
                stat = "OnTransport";
                break;
            case Hospitalized:
                stat = "Hospitalized";
                break;
            case Cured:
                stat = "Cured";
                break;
            case Dead:
                stat = "Dead";
                break;
            default:
                stat = "Error";
        }

        return this.name;
    }

    @Override
    public String getSymbol() {
        return this.name.replace("Patient", "P");
    }
}
