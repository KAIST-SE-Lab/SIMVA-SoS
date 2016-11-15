f = open('20161115220201mci0.001_debug.txt', 'r')
l = f.readlines()
print(len(l))
for each in l:
    divided_items = each.split()[1:-1]
    print(divided_items[40:])
