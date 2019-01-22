#!/usr/bin/env python
# -*- coding: utf-8 -*-
import base64
import datetime
import json
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


def main():
    host = '192.168.4.42'
    port = 8080
    base_url = 'http://{}:{}/face/'.format(host, port)
    # url = base_url + 'register'
    headers = {'Content-Type': 'application/json; charset=utf-8'}
    # filename = '/home/xiaojing/MyGitProject/project/model/Spoof/NCITfake/活体测试集/亮光线数据集/changbaihui/face/202_rgb.png'
    #
    # filename1 = '/home/xiaojing/MyGitProject/project/model/Spoof/NCITfake/活体测试集/亮光线数据集/changbaihui/face/202_rout.png'
    # with open(filename, "rb") as f:
    #     # b64encode是编码，b64decode是解码
    #     src = base64.b64encode(f.read())
    # with open(filename1, "rb") as f:
    #     src1 = base64.b64encode(f.read())
    # values = {'imageId': '00555',
    #           'faces': [src],
    #           'faces1': [src1]}
    #
    # j_data = json.dumps(values, cls=MyEncoder)
    # j_data = bytes(j_data, 'utf8')
    #
    # res = jsonPost(url, j_data, headers)
    # data = json.loads(str(res[1], 'utf-8'))
    # # data = json.loads(res)
    # if data:
    #     if data['errno'] == '0':
    #         # has_key方法在python2中是可以使用的，在python3中删除了。
    #         # if data["data"].has_key("score"):
    #         if "score" in data["data"]:
    #             score = data["data"]["score"]
    # ir_score = data["data"]["ir_score"]
    # rgb_score = data["data"]["rgb_score"]
    # print("置信度分值:" + ir_score)
    # print("误拒率分值:" + rgb_score)

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
        data = json.loads(str(res[1], 'utf-8'))
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


if __name__ == '__main__':
    main()
