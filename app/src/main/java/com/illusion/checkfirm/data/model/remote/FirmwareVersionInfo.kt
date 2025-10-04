package com.illusion.checkfirm.data.model.remote

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlOtherAttributes
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
@XmlSerialName("versioninfo")
data class FirmwareVersionInfo(
    @XmlElement(true)
    val url: String,

    @XmlElement(true)
    val firmware: Firmware,

    @XmlElement(true)
    val polling: Polling,

    @XmlElement(true)
    @XmlSerialName("ActiveDeviceInfo")
    val activeDeviceInfo: ActiveDeviceInfo,

    @XmlElement(true)
    @XmlSerialName("WiFiConnectedPollingInfo")
    val wifiConnectedPollingInfo: WiFiConnectedPollingInfo
)

@Serializable
@XmlSerialName("firmware")
data class Firmware(
    @XmlElement(true) val model: String,
    @XmlElement(true) val cc: String,
    @XmlElement(true) val version: Version
)

@Serializable
@XmlSerialName("version")
data class Version(
    @XmlElement(true)
    val latest: Latest,

    @XmlElement(true)
    @XmlSerialName("upgrade")
    val previous: Previous
)

@Serializable
@XmlSerialName("latest")
data class Latest(
    @XmlSerialName("o")
    val androidVersion: String = "",

    @XmlValue(true)
    val firmware: String
)

@Serializable
@XmlSerialName("upgrade")
data class Previous(
    @XmlSerialName("value")
    val list: List<Value>
)

@Serializable
@XmlSerialName("value")
data class Value(
    @XmlOtherAttributes
    @XmlSerialName("rcount")
    val rCount: String,

    @XmlOtherAttributes
    @XmlSerialName("fwsize")
    val fwSize: String,

    @XmlValue(true)
    val firmware: String
)

@Serializable
@XmlSerialName("polling")
data class Polling(
    @XmlElement(true) val period: String,
    @XmlElement(true) val time: String,
    @XmlElement(true) val range: String
)

@Serializable
@XmlSerialName("ActiveDeviceInfo")
data class ActiveDeviceInfo(
    @XmlElement(true)
    @XmlSerialName("CycleOfDeviceHeartbeat")
    val cycleOfHeartbeat: String,

    @XmlElement(true)
    @XmlSerialName("ServiceURL")
    val serviceUrl: String
)

@Serializable
@XmlSerialName("WiFiConnectedPollingInfo")
data class WiFiConnectedPollingInfo(
    @XmlElement(true)
    @XmlSerialName("Cycle")
    val cycle: String,

    @XmlElement(true)
    @XmlSerialName("Activated")
    val activated: String
)