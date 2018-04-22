from SoS import SoS
from CS import FireFighter
from Scenario import Scenario
from Event import Event
from Action import MCIAction
from TimeBound import ConstantTimeBound
from Simulator import Simulator

'''Input generation'''
yongjun = FireFighter('yongjun', 0.4)
sumin = FireFighter('sumin', 0.5)
mapSize = 10
MCIMap = [0 for i in range(mapSize)]
MCISoS = SoS([yongjun, sumin], MCIMap)

MCIEvents = []
for i in range(10):
    MCIEvents.append(Event(MCIAction('patient +1', MCIMap), ConstantTimeBound(i)))
MCIScenario = Scenario(MCIEvents)

'''Simulation'''
MCISim = Simulator(10, MCISoS, MCIScenario)

MCILogs = MCISim.run()

print('log print')
for log in MCILogs:
    print(log)

print()
print('yongjun rescued:', yongjun.rescued)
print('sumin rescued:', sumin.rescued)
print('final map:', MCIMap)

'''Verification'''
#todo: SMC
