#!/bin/sh
#
# scheduler Start/stop the Scheduler server daemon.
#
# chkconfig: 345 90 60
# description: Scheduler Server
DENODO_HOME="/opt/denodo_platform"
VDP_USER="vdp"
# Source function library.
#. /etc/rc.d/init.d/functions
start() {
   local PID=$(get_pid)
   if [ -z $PID ]; then
          su -c "$DENODO_HOME/bin/scheduler_startup.sh" $VDP_USER || return 0
   else
          echo "Scheduler server (pid $PID) is already running."
   fi
}
stop() {
   local PID=$(get_pid)
   if [ -z $PID ]; then
          echo "Scheduler server is not running."
   else
          $DENODO_HOME/bin/scheduler_shutdown.sh
   fi
}
restart() {
   stop
   start
}
reload() {
   restart
}
force_reload() {
   # new configuration takes effect after restart
   restart
}
status() {
   local PID=$(get_pid)
   if [ -z $PID ]; then
          echo "Scheduler server is not running."
   else
          echo "Scheduler server (pid $PID) is running."
   fi
}
get_pid() {
   local P=`ps -fwwu $VDP_USER | grep -- "Denodo Scheduler Server" | grep -v grep | awk '{print $2}'`
   echo "$P"
}
case "$1" in
   start)
          start
          ;;
   stop)
          stop
          ;;
   restart)
          restart
          ;;
   status)
          status
          ;;
   *)
          echo $"Usage: $0 {start|stop|status|restart}"
          exit 2
esac
exit $?