import deepcut

def main(data,discreaseIndex):

    discreaseResult = ""

    discreaseList = discreaseIndex.split()

    textloop = ""

    text_cut = deepcut.tokenize(data)

    for din in range(len(discreaseList)):
        for index in range(len(text_cut)):
            #textloop += text_cut[index] + "|"
            if discreaseList[din] == "1":
                if text_cut[index] == "ตาล" or text_cut[index] == "น้ำตาล":
                    discreaseResult +="1"
            elif discreaseList[din] == "2":
                if text_cut[index] == "โซเดียม" or text_cut[index] == "โขเดียม":
                    discreaseResult +="2"
            elif discreaseList[din] == "3":
                if text_cut[index] == "ถั่วเหลือง" or text_cut[index] == "ถัวเหลือง" or text_cut[index] == "ถัว" or text_cut[index] == "เหลือง":
                    discreaseResult +="3"
            elif discreaseList[din] == "4":
                if text_cut[index] == "ทะเล":
                    discreaseResult +="4"
        

    return discreaseResult

