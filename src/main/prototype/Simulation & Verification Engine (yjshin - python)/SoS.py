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
        return logs
