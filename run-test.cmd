set proxyHost=octodev01
set proxyPort=3128
set proxyUser=myproxyuser
set proxyPassword=password

mvn -Dhttps.proxyHost=%proxyHost% -Dhttps.proxyPort=%proxyPort% -Dhttps.proxyUser=%proxyUser% -Dhttps.proxyPassword=%proxyPassword% test $*
