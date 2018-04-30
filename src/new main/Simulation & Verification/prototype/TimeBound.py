import random
from datetime import datetime
random.seed(datetime.now())


class TimeBound(object):
    def __init__(self):
        pass

    def getValue(self):
        pass


class ConstantTimeBound(TimeBound):
    def __init__(self, value):
        self.value = value

    def getValue(self):
        return self.value


class UniformDistributionTimeBound(TimeBound):
    def __init__(self, start, end):
        self.start = start
        self.end = end
        self.value = None

    def getValue(self):
        if not self.value:
            self.value = random.randrange(self.start, self.end)
        return self.value


class NormalDistributionTimeBound(TimeBound):
    def __init__(self, mu, sigma):
        self.mu = mu
        self.sigma = sigma
        self.value = None

    def getValue(self):
        if not self.value:
            self.value = int(random.normalvariate(self.mu, self.sigma))
        return self.value
