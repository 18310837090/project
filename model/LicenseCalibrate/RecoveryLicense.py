# -*- coding:utf-8 -*-

"""
description: 对车牌图片进行校正
date: 2018/12/24
Author: xiaojing
"""
import math
import cv2
import imutils
import numpy as np
from imutils.perspective import four_point_transform
from scipy import ndimage


# 车牌颜色信息二值化
def binarization(img):
    img = cv2.resize(img, (640, 480), interpolation=cv2.INTER_CUBIC)

    # 车牌变为灰度图像
    gray_img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

    # 均值滤波  去除噪声
    kernel = np.ones((3, 3), np.float32) / 9
    gray_img = cv2.filter2D(gray_img, -1, kernel)

    # 二值化处理
    ret, thresh = cv2.threshold(gray_img, 127, 255, cv2.THRESH_BINARY)
    return thresh


# 使用标准霍夫变换进行直线探测,并计算角度
def detection_linear(img):
    img_copy = img.copy()
    edges_img = cv2.Canny(img, 50, 150, apertureSize=3)
    lines = cv2.HoughLines(edges_img, 1, np.pi/180, 190)
    for line in lines:
        rho, theta = line[0]  # line[0]存储的是点到直线的极径和极角，其中极角是弧度表示的。
        a = np.cos(theta)  # theta是弧度
        b = np.sin(theta)
        x0 = a * rho    # 代表x = r * cos(theta)
        y0 = b * rho    # 代表y = r * sin(theta)
        x1 = int(x0 + 1000 * (-b))  # 计算直线起点横坐标
        y1 = int(y0 + 1000 * a)  # 计算起始起点纵坐标
        x2 = int(x0 - 1000 * (-b))  # 计算直线终点横坐标
        y2 = int(y0 - 1000 * a)  # 计算直线终点纵坐标    注：这里的数值1000给出了画出的线段长度范围大小，数值越小，画出的线段越短，数值越大，画出的线段越长
        cv2.line(img_copy, (x1, y1), (x2, y2), (0, 0, 255), 2)  # 点的坐标必须是元组，不能是列表。

        if x1 == x2:
            rotate_angle = 0
        else:
            k = float(y2 - y1) / (x2 - x1)  # 计算斜率
            rotate_angle = math.degrees(math.atan(k))

    return rotate_angle


# 透视矫正
def perspective_transformation(img):
    # 灰度化
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

    # 高斯模糊
    blurred = cv2.GaussianBlur(gray, (5, 5), 0)

    # 膨胀
    dilate = cv2.dilate(blurred, cv2.getStructuringElement(cv2.MORPH_RECT, (3, 3)))

    # Canny边缘检测
    edged = cv2.Canny(dilate, 30, 120, 3)  # 修改滞后阈值30,120来拟合需要检测的物体边缘像素点
    # cv2.imshow("edged", edged)

    cnts = cv2.findContours(edged.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    cnts = cnts[0] if imutils.is_cv2() else cnts[1]  # 判断是OpenCV2还是OpenCV3
    docCnt = None

    # 确保至少找到一个轮廓
    if len(cnts) > 0:
        # 按轮廓大小降序排列
        cnts = sorted(cnts, key=cv2.contourArea, reverse=True)
        for c in cnts:
            # 近似轮廓
            peri = cv2.arcLength(c, True)
            approx = cv2.approxPolyDP(c, 0.02 * peri, True)
            # 如果我们的近似轮廓有四个点，则确定找到了纸
            if len(approx) == 4:
                docCnt = approx
                break

    # 对原始图像应用四点透视变换，以获得纸张的俯视图
    paper = four_point_transform(img, docCnt.reshape(4, 2))
    return paper


def init(path):
    img = cv2.imread(path)
    new_img = binarization(img)  # 车牌图像二值化处理
    rotate_angle = detection_linear(new_img)  # 探测直线，获取倾斜角度
    level_img = ndimage.rotate(img, rotate_angle)  # 水平校正
    vertical_img = perspective_transformation(level_img)  # 垂直校正
    cv2.imshow("level_img", level_img)
    cv2.imshow("vertical_img", vertical_img)
    cv2.waitKey(0)
    cv2.destroyAllWindows()


if __name__ == '__main__':
    path = "picture/JapaneseLicense2.jpg"
    init(path)
