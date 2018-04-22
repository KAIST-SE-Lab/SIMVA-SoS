from SoS import SoS
from CS import Member
from Scenario import Scenario
from Event import Event
from Action import Action
from TimeBound import ConstantTimeBound
from Simulator import Simulator

yongjun = Member('yongjun', 0.4)
sumin = Member('sumin', 0.5)
mapSize = 10
chocolateMap = [0 for i in range(mapSize)]
helloSoS = SoS([yongjun, sumin], chocolateMap)

helloEvents = []
#todo: event가 환경을 알 수 있도록 파라미터 넣어줘야함.
for i in range(10):
    helloEvents.append(Event(Action('chocolate +1'), ConstantTimeBound(i*10+5)))
helloScenario = Scenario(helloEvents)

helloSim = Simulator(100, helloSoS, helloScenario)

helloLogs = helloSim.run()

print('log print')
for log in helloLogs:
    print(log)

print(yongjun.chocolate)
print(sumin.chocolate)
print(chocolateMap)
