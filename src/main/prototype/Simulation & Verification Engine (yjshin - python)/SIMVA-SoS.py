from SoS import SoS
from CS import FireFighter
from Scenario import Scenario
from Event import Event
from Action import PatientOccurrence
from TimeBound import ConstantTimeBound
from Simulator import Simulator
from PropertyChecker import MCIPropertyChecker
from Verifier import SPRT
from Property import MCIProperty

'''Input generation'''
fireFighterPr = 0.8
numOfFireFighters = 3
CSs = [FireFighter(str(i), fireFighterPr) for i in range(numOfFireFighters)]
mapSize = 20
MCIMap = [0 for i in range(mapSize)]
MCISoS = SoS(CSs, MCIMap)

MCIEvents = []
numOfPatients = 20
for i in range(numOfPatients):
    MCIEvents.append(Event(PatientOccurrence('patient +1', MCIMap), ConstantTimeBound(0)))
MCIScenario = Scenario(MCIEvents)

'''Simulation'''
simulationTime = 15
MCISim = Simulator(simulationTime, MCISoS, MCIScenario)

repeatSim = 2000
MCILogs = []
for i in range(repeatSim):
    MCILog = MCISim.run()
    MCILogs.append(MCILog)

'''
print('log print (only last log)')
for log in MCILogs:
    print(log[-1], sum(log[-1][0]), sum(log[-1][1]))
'''
'''
print()
for CS in CSs:
    print(CS.name, 'rescued:', CS.rescued)
print('final map:', MCIMap)
print()
'''

'''Verification'''
property = MCIProperty('RescuedPatientProperty', 'RescuedPatientRatioUpperThanValue', 'MCIPropertyType', 0.8)
checker = MCIPropertyChecker()
verifier = SPRT(checker)
print('Verify existed logs')
verifier.verifyExistedLogs(MCILogs, property)
print()
print('Verify with simulator')
verifier.verifyWithSimulator(MCISim, property, repeatSim)
