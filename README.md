# Data Virtualization

Data virtualization is a logical data layer that integrate
s all enterprise data siloed across the disparate systems, manages the unified data for centralized security and governance, and delivers it to business users in real time.

# Denodo
Denodo is the leader in data virtualization. With more than 20 years of innovation, the Denodo Platform offers enterprise-grade data virtualization with an easy-to-use interface. More than 700 customers across 35 industries trust it to conduct complex business operations including supplier management, regulatory compliance, data-as-a-service, systems modernization, and others.

# Obejctive of the project
This project aims at providing automation for variuos development and admin activties done in Denodo. As part of this project several small python scripts are created which individually perform a specific functionality. I am in process of compiling these into a singe library

Please note - I am new to Python and this is my first self study project -  so you may find the code amateurish. Please feel free to suggest and guide me with better way of implementing. 

# Functionalities implemented
## 1. Create interface Views on of top of derived views
 In any Denodo implementation, often special types of views known as Interface views created which are accessed by downstream reporting applications. These views  consist only of a definition of fields and a reference to another implementaion view. These interface views are often used to do top-down design where you first   define the fields of the interface and at a later stage, associate the “implementation view” of the interface.
Often interface views are mapped to one “implementation view” with  one to one mapping of fields and no business logic.
When you have hundreds of interface views to be created or maintained syncing metadata between "interface" and "implementation" can be challenging.
 
This script creates an interface view over the supplied interface view. 
     
## 2. Create Materialize view the test dataa
Often when testing any derived view, a reliable test data is needed when permutations and combinations of different values. This script creates a materialized view with same metadata as given derived view , and populates it with test data. Along with derived view, column list have to be provided for which test data needs to be generated. 
  
## 3. Code Review  
1. Check the naming convention of the views - base, derived and interface views.
2. Check between level 1 derived view and level 2 view - if column names are same then the data typs should also match.
3. Check between level 2 derived view and interface view - if column names are same then the data typs should also match.
  
## 4. Running the view and exporting all records into CSV file
Often its required to share output of views in CSV format. Running multiple views and exporting data into CSV file could be time consuming. This functionaly runs given view and export data into CSV file and Zip the CSV file.

## 5. Reusable VQL Queries
 These queries are created on Denodo Metadata Tables, Stored Procedure
 1. Find all views in a particular folder
 2. Find all views which are updated after certain date
 3. Finding Dependent Views for given view
 4. Finding Columns for given view
 5. List of the indexes of views 
 6. Extracts information about the source of a JDBC base view

  



