#!/bin/sh
# preview.sh by Aleksei Eaves
# bash script which generates preview.txt which displays the first five lines of every CSV file in the directory.
rm preview.txt
for i in $(ls -1 | grep csv)
do
	echo -e "\t~~~" $i "~~~" >> preview.txt
	head -n 5 $i >> preview.txt
done