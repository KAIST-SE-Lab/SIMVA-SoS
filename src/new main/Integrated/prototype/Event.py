class Event(object):
    def __init__(self, action, timeBound):
        self.action = action
        self.timeBound = timeBound

    def occur(self, current):
        if self.timeBound.getValue() == current:
            result = self.action.do()
            return result

    def reset(self):
        self.timeBound.reset()