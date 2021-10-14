#from pythainlp import sent_tokenize, word_tokenize
import numpy as np
import cv2
import easyocr
from matplotlib import pyplot as plt

def main(data,discreaseIndex):

    IMAGE_PATH = data
    dIndex = discreaseIndex

    # Load color image (BGR) and convert to gray
    img = cv2.imread(IMAGE_PATH,1)
    img_gray = cv2.cvtColor(img, cv2.COLOR_RGB2GRAY)

    # Load in grayscale mode
    img_gray_mode = cv2.imread(IMAGE_PATH,0)

    # Matrix of one which is multipled by scaler value of 60 matrix has dimensions same as our input image
    #Intensity_Matrix = np.ones(img_gray_mode, dtype="uint8") *60  

    # Subtract Intensity Matrix from input image in order to decease the brightness
    #contrast_img = cv2.subtract(img_gray_mode,Intensity_Matrix) 

    #threshold_img = cv2.adaptiveThreshold(contrast_img,255,cv2.ADAPTIVE_THRESH_MEAN_C, cv2.THRESH_BINARY,11,2)

    discreaseResult = ""

    #reader = easyocr.Reader(['th'])
    #result = reader.readtext(threshold_img)
    #for detection in result:
        #top_left = tuple((int(val) for val in detection[0][0]))
        #buttom_right = tuple((int(val) for val in detection[0][2]))
        #text = detection[1]
        #font = cv2.FONT_HERSHEY_DUPLEX
        #img = cv2.rectangle(img, top_left, buttom_right, (0,255,0), 5)
        #img = cv2.putText(img, text, top_left, font, 1,(255,255,255),2, cv2.LINE_AA)

        
        #text_sub = word_tokenize(text,engine="multi_cut")
        #print(text_sub)
        #for index in range(len(text_sub)):
        #    if text_sub[index] == 'น้ำตาล' or text_sub[index] == 'นำตาล':
        #        discreaseResult += 1
        #    elif text_sub[index] == 'โซเดียม':
        #        discreaseResult += 2
        #    elif text_sub[index] == 'ถั่วเหลือง' or text_sub[index] == 'ถวเหลอง' or text_sub[index] == 'ถว':
        #        discreaseResult += 3
        #    elif text_sub[index] == 'ทะเล':
        #        discreaseResult += 4

    return discreaseResult


