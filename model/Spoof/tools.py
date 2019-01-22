# !/usr/bin/env python
# -*- coding: utf-8 -*-

import dlib
import cv2
import glob
import matplotlib.image as mpimg
import matplotlib.pyplot as plt
import numpy as np
from sklearn.multiclass import OneVsRestClassifier
from sklearn.svm import SVR
from skimage import feature as skft


radius = 1
n_point = radius * 8
num_train = 200
num_test = 160
img_size = 201
block_number = 3
block_size = img_size/3
faceSize_w = 135
faceSize_h = 135

detector = dlib.get_frontal_face_detector()
win = dlib.image_window()


def cropFace(imgPath, detect = True):
    img = cv2.imread(imgPath)

    if img is None:
        print 'No input image.'
        return None

    if detect:
        dets = detector(img, 1)
        if len(dets)>0:
            h, w, c = img.shape
            m_w = 0.5####寬度放大倍數
            m_h = ((4-3)+4*m_w)/3
            print m_w,m_h

            x0 = max(dets[0].left()-int(m_w*0.5*(dets[0].right()-dets[0].left())),0)
            x1 = min(dets[0].right()+int(m_w*0.5*(dets[0].right()-dets[0].left())),w)
            y0 = max(dets[0].top()-int(m_h*0.6*(dets[0].right()-dets[0].left())),0)
            y1 = min(dets[0].bottom()+int(m_h*0.4*(dets[0].right()-dets[0].left())),h)
            face = img[y0:y1,x0:x1]
            # face = img[max(dets[0].top(),0):min(dets[0].bottom(),h),max(dets[0].left(),0):min(dets[0].right(),w)]
            # face = cv2.resize(face, (faceSize_w, faceSize_h))
            return face
        else:
            print 'No face detected.'
            return None
    else:
        # face = cv2.resize(img, (faceSize_w, faceSize_h))
        return img


def getFace(img_path):
    img = cv2.imread(img_path)
    img = cv2.cvtColor(img,cv2.COLOR_BGR2GRAY)
    if img is None:
        print 'No input image.'
        return None
    dets = detector(img, 1)
    if len(dets)>0:
        out = img[dets[0].top():dets[0].bottom(),dets[0].left():dets[0].right()]
        out = cv2.resize(out,(img_size,img_size))
        return out
    else:
        print 'No face detected.'
        return None


def loadPicture():
    train_data = np.zeros((num_train, block_size, block_size))
    test_data = np.zeros((num_test, block_size, block_size))
    train_label = np.zeros((num_train))
    test_label = np.zeros((num_test))

    train_list = glob.glob('trainData/*.png')
    test_list = glob.glob('testData/*.png')

    for img_path in train_list:
        img = getFace(img_path)
        train_index = 0
        for row in range(block_number):
            for col in range(block_number):
                emmm = img[block_size*row:block_size*(row+1),block_size*col:block_size*(col+1)]
                train_data[train_index] = emmm
                print train_data[train_index]
                # train_label[train_index] = 0
                # train_index+=1

    for img_path in test_list:
        img = getFace(img_path)
        test_index = 0
        for row in np.arange(block_number):
            for col in np.arange(block_number):
                test_data[test_index] = img[block_size*row:block_size*(row+1),block_size*col:block_size*(col+1)]
                test_label[test_index] = 0

                cv2.imshow('test', test_data[test_index])
                cv2.waitKey()
                test_index += 1

    # for i in np.arange(40):
    #     image = mpimg.imread('picture/'+str(i)+'.tiff')
    #     data = np.zeros((img_size, img_size))
    #     data[0:image.shape[0],0:image.shape[1]] = image
    #     index = 0
    #     for row in np.arange(block_number):
    #         for col in np.arange(block_number):
    #             if index<5:
    #                 train_data[train_index,:,:] = data[171*row:171*(row+1),171*col:171*(col+1)]
    #                 train_label[train_index] = i
    #                 train_index+=1
    #             else:
    #                 test_data[test_index,:,:] = data[171*row:171*(row+1),171*col:171*(col+1)]
    #                 test_label[test_index] = i
    #                 test_index+=1
    #                 index+=1
    return train_data,test_data,train_label,test_label


def texture_detect(train_data, test_data):
    train_hist = np.zeros((num_train, 256))
    test_hist = np.zeros((num_test, 256))
    for i in np.arange(num_train):
        lbp=skft.local_binary_pattern(train_data[i],n_point,radius,'default')
        max_bins = int(lbp.max() + 1)
        train_hist[i], _ = np.histogram(lbp, normed=True, bins=max_bins, range=(0, max_bins))

        for i in np.arange(num_test):
            lbp = skft.local_binary_pattern(test_data[i],n_point,radius,'default')
            max_bins = int(lbp.max() + 1)
            test_hist[i], _ = np.histogram(lbp, normed=True, bins=max_bins, range=(0, max_bins))

    return train_hist,test_hist


def SVM():
    train_data, test_data, train_label, test_label = loadPicture()
    train_hist, test_hist = texture_detect()
    svr_rbf = SVR(kernel='rbf', C=1e3, gamma=0.1)
    OneVsRestClassifier(svr_rbf, -1).fit(train_hist, train_label).score(test_hist, test_label)


def TakePhoto(name):
    cap = cv2.VideoCapture(2)
    cap.set(cv2.CAP_PROP_FRAME_WIDTH, 1280)
    cap.set(cv2.CAP_PROP_FRAME_HEIGHT, 720)

    cap1 = cv2.VideoCapture(1)
    cap1.set(cv2.CAP_PROP_FRAME_WIDTH, 1280)
    cap1.set(cv2.CAP_PROP_FRAME_HEIGHT, 720)

    i = 0

    fps = cap.get(cv2.CAP_PROP_FPS)
    fps1 = cap1.get(cv2.CAP_PROP_FPS)

    size = (1280, 720)

    # name = 'emmm'
    # inf = cv2.VideoWriter(name + "_inf.avi", cv2.VideoWriter_fourcc(*'DIVX'), fps, size)
    # rgb = cv2.VideoWriter(name + "_rgb.avi", cv2.VideoWriter_fourcc(*'DIVX'), fps1, size)
    #
    while (1):
        ret, frame = cap.read()
        ret1, frame1 = cap1.read()
        frame = np.rot90(frame, 3)
        frame1 = np.rot90(frame1, 3)
        cv2.imshow("inf", frame)
        cv2.imshow("rgb", frame1)
        detect = True

        if detect:
            dets = detector(frame, 1)
            if len(dets) > 0:
                m = int((dets[0].bottom() - dets[0].top()) * 0.3)
                h, w, c = frame.shape
                face = frame[max(dets[0].top() - m, 0):min(dets[0].bottom() + m, h),
                       max(dets[0].left() - m, 0):min(dets[0].right() + m, w)]
                face = cv2.resize(face, (faceSize_w, faceSize_h))
                return face
            else:
                print 'No face detected.'
                return None

        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

        i += 1
        if i > 50:
            break
    cap.release()
    cv2.destroyAllWindows()


def crop_all():
    crop_list = glob.glob('./fake/*.jpg')
    idx = 0
    for path in crop_list:
        face = cropFace(path)
        if face is None:
            continue
        h,w,c = face.shape
        face = cv2.resize(face, (600, int(600*h/w)))
        cv2.imshow('path', face)
        if cv2.waitKey() & 0xFF == ord('s'):
            save_path = './fake_4_3/'
            print 'Save', idx
            idx+=1
            cv2.imwrite(save_path+str(idx)+'.png',face)
        else:
            print 'Skip'
            continue


if __name__ == '__main__':
    list = glob.glob('')
    # TakePhoto('emmm')
    crop_all()
    # path = '/home/liangxiao/PycharmProjects/Liveness/NCITSpoof/chenyao/frame/8_rgb.png'




    # live_img = cv2.imread('live.png')
    # loadPicture()


    # live_yrb = cv2.cvtColor(live_img,cv2.COLOR_BGR2YCrCb)
    # cv2.imshow('l_y',live_yrb[:,:,0])
    #
    # nolive_img = cv2.imread('nolive.png')
    # nolive_yrb = cv2.cvtColor(nolive_img,cv2.COLOR_BGR2YCrCb)
    # cv2.imshow('n_y', nolive_yrb[:, :, 0])
    #
    # cv2.imshow('l_cr', live_yrb[:, :, 1])
    # cv2.imshow('n_cr', nolive_yrb[:, :, 1])
    #
    # cv2.imshow('l_cb', live_yrb[:, :, 2])
    # cv2.imshow('n_cb', nolive_yrb[:, :, 2])
    # cv2.waitKey()
