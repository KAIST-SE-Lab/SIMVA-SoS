import random

class Action(object):
    def __init__(self, name):
        self.name = name

    def do(self):
        return 'do action: ' + self.name

class MCIAction(Action):
    def __init__(self, name, environment):
        super(MCIAction, self).__init__(name)
        self.environment = environment

    def do(self):
        i = random.randrange(len(self.environment))
        self.environment[i] = self.environment[i] + 1
        return super().do() + ' at ' + str(i) + ' ' + str(self.environment)
