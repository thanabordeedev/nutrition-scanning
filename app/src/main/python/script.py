from NS2 import IMAGE_PATH
import numpy as np
import cv2
import easyocr
from PIL import Image
import base64
import io
from matplotlib import pyplot as plt


def main(data1,data2):

    num1 = int(data1)
    num2 = int(data2) 

    summ = num1 + num2

    return "sum is " + str(summ)

    #IMAGE_PATH = data
    #dIndex = discreaseIndex


    #reader = easyocr.Reader('th')
