package com.illusion.checkfirm.core.domain.model
import org.junit.Assert.assertEquals
import org.junit.Test
class DeviceStorageKeyTest {
    @Test fun migratedDeviceUsesTheLegacyXmlDatabaseKey() {
        assertEquals("DeviceItem(model=SM-S928B, csc=EUX)", Device("SM-S928B", "EUX").storageKey)
    }
}
