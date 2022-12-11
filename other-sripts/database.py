import os
import numpy
from PIL import Image
import cv2
import matplotlib as plt
from collections import Counter

files = []
out_file = "data.txt"
debug = True
# format : BLOCK_NAME:RED/GREEN/BLUE

if not debug:
    for file in os.listdir():

        if not (str(file).endswith("py") or str(file).endswith("txt")):

            image = Image.open(file)

            image_pixels = numpy.array(image).tolist()

            red = 0
            green = 0
            blue = 0

            pixel_count = 1

            for line in image_pixels:
                for pixel in line:
                    try:
                        if(pixel[3] != 0):
                            red+=pixel[0]
                            green+=pixel[1]
                            blue+=pixel[2]
                            pixel_count+=1

                    except IndexError:
                        pass
                    except TypeError:
                        pass


            with open(out_file, 'a+') as out:

                block_name = str(file).split(".")[0]

                try:
                    # remove [ for the next big part of code
                    # [ is for formatting in text editor
                    out.write(f"{block_name}:{round(red/pixel_count)} {round(green/pixel_count)} {round(blue/pixel_count)}[\n")
                except TypeError:
                    pass

colors = []

# test our data
with open('data.txt', 'r') as file:
    for line in file.readlines():
        image = cv2.imread(line.split(":")[0]+'.png')
        image = cv2.resize(image, (256,256))

        RGBcolor = [int(i.split("[")[0]) for i in line.split(":")[1].split(" ")]
        BGRcolor = [RGBcolor[2], RGBcolor[1], RGBcolor[0]]

        colors.append(RGBcolor)

        cv2.rectangle(image, (0,0), (20,20), BGRcolor, 10)
        
        cv2.imshow('image', image)
        cv2.waitKey(0)
