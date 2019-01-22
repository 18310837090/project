# !/usr/bin/env python
# -*- coding: utf-8 -*-
import cv2
import os
import numpy as np
from numpy.fft import fft,ifft
import matplotlib.pyplot as plt
from PIL import Image
import dlib
import glob
from sklearn.externals import joblib
from skimage.feature import local_binary_pattern
from sklearn.model_selection import train_test_split
from sklearn import svm

# 频谱图横向纵向分割块的数量
w_divide_num = 9
h_divide_num = 9


# 检测对象图像尺寸（可以被9（w_divide_num，h_divide_num）整除）
faceSize_w = 135
faceSize_h = 135

step_w = faceSize_w/w_divide_num
step_h = faceSize_h/h_divide_num

# settings for LBP
radius = 1
n_points = 8 * radius

faceFile = "./haarcascades/haarcascade_frontalface_alt2.xml"
face_classfier = cv2.CascadeClassifier(faceFile)

# 缩小脸部面积，减少脸部周围背景的干扰
faceCropRate = 0.0
suffix = ["jpg"]
detector = dlib.get_frontal_face_detector()
# win = dlib.image_window()

# 训练数据集_真人
trainDataSet_ClientFace = '/home/liangxiao/PycharmProjects/Liveness/NUAA/Detectedface/ClientFace'
# 训练数据集_照片
trainDataSet_ImposterFace = '/home/liangxiao/PycharmProjects/Liveness/NUAA/Detectedface/ImposterFace'


def getFace(img, detect=False):
    # 读取文件
    # img = cv2.imread(imgPath)
    # 转换为灰度图显示
    # img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    # cv2.imshow('face',face)
    # cv2.waitKey()

    if img is None:
        print 'No input image.'
        return None

    if detect:
        dets = detector(img, 1)
        if len(dets)>0:
            m = int((dets[0].bottom() - dets[0].top())*0.3)
            h, w, c = img.shape
            face = img[max(dets[0].top()-m,0):min(dets[0].bottom()+m,h),max(dets[0].left()-m,0):min(dets[0].right()+m,w)]
            face = cv2.resize(face, (faceSize_w, faceSize_h))
            return face
        else:
            print 'No face detected.'
            return None
    else:
        face = cv2.resize(img, (faceSize_w, faceSize_h))
        return face


def normalization(array):
    max = np.max(array)
    min = np.min(array)
    normalized = [(d-min)/(max-min) for d in array]
    return normalized


def getFS(img):
    # 快速傅里叶变换算法得到频率分布
    f = np.fft.fft2(img)
    # 默认结果中心点位置是在左上角，转移到中间位置
    fshift = np.fft.fftshift(f)
    # fft 结果是复数，求绝对值结果才是振幅
    magnitude_spectrum = np.log(1 + np.abs(fshift))
    # magnitude_spectrum = np.abs(fshift)

    FSFeathrer = []

    for i in range(h_divide_num):
        for j in range(w_divide_num):
            #[行开始：行终了，列开始：列终了]
            FSFeathrer.append(magnitude_spectrum[i*step_h:(i+1)*step_h,j*step_w:(j+1)*step_w])

    FSFeathrerMean = [f.mean() for f in FSFeathrer]
    # 归一化
    FSFeathrerMean = normalization(FSFeathrerMean)

    # 图像整体频谱，子区域频谱，子区域频谱平均值
    return magnitude_spectrum,FSFeathrer,FSFeathrerMean


def getLbp_block(img):
    img_block = []
    for i in range(h_divide_num):
        for j in range(w_divide_num):
            # [行开始：行终了，列开始：列终了]
            img_block.append(img[i * step_h:(i + 1) * step_h, j * step_w:(j + 1) * step_w])

    # 处理 mode : default , Uniform
    lbp = [local_binary_pattern(im, n_points, radius, method='default') for im in img_block]

    # 二维变一维
    lbp_array = [l.flatten() for l in lbp]

    histStack = []
    histMean = None
    for i in range(len(lbp_array)):
        # LBP直方图 bins:分箱大小， normed：是否归一化
        hist, bin_edges = np.histogram(lbp_array[i], bins=256, normed=True)
        histStack = np.hstack((histStack, hist))

        if i == 0:
            histMean = hist
        else:
            histMean = np.add(histMean,hist)
    # 归一化
    histStack = normalization(histStack)
    histMean = normalization(histMean)

    # hist, bin_edges = np.histogram(lbp_array[1], bins=256, normed=True)
    # plt.plot(histMean)
    # plt.show()
    return histMean


def getLbp_YCrCb(img):
    img_YCrCb = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
    # img_YCrCb = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    img_block = []
    for c in range(img_YCrCb.shape[2]):
        for i in range(h_divide_num):
            for j in range(w_divide_num):
                # [行开始：行终了，列开始：列终了]
                img_block.append(img_YCrCb[i * step_h:(i + 1) * step_h, j * step_w:(j + 1) * step_w, c])
                # cv2.imshow('eee', img_YCrCb[i * step_h:(i + 1) * step_h, j * step_w:(j + 1) * step_w,c])
                # cv2.waitKey()
    # 处理 mode : default , Uniform
    lbp = [local_binary_pattern(im, n_points, radius, method='default') for im in img_block]
    # cv2.imshow('lbp',lbp[0])
    # cv2.waitKey()
    # 二维变一维
    lbp_array = [l.flatten() for l in lbp]
    histStack = []
    histMean = None
    for i in range(len(lbp_array)):
        # LBP直方图 bins:分箱大小， normed：是否归一化
        hist, bin_edges = np.histogram(lbp_array[i], bins=256, normed=True)
        # histStack = np.hstack((histStack, hist))

        if i == 0:
            histMean = hist
        else:
            histMean = np.add(histMean,hist)
    # 归一化
    # histStack = normalization(histStack)
    histMean = normalization(histMean)
    # hist, bin_edges = np.histogram(lbp_array[1], bins=256, normed=True)
    # plt.plot(histMean)
    # plt.show()
    return histMean


def getTrainTestData(dir,suffix,dataType,use_fft,use_lbp,use_glcm=False):
    #设置样本标签
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
                print 'load ', filepath
                face = getFace(filepath)
                cv2.imshow('emm',face)
                cv2.waitKey()
                # 获取频谱特征
                if use_fft:
                    face_fft = cv2.cvtColor(face,cv2.COLOR_BGR2GRAY)
                    magnitude_spectrum, FSFeathrer, FSFeathrerMean = getFS(face_fft)
                    fs_lbp = np.hstack((FSFeathrerMean,dtype))

                if use_lbp:
                # 获取LBP特征
                # lbp_hist = getLbp(face)
                # lbp_hist = getLbp_block(face)
                    lbp_hist = getLbp_YCrCb(face)
                    fs_lbp = np.hstack((lbp_hist, dtype))
                if use_lbp and use_fft:
                    fs_lbp = np.hstack((FSFeathrerMean, lbp_hist, dtype))
                # fs_lbp = np.hstack((FSFeathrerMean, dtype))
                # 添加到返回值中
                trainTestData.append(fs_lbp)

    print 'Load', len(trainTestData), 'images'
    return trainTestData


def test_image(model_path, img_path, use_fft = True, use_lbp = True):
    model = joblib.load(model_path)
    img = getFace(img_path)
    if img is None:
        return
    # 获取频谱特征
    if use_fft:
        face_fft = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        magnitude_spectrum, FSFeathrer, FSFeathrerMean = getFS(face_fft)
        # print "len(FSFeathrerMean): ", len(FSFeathrerMean)

        fs_lbp = np.hstack((FSFeathrerMean))
        # print "len(fs_lbp): ", len(fs_lbp)

    # 获取LBP特征
    if use_lbp:
        lbp_hist = getLbp_YCrCb(img)
        # print "len(lbp_hist): ", len(lbp_hist)
        fs_lbp = np.hstack((lbp_hist))
        # print "len(fs_lbp): ", len(fs_lbp)

    if use_lbp and use_fft:
        fs_lbp = np.hstack((FSFeathrerMean, lbp_hist))
        # print "len(fs_lbp): ", len(fs_lbp)
    # plt.subplot(311), plt.imshow(img, 'gray')
    # plt.subplot(312), plt.imshow(magnitude_spectrum, 'gray')
    # plt.subplot(313), plt.plot(fs_lbp)
    # plt.show()
    y_hat = model.predict([fs_lbp])
    if y_hat>0.5:
        return 1
        # print img_path, 'is', y_hat[0], ' Imposter Face'
    else:
        return 0
        # print img_path, 'is', y_hat[0], ' Real Face'


def test_face(model, img, use_fft, use_lbp):
    # 获取频谱特征
    if use_fft:
        face_fft = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        magnitude_spectrum, FSFeathrer, FSFeathrerMean = getFS(face_fft)
        fs_lbp = np.hstack((FSFeathrerMean))

    # 获取LBP特征
    if use_lbp:
        lbp_hist = getLbp_YCrCb(img)
        fs_lbp = np.hstack((lbp_hist))
    if use_lbp and use_fft:
        fs_lbp = np.hstack((FSFeathrerMean, lbp_hist))

    y_hat = model.predict([fs_lbp])
    if y_hat>0.5:
        return 1
        # print 'Detected face is', y_hat[0], ' Imposter Face'
    else:
        return 0
        # print 'Detected face is', y_hat[0], ' Real Face'


def trainSVM(model_path, use_fft, use_lbp, kernel):
    # 取得训练集的真人的训练数据
    print "get trainTestData_ClientFace"
    trainTestData_ClientFace = getTrainTestData(trainDataSet_ClientFace, suffix, 'ClientFace',use_fft,use_lbp)

    # 取得训练集的照片的训练数据
    print "get trainTestData_ImposterFace"
    trainTestData_ImposterFace = getTrainTestData(trainDataSet_ImposterFace, suffix, 'ImposterFace',use_fft,use_lbp)

    # 把真人数据和照片数据拼接
    print "get trainTestData"
    trainTestData = np.vstack((trainTestData_ClientFace, trainTestData_ImposterFace))

    # 分为训练集与测试集
    # 81 + 128 + 1 = 210, 209 = 210 - 1
    if use_fft:
        x, y = np.split(trainTestData, (81,), axis=1)
    if use_lbp:
        x, y = np.split(trainTestData, (256,), axis=1)
    if use_lbp and use_fft:
        x, y = np.split(trainTestData, (209,), axis=1)


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

    C = [0.2,0.4,0.6,0.8,1]
    gamma = [4,8,12,16]
    for c in C:
        for g in gamma:
            print "now fitting, C=",c,'gamma=',g
            clf = svm.SVC(C=c, kernel=kernel, gamma=g, decision_function_shape='ovr')
            clf.fit(x_train, y_train.ravel())
            from sklearn.externals import joblib
            joblib.dump(clf, str(c)+'_'+str(g)+model_path)

            # 计算svc分类器的准确率
            print 'Accuracy of training set=', clf.score(x_train, y_train)  # 精度
            y_hat = clf.predict(x_train)
            print 'Accuracy of testing set=', clf.score(x_test, y_test)
            y_hat = clf.predict(x_test)

if __name__ == "__main__":

    # model_path = "lbp_svm_hsv_all.model"
    # trainSVM(model_path, use_fft=False, use_lbp=True, kernel = 'linear')
    #
    # model_path = "models/0.4_4_lbp_hsv_rbf_256.model"
    # trainSVM(model_path, use_fft=False, use_lbp=True, kernel = 'rbf')

    # model_path = "fft_svm_hsv_all_rbf.model"
    # trainSVM(model_path, use_fft=True, use_lbp=False, kernel = 'rbf')
    #
    # model_path = "fft_lbp_svm_hsv_all_rbf.model"
    # trainSVM(model_path, use_fft=True, use_lbp=True, kernel = 'rbf')
    # img_list = glob.glob('/home/liangxiao/PycharmProjects/Liveness/fft/image/*.jpg')
    C = [0.4,0.6,0.8]
    Gamma = [4,8,12,16]
    file = ['card','paper','paper2','screen']
    for f in file:
        # img_list = glob.glob('/home/liangxiao/PycharmProjects/Liveness/Testimg/fake/'+f+'/*.png')
        img_list = glob.glob('/home/liangxiao/PycharmProjects/Liveness/Testimg/fake/'+f+'/*.png')
        for c in C:
            for g in Gamma:
                model_path = "models/" + str(c) + "_" + str(g) + "_lbp_hsv_rbf_256.model"
                count = 0
                num = 0
                for img_path in img_list:
                    result = test_image(model_path, img_path, use_fft = False, use_lbp = True)
                    if result is not None:
                        num += 1
                        if result > 0.5:
                            print img_path, 'is Imposter Face'
                            count += 1
        print 'Detect', count, ' Imposter Faces of ', f

    C = [0.4,0.6,0.8]
    Gamma = [4,8,12,16]
    img_list = glob.glob('/home/liangxiao/PycharmProjects/Liveness/NCIT_dataset/*/*.jpg')
    for c in C:
        for g in Gamma:
            model_path = "models/" + str(c) + "_" + str(g) + "_lbp_hsv_rbf_256.model"
            count = 0
            num = 0
            for img_path in img_list:
                result = test_image(model_path, img_path, use_fft=False, use_lbp=True)
                if result is not None:
                    num+=1
                    if result > 0.5:
                        print img_path, 'is Imposter Face'
                        count += 1
                else:
                    print img_path, 'is Real Face'
            print model_path, 'Detect', count, ' Imposter Faces of NCIT', num
