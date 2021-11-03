import numpy as np
import cv2
from PIL import Image
import base64
import io
import easyocr
from matplotlib import pyplot as plt
import deepcut

def main(data,discreaseIndex):

    decoded_data = base64.b64decode(data)
    np_data = np.fromstring(decoded_data,np.uint8)
    img = cv2.imdecode(np_data,cv2.IMREAD_UNCHANGED)

    img_gray = cv2.cvtColor(img, cv2.COLOR_RGB2GRAY)

    # Matrix of one which is multipled by scaler value of 60 matrix has dimensions same as our input image
    Intensity_Matrix = np.ones(img_gray.shape, dtype="uint8") *60  

    # Subtract Intensity Matrix from input image in order to decease the brightness
    contrast_img = cv2.subtract(img_gray,Intensity_Matrix) 

    threshold_img = cv2.adaptiveThreshold(contrast_img,255,cv2.ADAPTIVE_THRESH_MEAN_C, cv2.THRESH_BINARY,11,2)

    discreaseResult = ""

    reader = easyocr.Reader(['th'],recog_network='thai_g1')
    result = reader.readtext(threshold_img)
    for detection in result:
        text = detection[1]
        discreaseList = discreaseIndex.split()

        text_cut = deepcut.tokenize(text)

        for din in discreaseList:
            for index in range(len(text_cut)):
                if din == 1:
                    if text_cut[index] == "น้ำตาล":
                        discreaseResult +="1"
                elif din == 2:
                    if text_cut[index] == "โซเดียม":
                        discreaseResult +="2"
                elif din == 3:
                    if text_cut[index] == "ถั่วเหลือง":
                        discreaseResult +="3"
                elif din == 4:
                    if text_cut[index] == "ทะเล":
                        discreaseResult +="4"

        if discreaseResult == "":
            discreaseResult +="5"

	#remove pycache
    del decoded_data
    del np_data
    del img_gray
    del Intensity_Matrix
    del contrast_img
    del reader
    del result

    #if discreaseResult == "":
        #discreaseResult +="5"

    return discreaseResult

