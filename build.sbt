name := "unfiltered-demo-json"

version := "1.0"

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
    "net.databinder" %% "unfiltered"        % "0.6.4",
    "net.databinder" %% "unfiltered-jetty"  % "0.6.4",
    "net.databinder" %% "unfiltered-netty"  % "0.6.4",
	"net.databinder" %% "unfiltered-filter" % "0.6.4",
	"net.databinder" %% "unfiltered-util"   % "0.6.4",
	"net.databinder" %% "unfiltered-json"   % "0.6.4",
	"net.liftweb"    %% "lift-json"         % "2.4-M5"	
)
