import random

class Verifier(object):
    def __init__(self, checker):
        self.propertyChecker = checker

    def verify(self, simulationLogs, verificationProperty):
        check = True
        for simulationLog in simulationLogs:
            if not self.propertyChecker.check(simulationLog, property):
                check = False
        return check

class SPRT(Verifier):
    def __init__(self, checker):
        super(SPRT, self).__init__(checker)
        self.alpha = 0.05
        self.beta = 0.05
        self.delta = 0.01
        self.minimumSample = 2

    def verify(self, simulationLogs, verificationProperty):
        for i in range(100):
            theta = i * 0.01
            numOfSample = 0
            numOfTrue = 0
            random.shuffle(simulationLogs)

            j = 0
            while self.isSampleNeeded(j):
                if not j < len(simulationLogs):
                    print("Lack of simulation logs")
                    return False
                numOfSample = numOfSample + 1
                if self.propertyChecker.check(simulationLogs[j], verificationProperty):
                    numOfTrue = numOfTrue + 1
                j = j + 1

            result = numOfTrue > numOfSample//2   #todo: 각 theta에 대한 결정 함수 필요
            print('theta:', round(theta, 2), ' num of samples:', numOfSample, ' result:', result)



    def isSampleNeeded(self, currentSamples):   #todo: 샘플 필요한지
        if currentSamples < self.minimumSample:
            return True
        #todo: 인텔리제이 SPRT 97~104줄
        return False
