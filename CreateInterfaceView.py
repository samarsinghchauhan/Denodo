#!/usr/bin/python

# This program Creates a 1:1 derived view for a  view supplied
import pyodbc as dbdriver
from socket import gethostname
from mypackage import denodo_functions
from mypackage import denodo_connection

env="dev"

# Get Connection Details
odbedriver=denodo_connection. details [env]['odbodriver']
denodo_server=denodo_connection.details[env]['server']
denodo_port=denodo_connection.details[env]['port']
denodo_vdb="denodo_vdb"
denodo_uid=denodo_connection. details[env] ['userid']
denodo_pwd=denodo_connection.details[env]['password']
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

def create_finalview  (odbcdriver, denodo_server, denodo_port, denodo_vdb, denodo_uid, denodo_pwd,view_name):
    # Derived folder name based on view name 
    input_view_name_db=denodo_vdb+". "+view_name 
    source_system=view_name.replace("dqlcdm_dv_102_","").replace("dqlcdm_dv_101_","").split("_")[0]
    output_folder_name-"/publish/final views/"+source_system
    output_view_name=view_name.replace("_dv_101_","_fv_").replace(" dv_102_","_fv_")
    output_view_name_db=denodo_vdb+"."view_name.replace("_dv_102_","_fv_")

    print  ("Creating final view: ", output_view_name)
    query_columns =r"desc view {0}". Format  (view_name)
    print (query_columns)

    ## Execute the count query first to get number of derived views 
    results=runVQL(odbcdriver, denodo_server, denodo_port, denodo_vdb, denodo_uid, denodo_pwd, query_columns)
    print(results)
    query_create_fv=r"create or replace view {0} folder='{1} as ".format(output_view_name, output_folder_name)
    query_create_fv=query_create_fv+"\n"+"select"+"\n"
    
    for n in range(0,len (results)):
        if n+1 == len (results):
            query_create_fv=query_create_fv+" "+results[n][0]+"\n"+"from "+input_view_name_db

        else: 
            query_create_fv=query_create_fv+" "+results[n][0]+",\n"
    print(query_create_fv)
    results=runVQL(odbcdriver, denodo_server, denodo_port, denodo_vdb, denodo_uid, denodo_pwd, query_create_fv)
    return output_view_name


def create_finalinterfaceview (odbodriver,denodo_server,denodo_port, denodo_vdb, denodo_uid, denodo_pwd, view_name) : 
    # Derived folder name based on view name
    input_view_name_db=denodo_vdb+"."+view_name
    source_system=view_name.replace("dqlcdm fv ","").split(" ")[0]
    output_folder_name="/publish/final interface views/"+source_system
    output_view_name=view_name.replace(" fv fiv ")
    output_view_name_db=denodo_vdb+" "+output_view_name

    print("Creating final interface view:",output_view_name) ## Query to find the columns
    query_columns=r"desc view (0}".format (input_view_name_db) 
    query_create_fiv=r"create or replace interface view {0}".format(output_view_name)
    query_create_fiv=query_create_fiv+"\n"

    ## Execute the count query first to get number of derived views 
    results=runVQL(odbcdriver,denodo_server,denodo_port,denodo_vdb,denodo_uid,denodo_pwd,query_columns)
    
    for n in range(0,len (results)) :
        #print (n,len (results))
        #print (results[n][0],results[n][1],results[n][2])
        if results[n][1]=="BIGINT": datatype="long"
        elif results[n] [1]=="BIT": datatype="boolean"
        elif results[n] [1]=="BLOB": datatype="blob"
        elif results[n][1]=="BOOLEAN": datatype="boolean"
        elif results[n][1]=="CHAR": datatype="text"
        elif results[n] =="CLOB": datatype="text"
        elif results[n] [1]=="DATE": datatype="localdate"
        elif results[n][1]=="DECIMAL": datatype="decimal"
