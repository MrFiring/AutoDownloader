import os
import shutil
from os import listdir
from os.path import isfile, join, exists

p = "/media/Disk_D/Development/Projects/JAVA/AutoDownloaderServer/forTest"
MP3_PATH = "/media/Disk_D/Development/Projects/JAVA/AutoDownloaderServer"
if( not exists(p)):
	os.mkdir(p)



onlyFiles = [f for f in listdir(p) if isfile(join(p,f))]

mp3List = [m for m in onlyFiles if m.endswith(".mp3")]
normalMp3List = []

if(len(mp3List) > 0):
        print ("BAD FILES: ")
        bads = open(p+"/badfiles.list", "w")
        for mp3 in mp3List:
            f = open(p+ "/" + mp3,'r')
            line = f.readline()
            f.close()
            if( not line.find("<!DOCTYPE html PUBLIC")):
                print (mp3)
                bads.write(mp3 + "\n")
                os.remove(p+"/"+mp3)
            else:
                shutil.copy(p+"/"+mp3, MP3_PATH)
                normalMp3List.append(mp3)
                os.remove(p+"/"+mp3)
        bads.close()
        print ("\n\n\nNormals: ")
        for st in normalMp3List:
                print (st)             
else:
        print ("Files not found.")


        
raw_input("Press any key to close")
