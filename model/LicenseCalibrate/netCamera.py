import cv2
from PIL import Image

import pytesseract


cap = cv2.VideoCapture(0)
i = 0
while (1):
    ret, frame = cap.read()

    # 框选氏名
    cv2.rectangle(frame.copy(), (60, 77), (106, 105), (0, 255, 0), 1)

    pytesseract.image_to_string(Image.open("1234.png", 'r'), lang="chi")

    k = cv2.waitKey(1)
    if k == 27:
        break
    elif k == ord('s'):
        cv2.imwrite(str(i) + '.jpg', frame)
        i += 1
    cv2.imshow("capture", frame)
cap.release()
cv2.destroyAllWindows()
