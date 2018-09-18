import random
from datetime import datetime
random.seed(datetime.now())


class SoS(object):
    def __init__(self, CSs, environment):
        self.CSs = CSs
        self.environment = environment
        pass

    def run(self, tick):
        logs = []
        random.shuffle(self.CSs)
        for CS in self.CSs:
            result = CS.act(tick, self.environment)
            if result:
                logs.append(result)
        logs.append(str(self.environment))
        return logs

    def reset(self):
        for CS in self.CSs:
            CS.reset()
        self.resetEnvironment()

    def resetEnvironment(self):
        for i in range(len(self.environment)):
            self.environment[i] = 0
