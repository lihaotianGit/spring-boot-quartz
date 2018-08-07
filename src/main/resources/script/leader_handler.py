#!/usr/bin/env python
# -*- coding: utf-8 -*-

""" 监听ZooKeeper选主节点，将Nginx访问配置切换到主服务。 """

# 脚本与Nginx需部署在一台机器上。
# 脚本运行前提是应用在获取Leader身份后，将自身服务数据写入ZooKeeper的Leader节点。
# 脚本采用推拉结合的方式，轮询、监听Leader节点的变化。

import atexit
import shutil
import subprocess
import time

from kazoo.client import KazooClient

__author__ = 'im.lht@qq.com'

ZK_HOSTS = '127.0.0.1:2181'
LEADER_PATH = '/leader'

NGINX_CONFIG_FILE_PATH = '/usr/local/etc/nginx/'
NGINX_CONF = 'nginx.conf'
NGINX_MASTER_PROCESS = 'nginx: master process nginx'
NGINX_WORKER_PROCESS = 'nginx: worker process'

COMMAND_PS_NGINX = 'ps -ef | grep nginx'
COMMAND_RELOAD_NGINX = 'sudo nginx -s reload'
COMMAND_START_NGINX = 'sudo nginx'

POLL_INTERVAL_SECONDS = 10
WATCH_WAITING_SECONDS = 2

last_leader_info = 0


def shutdown_hook():
    if zk is not None:
        zk.stop()
        print 'KazooClient stopped.'
    return


# 创建并启动zk客户端，注册shutdown hook
zk = KazooClient(hosts=ZK_HOSTS)
zk.start()
atexit.register(shutdown_hook)


def exec_process(command):
    return subprocess.Popen(command, stdout=subprocess.PIPE, stderr=None, shell=True).communicate()[0]


def print_nginx_state(result, exec_type):
    if result == '':
        print '%s%s%s' % ('Nginx ', exec_type, '.')
    else:
        print 'Nginx error.'
        raise


# 替换配置文件并重载Nginx
def reload_nginx(new_leader_info):
    src_file = '%s%s%s%s' % (NGINX_CONFIG_FILE_PATH, new_leader_info, '_', NGINX_CONF)
    dst_file = NGINX_CONFIG_FILE_PATH + NGINX_CONF
    shutil.copyfile(src_file, dst_file)

    check_return = exec_process(COMMAND_PS_NGINX)
    if NGINX_MASTER_PROCESS in check_return and NGINX_WORKER_PROCESS in check_return:
        exec_result = exec_process(COMMAND_RELOAD_NGINX)
        print_nginx_state(exec_result, 'reloaded, leader_info: ' + str(new_leader_info))
    else:
        exec_result = exec_process(COMMAND_START_NGINX)
        print_nginx_state(exec_result, 'started, leader_info: ' + str(new_leader_info))
    return


# 获取Leader节点的服务数据并与上次的进行对比，如不同则替换Nginx配置文件
def handle_leader(children):
    if len(children) > 0:
        new_leader_info = zk.get(LEADER_PATH)[0]
        global last_leader_info
        print 'new_leader_info: ' + str(new_leader_info) + ', last_leader_info: ' + str(last_leader_info)
        if new_leader_info != last_leader_info:
            last_leader_info = new_leader_info
            reload_nginx(new_leader_info)
    else:
        print 'All server dead!'


# 入口：轮询Leader节点
def poll():
    while True:
        time.sleep(POLL_INTERVAL_SECONDS)
        print 'Polling.'
        handle_leader(zk.get_children(LEADER_PATH))


# 入口：监听Leader节点
@zk.ChildrenWatch(LEADER_PATH)
def watch(children):
    time.sleep(WATCH_WAITING_SECONDS)
    print 'Watching.'
    handle_leader(children)
    return


poll()
