#!/usr/bin/python

## Connection parameters to the Denodo VDP Server
details= {
'dev':{
'odbcdriver' : 'DenodoODBC Unicode(x64)',
'server': 'deenodo.dev.corp
'port' : '9996',
'userid':'devuser',
'password':'devpassword'
},
preprod':{
'odbcdriver' : 'DenodoODBC Unicode(x64)',
'server': 'deenodo.preprod.corp
'port' : '9996',
'userid':'devuser',
'password':'devpassword'
},
'prod':{
'odbcdriver' : 'DenodoODBC Unicode(x64)',
'server': 'deenodo.prod.corp
'port' : '9996',
'userid':'devuser',
'password':'devpassword'
}