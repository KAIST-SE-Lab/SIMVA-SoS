class Property(object):
    def __init__(self, name, specification, propertyType):
        self.name = name
        self.specification = specification
        self.type = propertyType

    def getValue(self):
        return None


class MCIProperty(Property):
    def __init__(self, name, specification, propertyType, etc):
        super(MCIProperty, self).__init__(name, specification, propertyType)
        self.etc = etc

    def getValue(self):
        return self.etc