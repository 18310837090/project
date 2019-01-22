# !/usr/bin/env python
# -*- coding: utf-8 -*-
from sklearn.model_selection import train_test_split
from sklearn import svm

from svmWithFsLbp import getLbp_YCrCb, getFS
from TakeRGBVideo import getFace
from sklearn.externals import joblib
import numpy as np
import time
import cv2
import os
size = (600, 800)
# 训练数据集_真人
trainDataSet_ClientFace = '/home/xiaojing/MyGitProject/project/model/Spoof/Detectedface/ClientFace'
# 训练数据集_照片
trainDataSet_ImposterFace = '/home/xiaojing/MyGitProject/project/model/Spoof/Detectedface/ImposterFace'
suffix = ["jpg", "png"]
gray_level = 16


def getTrainTestData(dir, suffix, dataType, use_fft, use_lbp, use_glcm):
    # 设置样本标签
    dtype = 0
    if dataType == "ClientFace":
        dtype = 0
    elif dataType == "ImposterFace":
        dtype = 1

    trainTestData = []
    for root, dirs, files in os.walk(dir):
        for file in files:
            filepath = os.path.join(root, file)
            filesuffix = os.path.splitext(filepath)[1][1:]
            if filesuffix in suffix:  # 遍历找到指定后缀的文件名["jpg",png]等
                rgb_face = cv2.imread(filepath)
                rgb_face = cv2.resize(rgb_face, (135, 135))
                # 获取频谱特征
                if use_fft:
                    rgb_face_fft = cv2.cvtColor(rgb_face, cv2.COLOR_BGR2GRAY)
                    rgb_magnitude_spectrum, rgb_FSFeathrer, rgb_FSFeathrerMean = getFS(rgb_face_fft)
                    fs_lbp = np.hstack((rgb_FSFeathrerMean, dtype))

                # 获取LBP特征
                if use_lbp:
                    rgb_lbp_hist = getLbp_YCrCb(rgb_face)
                    fs_lbp = np.hstack((rgb_lbp_hist, dtype))
                # 将FS特征和LBP特征级联，并且拼接上样本标签
                # if use_lbp and use_fft:
                #     fs_lbp = np.hstack((rgb_FSFeathrerMean, inf_FSFeathrerMean, rgb_lbp_hist, inf_lbp_hist, dtype))
                if use_glcm:
                    rgb_face_glcm = cv2.cvtColor(rgb_face, cv2.COLOR_BGR2GRAY)
                    # cv2.imshow('rgb',rgb_face_glcm)
                    cv2.waitKey()
                    rgb_glcm_0 = getGlcm(rgb_face_glcm, 1, 0)
                    asm0, con0, eng0, idm0 = feature_computer(rgb_glcm_0)

                    fs_lbp = np.hstack((asm0, con0, eng0, idm0, dtype))
                    print len(fs_lbp)
                trainTestData.append(fs_lbp)

    print 'Load', len(trainTestData), 'images'
    return trainTestData


def trainSVM(model_path, use_fft, use_lbp, kernel, use_glcm):
    # 取得训练集的真人的训练数据
    print "get trainTestData_ClientFace"
    trainTestData_ClientFace = getTrainTestData(trainDataSet_ClientFace, suffix, 'ClientFace', use_fft, use_lbp, use_glcm)

    # 取得训练集的照片的训练数据
    print "get trainTestData_ImposterFace"
    trainTestData_ImposterFace = getTrainTestData(trainDataSet_ImposterFace, suffix, 'ImposterFace', use_fft, use_lbp, use_glcm)

    # 把真人数据和照片数据拼接
    print "get trainTestData"
    trainTestData = np.vstack((trainTestData_ClientFace, trainTestData_ImposterFace))

    # 分为训练集与测试集
    # 81 + 128 + 1 = 210, 209 = 210 - 1
    if use_fft:
        x, y = np.split(trainTestData, (81,), axis=1)
    if use_lbp:
        x, y = np.split(trainTestData, (256,), axis=1)
    # if use_lbp and use_fft:
    #     x, y = np.split(trainTestData, (209,), axis=1)

    # random_state：是随机数的种子。
    # 随机数种子：其实就是该组随机数的编号，在需要重复试验的时候，保证得到一组一样的随机数。比如你每次都填1，其他参数一样的情况下你得到的随机数组是一样的。但填0或不填，每次都会不一样。随机数的产生取决于种子，随机数和种子之间的关系遵从以下两个规则：种子不同，产生不同的随机数；种子相同，即使实例不同也产生相同的随机数。
    print "get x_train, x_test, y_train, y_test"
    x_train, x_test, y_train, y_test = train_test_split(x, y, random_state=1, train_size=0.7, test_size=0.3)

    # 训练svm分类器
    # kernel = 'linear'时，为线性核，C越大分类效果越好，但有可能会过拟合（defaul C = 1）。
    # kernel = 'rbf'时（default），为高斯核，gamma值越小，分类界面越连续；gamma值越大，分类界面越“散”，分类效果越好，但有可能会过拟合。
    # decision_function_shape = 'ovr'时，为one v rest，即一个类别与其他类别进行划分，
    # decision_function_shape = 'ovo'时，为one v one，即将类别两两之间进行划分，用二分类的方法模拟多分类的结果。
    # clf = svm.SVC(C=0.8, kernel='rbf', gamma=20, decision_function_shape='ovr')

    #  C惩罚系数，误差宽容度，gamma是RBF函数作为kernel后该函数自带的一个参数
    C = [0.2, 0.4, 0.6, 0.8, 1]
    gamma = [4, 8, 12, 16]
    for c in C:
        for g in gamma:
            print ""
            print "now fitting, C=", c, 'gamma=', g
            clf = svm.SVC(C=c, kernel=kernel, gamma=g, decision_function_shape='ovr')
            clf.fit(x_train, y_train.ravel())
            from sklearn.externals import joblib
            joblib.dump(clf, str(c)+'_'+str(g)+model_path)

            # 计算svc分类器的准确率
            print 'Accuracy of training set=', clf.score(x_train, y_train)  # 精度
            y_hat = clf.predict(x_train)
            print 'Accuracy of testing set=', clf.score(x_test, y_test)
            y_hat = clf.predict(x_test)


def TakeFace(model_path):
    cap_rgb = cv2.VideoCapture(0)
    cap_rgb.set(cv2.CAP_PROP_FRAME_WIDTH, size[0])
    cap_rgb.set(cv2.CAP_PROP_FRAME_HEIGHT, size[1])

    model = joblib.load(model_path)
    while (cap_rgb.isOpened()):

        ret_rgb, frame_rgb = cap_rgb.read()
        if ret_rgb == True:
            frame_rgb = np.rot90(frame_rgb, 3)

            face_rgb = getFace(frame_rgb)

            cv2.imshow('rgb', frame_rgb)
            if cv2.waitKey(1):  # & 0xFF == ord('q'):
                if face_rgb is not None:
                    # cv2.imwrite('emm_rgb.png', face_rgb)
                    # cv2.imwrite('emm_inf.png', face_inf)
                    # print 'Write emmm...'
                    #
                    # rgb_path = 'emm_rgb.png'
                    # inf_path = 'emm_inf.png'
                    start = time.clock()
                    test_image(model, face_rgb, use_lbp=True, use_fft=False)
                    end = time.clock()
                    cv2.imshow('rgb_face', face_rgb)
                    # print("Running time is : %.03f seconds" % (end - start))
        else:
            break

    # Release everything if job is finished
    cap_rgb.release()
    cv2.destroyAllWindows()


def maxGrayLevel(img):
    max_gray_level = 0
    (height, width) = img.shape
    for y in range(height):
        for x in range(width):
            if img[y][x] > max_gray_level:
                max_gray_level = img[y][x]
    return max_gray_level+1


def getGlcm(input, d_x, d_y):

    srcdata = input.copy()
    ret = [[0.0 for i in range(gray_level)] for j in range(gray_level)]
    (height, width) = input.shape
    max_gray_level = maxGrayLevel(input)
    if max_gray_level > gray_level:
        for y in range(height):
            for x in range(width):
                srcdata[j][i] = srcdata[j][i] * gray_level / max_gray_level

    for j in range(height - d_y):
        for i in range(width - d_x):
            rows = srcdata[j][i]
            cols = srcdata[j + d_y][i + d_x]
            print rows, cols
            print len(ret), len(ret[0])
            ret[rows][cols] = 1.0
    for i in range(gray_level):
        for j in range(gray_level):
            ret[i][j] /= float(height * width)
    return ret


def feature_computer(p):
    Con = 0.0
    Eng = 0.0
    Asm = 0.0
    Idm = 0.0
    for i in range(gray_level):
        for j in range(gray_level):
            Con += (i - j) * (i - j) * p[i][j]
            Asm += p[i][j] * p[i][j]
            Idm += p[i][j] / (1 + (i - j) * (i - j))
            if p[i][j] > 0.0:
                Eng += p[i][j] * np.log(p[i][j])
    return Asm, Con, -Eng, Idm


def test_image(model, rgb_face, use_fft=False, use_lbp=True, use_glcm=False):

    # 获取频谱特征
    if use_fft:
        rgb_face_fft = cv2.cvtColor(rgb_face, cv2.COLOR_BGR2GRAY)
        rgb_magnitude_spectrum, rgb_FSFeathrer, rgb_FSFeathrerMean = getFS(rgb_face_fft)

        fs_lbp = np.hstack((rgb_FSFeathrerMean))

    if use_lbp:
        # 获取LBP特征
        rgb_lbp_hist = getLbp_YCrCb(rgb_face)
        fs_lbp = np.hstack((rgb_lbp_hist))

    if use_glcm:
        rgb_face_glcm = cv2.cvtColor(rgb_face, cv2.COLOR_BGR2GRAY)
        rgb_glcm_0 = getGlcm(rgb_face_glcm, 1, 0)
        asm0, con0, eng0, idm0 = feature_computer(rgb_glcm_0)
        fs_lbp = np.hstack((asm0, con0, eng0, idm0))

    y_hat = model.predict([fs_lbp])
    if y_hat > 0.5:
        print y_hat[0], 'Paper Imposter Face'
    else:
        print y_hat[0], 'Real Face'
    return y_hat


def testSVM(model_path, data_path):
    model = joblib.load(model_path)
    sum = 0
    for root, dirs, files in os.walk(data_path):
        for file in files:
            filepath = os.path.join(root, file)
            filesuffix = os.path.splitext(filepath)[1][1:]
            if filesuffix in suffix:  # 遍历找到指定后缀的文件名["jpg",png]等
                rgb_face = cv2.imread(filepath)
                result = test_image(model, rgb_face, use_lbp=True, use_fft=False, use_glcm=False)
                sum += result
    print sum, 'Imp face'


if __name__ == '__main__':
    while(1):
        print "请输入操作:"
        userInput = raw_input(">")
        if userInput == '1':  # svm训练RGB图像
            trainSVM('rgb.model', use_fft=False, use_lbp=True, kernel='rbf', use_glcm=False)
        elif userInput == '2':  # 实时测试模型
            model_path = './models/0.2_4rgbonly.model'
            # model_path = './models/1_16rgb.model'
            TakeFace(model_path)
        elif userInput == '3':  # 图片测试模型
            model_path = './models/0.2_4rgbonly.model'
            data_path = './NCITfake/chenzy/face'
            testSVM(model_path, data_path)
        else:  # 测试集图片评估模型
            model_path = './models/0.2_4rgbonly.model'
            data_path = './Detectedface/ImposterFace/00'+userInput
            testSVM(model_path, data_path)
