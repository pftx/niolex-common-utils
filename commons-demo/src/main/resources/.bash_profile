# .bash_profile

# Get the aliases and functions
if [ -f ~/.bashrc ]; then
	. ~/.bashrc
fi

# User specific environment and startup programs
export HOSTIP=`echo $SSH_CONNECTION|awk '{print $3}'`
export PROMPT_COMMAND='echo -ne "\033]0;${USER}@$HOSTIP:${PWD/#$HOME/~}\007"'
export PS1="[\u:\W]\$ "
export IPTR="10.34.130.92"
export CDPATH=$CDPATH:.:~:~/local
export LANG=en_US.utf8
export LC_ALL=en_US

# User specific environment and startup programs
export JAVA_HOME=/usr/lib/jvm/java-1.6.0-openjdk-1.6.0.0.x86_64
export CLASS_PATH=.

# CUBRID parameters
export CUBRID=~/cubrid/current
export CUBRID_SRC=~/dev/current
export CUBRID_DATABASES=$CUBRID/databases
export CUBRID_LANG=en_US
export CUBRID_CHARSET=en_US
export CQT_HOME=~/CQT_HOME

export PATH=$CUBRID/bin:$JAVA_HOME/bin:$PATH:/usr/sbin:/sbin:$HOME/bin:
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$CUBRID/lib
export include_path=.:$include_path:$CUBRID/include

# Common shotcut
alias vi=vim
alias ip='echo $HOSTIP'
alias ftail='tail -n 100 -f nohup.out'

#-------------------------------------------------------------------------------
# set CUBRID environment variables
#-------------------------------------------------------------------------------
if [ -e "~/.cubrid.sh" ]; then
  . ~/.cubrid.sh
fi

