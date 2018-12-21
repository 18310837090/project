# -*- coding:utf-8 -*-
import os
import xml.dom.minidom


def init(dir_path, nodename):
	node_labels = []
	file = os.listdir(dir_path)
	for f in file:
		full_path = os.path.join(dir_path, f)
		dom = xml.dom.minidom.parse(full_path)
		# change_node_value(dom, nodename, full_path)
		get_label_name(dom, nodename, node_labels)


def change_node_value(dom, nodename, full_path):
	# args = full_path
	args = 'JPEGImages'
	root = dom.documentElement
	node = root.getElementsByTagName(nodename)
	# value = node[0].childNodes[0].nodeValue
	node[0].firstChild.data = args  # 将该节点值赋予新值
	with open(full_path, 'w') as fi:  # 将修改写入到xml文件
		dom.writexml(fi)


def get_label_name(dom, nodename, node_labels):
	root = dom.documentElement
	node = root.getElementsByTagName(nodename)
	value = node[0].childNodes[0].nodeValue
	value = str(value)
	if node_labels == '':
		node_labels.append(value)
	else:
		if value!='plate' or value!='cup' or value!='bowl':
			node_labels.append(value)
			print node_labels
	print node_labels


def dep_traindata_valdata(dir_path):  # 分离训练集、测试集
	for i in os.listdir(dir_path):
		print i.split('.')[0]


dir_path = "/usr/local/machine_learning/pycharm_workspace/yinerche_detector/Annotations"
nodename = ["path", 'name', 'folder']
a = 1
if a == 1:
	init(dir_path, nodename[1])
elif a == 2:
	dep_traindata_valdata(dir_path)
