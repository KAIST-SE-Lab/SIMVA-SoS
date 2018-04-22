class Simulator(object):
    def __init__(self, time, SoS, scenario):
        self.time = time
        self.SoS = SoS
        self.scenario = scenario
        self.simulationLog = []
        pass

    def run(self):
        for tick in range(self.time):
            print('tick:', tick)
            for event in self.scenario.events:
                result = event.occur(tick)
                if result:
                    self.simulationLog.append((tick, result))
            result = self.SoS.run(tick)
            if result:
                for r in result:
                    self.simulationLog.append((tick, r))
        return self.simulationLog

    def stop(self):
        pass

    def monitor(self):
        pass



