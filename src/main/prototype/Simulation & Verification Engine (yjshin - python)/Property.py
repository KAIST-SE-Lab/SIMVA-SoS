class Property(object):
    def __init__(self, name, specification, propertyType):
        self.name = name
        self.specification = specification
        self.type = type

class MCIProperty(Property):
    def __init__(self, name, specification, propertyType, etc):
        super(MCIProperty, self).__init__(name, specification, propertyType)
        self.etc = etc