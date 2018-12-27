# thresh = cv2.adaptiveThreshold(equ, 255,cv2.ADAPTIVE_THRESH_GAUSSIAN_C,cv2.THRESH_BINARY, 11, 2)
# for i in range(thresh.shape[0]):
#     for j in range(thresh.shape[1]):
#         thresh[i][j] = 255 -thresh[i][j]
#
# cv2.imshow("thresh", thresh)

# image, contours, hierarchy = cv2.findContours(edged.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
# cv2.drawContours()
# for i in range(0, len(contours)):
#     x, y, w, h = cv2.boundingRect(contours[i])

# img = cv2.polylines(image, contours[i], 1, (30, 170, 0), 5)
# if w*h < 1000:
#     continue
# img = cv2.rectangle(img, (x, y), (x+w, y+h), (0, 255, 0), 1)

"""
x, y, w, h = cv2.boundingRect(img)
    参数：
    img  是一个二值图
    x，y 是矩阵左上点的坐标，
    w，h 是矩阵的宽和高

cv2.rectangle(img, (x,y), (x+w,y+h), (0,255,0), 2)
    img：       原图
    (x，y）：   矩阵的左上点坐标
    (x+w，y+h)：是矩阵的右下点坐标
    (0,255,0)： 是画线对应的rgb颜色
    2：         线宽
"""
# cv2.imshow("img", img)
