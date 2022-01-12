/*******.    Find all views in a particular folder. **************************/

select *
from get_views()
where input_database_name='Denodo_vdb'
and folder in (<list of Folders >)


/*******.    Find all views which are updated after certain date. ***************/

select *
from get_views()
where input_database_name='Denodo_vdb'
and last_modification_date >= ' 2021-12-06'

/*********. Dependent Views *****************************************************/ 

select *
from view_dependencies()
where input_database_name='denodo_vdb'
and input_view_name='denodo_dv_sample_view'
and position ('Datasource' IN dependency_type > 0 )



