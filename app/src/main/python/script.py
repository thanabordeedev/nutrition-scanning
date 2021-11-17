import deepcut

def main(data,disIndex):

    discreaseResult = ""

    text_cut = deepcut.tokenize(data)

    discreaseIndex = str(disIndex)

    #pass
    for index in range(len(text_cut)):
        if text_cut[index] == "ตาล" or text_cut[index] == "น้ำตาล":
                discreaseResult = discreaseResult+"1#"
        elif text_cut[index] == "โซเดียม" or text_cut[index] == "โขเดียม":
                discreaseResult = discreaseResult+"2#"
        elif text_cut[index] == "ถั่วเหลือง" or text_cut[index] == "ถัวเหลือง" or text_cut[index] == "ถัว" or text_cut[index] == "เหลือง":
                discreaseResult = discreaseResult+"3#"
        elif text_cut[index] == "ทะเล":
                discreaseResult = discreaseResult+"4#"

    #check duplicate number
    discreaseResultList = discreaseResult.split('#')
    discreaseIndexList = discreaseIndex.split('#')

    countlist = [0,0,0,0]

    #for clf in range(len(discreaseResultList)):
    #    countlist.append(0)

    for drl in range(len(discreaseResultList)):
        if discreaseResultList[drl] == '':
                discreaseResultList[drl] = '0'

    for dil in range(len(discreaseIndexList)):
        if discreaseIndexList[dil] == '':
                discreaseIndexList[dil] = '0'

    new_discreaseResultList = [int(d) for d in discreaseResultList]
    new_discreaseIndexList = [int(e) for e in discreaseIndexList]

    for ndrl in range(len(new_discreaseResultList)):
        if new_discreaseResultList[ndrl] == 1 :
                countlist[0] +=1
        elif new_discreaseResultList[ndrl] == 2 :
                countlist[1] +=1
        elif new_discreaseResultList[ndrl] == 3 :
                countlist[2] +=1
        elif new_discreaseResultList[ndrl] == 4 :
                countlist[3] +=1

    newdiscreaseResult = ""

    # loop for input text again
    for cl in range(len(countlist)):
        if cl == 0:
                if countlist[cl] > 0:
                        newdiscreaseResult += '1#'
        elif cl == 1:
                if countlist[cl] > 0:
                        newdiscreaseResult += '2#'
        elif cl == 2:
                if countlist[cl] > 0:
                        newdiscreaseResult += '3#'
        elif cl == 3:
                if countlist[cl] > 0:
                        newdiscreaseResult += '4#'

    #split again
    newdiscreaseResultList = newdiscreaseResult.split('#')

    for ndrl in range(len(newdiscreaseResultList)):
        if newdiscreaseResultList[ndrl] == '':
                newdiscreaseResultList[ndrl] = '0'

    new2_discreaseResultList = [int(f) for f in newdiscreaseResultList]

    discreaseResultChk = ""

    #loop for check same for in for
    for ndil in range(len(new_discreaseIndexList)):
        for n2drl in range(len(new2_discreaseResultList)):
                if new_discreaseIndexList[ndil] == new2_discreaseResultList[n2drl]:
                        discreaseResultChk += str(new_discreaseIndexList[ndil])

    return discreaseResultChk

