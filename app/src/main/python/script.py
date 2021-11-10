import deepcut

def main(data,disIndex):

    discreaseResult = ""

    disIndex2 = str(disIndex)

    text_cut = deepcut.tokenize(data)

    #pass
    for index in range(len(text_cut)):
        if text_cut[index] == "ตาล" or text_cut[index] == "น้ำตาล":
                discreaseResult = discreaseResult+"1"
        elif text_cut[index] == "โซเดียม" or text_cut[index] == "โขเดียม":
                discreaseResult = discreaseResult+"2"
        elif text_cut[index] == "ถั่วเหลือง" or text_cut[index] == "ถัวเหลือง" or text_cut[index] == "ถัว" or text_cut[index] == "เหลือง":
                discreaseResult = discreaseResult+"3"
        elif text_cut[index] == "ทะเล":
                discreaseResult = discreaseResult+"4"

    #split text again
    discreaseResultList = discreaseResult.split()

    discreaseResultchk = ""

    disIndexList = disIndex2.split()
     
    for dil in range(len(disIndexList)):
        for drl in range(len(discreaseResultList)):
                if disIndexList[dil] == "1":
                        if discreaseResultList[drl] == "1":
                                discreaseResultchk = discreaseResultchk + "1"
                elif disIndexList[dil] == "2":
                        if discreaseResultList[drl] == "2":
                                discreaseResultchk = discreaseResultchk + "2"
                elif disIndexList[dil] == "3":
                        if discreaseResultList[drl] == "3":
                                discreaseResultchk = discreaseResultchk + "3"
                elif disIndexList[dil] == "4":
                        if discreaseResultList[drl] == "4":
                                discreaseResultchk = discreaseResultchk + "4"
                

    return discreaseResultchk

