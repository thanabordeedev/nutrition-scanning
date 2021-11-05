import deepcut

def main(data,discreaseIndex):

    discreaseResult = ""

    discreaseList = discreaseIndex.split()

    datasplit = data.split("\n")

    text_cut = deepcut.tokenize(datasplit)

    for din in discreaseList:
        for index in range(len(text_cut)):
            if din == 1:
                if text_cut[index] == "ตาล" or text_cut[index] == "น้ำตาล":
                    discreaseResult +="1"
            elif din == 2:
                if text_cut[index] == "โซเดียม":
                    discreaseResult +="2"
            elif din == 3:
                if text_cut[index] == "ถั่วเหลือง" or text_cut[index] == "ถัวเหลือง":
                    discreaseResult +="3"
            elif din == 4:
                if text_cut[index] == "ทะเล":
                    discreaseResult +="4"

    if discreaseResult == "":
        discreaseResult +="5"

    return discreaseResult

