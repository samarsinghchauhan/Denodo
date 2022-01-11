#!/usr/bin/python
 
import xlrd
import xlwt
import csv
import datetime
import pyodbc as dbdriver
from socket import gethostname
from datet
from datetime import date

## Python Functions

## Function to run VQL
def runVQL(odbcdriver,denodo_server,denodo_port,denodo_vdb,denodo_uid,denodo_pwd,query,create_flag="N"):
    ## Create the useragent as the concatenation of
    ## the client hostname and the python library used
    client_hostname = gethostname()
    useragent = "%s-%s" % (dbdriver._name_,client_hostname)
    ## Establishing a connection
    connection = dbdriver.connect(
        driver = odbcdriver,
        server = denodo_server,
        port = denodo_port,
        database = denodo_vdb,
        uid = denodo_uid,
        pwd = denodo_pwd,
        useragent = useragent
    )
    ## Declar cursor

    cur = connection.cursor()
    try:
        cur.execute(query)
    except:
        print("Query Failed",query)
        return 0
    if create_flag=="Y":
        results=""
    else:
        results=cur.fetchall()
    cur.close()
    conection.close()
    return results

 ## Function to run vQL and return formatted results
def runVQLoutput(odbcdriver, denodo_server,denodo_port, denodo_vdb,denodo_uid,denodo_pwd,query,create_flag="N"):
    ## Create the useragent as the concatenation of
    ## the client hostname and the python library used
    
    client_hostname = gethostname()
    useragent = "%s-%s" % (dbdriver._name_,client_hostname)
    ## Establishing a connection
        connection = dbdriver.connect(
        driver = odbcdriver,
        server = denodo_server,
        port = denodo_port,
        database = denodo_vdb,
        uid = denodo_uid,
        pwd = denodo_pwd,
        useragent = useragent
    )
    ## Declar cursor
    cur = connection.cursor()
    try:
        cur.execute(query)
    except:
        print("Query failed", query)
        return ""
    
    if create_flag == "Y":
        results=""
    else:
        results = cur.fetchall()

    formattedresults = []
    elements=[]
    for element in cur.description:
        elements.append(element[0])
    formattedresults.append(elements)

    for line in results:
        formattedresults.append(list(map(str,list(line))))
    cur.close()
    connection.close()
    return formattedresults

    #Functionto export results to CSV
    def exportCSV(results,outputfile):
        with open(outputfile,'w',newline='') as csvfile:
            writer=csv.writer(csvfile)
            for row in results:
                writer.writerrow(row)
        print("Results exported to file",outputfile)

##Function to check if view name is valid and it actually exists, returns 1 if valid, else returns 0
def check_if_valid_view(odbcdriver,denodo_server,denodo_port,denodo_vdb,denodo_uid,denodo_pwd,view_name): 
    query = r"select count(*) from {0}.get_views() where name='{(1}".format(denodo_vdb,view_name)
    results=runVQL(odbcdriver,denodo_server,denodo_port,denodo_vdb,denodo_uid,denodo_pwd,query)
    return results[o][o]

##Function returns list of views for given server or inside folder or by full name or partial name
def get_views(odbcdriver,denodo_server,denodo_port,denodo_vdb,denodo_uid,denodo_pwd,folder,view_name): 
    folder=folder.lower()
    view_list=[]

    if view_name and view_name != ": 
        print("Getting view list:"+view_name)
        querylist=r"select name \
                from {0}.get_views() \ 
                where input_database_name='{o}' \
                and name like '{1}%' \
                order by 1".format(denodo_vdb,view_name)

        # check if any folder was provided elif folder and folder != ":
        print("Getting view list for folder:"+folder)
        querylist=r"select name \
        from {0}.get_views() \
        where input_database_name='{0}"\
        and folder ='{1} \
        order by 1".format(denodo_vdb,folder)
        else:
        print("Getting view list for database:"+denodo_vdb)
        querylist=r"select name \
        from (O).get_views()
        where input_database_name='{0}"\



