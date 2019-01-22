# !/usr/bin/env python
# -*- coding: utf-8 -*-
import numpy as np
import cv2
import os
import dlib
import face_recognition as fr

file_path = './NCITfake'
size = (600, 800)
fourcc = cv2.VideoWriter_fourcc(*'XVID')
fps = 30
faceSize_w = 135
faceSize_h = 135
# faceSize_w = 300
# faceSize_h = 300
#  使用官方提供的模型构建特征提取器
# PREDICTOR_PATH = "shape_predictor_68_face_landmarks.dat"
# predictor = dlib.shape_predictor(PREDICTOR_PATH)
detector = dlib.get_frontal_face_detector()
num_img = 499
EXTRACT_FREQUENCY = 1


def extract_frames(video_path, dst_folder, index):
    # 主操作
    import cv2
    video = cv2.VideoCapture()
    if not video.open(video_path):
        print("can not open the video")
        exit(1)
    count = 1
    while True:
        _, frame = video.read()
        if frame is None:
            break
        if count % EXTRACT_FREQUENCY == 0:
            save_path = "{}/{:>03d}.jpg".format(dst_folder, index)
            cv2.imwrite(save_path, frame)
            index += 1
        count += 1
    video.release()
    # 打印出所提取帧的总数
    print("Totally save {:d} pics".format(index-1))


def getFace1(img):
    dets = fr.face_locations(img)
    if len(dets) > 0:
        img = img[dets[0][0]:dets[0][3], dets[0][2]:dets[0][3]]
    return img


def getFace(img):
    dets = detector(img, 1)

    if len(dets) > 0:
        # m = int((dets[0].bottom() - dets[0].top()) * 0.6)
        m = int((dets[0].bottom() - dets[0].top()) * 0.2)
        h, w, c = img.shape
        face = img[max(dets[0].top() - m, 0):min(dets[0].bottom() + m, h),
               max(dets[0].left() - m, 0):min(dets[0].right() + m, w)]
        face = cv2.resize(face, (faceSize_w, faceSize_h))

        return face
    else:
        return None


def TakeFace(name, write_face=True, write_frame = True):
    cap_rgb = cv2.VideoCapture(1)
    cap_rgb.set(cv2.CAP_PROP_FRAME_WIDTH, size[1])
    cap_rgb.set(cv2.CAP_PROP_FRAME_HEIGHT, size[0])
    idx = 0

    while (cap_rgb.isOpened()):

        ret_rgb, frame_rgb = cap_rgb.read()
        if ret_rgb == True:
            frame_rgb = np.rot90(frame_rgb, 3)  # 镜头旋转90度

            if write_face:
                face_rgb = getFace(frame_rgb)
                if face_rgb is not None:
                    cv2.imwrite(os.path.join(file_path, name, 'face', str(idx) + '_rgb.png'), face_rgb)

            cv2.imshow('rgb', frame_rgb)
            if cv2.waitKey(100) & 0xFF == ord('q'):
                print 'Take ', idx, 'photos...'
                break
            if idx > num_img:
                break
    cap_rgb.release()
    cv2.destroyAllWindows()


def TakeVideo(name, start, write_face=True, write_frame=False):

    cap_rgb = cv2.VideoCapture(0)
    fps_rgb = cap_rgb.get(cv2.CAP_PROP_FPS)
    cap_rgb.set(cv2.CAP_PROP_FRAME_WIDTH, size[1])
    cap_rgb.set(cv2.CAP_PROP_FRAME_HEIGHT, size[0])
    out_rgb = cv2.VideoWriter(os.path.join(file_path, (name + '_rgb.avi')), fourcc, fps_rgb, size)

    print "Start recording, Press 'q' to exit..."
    idx = 0

    while (cap_rgb.isOpened()):

        ret_rgb, frame_rgb = cap_rgb.read()
        if ret_rgb == True:
            frame_rgb = np.rot90(frame_rgb, 3)

            # out_rgb.write(frame_rgb)

            if write_face:
                face_rgb = getFace(frame_rgb)
                if face_rgb is not None:
                    cv2.imwrite(os.path.join(file_path, name, 'face', str(start) + '_rgb.png'), face_rgb)
                    print 'Take ', idx, 'photos...'
                    if write_frame:
                        cv2.imwrite(os.path.join(file_path, name, 'frame', str(start) + '_rgb.png'), frame_rgb)
                    idx += 1
                    start += 1

            cv2.imshow('rgb', frame_rgb)

            if cv2.waitKey(100) & 0xFF == ord('q'):
                print 'Take ', idx, 'photos...'
                break
            if idx > num_img:
                break
    cap_rgb.release()
    out_rgb.release()
    cv2.destroyAllWindows()
    return start


def Take_rgbAndrout_Video(name, start, write_face=True, write_frame=True):
    cap_rgb = cv2.VideoCapture(0)
    cap_rout = cv2.VideoCapture(1)

    # 补光
    # cap_buguang = cv2.VideoCapture(2)

    fps_rgb = cap_rgb.get(cv2.CAP_PROP_FPS)
    fps_rout = cap_rout.get(cv2.CAP_PROP_FPS)
    cap_rgb.set(cv2.CAP_PROP_FRAME_WIDTH, size[1])
    cap_rgb.set(cv2.CAP_PROP_FRAME_HEIGHT, size[0])
    cap_rout.set(cv2.CAP_PROP_FRAME_WIDTH, size[1])
    cap_rout.set(cv2.CAP_PROP_FRAME_HEIGHT, size[0])
    # out_rgb = cv2.VideoWriter(os.path.join(file_path, (name + '_rgb.avi')), fourcc, fps_rgb, size)
    # out_rout = cv2.VideoWriter(os.path.join(file_path, (name + '_rout.avi')), fourcc, fps_rout, size)

    print "Start recording, Press 'q' to exit..."
    idx = 0

    while (cap_rgb.isOpened() and cap_rout.isOpened()):

        ret_rgb, frame_rgb = cap_rgb.read()
        ret_rout, frame_rout = cap_rout.read()
        if ret_rgb == True and ret_rout == True:
            frame_rgb = np.rot90(frame_rgb, 3)
            frame_rout = np.rot90(frame_rout, 3)

            # out_rgb.write(frame_rgb)
            # out_rout.write(frame_rout)

            if write_face:
                face_rgb = getFace(frame_rgb)
                face_rout = getFace(frame_rout)
                if face_rgb is not None and face_rout is not None:
                    cv2.imwrite(os.path.join(file_path, name, 'face', str(start) + '_rgb.png'), face_rgb)
                    cv2.imwrite(os.path.join(file_path, name, 'face', str(start) + '_rout.png'), face_rout)
                    if write_frame:
                        cv2.imwrite(os.path.join(file_path, name, 'frame', str(start) + '_rgb.png'), frame_rgb)
                        cv2.imwrite(os.path.join(file_path, name, 'frame', str(start) + '_rout.png'), frame_rout)
                    idx += 1
                    start += 1
                    print 'Take ', idx, 'photos...'

            cv2.imshow('rgb', frame_rgb)
            cv2.imshow('rout', frame_rout)
            if cv2.waitKey(100) & 0xFF == ord('q'):
                print 'Take ', idx, 'photos...'
                break
            if idx > num_img:
                break
    # cap_rgb.release()
    # out_rgb.release()
    cv2.destroyAllWindows()
    return start


def aadad():
    start = 0
    dir_path = '/home/xiaojing/图片/123132'
    for f in os.listdir(dir_path):
        img = cv2.imread(dir_path+'/'+f)
        face = getFace(img)
        cv2.imwrite(os.path.join('/home/xiaojing/图片/333/', str(start) + '.png'), face)
        start += 1


if __name__ == '__main__':
    start = 0
    while(1):
        print 'Input your name:'
        name = raw_input(">")
        if not os.path.exists(os.path.join(file_path, name)):
            os.mkdir(os.path.join(file_path, name))
            os.mkdir(os.path.join(file_path, name, 'frame'))
            os.mkdir(os.path.join(file_path, name, 'face'))
        start = TakeVideo(name, start)
        # start = Take_rgbAndrout_Video(name, start)
    # aadad()
