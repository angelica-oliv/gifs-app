sudo update-alternatives --set javac /usr/lib/jvm/java-11-openjdk-amd64/bin/javac
sudo update-alternatives --set java /usr/lib/jvm/java-11-openjdk-amd64/bin/java

export JAVA_HOME='/usr/lib/jvm/java-11-openjdk-amd64'
envman add --key JAVA_HOME --value '/usr/lib/jvm/java-11-openjdk-amd64'