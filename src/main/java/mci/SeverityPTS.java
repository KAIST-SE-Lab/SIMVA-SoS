package mci;

import kr.ac.kaist.se.simulator.BaseAction;

/**
 * SeverityPTS.java
 * Severity patient first PTS class

 * Author: Junho Kim <jhkim@se.kaist.ac.kr>

 * The MIT License (MIT)

 * Copyright (c) 2016 Junho Kim

 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions: TBD
 */
public class SeverityPTS extends BasePTS{

    @Override
    public int getUtility(BaseAction a) {
        //Utility는 항상 발생한 자리에서 선택이 되기 때문에 Raised Location을 기준으로 선택함
        RescueAction rA = (RescueAction) a;
        RescueAction.PatientStatus pStat = rA.getPatientStatus();

        int patientSavingBenefit = -1;

        if(pStat == RescueAction.PatientStatus.Dangerous)
            patientSavingBenefit = 60;
        else if(pStat == RescueAction.PatientStatus.Very_Dangerous)
            patientSavingBenefit = 80;

        if(rA.isAcknowledged())
            patientSavingBenefit += 10;

        int distance = rA.getRaisedLoc() > 50? rA.getRaisedLoc()-50 : 50 - rA.getRaisedLoc();
        distance *= 2;

        return patientSavingBenefit - distance;
    }

}
