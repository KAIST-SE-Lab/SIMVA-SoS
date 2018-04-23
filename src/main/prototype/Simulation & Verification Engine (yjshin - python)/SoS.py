import random


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
        self.resetEnvironment(self.environment)

    def resetEnvironment(self, environment):
        for i in range(len(environment)):
            environment[i] = 0
