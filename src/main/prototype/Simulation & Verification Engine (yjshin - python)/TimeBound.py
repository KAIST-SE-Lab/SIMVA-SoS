class TimeBound(object):
    def __init__(self):
        pass

    def value(self):
        pass


class ConstantTimeBound(TimeBound):
    def __init__(self, value):
        self.value = value

    def getValue(self):
        return self.value
