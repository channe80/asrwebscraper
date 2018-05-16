@javax.xml.bind.annotation.XmlSchema (
	    xmlns = { 
	      @javax.xml.bind.annotation.XmlNs(prefix = "com", 
	                 namespaceURI="http://api.trademe.co.nz/v1"),
	      @javax.xml.bind.annotation.XmlNs(prefix="xsd", namespaceURI="http://www.w3.org/2001/XMLSchema")
	    },
	    namespace = "http://api.trademe.co.nz/v1", 
	    elementFormDefault = javax.xml.bind.annotation.XmlNsForm.QUALIFIED,
	    attributeFormDefault = javax.xml.bind.annotation.XmlNsForm.UNQUALIFIED
)

//@javax.xml.bind.annotation.XmlSchema (
//	    xmlns = { 
//	      @javax.xml.bind.annotation.XmlNs(prefix = "com", 
//	                 namespaceURI="http://api.tmsandbox.co.nz/v1"),
//	      @javax.xml.bind.annotation.XmlNs(prefix="xsd", namespaceURI="http://www.w3.org/2001/XMLSchema")
//	    },
//	    namespace = "http://api.tmsandbox.co.nz/v1", 
//	    elementFormDefault = javax.xml.bind.annotation.XmlNsForm.QUALIFIED,
//	    attributeFormDefault = javax.xml.bind.annotation.XmlNsForm.UNQUALIFIED
//	  )  
package com.as.app.trademe.beans;