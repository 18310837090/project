#!/usr/bin/env python
# -*- coding: utf-8 -*-
import base64
import datetime
import json
import time
import urllib as urllibRequest
import os
import sys


if sys.version_info <= (2, 7):
    import urllib2 as urllibRequest
    import urllib2.HTTPError as urllibError
else:
    import urllib.request as urllibRequest
    import urllib.error as urllibError

import numpy as np


class MyEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, np.ndarray):
            return obj.tolist()
        elif isinstance(obj, bytes):
            return str(obj, encoding='utf-8')
        return json.JSONEncoder.default(self, obj)


# post json to url
def jsonPost(url, j_data, headers):
    req = urllibRequest.Request(url, j_data, headers)

    try:
        page = urllibRequest.urlopen(req)
        res = page.read()
        code = page.code
        page.close()
    # except urllibError as e:
    #     code = e.code
    #     res = e.msg
    except Exception as e:
        code = 400
        res = ''
        print(e)
    return code, res


def xiancheng1():
    # host = '172.25.4.82'
    host = '172.25.4.82'
    flg = True
    port = 8080
    base_url = 'http://{}:{}/face/'.format(host, port)
    headers = {'Content-Type': 'application/json; charset=utf-8'}
    number = 0

    filename = 'C:\\Users\\chenzy\\Desktop\\yanghaoran\\image\\true10.jpg'

    src = ''
    with open(filename, "rb") as f:
        # b64encode是编码，b64decode是解码
        src = base64.b64encode(f.read())

    if flg:
        star = datetime.datetime.now().strftime('%Y%m%d%H%M%S%f')
        url = base_url + 'register'
        values = {'imageId': '00555',
                  'faces': [src]}

        j_data = json.dumps(values, cls=MyEncoder)
        j_data = bytes(j_data, 'utf8')
        while number < 100:
            star_time = datetime.datetime.now().strftime('%Y%m%d%H%M%S%f')
            res = jsonPost(url, j_data, headers)
            print(res)
            data = json.loads(res[1])

            score = 0
            if data:
                # print(data['errno'])
                if data['errno'] == 0:
                    # has_key方法在python2中是可以使用的，在python3中删除了。
                    # if data["data"].has_key("score"):
                    if "score" in data["data"]:
                        score = data["data"]["score"]

            print("分值:" + str(score))
            number += 1
            end_time = datetime.datetime.now().strftime('%Y%m%d%H%M%S%f')
            print((int(end_time) - int(star_time)) / 1000)
            # time.sleep(1)
        else:
            print("循环次数: >100,结束")

        end = datetime.datetime.now().strftime('%Y%m%d%H%M%S%f')
        print((int(end) - int(star)) / 1000)
        time.sleep(1)


def xiancheng2():
    time.sleep(0.3)
    host = '192.168.0.114'
    #host = 'localhost'
    flg = True
    port = 8080
    base_url = 'http://{}:{}/face/'.format(host, port)
    headers = {'Content-Type': 'application/json; charset=utf-8'}
    number = 0

    filename = 'C:\\Users\\chenzy\\Desktop\\yanghaoran\\image\\true10.jpg'

    src = ''
    with open(filename, "rb") as f:
        # b64encode是编码，b64decode是解码
        src = base64.b64encode(f.read())

    if flg:
        star = datetime.datetime.now().strftime('%Y%m%d%H%M%S%f')
        url = base_url + 'register'
        values = {'imageId': '00555',
                  'faces': [src]}

        j_data = json.dumps(values, cls=MyEncoder)
        j_data = bytes(j_data, 'utf8')

        while number < 100:
            star_time = datetime.datetime.now().strftime('%Y%m%d%H%M%S%f')

            res = jsonPost(url, j_data, headers)
            print(res)
            data = json.loads(res[1])

            score = 0
            if data:
                # print(data['errno'])
                if data['errno'] == 0:
                    # has_key方法在python2中是可以使用的，在python3中删除了。
                    # if data["data"].has_key("score"):
                    if "score" in data["data"]:
                        score = data["data"]["score"]

            print("分值:" + str(score))
            number += 1
            end_time = datetime.datetime.now().strftime('%Y%m%d%H%M%S%f')
            print((int(end_time) - int(star_time)) / 1000)
            time.sleep(1)
        else:
            print("循环次数: >100,结束")

        end = datetime.datetime.now().strftime('%Y%m%d%H%M%S%f')
        print((int(end) - int(star)) / 1000)


def xiancheng3():
    host = '192.168.0.114'
    time.sleep(0.5)
    #host = 'localhost'
    flg = True
    port = 8080
    base_url = 'http://{}:{}/face/'.format(host, port)
    headers = {'Content-Type': 'application/json; charset=utf-8'}
    number = 0

    filename = 'C:\\Users\\chenzy\\Desktop\\yanghaoran\\image\\true10.jpg'

    src = ''
    with open(filename, "rb") as f:
        # b64encode是编码，b64decode是解码
        src = base64.b64encode(f.read())

    if flg:
        star = datetime.datetime.now().strftime('%Y%m%d%H%M%S%f')
        url = base_url + 'register'
        values = {'imageId': '00555',
                  'faces': [src]}

        j_data = json.dumps(values, cls=MyEncoder)
        j_data = bytes(j_data, 'utf8')

    while number < 100:
        star_time = datetime.datetime.now().strftime('%Y%m%d%H%M%S%f')

        res = jsonPost(url, j_data, headers)
        print(res)
        data = json.loads(res[1])

        score = 0
        if data:
            # print(data['errno'])
            if data['errno'] == 0:
                # has_key方法在python2中是可以使用的，在python3中删除了。
                # if data["data"].has_key("score"):
                if "score" in data["data"]:
                    score = data["data"]["score"]

        print("分值:" + str(score))
        number += 1
        end_time = datetime.datetime.now().strftime('%Y%m%d%H%M%S%f')
        print((int(end_time) - int(star_time)) / 1000)
        # time.sleep(1)
    else:
        print("循环次数: >100,结束")

    end = datetime.datetime.now().strftime('%Y%m%d%H%M%S%f')
    print((int(end) - int(star)) / 1000)
    #time.sleep(1)


def xiancheng4():
    host = '192.168.0.114'
    time.sleep(0.5)
    #host = 'localhost'
    flg = True
    port = 8080
    base_url = 'http://{}:{}/face/'.format(host, port)
    headers = {'Content-Type': 'application/json; charset=utf-8'}
    number = 0

    filename = 'C:\\Users\\chenzy\\Desktop\\yanghaoran\\image\\true10.jpg'

    src = ''
    with open(filename, "rb") as f:
        # b64encode是编码，b64decode是解码
        src = base64.b64encode(f.read())

    if flg:
        star = datetime.datetime.now().strftime('%Y%m%d%H%M%S%f')
        url = base_url + 'register'
        values = {'imageId': '00555',
                  'faces': [src]}

        j_data = json.dumps(values, cls=MyEncoder)
        j_data = bytes(j_data, 'utf8')

    while number < 100:
        star_time = datetime.datetime.now().strftime('%Y%m%d%H%M%S%f')

        res = jsonPost(url, j_data, headers)
        print(res)
        data = json.loads(res[1])

        score = 0
        if data:
            # print(data['errno'])
            if data['errno'] == 0:
                # has_key方法在python2中是可以使用的，在python3中删除了。
                # if data["data"].has_key("score"):
                if "score" in data["data"]:
                    score = data["data"]["score"]

        print("分值:" + str(score))
        number += 1
        end_time = datetime.datetime.now().strftime('%Y%m%d%H%M%S%f')
        print((int(end_time) - int(star_time)) / 1000)
        # time.sleep(1)
    else:
        print("循环次数: >100,结束")

    end = datetime.datetime.now().strftime('%Y%m%d%H%M%S%f')
    print((int(end) - int(star)) / 1000)


def main():
    host = '192.168.4.42'
    port = 8080
    base_url = 'http://{}:{}/face/'.format(host, port)
    headers = {'Content-Type': 'application/json; charset=utf-8'}
    path = "/home/xiaojing/MyGitProject/project/model/Spoof/NCITfake/活体测试集/亮光线数据集/changbaihui/face"
    for start_num in range(202, 302, 1):
        filename = path + "/" + str(start_num) + "_rgb.png"
        filename1 = path + "/" + str(start_num) + "_rout.png"
        with open(filename, "rb") as f:
            # b64encode是编码，b64decode是解码
            src = base64.b64encode(f.read())
        with open(filename1, "rb") as f:
            src1 = base64.b64encode(f.read())

        star_time = datetime.datetime.now().strftime('%Y%m%d%H%M%S%f')

        url = base_url + 'register'
        values = {'imageId': '00555',
                  'faces': [src],
                  'faces1': [src1]}

        j_data = json.dumps(values, cls=MyEncoder)
        j_data = bytes(j_data, 'utf8')

        res = jsonPost(url, j_data, headers)
        # data = json.loads(str(res[1], 'utf-8'))
        data = json.loads(res)
        if data:
            if data['errno'] == '0':
                # has_key方法在python2中是可以使用的，在python3中删除了。
                # if data["data"].has_key("score"):
                if "score" in data["data"]:
                    score = data["data"]["score"]
        ir_score = data["data"]["ir_score"]
        rgb_score = data["data"]["rgb_score"]
        print("置信度分值:" + ir_score)
        print("误拒率分值:" + rgb_score)
        end_time = datetime.datetime.now().strftime('%Y%m%d%H%M%S%f')
        print('此次耗时:', (int(end_time) - int(star_time)) / 1000)


# 把照片信息写入picturedata,json文件
def openfilePictureWite():
    host = '172.25.4.83'
    flg = True
    port = 8080
    base_url = 'http://{}:{}/face/'.format(host, port)
    headers = {'Content-Type': 'application/json; charset=utf-8'}

    all = os.walk('F:\\数据集\\1031710')
    for path,dir,filelist in all:
        for filename in filelist:
            if filename.endswith('jpg') or filename.endswith('png') :
                # print(filename)
                # print(filename[0:6])

                if filename[0:6] == '201810':
                    filename = os.path.join(path,filename)
                    number = 0

                    # filename = 'C:\\Users\\chenzy\\Desktop\\yanghaoran\\image\\true10.jpg'

                    src = ''
                    with open(filename, "rb") as f:
                        # b64encode是编码，b64decode是解码
                        src = base64.b64encode(f.read())

                    if flg:
                        star = datetime.datetime.now().strftime('%Y%m%d%H%M%S%f')
                        url = base_url + 'register'
                        values = {'imageId': '00555',
                                  'faces': [src]}

                        j_data = json.dumps(values, cls=MyEncoder)
                        j_data = bytes(j_data, 'utf8')

                        res = jsonPost(url, j_data, headers)
                        # print(res)
                        data = json.loads(res[1])

                        score = 0
                        if data:
                            # print(data['errno'])
                            if data['errno'] == 0:
                                # has_key方法在python2中是可以使用的，在python3中删除了。
                                # if data["data"].has_key("score"):
                                if "score" in data["data"]:
                                    score = data["data"]["score"]
                        stee = str(filename) + ' , ' + str(score)
                        print(stee)
                        #
                        # file = r'D:\tes1t.txt'
                        # with open(file, 'a+') as f:
                        #     f.write(stee + '\n')  # 加\n换行显示
                    # number += 1
                    end_time = datetime.datetime.now().strftime('%Y%m%d%H%M%S%f')
                    print((int(end_time) - int(star)) / 1000)

                #print(filename)


def text():
    host = '192.168.0.114'
    flg = True
    port = 8080
    base_url = 'http://{}:{}/face/'.format(host, port)
    headers = {'Content-Type': 'application/json; charset=utf-8'}
    number = 0

    filename = 'C:\\Users\\chenzy\\Desktop\\yanghaoran\\image\\true10.jpg'

    src = ''
    with open(filename, "rb") as f:
        # b64encode是编码，b64decode是解码
        src = base64.b64encode(f.read())

    if flg:
        star = datetime.datetime.now().strftime('%Y%m%d%H%M%S%f')
        url = base_url + 'register'
        values = {'imageId': '00555',
                  'faces': [src]}

        j_data = json.dumps(values, cls=MyEncoder)
        j_data = bytes(j_data, 'utf8')
        star_time = datetime.datetime.now().strftime('%Y%m%d%H%M%S%f')
        res = jsonPost(url, j_data, headers)
        print(res)
        data = json.loads(res[1])

        score = 0
        if data:
            # print(data['errno'])
            if data['errno'] == 0:
                # has_key方法在python2中是可以使用的，在python3中删除了。
                # if data["data"].has_key("score"):
                if "score" in data["data"]:
                    score = data["data"]["score"]

        print("分值:" + str(score))
        number += 1
        end_time = datetime.datetime.now().strftime('%Y%m%d%H%M%S%f')
        print((int(end_time) - int(star_time)) / 1000)


if __name__ == '__main__':
    # facaApi()
    #text()
    # openfilePictureWite()
    main()
