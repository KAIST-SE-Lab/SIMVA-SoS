class Action(object):
    def __init__(self, name):
        self.name = name
        pass

    def do(self):
        return 'do action: ' + self.name
