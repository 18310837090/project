#!/usr/bin/env bash

CUDA_VISIBLE_DEVICES=3 python object_detection/model_main.py \
--pipeline_config_path=/usr/local/machine_learning/pycharm_workspace/yinerche_detector/data/ssd_mobilenet_v1_pram.config \
--model_dir=/usr/local/machine_learning/pycharm_workspace/yinerche_detector/train \
--num_train_steps=50000 \
--num_eval_steps=2000 \
--alsologtostderr
