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

        if sum(rescued) > sum(environment): # rescued more than 50% of patients
            return True
        else:
            return False
