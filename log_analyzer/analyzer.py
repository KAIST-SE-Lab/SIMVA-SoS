f = open('20161116114150mci0.001_debug.txt', 'r')
l = f.readlines()
# print(len(l))
total_patients = set()
for each in l:
    divided_items = each.split()[:-1]
    patients_info = divided_items[5:]
    print(divided_items)
    if len(patients_info) > 0:
        for p in patients_info:
            patient = p.split('/')
            life = patient[2]
            total_patients.add(patient[0])
            if int(life) < 10:
                print(patient[0], life)
    # print(divided_items[5:])
print(len(total_patients))
