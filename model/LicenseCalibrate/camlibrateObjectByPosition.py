# -*- coding:utf-8 -*-

"""
description: 对车牌图片进行校正,使用位置定位确认
date: 2019/1/2
Author: xiaojing
"""
import os
import cv2


def binarization(img):
    # 车牌变为灰度图像
    gray_img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

    # 均值滤波  去除噪声
    # kernel = np.ones((3, 3), np.float32) / 9
    # gray_img = cv2.filter2D(gray_img, -1, kernel)

    # 高斯模糊
    blurred = cv2.GaussianBlur(gray_img, (5, 5), 0)

    # 膨胀
    dilate = cv2.dilate(blurred, cv2.getStructuringElement(cv2.MORPH_RECT, (2, 2)))
    # 腐蚀
    erode = cv2.erode(dilate, cv2.getStructuringElement(cv2.MORPH_RECT, (3, 3)))
    dilate1 = cv2.dilate(erode, cv2.getStructuringElement(cv2.MORPH_RECT, (2, 2)))

    cv2.imshow("dilate1", dilate1)

    # 二值化处理
    ret, thresh = cv2.threshold(dilate1, 150, 180, cv2.THRESH_BINARY)

    return thresh


def openCamera():
    cap = cv2.VideoCapture(0)
    while (1):
        ret, frame = cap.read()
        frame_copy = frame.copy()
        # frame = binarization(frame)
        # 框选驾照
        cv2.rectangle(frame_copy, (45, 55), (600, 400), (0, 255, 0), 1)
        # cv2.putText(frame_copy, "kaishi", (106, 70), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 0, 255), 1)

        image1 = frame[77:105, 60:106]
        image2 = frame[77:105, 106:400]
        image3 = frame[127:156, 106:584]
        image4 = frame[77:105, 566:584]
        image5 = frame[363:386, 67:86]

        # 框选氏名
        cv2.rectangle(frame_copy, (60, 77), (106, 105), (0, 255, 0), 1)

        # 框选名字
        cv2.rectangle(frame_copy, (106, 77), (400, 105), (0, 255, 0), 1)

        # 框选住址
        cv2.rectangle(frame_copy, (106, 127), (584, 156), (0, 255, 0), 1)

        # 框选文字“生”
        cv2.rectangle(frame_copy, (566, 77), (584, 105), (0, 255, 0), 1)

        # 框选文字“二种”
        cv2.rectangle(frame_copy, (67, 363), (86, 386), (0, 255, 0), 1)

        cv2.imshow("frame_copy", frame_copy)

        key = cv2.waitKey(1)
        if key == 27:
            break
        elif key == ord('s'):
            cv2.destroyAllWindows()

            cv2.imwrite("image1.jpg", image1)
            cv2.imwrite("image2.jpg", image2)
            cv2.imwrite("image3.jpg", image3)
            cv2.imwrite("image4.jpg", image4)
            cv2.imwrite("image5.jpg", image5)
            # -psd 7 表示告诉tesseract code.jpg图片是一行文本  这个参数可以减少识别错误率.默认为 3

            os.popen("tesseract image1.jpg image1 -l jpn -psd 7")
            info1 = read_file("image1.txt")
            print info1

            os.popen("tesseract image2.jpg image2 -l jpn -psd 7")
            info2 = read_file("image2.txt")
            print info2

            os.popen("tesseract image3.jpg image3 -l jpn -psd 7")
            info3 = read_file("image3.txt")
            print info3

            os.popen("tesseract image4.jpg image4 -l jpn -psd 7")
            info4 = read_file("image4.txt")
            print info4

            os.popen("tesseract image5.jpg image5 -l jpn -psd 7")
            info5 = read_file("image5.txt")
            print info5

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

            cv2.waitKey(0)
            break


def read_file(file):
    result = ""
    with open((os.path.join(file)), 'r') as f:
        data = f.readlines()
        for line in data:
            odom = line.split()
            tmp_str = "".join(odom)
            result = result.join(tmp_str.split())
            print result
    return result


if __name__ == '__main__':
    openCamera()
    # os.popen("tesseract image3.jpg image3 -l chi_sim")
    # read_file("image3.txt")
