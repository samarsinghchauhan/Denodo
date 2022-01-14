#!/usr/bin/python
########################################################################
# This program creates a materialized view with test data for
# give view and column name 
########################################################################
from mypackage import denodo functions
from mypackage import denodo connection
import datetime
import pyodbe as dbdriver
from socket import gethostname

from datetime import datetime
from datetime import date
import xlrd
import xlwt
import os.path
from os import path
############################################################
env="dev"
# Get connection details
odbcdriver=denodo_connection.details[env] ['odbcdriver']
denodo_server=denodo_connection.details[env]['server']
denodo_port=denodo_connection.details[env]['port']
denodo_vdb="dqlcdm vdb"
denodo_uid=denodo_connection.details[env]['userid']
denodo_pwd=denodo_connection.details[env]['password']

############################################################
text_values={"JOHN"," JOHN DOE","JOHN DOE SR","JOHN DOE SMITH","JOHN DOE SR.",""," "
"!@#$%^&*()-/","JOHN O` DOE","JOHN.DOE","JOHN,DOE","JOHN ",
"John P. O'Neill","JOHN.DOE@EMAIL.COM","JOHN 1234567890","123456789",
"123456789.00","123456789.99","0","0.0","-1","-0.0001","12345 12345",
"1","22","333","4444","55555","666666","7777777"}
int_values={"0","","1","-1","0.0","-0.1"}
decimal_values={}
date_values={}

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

if __name__ == "__main__":
    in_view=input("View names (Single or comma seperated views):")
    in_col_list=input("List of test Columns:")
    in_date_col=input("Date Column ( Press enter if no Column)")
    in_date_val=''

    if in_date_col != "":
        in_date_val=input("Date Value in YYYY-MM-DD format including quotes:")

    for view in in_view.split():
        print(in_view)
        mat_view=r"mv_{0}".format(view)
        create_mv=r"create or replace materialized table {0} as select * from {1} where 1=2".format(mat_views,view)
        runVQL(odbcdriver,denodo_server,denodo_port,denodo_vdb,denodo_uid,denodo_pwd,create_mv)
        print()

