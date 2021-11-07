import deepcut

def main(data):

    discreaseResult = ""

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

    return discreaseResult

