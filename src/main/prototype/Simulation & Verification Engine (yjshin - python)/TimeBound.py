import random
from datetime import datetime
random.seed(datetime.now())


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


class UniformDistributionTimeBound(TimeBound):
    def __init__(self, start, end):
        self.start = start
        self.end = end

    def getValue(self):
        return random.randrange(self.start, self.end)


class NormalDistributionTimeBound(TimeBound):
    def __init__(self, mu, sigma):
        self.mu = mu
        self.sigma = sigma

    def getValue(self):
        return int(random.normalvariate(self.mu, self.sigma))
