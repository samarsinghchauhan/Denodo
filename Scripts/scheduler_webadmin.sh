#!/bin/sh
#
# scheduler_webadmin  Start/stop the Scheduler Administration Tool daemon.
#
# chkconfig: 345 90 60
# description: Scheduler Administration Tool
DENODO_HOME="/opt/denodo_platform"
VDP_USER="vdp"
# Source function library.
. /etc/rc.d/init.d/functions
start() {
   su -c "$DENODO_HOME/bin/scheduler_webadmin_startup.sh" $VDP_USER || return 0
}
stop() {
   $DENODO_HOME/bin/scheduler_webadmin_shutdown.sh
}
restart() {
   stop
   start
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
   *)
          echo $"Usage: $0 {start|stop|restart}"
          exit 2
esac
exit $?