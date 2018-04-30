class Scenario(object):
    def __init__(self, events):
        self.events = events

    def addEvent(self, event):
        self.events.append(event)

    def addEvents(self, events):
        self.events = self.events + events