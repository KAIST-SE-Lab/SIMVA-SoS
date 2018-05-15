from xml.etree.ElementTree import parse

'''
This parser will generate SIMVA-SoS.py from the given input files.
User will give inputs in xml and txt file formats.
This parser generate machine executable python file that is body of the SIMVA-SoS simulation & verification execution.
Writer: Yong-Jun Shin
'''
def readContents(file):
    content = ''
    f = open(file, 'r')
    while True:
        line = f.readline()
        if not line:
            break
        content = content + line + '\n'
    return content


def importCodeWrite(file):
    '''
    지금은 상위폴더에서 import 하고있는데 나중에는 같은 output 폴더에 필요한 파일들이 생성되어 들어갈 것
    python은 같은 폴더에 없으면 상위 폴더에서 import를 찾는 건지 뭔지, 상위폴더라는 명시 안했는데도 되고 있음
    '''
    file.write('from SoS import SoS\n')
    file.write('from CS import FireFighter\n')
    file.write('from Scenario import Scenario\n')
    file.write('from Event import Event\n')
    file.write('from Action import PatientOccurrence\n')
    file.write('from TimeBound import ConstantTimeBound\n')
    file.write('from Simulator import Simulator\n')
    file.write('from PropertyChecker import MCIPropertyChecker\n')
    file.write('from Verifier import SPRT\n')
    file.write('from Property import MCIProperty\n')
    file.write('\n')


def parseInputFiles(inputFileDir, modelConfigFile, simulationConfigFile, verificationConfigFile, outputFile):
    f = open(outputFile, 'w')

    importCodeWrite(f)

    # modelConfig
    modelTree = parse(inputFileDir + '/' + modelConfigFile)
    modelRoot = modelTree.getroot()

    environmentTag = modelRoot.find("environment")
    environmentCode = environmentTag.get("name") + ' = '
    environmentCode = environmentCode + readContents(inputFileDir + '/' + environmentTag.get('file'))
    #print(environmentCode, end='')
    f.write(environmentCode)

    CSsTag = modelRoot.find("CSs")
    CSsName = CSsTag.get("name")
    CSsCode = CSsName + ' = []\n'
    for CS in CSsTag.getchildren():
        CSsCode = CSsCode + CSsName + '.append(' + CS.get('type') + '(' + CS.get('parameter') + '))\n'
    CSsCode = CSsCode + '\n'
    #print(CSsCode, end='')
    f.write(CSsCode)

    SoSTag = modelRoot.find("SoS")
    SoSCode = SoSTag.get("name") + ' = '
    SoSCode = SoSCode + 'SoS(' + SoSTag.get("CSs") + ', ' + SoSTag.get("environment") + ')\n\n'
    #print(SoSCode, end='')
    f.write(SoSCode)

    ScenarioTag = modelRoot.find("Scenario")
    EventsTag = ScenarioTag.find("Events")
    eventsName = EventsTag.get("name")
    ScenarioCode = eventsName + ' = []\n'
    for event in EventsTag.getchildren():
        if event.tag == 'Event':
            ScenarioCode = ScenarioCode + eventsName + '.append(Event(' + event.get("action") + '(' + event.get("actionParameter") + '), ' + event.get('timeBound') + '(' + event.get('timeBoundParameter') + ')))\n'
    ScenarioCode = ScenarioCode + ScenarioTag.get("name") + ' = Scenario(' + eventsName + ')\n\n'
    #print(ScenarioCode, end='')
    f.write(ScenarioCode)

    # simulationConfig
    simulationTree = parse(inputFileDir + '/' + simulationConfigFile)
    simulationRoot = simulationTree.getroot()

    simulatorTag = simulationRoot.find("simulator")
    simulatorName = simulatorTag.get("name")
    simulatorCode = simulatorName + ' = Simulator(' + simulatorTag.get("simulationTime") + ', ' + simulatorTag.get("targetSoS") + ', ' + simulatorTag.get("targetScenario") + ')\n\n'
    #print(simulatorCode, end='')
    f.write(simulatorCode)

    # verificationConfig
    verificationTree = parse(inputFileDir + '/' + verificationConfigFile)
    verificationRoot = verificationTree.getroot()

    propertyTag = verificationRoot.find("property")
    propertyName = propertyTag.get("name")
    propertyCode = propertyName + ' = ' + propertyTag.get("type") + '(' + propertyTag.get("parName") + ', ' + propertyTag.get("parSpecification") + ', ' + propertyTag.get("parPropertyType")
    for child in propertyTag.getchildren():
        if child.tag == 'additional':
            parameterTag = child.find("parameter")
            propertyCode = propertyCode + ', ' + parameterTag.get("value")
    propertyCode = propertyCode + ')\n'
    #print(propertyCode, end='')
    f.write(propertyCode)

    verifierTag = verificationRoot.find("verifier")
    checkerTag = verifierTag.find("checker")
    checkerName = checkerTag.get("name")
    checkerCode = checkerName + ' = ' + checkerTag.get("type") + '()\n'
    #print(checkerCode, end='')
    f.write(checkerCode)

    verifierName = verifierTag.get("name")
    verifierCode = verifierName + ' = ' + verifierTag.get("type") + '(' + checkerName + ')\n'
    #print(verifierCode, end='')
    f.write(verifierCode)

    # verify
    verifyCode = verifierName + '.verifyWithSimulator(' + simulatorName + ', ' + propertyName + ', ' + verifierTag.find("maxRepeatSimulation").get("value") + ')\n\n'
    #print(verifyCode, end='')
    f.write(verifyCode)

    print('generated file:', outputFile)
