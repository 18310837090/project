from svmWithFsLbp import getFace
import cv2

img_path = './Testimg/'
write_path = './Testimg/real/'
num_imgs = 60
for i in range(num_imgs):
    rgb_path = img_path + 'rgb_' + str(i) + '.png'
    rout_path = img_path + 'rout_' + str(i) + '.png'
    rgb_img = getFace(rgb_path, detect=True)
    rout_img = getFace(rout_path, detect=True)
    if rout_img is None:
        continue
    if rgb_img is None:
        continue
    cv2.imshow('rgb', rgb_img)
    cv2.imshow('rout', rout_img)

    rgb_path.replace('/Testimg/', '/Testimg/real/')
    rout_path.replace('/Testimg/', '/Testimg/real/')
    cv2.imwrite(rgb_path, rgb_img)
    cv2.imwrite(rout_path, rout_img)
