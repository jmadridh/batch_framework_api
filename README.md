# Batch Framework Application 
---
Developed for Inchcape PLC.


## Description
---
This Batch API provides a mechanism to push data to Salesforce Cloud from a hierarchy folder SFTP server. 


### Pre-requirements
---

There are a number of pre-requirements to run this batch api. 
By group;
> * File
> * System API
> * Queueing Mechanism


In the first group of pre-requirements, it involves and describe the set of features that a file **MAY HAVE**.
1. The file **MUST** be stored previously in a folder SFTP.
2. The file **FOLLOW** a pattern name with a *CSV* extension.
3. The file **HAS** 'read/write' permissions.
4. The file **HAS* a file structure human readable.

In the second group of pre-requirements, it is related to the bridge system which serves to operate functions to push data into Salesforce Cloud. 
1. The System API **SHOULD** be running and listening on the HTTPS port.
2. The System API **MUST** performs the operation required to push data in a secured channel communication.
3. The System API **SHOULD** track each transaction in a logging system.

In the final set of pre-requirements, those are enclosed to the queue mechanism: 
1. To persist records from the file, the batch api *WILL* publish them to an Exchange in Anypoint MQ Service configured to Inchcape.
2. The Queue *SHOULD HAVE* a TTL in order to let the retry mechanism to process all those records in case of unexpected errors. 
3. The Exchange and Queue *SHOULD HAVE* a pattern name that correlate with the batch interface. 
4. The group of exchange and queue *SHOULD HAVE* previously configured.


### Endpoints
---
Current available endpoints

*Settings*
`https://globalbatchapi.cloudhub.io/batch/setting`
Endpoint to set parameter configurations.

#### Body
---
```
Under construction
```
#### Response
---
```
Under construction
```

*Reporting*
`https://globalbatchapi.cloudhub.io/batch/report`
Endpoint to retrieve latest execution of batch interface.

#### Body
---
```
Under construction
```

#### Response
```
Under construction
```

### Go further ###
---
- Inchcape [About](https://www.inchcape.co.uk/about-us/) 