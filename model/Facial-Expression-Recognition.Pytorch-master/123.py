# -*-coding:utf-8 -*-
import torch

checkpoint = torch.load("./FER2013_VGG19/PrivateTest_model.t7", "cpu")
for k, v in checkpoint.items():
    print(k, v.size())    # 打印网络中的变量名
