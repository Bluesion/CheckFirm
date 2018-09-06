package com.illusion.checkfirm.utils

import javax.activation.DataHandler
import javax.activation.DataSource
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.Properties

class GMailSender(private val user: String, private val password: String) : javax.mail.Authenticator() {
    private val session: Session

    init {
        val props = Properties()
        props.setProperty("mail.transport.protocol", "smtp")
        props.setProperty("mail.host", "smtp.gmail.com")
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.port"] = "465"
        props["mail.smtp.socketFactory.port"] = "465"
        props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
        props["mail.smtp.socketFactory.fallback"] = "false"
        props.setProperty("mail.smtp.quitwait", "false")

        session = Session.getDefaultInstance(props, this)
    }

    override fun getPasswordAuthentication(): PasswordAuthentication {
        return PasswordAuthentication(user, password)
    }

    @Synchronized
    @Throws(Exception::class)
    fun sendMail(subject: String, body: String, sender: String, recipients: String) {
        val message = MimeMessage(session)
        val handler = DataHandler(ByteArrayDataSource(body.toByteArray(), "text/plain"))
        message.sender = InternetAddress(sender)
        message.subject = subject
        message.dataHandler = handler
        if (recipients.indexOf(',') > 0)
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients))
        else
            message.setRecipient(Message.RecipientType.TO, InternetAddress(recipients))
        Transport.send(message)
    }

    inner class ByteArrayDataSource internal constructor(private val data: ByteArray, private var type: String?) : DataSource {
        override fun getContentType(): String {
            return if (type == null)
                "application/octet-stream"
            else
                type!!
        }

        override fun getInputStream(): InputStream {
            return ByteArrayInputStream(data)
        }

        override fun getName(): String {
            return "ByteArrayDataSource"
        }

        @Throws(IOException::class)
        override fun getOutputStream(): OutputStream {
            throw IOException("Not Supported")
        }
    }
}