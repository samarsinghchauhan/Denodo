/*******.    Find all views in a particular folder. *********************/
select *
from get_views()
where input_database_name='Denodo_vdb'
and folder in (<list of Folders >)


/*******.    Find all views which are updated after certain date. ********/
select *
from get_views()
where input_database_name='Denodo_vdb'
and last_modification_date >= ' 2021-12-06'

/*********. Dependent Views ************************************************/ 
select *
from view_dependencies()
where input_database_name='denodo_vdb'
and input_view_name='denodo_dv_sample_view'
and position ('Datasource' IN dependency_type > 0 )

/*********. View Columns *****************************************************/ 
SELECT view_name, column_name, column_vdp_type, column_sql_type, column_is_primary_key
FROM GET_VIEW_COLUMNS()
WHERE input_database_name='denodo_vdb' and input_view_name = '%invoice%'

/******* list of the indexes of views ****************************************/
SELECT unique, index_name, type
FROM get_view_indexes()
WHERE input_database_name ='denodo_vdb' AND input_view_name = 'invoice'

***** Get information about the source of a JDBC base view. ******************/
SELECT *
FROM GET_SOURCE_TABLE()
WHERE input_database_name = 'denodo_vdb'
AND input_view_name = 'invoice';
