class PropertyChecker(object):
    def __init__(self):
        pass

    def check(self, simulationLog, verificationProperty):
        return True


class MCIPropertyChecker(PropertyChecker):
    def __init__(self):
        pass

    def check(self, simulationLog, verificationProperty):
        result = simulationLog[-1]
        rescued = result[0]
        environment = result[1]

        numberOfRescued = sum(rescued)
        numberOfPatients = numberOfRescued + sum(environment)
        if (numberOfRescued / numberOfPatients) > verificationProperty.getValue(): # rescued more than 50% of patients
            #print(True)
            return True
        else:
            #print(False)
            return False
