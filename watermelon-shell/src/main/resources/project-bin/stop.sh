#!/usr/bin/env bash

projectName=example

export PROJECT_HOME="$(cd "$(dirname "$0")"/../; pwd)/"
echo "Project ${projectName} dir: ${PROJECT_HOME}"

ps -ef | grep ${projectName} | grep -v grep | awk '{print $2;}' | while read proPid; do
    kill -9 ${proPid}
	echo "Project ${projectName} stopped on PID: ${proPid}."
	exit 0;
done;

if [ $? -ne 0 ];then
    echo "Project ${projectName} is not running."
	exit 1
fi
