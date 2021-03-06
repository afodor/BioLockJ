# AddMetadataToTaxaTables
Add to module run order:                    
`#BioModule biolockj.module.report.taxa.AddMetadataToTaxaTables`

## Description 
Map metadata onto taxa tables using sample ID.

## Properties 
*Properties are the `name=value` pairs in the [configuration](../../../Configuration#properties) file.*                   

### AddMetadataToTaxaTables properties: 
*none*

### General properties applicable to this module: 
| Property| Description |
| :--- | :--- |
| *cluster.batchCommand* | _string_ <br>Terminal command used to submit jobs on the cluster<br>*default:*  *null* |
| *cluster.jobHeader* | _string_ <br>Header written at top of worker scripts<br>*default:*  *null* |
| *cluster.modules* | _list_ <br>List of cluster modules to load at start of worker scripts<br>*default:*  *null* |
| *cluster.prologue* | _string_ <br>To run at the start of every script after loading cluster modules (if any)<br>*default:*  *null* |
| *cluster.statusCommand* | _string_ <br>Terminal command used to check the status of jobs on the cluster<br>*default:*  *null* |
| *docker.saveContainerOnExit* | _boolean_ <br>If Y, docker run command will NOT include the --rm flag<br>*default:*  *null* |
| *docker.verifyImage* | _boolean_ <br>In check dependencies, run a test to verify the docker image.<br>*default:*  *null* |
| *metadata.columnDelim* | _string_ <br>defines how metadata columns are separated; Typically files are tab or comma separated.<br>*default:*  \t |
| *metadata.commentChar* | _string_ <br>metadata file comment indicator; Empty string is a valid option indicating no comments in metadata file.<br>*default:*  *null* |
| *metadata.filePath* | _string_ <br>If absolute file path, use file as metadata.<br>If directory path, must find exactly 1 file within, to use as metadata.<br>*default:*  *null* |
| *metadata.nullValue* | _string_ <br>metadata cells with this value will be treated as empty<br>*default:*  NA |
| *report.taxonomyLevels* | _list_ <br>Options: domain,phylum,class,order,family,genus,species. Generate reports for listed taxonomy levels<br>*default:*  phylum,class,order,family,genus |
| *script.defaultHeader* | _string_ <br>Store default script header for MAIN script and locally run WORKER scripts.<br>*default:*  #!/bin/bash |
| *script.numThreads* | _integer_ <br>Used to reserve cluster resources and passed to any external application call that accepts a numThreads parameter.<br>*default:*  8 |
| *script.numWorkers* | _integer_ <br>Set number of samples to process per script (if parallel processing)<br>*default:*  1 |
| *script.permissions* | _string_ <br>Used as chmod permission parameter (ex: 774)<br>*default:*  770 |
| *script.timeout* | _integer_ <br>Sets # of minutes before worker scripts times out.<br>*default:*  *null* |

## Details 
The output of this module will have a row for each sample (just like the metadata and the taxa tables) and columns for data AND metadata.                   
*If the pipeline input does not include at least one taxa table, then the BuildTaxaTables class is added by this module as a pre-requisite.*


## Adds modules 
**pre-requisite modules**                    
*pipeline-dependent*                   
**post-requisite modules**                    
*none found*                   

## Docker 
If running in docker, this module will run in a docker container from this image:<br>
```
biolockjdevteam/biolockj_controller:v1.3.6
```
This can be modified using the following properties:<br>
`AddMetadataToTaxaTables.imageOwner`<br>
`AddMetadataToTaxaTables.imageName`<br>
`AddMetadataToTaxaTables.imageTag`<br>

## Citation 
Module developed by Mike Sioda and Anthony Fodor                   
BioLockJ v1.3.6

