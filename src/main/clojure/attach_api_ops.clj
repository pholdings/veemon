(ns attach-api-ops
    "Convenience library around the Java Attach API, a private API from Sun for connecting to and
    working with Sun Java Virtual Machines, documented at http://java.sun.com/javase/6/docs/jdk/api/attach/spec/.
    As a private API, it may change at Sun's pleasure (it is not part of an open, community specification)."
   (:import [com.sun.tools.attach VirtualMachine 
                                  VirtualMachineDescriptor 
                                  AttachNotSupportedException 
                                  AgentLoadException 
                                  AgentInitializationException 
                                  AttachPermission]
            [com.sun.tools.attach.spi AttachProvider]))

(defn vm-providers
      "List virtual machine providers on this system; you need a provider to get a VM which you can attach to"
      []
      (AttachProvider/providers))

(defn desc-provider
      "Returns a text description of the attach provider prov (name, type)"
      [prov]
      (format "%s (type %s)" (.name prov) (.type prov)))

(defn vm-id-list
      "List VMs ids which an attach provider prov can attach to. When you have the id, call attach-to to access it"
      [prov]
      (.listVirtualMachines prov))

(defn attach-to
      "Attach to the given vmid using the given attach provider prov so that we can introspect against it"
      [prov vmid]
      (.attachVirtualMachine prov vmid))

(defn vm-properties
      "Returns a Clojure Map of the system properties for the virtual machine vm"
      [vm]
      (let [sprops (.getSystemProperties vm)
            cprops {}]
        (for [k (keys sprops)] (assoc cprops k (.getProperty sprops k)))))

