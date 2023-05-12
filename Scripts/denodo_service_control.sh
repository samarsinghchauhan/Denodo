#!/bin/bash
DENODO_HOME=/opt/denodo
case "$1" in
        vqlserver)
                case "$2" in
                        start)
                                echo "Starting Denodo Virtual DataPort..."
                                $DENODO_HOME/bin/vqlserver.sh startup > /dev/null 2>&1
                                echo "ok"
                                ;;
                        stop)
                                echo "Stopping Denodo Virtual DataPort..."
                                $DENODO_HOME/bin/vqlserver.sh shutdown > /dev/null 2>&1
                                echo "ok"
                                ;;
                esac
                ;;
        scheduler)
                case "$2" in
                        start)
                                echo "Starting Denodo Scheduler..."
                                $DENODO_HOME/bin/scheduler_startup.sh > /dev/null 2>&1
                                echo "ok"
                                ;;
                        stop)
                                echo "Stopping Denodo Scheduler..."
                                $DENODO_HOME/bin/scheduler_shutdown.sh > /dev/null 2>&1
                                echo "ok"
                                ;;
                esac
                ;;
        denodo-data-catalog)
                case "$2" in
                        start)
                                echo "Start Denodo Data Catalog..."
                                $DENODO_HOME/bin/webcontainer.sh start denodo-data-catalog > /dev/null 2>&1
                                echo "ok"
                                ;;
                        stop)
                                echo "Stopping Denodo Data Catalog..."
                                $DENODO_HOME/bin/webcontainer.sh stop denodo-data-catalog > /dev/null 2>&1
                                echo "ok"
                                ;;
                esac
                ;;
        denodo-scheduler-admin)
                case "$2" in
                        start)
                                echo "Starting Denodo Scheduler Admin Tool..."
                                $DENODO_HOME/bin/webcontainer.sh start webadmin/denodo-scheduler-admin > /dev/null 2>&1
                                echo "ok"
                                ;;
                        stop)
                                echo "Stopping Denodo Scheduler Admin Tool..."
                                $DENODO_HOME/bin/webcontainer.sh stop webadmin/denodo-scheduler-admin > /dev/null 2>&1
                                echo "ok"
                                ;;
                esac
                ;;
                diagnostic-monitoring-tool)
                case "$2" in
                        start)
                                echo "Starting Denodo Diagnostic & Monitoring Tool..."
                                $DENODO_HOME/bin/webcontainer.sh start diagnostic-monitoring-tool > /dev/null 2>&1
                                echo "ok"
                                ;;
                        stop)
                                echo "Stopping Denodo Diagnostic & Monitoring Tool..."
                                $DENODO_HOME/bin/webcontainer.sh stop diagnostic-monitoring-tool > /dev/null 2>&1
                                echo "ok"
                                ;;
                esac
                ;;
                designstudio)
                case "$2" in
                        start)
                                echo "Starting Denodo Web Design Studio..."
                                $DENODO_HOME/bin/webcontainer.sh start denodo-design-studio > /dev/null 2>&1
                                echo "ok"
                                ;;
                        stop)
                                echo "Stopping Denodo Web Design Studio Tool..."
                                $DENODO_HOME/bin/webcontainer.sh stop deonodo-design-studio > /dev/null 2>&1
                                echo "ok"
                                ;;
                esac
                ;;
esac
exit 0