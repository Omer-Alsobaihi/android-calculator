#!/bin/sh

#
# Gradle start up script for POSIX
#

# Attempt to set APP_HOME

# Resolve links: $0 may be a link
PRG="$0"
while [ -h "$PRG" ] ; do
    ls=$(ls -ld "$PRG")
    link=$(expr "$ls" : '.*-> \(.*\)$')
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=$(dirname "$PRG")/"$link"
    fi
done
SAVED="`pwd`"
cd "$(dirname "$PRG")/" >/dev/null
APP_HOME="`pwd -P`"
cd "$SAVED" >/dev/null

APP_NAME="Gradle"
APP_BASE_NAME=$(basename "$0")

# Use the maximum available, or set MAX_FD != -1 to use that value.
MAX_FD="maximum"

warn () {
    echo "$*"
} >&2

die () {
    echo
    echo "$*"
    echo
    exit 1
} >&2

# OS specific support (must be 'true' or 'false').
cygwin=false
msys=false
darwin=false
nonstop=false
case "$(uname)" in
  CYGWIN* )
    cygwin=true
    ;;
  Darwin* )
    darwin=true
    ;;
  MINGW* )
    msys=true
    ;;
  NONSTOP* )
    nonstop=true
    ;;
esac

CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar


# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        JAVACMD="$JAVA_HOME/jre/sh/java"
    else
        JAVACMD="$JAVA_HOME/bin/java"
    fi
    if [ ! -x "$JAVACMD" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME"
    fi
else
    JAVACMD="java"
    which java >/dev/null 2>&1 || die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH."
fi

# Increase the maximum file descriptors if we can.
if ! "$cygwin" && ! "$darwin" && ! "$nonstop" ; then
    MAX_FD_LIMIT=$(ulimit -H -n)
    if ! [ "$MAX_FD_LIMIT" = "limit" ] ; then
        if [ $? -eq 0 ] ; then
            MAX_FD="$MAX_FD_LIMIT"
        else
            warn "Could not query maximum file descriptor limit:"
        fi
    fi
fi

# For Darwin, add options to specify how the application appears in the dock
if $darwin; then
    GRADLE_OPTS="$GRADLE_OPTS "-Xdock:name=$APP_NAME" "-Xdock:icon=$APP_HOME/media/gradle.icns""
fi

# For Cygwin, switch paths to Windows format before running java
if $cygwin ; then
    APP_HOME=$(cygpath --path --mixed "$APP_HOME")
    CLASSPATH=$(cygpath --path --mixed "$CLASSPATH")
    JAVACMD=$(cygpath --unix "$JAVACMD")
    GRADLE_OPTS=$(cygpath --path --mixed "$GRADLE_OPTS")
fi

# For Mingw, switch paths to Windows format before running java
if $msys ; then
    APP_HOME=$(cygpath --path --mixed "$APP_HOME")
    CLASSPATH=$(cygpath --path --mixed "$CLASSPATH")
    JAVACMD=$(cygpath --unix "$JAVACMD")
    GRADLE_OPTS=$(cygpath --path --mixed "$GRADLE_OPTS")
fi

# Save stdout and stderr
exec 3>&1
exec 4>&2

# Create a temporary file for stdout/stderr redirection
TMPSTDOUT=$(mktemp)
TMPSTDERR=$(mktemp)

# Run Gradle
"$JAVACMD" $JAVA_OPTS $GRADLE_OPTS     -classpath "$CLASSPATH"     org.gradle.wrapper.GradleWrapperMain "$@" </dev/null >"$TMPSTDOUT" 2>"$TMPSTDERR"

# Copy stdout and stderr to the original descriptors
cat "$TMPSTDOUT" >&3
cat "$TMPSTDERR" >&4

# Remove temporary files
rm -f "$TMPSTDOUT" "$TMPSTDERR"

# Exit with the Gradle exit code
exit $?
