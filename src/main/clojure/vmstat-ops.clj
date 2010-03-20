(ns vmstat-ops
    "Provides wrapper functions for working with Sun's jvmstat API; jvmstat is not a public API, which means Sun
    can change it at any time, for any reason, between any two releases. The jvmstat API allows you to discover
    JVMs running on a given host, connect to them, and ask them for information about their internal state, e.g.
    number of current threads, etc. To a certain extent some of this information can also be retrieved via the
    standard JMX APIs which are actually part of a standard. The difference is that jvmstat gives you some lower-
    level details about the JVM. You can find the JavaDocs for the jvmstat APIs online at
    http://openjdk.java.net/groups/serviceability/jvmstat/index.html.
    Note that when you are done gathering information from a host, it is a best practice to call detach-from-host
    as part of cleanup."

    (:import [sun.jvmstat.monitor MonitoredHost 
                                  HostIdentifier 
                                  MonitoredVm 
                                  StringMonitor LongMonitor IntegerMonitor ByteArrayMonitor 
                                  VmIdentifier]))


; TODO write convenience fns for different HI constructors   
(defn localhost-identifier 
      "returns the HostIdentifier for the vms on localhost"
      []
      (HostIdentifier. "localhost"))

(defn vmhost
      "returns a MonitoredHost; if no parameters given, uses localhost, otherwise, 
      returns a host for the given URI; see MH javadocs for URI syntax"
      ([] (vmhost "localhost"))
      ([host-uri] (MonitoredHost/getMonitoredHost host-uri)))

(defn local-vmhost 
      "returns the monitored host for local vms"
      []
      (vmhost "localhost"))

(defn vm-proc-ids-for 
      "returns seq of process id of vms on a monitored host; assume these are currently active vms"
      [vmhost] 
      (.activeVms vmhost))

(defn vmid 
      "returns a new VmIdentifier for a given vm proc id"
      [vm-proc-id]
      (VmIdentifier. (format "//%d?mode=r" vm-proc-id)))

(defn hostid-for
      "returns the HostIdentifier for a given VmIdentifier"
      [vmid]
      (.getHostIdentifier vmid))

(defn monitored-vm
      "returns a MonitoredVm for a given VmIdentifier"
      [mhost vmid]
      (.getMonitoredVm mhost vmid))

(defn detach-from-host
      "detaches from the monitored host vmhost; best practice to do this when done"
      [vmhost]
      (.detach vmhost))

(defn find-value
      "returns the value, as string, for a property prop for a monitored vm mvm"
      [mvm prop]
      (.findByName mvm prop))

(defn is-long-monitor
     [val]
     (isa? (class val) LongMonitor))

(defmulti string-value-for class)
(defmethod string-value-for LongMonitor [mval] (String/valueOf (.longValue mval)))
(defmethod string-value-for IntegerMonitor [mval] (String/valueOf (.intValue mval)))
(defmethod string-value-for StringMonitor [mval] (.stringValue mval))
(defmethod string-value-for ByteArrayMonitor [mval] (String/valueOf (.byteArrayValue mval)))

(defn desc-value-for
      "returns the value, as string, in the form '<base name>:<name>=<value>' for a property prop for a monitored vm mvm"
      [mvm prop]
      (do
        (let [mval (find-value mvm prop)]
        (println (str (.getBaseName mval) ":" (.getName mval) "=" (string-value-for mval))))))
        (format "%s:%s=%s" (.getBaseName mval) (.getName mval) (string-value-for mval))


; tests for local vm

;(.findByPattern mvm ".*")
