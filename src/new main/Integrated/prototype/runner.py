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
outputFileName = 'output/output.py'
verificationResultFileName = 'output/verification_result.txt'

# generate executable input
print('Parsing input files ...')
inputParser.parseInputFiles(inputFileDir, modelConfigFile, simulationConfigfile, verificationConfigFile, outputFileName)

# todo func slicing
'''here'''

# run generated file
print()
print('Running generated file ...')
os.system('python ' + outputFileName + ' > ' + verificationResultFileName)

print('Done:', verificationResultFileName)
