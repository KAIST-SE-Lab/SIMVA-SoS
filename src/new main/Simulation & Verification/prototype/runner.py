import inputParser
import os
'''
SIMVA-SoS runner script
This is a script to control execution file generation and SIMVA-SoS
Writer: Yong-Jun Shin
'''

# input file location and names
inputFileDir = 'input'
modelConfigFile = 'modelConfig.xml'
simulationConfigfile = 'simulationConfig.xml'
verificationConfigFile = 'verificationConfig.xml'
outputFileName = 'SIMVA-SoS.py'

# generate executable input
inputParser.parseInputFiles(inputFileDir, modelConfigFile, simulationConfigfile, verificationConfigFile, outputFileName)

# run generated SIMVA-SoS file
print('Running generated file ...')
os.system('python ' + outputFileName + ' > output/output.txt')
