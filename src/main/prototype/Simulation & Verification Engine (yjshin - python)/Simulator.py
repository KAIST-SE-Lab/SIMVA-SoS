class Simulator(object):
    def __init__(self, simulationTime, SoS, scenario):
        self.simulationTime = simulationTime
        self.SoS = SoS
        self.scenario = scenario
        self.simulationLog = []

    def run(self):
        for tick in range(self.simulationTime):
            #print('tick:', tick)
            for event in self.scenario.events:
                result = event.occur(tick)
                if result:
                    self.simulationLog.append((tick, result))
            result = self.SoS.run(tick)
            if result:
                for res in result:
                    self.simulationLog.append((tick, res))
        return self.simulationLog

    def stop(self):
        pass

    def monitor(self):
        pass

    def reset(self):
        self.simulationLog = []
        self.SoS.reset()



