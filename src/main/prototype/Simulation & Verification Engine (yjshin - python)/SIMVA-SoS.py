from SoS import SoS
from CS import FireFighter
from Scenario import Scenario
from Event import Event
from Action import MCIAction
from TimeBound import ConstantTimeBound
from Simulator import Simulator
from PropertyChecker import MCIPropertyChecker
from Verifier import SPRT
from Property import MCIProperty

'''Input generation'''
yongjun = FireFighter('yongjun', 0.6)
sumin = FireFighter('sumin', 0.7)
CSs = [yongjun, sumin]
mapSize = 10
MCIMap = [0 for i in range(mapSize)]
MCISoS = SoS(CSs, MCIMap)

MCIEvents = []
for i in range(10):
    MCIEvents.append(Event(MCIAction('patient +1', MCIMap), ConstantTimeBound(i)))
MCIScenario = Scenario(MCIEvents)

'''Simulation'''
MCISim = Simulator(10, MCISoS, MCIScenario)

repeatSim = 1000
MCILogs = []
for i in range(repeatSim):
    MCISim.reset()
    MCILog = MCISim.run()
    MCILog.append(([yongjun.rescued, sumin.rescued], MCIMap))
    MCILogs.append(MCILog)


print('log print (only last log)')
for log in MCILogs[-1]:
    print(log)

print()
print('yongjun rescued:', yongjun.rescued)
print('sumin rescued:', sumin.rescued)
print('final map:', MCIMap)
print()

'''Verification'''
property = MCIProperty('MCIProperty', 'MCIPropertySpecification', 'MCIPropertyType', 'MCIPropertyEtc')
checker = MCIPropertyChecker()
verifier = SPRT(checker)
verifier.verify(MCILogs, property)
