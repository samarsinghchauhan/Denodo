#!/usr/bin/python
######################################################################
# This program execute test cases on Denodo Views with CIB360 DQ rules
#  and captures test results in excel file
########################################################################
from mypackage import denodo_functions
from mypackage import denodo_connection
import datetime
from datetime import datetime
from datetime import date
import xlrd
import xlwt
import os. path
from os import path
env="dev"
# Get connection details
odbedriver=denodo_connection. details [env]['odbodriver']
denodo_server=denodo_connection.details[env]['server']
denodo_vdb="denodo_vdb"
denodo_uid=denodo_connection. details[env] ['userid']
denodo_pwd=denodo_connection.details[env]['password']
#Folder and file details
foldername="C:\\Output"
inputfilename= "DenodoViews.xls"
input_Sheet_name="Sheet1"


thiscript=__file__
filename=thiscript.replace(".py",".check")
checkpointfile=foldername+"\\"+filename

#current timestamp
dt=date. today (). strftime ('%Y-%m-%d')
viewlist=[]
#final results={"rownumber":,"details":}
final_results={}
if __name__ == "__main__":

    skip=int (denodo_functions. getcheckpoint (checkpointfile))

    # Open the input file
    wb=xlrd.open_workbook (inputfilename)
    readsheet=wb.sheet_by_name(input_Sheet_name)
    noofrows=(readsheet.nrows)
    print ("Number of rules to process:", noofrows-1)
    opennewfileflag=1
    writefileflag=0
    prevview=""
    for n1 in range (1, noofrows):
        if n1 < skip:
            continue
        else:
            # for every row in the file. create and execute query
            if writefileflag == 1:
                                
                writeworkbook. save (output file)
                #print ("File saveed:", output file)
                opennewfileflag=0

                rownumber=readsheet.cell value (n1,0)
                view=readsheet.cell value (n1,1)
                rule=readsheet.cell value (n1,2)
                column=readsheet.cell value (n1,3)

                query=r"select distinct {0],(1] from {2].{3]".format (rule, column, denodo vdb, view)
                results=denodo_functions.runVQL(odbcdriver,denodo_server,denodo_port,denodo_vdb,denodo_uid,denodo_pwd,query,"N")
                print ("Processing-",rownumber, view,rule, column)
                print ("Curreview:", view, "PrevView:", prevview)

                if view != prevview:
                    opennewfileflag=1
                    writefileflag=1

                if opennewfileflag:
                
                    outputfilename = r"{0}_{1].xls". format (view, dt)
                    output_file = foldername+"\\"+outputfilename

                    #Write Sheet
                    writeworkbook = xlwt. Workbook ()
                    print ("File Opened:", output_file)
                    opennewfileflag=0

                    # new sheet for every query
                    sheetname=rule[0:20]
                    writesheet = writeworkbook.add_sheet(sheetname)
                    # Header Line
                    rownumber=0
                    row.writesheet.row(rownumber)
                    row.write(0,"#")
                    row.write (1, "View Name")
                    row.write (2, "Rule")
                    row.write (3, "Column")
                    row.write (4, "Test Query")
                    row.write (5, "Rule Value")
                    row.write (6, "Column Value")
                    
                    #print (results)
                    for n2 in range (0, len (results)) :
                        rownumber=rownumber+1
                        #print (rownumber)
                        row=writesheet.row(rownumber)
                        for C in range (0,7) :
                            if c == 0 and n2 == 0: row.write (c,n2+1)
                            if c == 1 and n2 == 0: row.write (c,view)
                            if c == 2 and n2 == 0: row.write (c,rule)
                            if c == 3 and n2 == 0: row.write (c, column)
                            if c == 4 and n2 == 0: row.write (c, query)
                            if c == 5 and n2 == 0: row.write (c,results [n2] [0]) 
                            if c == 6 and n2 == 0: row.write (c,results [n2] [1]) 
                        if n2 == len(final_results): writefileflag=1

                        prevview=view
                    denodo_functions.writecheckpoint(checkpointfile,str(n1))
                print("File Saved",output_file)
                writeworkbook.save(output_file)
                print("Program Completed Successfully!")