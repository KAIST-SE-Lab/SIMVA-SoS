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

MCIMap = [0 for i in range(20)]

CSs = []
CSs.append(FireFighter("0", 0.8))
CSs.append(FireFighter("1", 0.8))
CSs.append(FireFighter("2", 0.8))

MCISoS = SoS(CSs, MCIMap)

MCIEvents = []
MCIEvents.append(Event(PatientOccurrence('patient +1', MCIMap), ConstantTimeBound(0)))
MCIEvents.append(Event(PatientOccurrence('patient +1', MCIMap), ConstantTimeBound(0)))
MCIEvents.append(Event(PatientOccurrence('patient +1', MCIMap), ConstantTimeBound(0)))
MCIEvents.append(Event(PatientOccurrence('patient +1', MCIMap), ConstantTimeBound(0)))
MCIEvents.append(Event(PatientOccurrence('patient +1', MCIMap), ConstantTimeBound(0)))
MCIEvents.append(Event(PatientOccurrence('patient +1', MCIMap), ConstantTimeBound(0)))
MCIEvents.append(Event(PatientOccurrence('patient +1', MCIMap), ConstantTimeBound(0)))
MCIEvents.append(Event(PatientOccurrence('patient +1', MCIMap), ConstantTimeBound(0)))
MCIEvents.append(Event(PatientOccurrence('patient +1', MCIMap), ConstantTimeBound(0)))
MCIEvents.append(Event(PatientOccurrence('patient +1', MCIMap), ConstantTimeBound(0)))
MCIEvents.append(Event(PatientOccurrence('patient +1', MCIMap), ConstantTimeBound(0)))
MCIEvents.append(Event(PatientOccurrence('patient +1', MCIMap), ConstantTimeBound(0)))
MCIEvents.append(Event(PatientOccurrence('patient +1', MCIMap), ConstantTimeBound(0)))
MCIEvents.append(Event(PatientOccurrence('patient +1', MCIMap), ConstantTimeBound(0)))
MCIEvents.append(Event(PatientOccurrence('patient +1', MCIMap), ConstantTimeBound(0)))
MCIEvents.append(Event(PatientOccurrence('patient +1', MCIMap), ConstantTimeBound(0)))
MCIEvents.append(Event(PatientOccurrence('patient +1', MCIMap), ConstantTimeBound(0)))
MCIEvents.append(Event(PatientOccurrence('patient +1', MCIMap), ConstantTimeBound(0)))
MCIEvents.append(Event(PatientOccurrence('patient +1', MCIMap), ConstantTimeBound(0)))
MCIEvents.append(Event(PatientOccurrence('patient +1', MCIMap), ConstantTimeBound(0)))
MCIScenario = Scenario(MCIEvents)

MCISim = Simulator(15, MCISoS, MCIScenario)

rescuedProperty = MCIProperty('RescuedPatientProperty', 'RescuedPatientRatioUpperThanValue', 'MCIPropertyType', 0.8)
rescuedChecker = MCIPropertyChecker()
verifier = SPRT(rescuedChecker)
verifier.verifyWithSimulator(MCISim, rescuedProperty, 2000)

