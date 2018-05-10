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


modelTree = parse("modelConfig.xml")
modelRoot = modelTree.getroot()

environment = modelRoot.find("environment")
environmentCode = environment.get("name") + ' = '
environmentCode = environmentCode + readContents(environment.get('file'))
print(environmentCode, end='')

CSs = modelRoot.find("CSs")
CSsName = CSs.get("name")
CSsCode = CSsName + ' = []\n'
for CS in CSs.getchildren():
    CSsCode = CSsCode + CSsName + '.append(' + CS.get('type') + '(' + CS.get('parameter') + '))\n'
CSsCode = CSsCode + '\n'
print(CSsCode, end='')

SoS = modelRoot.find("SoS")
SoSCode = SoS.get("name") + ' = '
SoSCode = SoSCode + 'SoS(' + SoS.get("CSs") + ', ' + SoS.get("environment") + ')\n\n'
print(SoSCode, end='')

Scenario = modelRoot.find("Scenario")
Events = Scenario.find("Events")
eventsName = Events.get("name")
ScenarioCode = eventsName + ' = []\n'
for event in Events.getchildren():
    if event.tag == 'Event':
        ScenarioCode = ScenarioCode + eventsName + '.append(Event(' + event.get("action") + '(' + event.get("actionParameter") + '), ' + event.get('timeBound') + '(' + event.get('timeBoundParameter') + ')))\n'
ScenarioCode = ScenarioCode + Scenario.get("name") + ' = Scenario(' + eventsName + ')\n\n'
print(ScenarioCode, end='')
