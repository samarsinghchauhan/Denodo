#!/usr/bin/python

# This program check naming conventions of the view developed 
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

#Check naming convention of views   
def check_naming_standard(odbcdriver,denodo_server,denodo_vdb,denodo_port,denodo_uid,denodo_pwd):
    query=r"select name,folder\
        from get_views() \
        where database_name='{0}' \
        and  ( name not like 'denodo_dv_%_%' or \
            name not like 'denodo_fv_%_%' or \
            name not like 'denodo_fiv_%_%')".format(denodo_vdb)
    results=runVQL(odbcdriver,denodo_server,denodo_vdb,denodo_port,denodo_uid,denodo_pwd,query,"N")            
     
    if results==[]:
        print("No issues Found")
    else:
        print("Issues Found")
        print(results)
    

def check_dv_forfvfiv (odbcdriver, denodo_server, denodo_port, denodo_vdb, denodo_uid, denodo_pwd) :
    print("Check if any DVs which do not have corresponding FVs")
    query = r"select 'denodo_dv_102_'||view_name from \
                (SELECT replace (name, 'denodo_dv_102_','') as view name\
                FROM GET VIEWS () where NAME LIKE 'denodo_dv_102_8'\
                minus \
                SELECT replace (name, 'denodo fv ',' ') as view name \
                FROM GET VIEWS () \
                where NAME LIKE 'dlcdm_fv_%'\
                )"
    #print (query)
    ## Execute the count query first to get number of derived views
    results=runVQL(odbcdriver, denodo_server, denodo_port, denodo_vdb, denodo_uid, denodo_pwd, query)
    #print (results)
    if results == []:
        print ("None found")
    else:
        for view in results:
            print (view[0])
        print ("--------------------------------")
        print ("Check if any DVs which do not have corresponding FIVs")
        query = r"select 'dqlcdm_dv_102_'||view name from ( \
                 SELECT replace (name, 'dqlcdm_dv_102_', ' ') as \
                view_name FROM GET VIEWS () where NAME LIKE 'denodo_dv_102_%° \
                minus \
                SELECT replace (name, 'dqlcdm_fiv_', '') as view name FROM GET VIEWS () where NAME LIKE 'dqlcdm fiv %')t"
        results=runVQL (odbcdriver, denodo_server, denodo_port, denodo_vdb, denodo_uid, denodo_pwd, query)
        #print (results)
        if results == []:
            print("No issues found")
        else:
            for view in views:
                print(view[0])

This text has been OCR'd by the free version of Textify on macOS.
You can remove this message manually or by purchasing the PRO version (via Textify > Preferences) which will help me improve the app. Thank you.
----------------------------------------------------------------------

def check_fv (odbcdriver, denodo_server, denodo_port, denodo_vdb, denodo_uid, denodo_pwd) :
    print ("Test 4. Check if any FVs which do not have corresponding FiVs")
    query =r"select 'denodo_fv_'I|view_name from \
            (\
            SELECT replace (name, 'denodo_fv_', ' ') as view_name FROM GET_VIEWS () where NAME LIKE 'denodo_fv_%' \
            minus \
            SELECT replace (name, 'denodo_fiv','') as view_name FROM GET_VIEWS () where NAME LIKE 'denodo_fiv_%'\
            )t"

    #print (query)
    results=runVQL (odbcdriver, denodo_server, denodo_port, denodo_vdb, denodo_uid, denodo_pwd, query)
    #print (results)
    if results == []:
        print ("None found")
    else:
        for view in results:
            print (view[0])

def check_column_mismtach(odbcdriver, denodo_server, denodo_port, denodo_vdb, denodo_uid, denodo_pwd) :
    print ("Test 5. Check if any Level 02 with fields not propagated to FV")
    ## Query to find the columns
    query=r"SELECT name from get views () where folder='/combine/derived views/rfdm/level 02'"
    #print (query)
    results=runVQL(odbcdriver, denodo_server, denodo_port, denodo_vdb, denodo_uid, denodo_pwd, query)
    viewlist=[]

    for views in results:
        dv_102_view_name=views[0]
        viewlist.append (dv_102_view_name)
        fv_view_name=dv_102_view_name.replace("denodo_102_","denodo_fv_")
        print("Checking view:"+fv view name)
        query = r"select column name from get view columns () where input view name='(0}' \ 
                minus \
                select column name \
                from get_view_columns() \
                where input_view_name='(1}'".format (dv_102_view_name, fv_view_name)

                results=runVQL (odbcdriver, denodo_server, denodo_port, denodo_vdb, denodo_uid, denodo_pwd, query)
                if results != []:
                    print (dv_102_view_name)
                    print (results)
                else:
                    print ("OK")

