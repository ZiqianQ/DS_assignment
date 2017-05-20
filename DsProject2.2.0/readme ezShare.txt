The ezShare.jar contains two packages, Server and Client, the main method of each is Server and Client respectly. 
1,In order to use Server, 
you have to create a folder named "server_files" where you store this jar, which used to store the files that can be shared by clients. 

2,The command to use Server is "java -jar ezShare.jar Server.server (args)", where args could be:
    -advertisedhostname<arg>
    -connectionintervallimit<args>
    -exchangeinterval<arg>
    -port<arg>   (integer)
    -secret<arg>   
    -debug
  If you don't know how to use this commands, just enter -h/-help to get more information about them.

3,The Client contains many functions, the commond command is "java -jar ezShar.jar Client.Client (args)", the main functions are:
    -query
    -publish
    -remove
    -fetch
    -exchange
  If you want to get more information about how to use them, just enter -h/-help to get more.

4,The files that download from server using -fetch will be stored where the jar file keeps.

5,a log file will be create where the jar file keeps, you can open and see what command and situation were happened on server.

6,server have to create a folder named "server_files" to store file allowed fetch

7,client have to create a folder named "client_files" to download files

