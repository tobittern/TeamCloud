#!/bin/bash
#chkconfig: 5 80 90
#description:check
#

# source function library
#. /etc/rc.d/init.d/functions
#JAVA_HOME=/usr/bin
#export JAVA_HOME=$JAVA_HOME
#export PATH=$PATH:$JAVA_HOME/bin:$JAVA_HOME/jre/bin
AppName=euler-risk

# JVM参数
JVM_OPTS="-Dname=$AppName  -Duser.timezone=Asia/Shanghai -Xms64m -Xmx512m -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=258m -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDateStamps  -XX:+PrintGCDetails -XX:NewRatio=1 -XX:SurvivorRatio=30 -XX:+UseParallelGC -XX:+UseParallelOldGC"
cd ../
sleep 1
APP_HOME="/data/cloud"
LOG_PATH=$APP_HOME/log/$AppName/$AppName.log
PID_FILE=$APP_HOME/pid/$AppName.pid

start()
{
    echo "Starting $AppName ... "
    nohup java $JVM_OPTS -jar $APP_HOME/jar/$AppName.jar >/dev/null 2>$LOG_PATH & new_agent_pid=$!
    echo "$new_agent_pid"
    echo "$new_agent_pid" > $PID_FILE
    echo "start success."
}

stop()
{

     if [ -f $PID_FILE ];then
                    SPID=`cat $PID_FILE`
                      if [ "$SPID" != "" ];then
                         kill -9  $SPID

                         echo  > $PID_FILE
                      fi
                      sleep 3
                      CheckProcessStata $SPID >/dev/null
                      if [ $? != 0 ];then
                         rm $PID_FILE
                         echo "stop success."
                      fi
     fi
}

CheckProcessStata()
{
    CPS_PID=$1
    if [ "$CPS_PID" != "" ] ;then
        CPS_PIDLIST=`ps -ef|grep $CPS_PID|grep -v grep|awk -F" " '{print $2}'`
    else
        CPS_PIDLIST=`ps -ef|grep "$CPS_PNAME"|grep -v grep|awk -F" " '{print $2}'`
    fi

    for CPS_i in `echo $CPS_PIDLIST`
    do
        if [ "$CPS_PID" = "" ] ;then
            CPS_i1="$CPS_PID"
        else
            CPS_i1="$CPS_i"
        fi

        if [ "$CPS_i1" = "$CPS_PID" ] ;then
            #kill -s 0 $CPS_i
            kill -0 $CPS_i >/dev/null 2>&1
            if [ $? != 0 ] ;then
                echo "[`date`] MC-10500: Process $i have Dead"
                kill -9 $CPS_i >/dev/null 2>&1

                return 1
            else
                #echo "[`date`] MC-10501: Process is alive"
                return 0
            fi
        fi
    done
    echo "[`date`] MC-10502: Process $CPS_i is not exists"
    return 1
}

status()
{
  SPID=`cat $PID_FILE`
  CheckProcessStata $SPID >/dev/null
  if [ $? != 0 ];then
        echo "$prog:{$SPID}  Stopped ...."
  else
        echo "$prog:{$SPID} Running Normal."
  fi

}

restart()
{
    echo "stoping ... "
    stop
    echo "staring ..."
    start
}

case "$1" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    status)
         status
        ;;
    restart)
        restart
        ;;
    *)
        echo $"Usage: $0 {start|stop|restart}"
        RETVAL=1
esac
exit $RETVAL
