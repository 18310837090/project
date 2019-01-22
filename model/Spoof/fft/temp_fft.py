# !/usr/bin/env python
# -*- coding: utf-8 -*-
import cv2
import numpy as np
from numpy.fft import fft,ifft
import matplotlib.pyplot as plt
from PIL import Image

img_high = cv2.imread('./image/h2.jpg',0)
img_low = cv2.imread('./image/l2.jpg',0)

plt.subplot(321),plt.imshow(img_high,'gray'),plt.title('high_original')
plt.subplot(322),plt.imshow(img_low,'gray'),plt.title('low_original')

"""
sobelx = cv2.Sobel(img,cv2.CV_64F,1,0,ksize=3)#默认ksize=3
sobely = cv2.Sobel(img,cv2.CV_64F,0,1)
sobelxy = cv2.Sobel(img,cv2.CV_64F,1,1)
laplacian = cv2.Laplacian(img,cv2.CV_64F)#默认ksize=3
#人工生成一个高斯核，去和函数生成的比较
kernel = np.array([[0,-1,0],[-1,4,-1],[0,-1,0]],np.float32)#
img1 = np.float64(img)#转化为浮点型的
img_filter = cv2.filter2D(img1,-1,kernel)
sobelxy1 = cv2.Sobel(img1,-1,1,1)

plt.subplot(221),plt.imshow(sobelx,'gray')
plt.subplot(222),plt.imshow(sobely,'gray')
plt.subplot(223),plt.imshow(sobelxy,'gray')
plt.subplot(224),plt.imshow(laplacian,'gray')

plt.figure()
plt.imshow(img_filter,'gray')
"""

f_high = np.fft.fft2(img_high)
fshift_high = np.fft.fftshift(f_high)
magnitude_spectrum_high = np.log(np.abs(fshift_high))
"""
magnitudeNpArray = np.array(magnitude_spectrum)
magnitudeMax = magnitudeNpArray.max()
print magnitudeMax
magnitude_spectrum = magnitude_spectrum*255.0/magnitudeMax
"""
f_low = np.fft.fft2(img_low)
fshift_low = np.fft.fftshift(f_low)
magnitude_spectrum_low = np.log(np.abs(fshift_low))

plt.subplot(323),plt.imshow(magnitude_spectrum_high,'gray'),plt.title('magnitude_spectrum_high')
plt.subplot(324),plt.imshow(magnitude_spectrum_low,'gray'),plt.title('magnitude_spectrum_low')

#高通滤波
#--------------------------------
rows,cols = img_high.shape
mask = np.ones(img_high.shape,np.uint8)
mask[rows/2-30:rows/2+5,cols/2-30:cols/2+30] = 0
#--------------------------------
fshift_high = fshift_high*mask
f2shift_high = np.fft.ifftshift(fshift_high) #对新的进行逆变换
img_high_HP = np.fft.ifft2(f2shift_high)
#出来的是复数，无法显示
img_high_HP = np.abs(img_high_HP)
#调整大小范围便于显示
img_high_HP = (img_high_HP-np.amin(img_high_HP))/(np.amax(img_high_HP)-np.amin(img_high_HP))

fshift_low = fshift_low*mask
f2shift_low = np.fft.ifftshift(fshift_low) #对新的进行逆变换
img_low_HP = np.fft.ifft2(f2shift_low)
#出来的是复数，无法显示
img_low_HP = np.abs(img_low_HP)
#调整大小范围便于显示
img_low_HP = (img_low_HP-np.amin(img_low_HP))/(np.amax(img_low_HP)-np.amin(img_low_HP))

plt.subplot(325),plt.imshow(img_high_HP,'gray'),plt.title('img_high_HP')
plt.subplot(326),plt.imshow(img_low_HP,'gray'),plt.title('img_low_HP')

plt.show()

#频谱分析
def spectrumAnalyze(name, spectrum):
    print name

    spectrum_array = np.array(spectrum)
    print "mean: ", spectrum_array.mean()
    print "var: ", spectrum_array.var()
    print "std: ", spectrum_array.std()

    total = 0.0
    threshold = spectrum_array.max()*0.5
    thresholdOverCnt = 0
    highEnergy = 0.0
    highEnergyPositionRate = 0.1 #高频分量的位置分水岭（从图像中心开始）
    centerX = int(spectrum.shape[0] / 2)
    centerY = int(spectrum.shape[1] / 2)

    for i in range(spectrum.shape[0]):
        for j in range(spectrum.shape[1]):
            total = total + spectrum[i][j]
            #能量大于阀值的能量
            if spectrum[i][j] >= threshold:
                thresholdOverCnt = thresholdOverCnt + 1

            #高频分量能量
            if np.sqrt(np.square(i - centerX) + np.square(j - centerY)) > \
                     spectrum.shape[0]*highEnergyPositionRate:
                 highEnergy = highEnergy + spectrum[i][j]

    print "thresholdOverCnt: ", thresholdOverCnt
    print "thresholdOverRate: ", float(thresholdOverCnt)/float(spectrum.shape[0]*spectrum.shape[1])
    print "highEnergy: ", highEnergy

def hpFilter(srcImage):
    #打开图像文件并获取数据
    #srcIm=Image.open(srcImage).convert('L') # L:转灰度
    srcIm = Image.open(srcImage)

    srcArray=np.fromstring(srcIm.tobytes(),dtype=np.int8)

    #傅里叶变换并滤除低频信号
    result=fft(srcArray)
    #print result.shape
    #print 1e4
    hp_result = np.where(np.absolute(result) < 2e6, 0, result)

    spectrum = np.abs(np.log(np.fft.ifftshift(result)))

    temp = np.abs(np.fft.ifftshift(hp_result))
    temp = np.where(np.absolute(temp) == 0, 1, temp)
    hp_spectrum = np.abs(np.log(temp))
    #hp_spectrum = np.abs(np.log(np.abs(np.fft.ifftshift(hp_result))))
    #hp_spectrum = np.where(np.absolute(spectrum) < np.log(20e4), 0, spectrum)

    #傅里叶反变换,保留实部
    hp_result=ifft(hp_result)
    hp_result=np.int8(np.real(hp_result))
    #转换为图像
    im=Image.frombytes(srcIm.mode,srcIm.size,hp_result)
    #im.show()
    return srcIm,im,spectrum,hp_spectrum

#spectrumAnalyze("high",np.abs(magnitude_spectrum_high))
#spectrumAnalyze("low",np.abs(magnitude_spectrum_low))

srcIm_h,im_h,spectrum_h,hp_spectrum_h = hpFilter('./image/h3.jpg')
srcIm_l,im_l,spectrum_l,hp_spectrum_l = hpFilter('./image/l3.jpg')

plt.subplots_adjust(top=0.95, bottom=0.05, left=0.1, right=0.9, hspace=0.7,
                    wspace=0.35)
plt.subplot(421),plt.imshow(srcIm_h,'gray'),plt.title('srcIm_h')
plt.subplot(422),plt.imshow(srcIm_l,'gray'),plt.title('srcIm_l')
plt.subplot(423),plt.plot(spectrum_h),plt.title('spectrum_h')
plt.subplot(424),plt.plot(spectrum_l),plt.title('spectrum_l')
plt.subplot(425),plt.imshow(im_h,'gray'),plt.title('im_h')
plt.subplot(426),plt.imshow(im_l,'gray'),plt.title('im_l')
plt.subplot(427),plt.plot(hp_spectrum_h),plt.xlim(125000,145000),plt.ylim(12,15),plt.title('hp_spectrum_h')
plt.subplot(428),plt.plot(hp_spectrum_l),plt.xlim(125000,145000),plt.ylim(12,15),plt.title('hp_spectrum_l')

plt.show()

hp_spectrum_h_array = np.array(hp_spectrum_h)
hp_spectrum_l_array = np.array(hp_spectrum_l)
print hp_spectrum_h_array.sum()
print hp_spectrum_l_array.sum()

print hp_spectrum_h_array.max()
print hp_spectrum_l_array.max()

print hp_spectrum_h_array.mean()
print hp_spectrum_l_array.mean()

print (hp_spectrum_h_array - hp_spectrum_h_array.max()).mean()
print (hp_spectrum_l_array - hp_spectrum_l_array.max()).mean()

print hp_spectrum_h_array.sum()/hp_spectrum_l_array.sum()
