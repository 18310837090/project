# -*- coding:utf-8 -*-

"""
description: 对车牌图片进行校正,使用位置定位确认
date: 2019/1/2
Author: xiaojing
"""
import os
from PIL import Image

import cv2
import pytesseract


def binarization(img):
    # 车牌变为灰度图像
    gray_img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

    # 均值滤波  去除噪声
    # kernel = np.ones((3, 3), np.float32) / 9
    # gray_img = cv2.filter2D(gray_img, -1, kernel)

    # 高斯模糊
    blurred = cv2.GaussianBlur(gray_img, (5, 5), 0)

    # 膨胀
    dilate = cv2.dilate(blurred, cv2.getStructuringElement(cv2.MORPH_RECT, (3, 3)))
    # 腐蚀
    erode = cv2.erode(dilate, cv2.getStructuringElement(cv2.MORPH_RECT, (3, 3)))
    # 膨胀
    dilate1 = cv2.dilate(erode, cv2.getStructuringElement(cv2.MORPH_RECT, (3, 3)))

    # 自适应阈值化能够根据图像不同区域亮度分布，改变阈值
    thresh = cv2.adaptiveThreshold(dilate1, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY, 25, 10)
    # thresh = cv2.threshold(dilate, 125, 250, cv2.THRESH_BINARY_INV, 0)
    cv2.imshow("thresh", thresh)

    return thresh


def openCamera():
    cap = cv2.VideoCapture(0)
    while (1):
        ret, frame = cap.read()
        frame_copy = frame.copy()
        frame = binarization(frame)
        # 框选驾照
        cv2.rectangle(frame_copy, (45, 55), (600, 400), (0, 255, 0), 1)

        image1 = frame[77:105, 60:106]
        image2 = frame[77:105, 106:400]
        image3 = frame[127:156, 106:584]
        image4 = frame[77:105, 566:584]
        image5 = frame[363:400, 60:90]

        # 框选氏名
        cv2.rectangle(frame_copy, (60, 77), (106, 105), (0, 255, 0), 1)

        # 框选名字
        cv2.rectangle(frame_copy, (106, 77), (400, 105), (0, 255, 0), 1)

        # 框选住址
        cv2.rectangle(frame_copy, (106, 127), (584, 156), (0, 255, 0), 1)

        # 框选文字“生”
        cv2.rectangle(frame_copy, (566, 77), (584, 105), (0, 255, 0), 1)

        # 框选文字“二种”
        cv2.rectangle(frame_copy, (60, 363), (90, 390), (0, 255, 0), 1)

        # detect
        info1 = pytesseract.image_to_string(Image.fromarray(image1))
        info2 = pytesseract.image_to_string(Image.fromarray(image2))
        info3 = pytesseract.image_to_string(Image.fromarray(image3))
        info4 = pytesseract.image_to_string(Image.fromarray(image4))
        info5 = pytesseract.image_to_string(Image.fromarray(image5))

        # 框选氏名
        cv2.putText(frame_copy, info1, (60, 77), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 0, 255), 1)

        # 框选名字
        cv2.putText(frame_copy, info2, (106, 77), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 0, 255), 1)

        # 框选住址
        cv2.putText(frame_copy, info3, (106, 120), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 0, 255), 1)

        # 框选文字“生”
        cv2.putText(frame_copy, info4, (566, 70), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 0, 255), 1)

        # 框选文字“二种”
        cv2.putText(frame_copy, info5, (67, 360), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 0, 255), 1)

        cv2.imshow("frame_copy", frame_copy)

        key = cv2.waitKey(1)
        if key == 27:
            break
        elif key == ord('s'):

            # h = frame.shape[0]
            # w = frame.shape[1]
            #
            # for row in range(h):  # 遍历高
            #     for col in range(w):  # 遍历宽
            #         pv = frame[row, col]
            #         frame[row, col] = 255 - pv
            #
            # cv2.imwrite("thresh.jpg", frame)
            # cv2.destroyAllWindows()
            break


def read_file(file):
    global result
    result = ""
    with open((os.path.join(file)), 'r') as f:
        data = f.readlines()
        for line in data:
            odom = line.replace(' ', '').replace('\n', '')
            result += odom
    return result


if __name__ == '__main__':
    openCamera()
