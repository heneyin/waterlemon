#!/bin/bash

# INIT 
ROOTPATH=$(cd `dirname $0`; pwd)

# VARS
BEGIN_DAY=$1
END_DAY=$2
BEGIN_HOUR=$3
END_HOUR=$4

# PROCESS_TYPE
TYPE="TRANS"

BEGIN_DAY_S=`date -d "${BEGIN_DAY}" +%s`
END_DAY_S=`date -d "${END_DAY}" +%s`
BEGIN_HOUR_S=`date -d "${BEGIN_HOUR}" +%s`
END_HOUR_S=`date -d "${END_HOUR}" +%s`

# FUNCTION
Start() {
    for ((DAY_S=${BEGIN_DAY_S}; DAY_S<=${END_DAY_S}; DAY_S+=86400)) 
    do
        DAY=$(date -d @${DAY_S} +%F);
        echo $DAY;
        for ((HOUR_S=${BEGIN_HOUR_S}; HOUR_S<=${END_HOUR_S}; HOUR_S+=3600))
        do
            HOUR=$(date -d @${HOUR_S} +%H);
            echo "------------------ start process ${DAY} ${HOUR} ------------------------------";

            spark-submit --class com.henvealf.filetokafka.FileToKafkaMain --master yarn-client  --name file-to-kafka  -–conf spark.yarn.principal=bdi@EBSCN.COM -–conf spark.yarn.keytab=/home/bdi/keytabs/bdi.keytab  ./file-to-kafka-1.0.0-jar-with-dependencies.jar "/user/bdi/loader_formatter/loader/*/${DAY}/${HOUR}" cdh.master01.com:9092,cdh.master02.com:9092,cdh.master03.com:9092 bfd_input_topic_all bdi@EBSCN.COM  /home/bdi/keytabs/bdi.keytab        
   
            wait
        done
    done
}

# MAIN
if [[ $# -ne 4 ]];then
    echo -e "Usage:$0 YYYY-mm-dd YYYY-mm-dd dd dd\nRun the script like this:  ./$(basename $0) 2016-01-01 2016-01-07 00 23"
    exit 0
elif [[ ${BEGIN_DAY} =~ ^[0-9]{4}-[0-9]{2}-[0-9]{2}$ ]] && [[ ${END_DAY} =~ ^[0-9]{4}-[0-9]{2}-[0-9]{2}$ ]] && [[ ${BEGIN_DAY_S} -le ${END_DAY_S} ]] && date "+%F" -d "${BEGIN_DAY}" >/dev/null && date "+%F" -d "${END_DAY}" >/dev/null;then
    echo -e "BEGIN_DAY:${BEGIN_DAY}\nEND_DAY:${END_DAY}"
    if [[ ${BEGIN_HOUR} =~ ^([0-1]{1}[0-9]{1}|20|21|22|23|24)$ ]] && [[ ${END_HOUR} =~ ^([0-1]{1}[0-9]{1}|20|21|22|23|24)$ ]] && [[ ${BEGIN_HOUR_S} -le ${END_HOUR_S} ]] && date "+%d" -d "${BEGIN_HOUR}" >/dev/null && date "+%d" -d "${END_HOUR}" >/dev/null;then
        echo -e "BEGIN_HOUR:${BEGIN_HOUR}\nEND_HOUR:${END_HOUR}"
        Start
    fi
else
    echo "Not valid!!! Example:$0 2016-01-01 2016-01-05 00 05"
    exit 0
fi
