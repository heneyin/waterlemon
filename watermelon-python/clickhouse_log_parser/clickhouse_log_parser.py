#!/usr/bin/python
# -*- coding: UTF-8 -*-

import re
import time
from datetime import datetime
# from matplotlib import pyplot as plt  
# import numpy as np 

def extractCkhOneLog(line, pattern):
    matcher = pattern.match(line) 
    if (matcher):
        time = matcher.group(1)
        codeLine = matcher.group(2)
        queryId = matcher.group(3)
        logLevel = matcher.group(4)
        content = matcher.group(5)
        return OneLog(time= time, codeLine = codeLine, \
            queryId = queryId, logLevel= logLevel, \
            content = content)

def toByteNumber(num, unit):
    if unit == 'MiB':
        return float(num) * 1024 * 1024
    elif unit == 'KiB':
        return float(num) * 1024
    elif unit == 'GiB':
        return float(num) * 1024 * 1024 * 1024
    elif unit == 'TiB':
        return float(num) * 1024 * 1024 * 1024 * 1024
    elif unit == 'B':
        return float(num)
    else:
        print('unit: ', unit)

def extractMemUsed(line, pattern):
    matcher = pattern.match(line)
    if (matcher):
        memUse = matcher.group(1) 
        unit = matcher.group(2)
        return toByteNumber(memUse, unit)

def extractDiskReserving(line, pattern):
    matcher = pattern.match(line)
    if (matcher):
        reserving = matcher.group(1) 
        reservingUnit = matcher.group(2)
        unreserved = matcher.group(3)
        unreservedUnit = matcher.group(4)
        return [toByteNumber(reserving, reservingUnit), toByteNumber(unreserved, unreservedUnit)]

def toTime(timeStr):
    t = datetime.strptime(timeStr, "%Y.%m.%d %H:%M:%S.%f")
    
    return t

def toTimestamp(timeStr) :
    d = datetime.strptime(timeStr, "%Y.%m.%d %H:%M:%S.%f")
    t = d.timetuple()
    return int(time.mktime(t))
# def toImageByQuerys(querys) {
#     from matplotlib import pyplot as plt  
#     import numpy as np 
# }

# def generateXY(query) {
#     x = np.linspace(query.startTime, query.endTime)
# }
class OneLog:
    time = ''
    codeLine = 0
    queryId = ''
    logLevel = ''
    clazz = ''
    content = ''

    def __init__(self, time, codeLine, queryId, logLevel, content):
        self.time = time
        self.codeLine = codeLine
        self.queryId = queryId
        self.logLevel = logLevel
        self.content = content

class Query:
    id = ''
    isSuccess = True
    errorInfo = ''
    startTime = ''
    endTime = ''
    start = -1
    end = -1
    memoryTotalUsed = -1   # kb
    memoryQueryUsed = -1
    reservingOnDisk = -1
    unreservedOnDisk = -1

    def __init__(self, id, startTime, endTime, memoryTotalUsed = -1, memoryQueryUsed = -1):
        self.id = id
        self.startTime = startTime
        self.endTime = endTime
        self.start = toTimestamp(startTime)
        self.end = toTimestamp(endTime)
        self.memoryTotalUsed = memoryTotalUsed
        self.memoryQueryUsed = memoryQueryUsed

    def setEndTime(self, time) :

        if(time > self.startTime and time > self.endTime):
            self.endTime = time
    # second
    def took(self) :
        # start = toTime(self.startTime)
        # end = toTime(self.endTime)
        # took = end - start
        # if took.microseconds >= 0 :
        #     return took.microseconds
        # else: 
        #     return -1
        return self.end - self.start 

    def __str__(self):
        #return 'id: %s, isSuccess: %s, startTime: %s, endTime: %s, memoryTotalUsed: %s kb, memoryQueryUsed: %s kb, reserved on disk %s, took %s' % (self.id, self.isSuccess, self.startTime, self.endTime, self.memoryTotalUsed / 1024, self.memoryQueryUsed / 1024, self.reservingOnDisk, self.took())
        m = -1
        if (self.memoryQueryUsed >= 0):
            m =  self.memoryQueryUsed
        else :
            m =  self.memoryTotalUsed
        return '[%s, %s],'  % (self.start, m)
filenames = ['/Users/henvealf/projects/my/waterlemon/watermelon-python/clickhouse_log_parser/clickhouse-server.log']
querys = {}

pattern = re.compile(r'^([0-9]{4}\.[0-9]{2}\.[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}\.[0-9]{6}) \[ ([0-9]+) \] {(.*)} <([A-Za-z]+)> (.+)$')
patternMemTotalUse = re.compile(r'MemoryTracker: Peak memory usage \(total\): (.+) ([A-Za-z]+)\.')
patternMemQueryUse = re.compile(r'MemoryTracker: Peak memory usage \(for query\): (.+) ([A-Za-z]+)\.')

patternDiskReserving = re.compile(r'Reserving (.+) ([A-Za-z]+) on disk `default`, having unreserved (.+) ([A-Za-z]+)\.')
count = 0
for filename in filenames:
    with open(filename) as file:
        for line in file:
            count += 1
            if(count > 1000000000) : break
            oneLog = extractCkhOneLog(line, pattern)
            if( not oneLog) : 
                continue
            queryId = oneLog.queryId
            if (queryId): 
                # 解析 content
                memTotalUse = extractMemUsed(oneLog.content,  patternMemTotalUse)
                memQueryUse = extractMemUsed(oneLog.content, patternMemQueryUse,)
                reverings = extractDiskReserving(oneLog.content, patternDiskReserving)
                
                if (not querys.__contains__(queryId)):
                    query = Query(id = queryId, startTime = oneLog.time, endTime= oneLog.time)
                    if (memTotalUse is not None):
                        query.memoryTotalUsed = memTotalUse
                    if (memQueryUse is not None):
                        query.memoryQueryUsed = memQueryUse
                    if (reverings is not None) :
                        query.reservingOnDisk = reverings[0]
                        query.unreservedOnDisk = reverings[1]
                    querys[queryId] = query
                    
                else :
                    query = querys[queryId]
                    query.setEndTime(oneLog.time)
                    if (memTotalUse is not None):
                        query.memoryTotalUsed = memTotalUse
                    if (memQueryUse is not None):
                        query.memoryQueryUsed = memQueryUse
                    if (reverings is not None) :
                        query.reservingOnDisk = reverings[0]
                        query.unreservedOnDisk = reverings[1]
                    if oneLog.logLevel == 'Error':
                        query.isSuccess = False
                        query.errorInfo = oneLog.content
                    
allQuery = list(querys.values())
allQuery.sort(key=lambda x:x.startTime,reverse=False)
    #if value.took() > 0.5 * 1000000:
for q in allQuery:
    print('%s' %(q))
 
    #if not value.isSuccess:
    #    print('%s, message: %s' % (value, value.errorInfo))