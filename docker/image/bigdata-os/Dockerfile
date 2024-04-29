# CentOS with JDK 8
# build a new image with basic  centos
FROM centos:7
# who is the author
MAINTAINER kai.zhao


RUN mkdir -p /opt/bigdata
ADD bigdata_env.sh /etc/profile.d


#install jdk
ARG JAVA_VERSION=8
ARG JAVA_RELEASE=JDK

RUN pkg="java-1.$JAVA_VERSION.0-openjdk" && \
    if [ "$JAVA_RELEASE" = "JDK" ]; then \
        pkg="$pkg-devel"; \
    else \
        pkg="$pkg-headless"; \
    fi; \
    yum install -y "$pkg"
# set java environment variables

ENV JAVA_HOME /usr/lib/jvm/java-1.8.0
ENV JRE_HOME ${JAVA_HOME}/jre
ENV CLASSPATH .:${JAVA_HOME}/lib:${JRE_HOME}/lib

# install openssh
RUN yum -y install openssh-server openssh-clients
RUN yum -y install vim which net-tools
RUN yum install epel-release -y && yum -y install supervisor

#generate key files
RUN ssh-keygen -q -t rsa -b 2048 -f /etc/ssh/ssh_host_rsa_key -N ''
RUN ssh-keygen -q -t ecdsa -f /etc/ssh/ssh_host_ecdsa_key -N ''
RUN ssh-keygen -q -t dsa -f /etc/ssh/ssh_host_ed25519_key  -N ''

# login localhost without password
RUN ssh-keygen -f /root/.ssh/id_rsa -N ''
RUN touch /root/.ssh/authorized_keys
RUN cat /root/.ssh/id_rsa.pub >> /root/.ssh/authorized_keys

# set password of root
RUN echo "root:123456" | chpasswd



# open the port 22
EXPOSE 22
# when start a container it will be executed
CMD ["/usr/sbin/sshd","-D"]