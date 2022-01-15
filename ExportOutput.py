#!/usr/bin/python
##########################################
#  This program run Denodo views and extract results into CSV files
#  Extracted CSV files are saved to a shared network drive
##########################################

from mypackage import denodo_functions
from mypackage import denodo_connection

import os.path
from os import path


odbcdriver=denodo_connection.details[env]['odbcdriver']
denodo_server=denodo_connection.details[env]['server']
denodo_port=denodo_connection.details[env]['port']
denodo_vdb="denodo_vdb"
denodo_uid=denodo_connection.details[env]['userid']
denodo_pwd=denodo_connection.details[env]['password']

#Folder details
outfolder="c:\\output"

if __name__ == "__main__":
    env=input("select environment:(dev/uat/prod")
    viewlist=input("Enter comma seperated views")
    
    for view in viewlist.split(","):
        print("Exporting data for view:",view)
        outfile=r"{0}.csv".format(view)

        #Create output file in overwrite mode
        f = open(outfile,"w")
        query=r"select * from {0}.{1}".format(denodo_vdb,view)
        output=denodo_functions.runVQLoutput(odbcdriver,denodo_server,denodo_port,denodo_uid,denodo_pwd,query,create_flag="N")
        denodo_functions.exportCSV(output,outfile)
        f.close()
        
        #
