import numpy as np
import cv2
import matplotlib.pyplot as plt

img = cv2.imread('/Users/henvealf/Pictures/zxl/WechatIMG1764.png')
 
#dst = cv2.fastNlMeansDenoisingColored(img,None,10,10,7,21)
 
plt.subplot(121),plt.imshow(img)
plt.subplot(122),plt.imshow(dst)
plt.show()
