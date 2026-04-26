package com.illusion.checkfirm.feature.report.domain.usecase

import jakarta.inject.Inject
import jakarta.mail.Authenticator
import jakarta.mail.Message
import jakarta.mail.PasswordAuthentication
import jakarta.mail.Session
import jakarta.mail.Transport
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeBodyPart
import jakarta.mail.internet.MimeMessage
import jakarta.mail.internet.MimeMultipart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Properties

class SubmitReportUseCase @Inject constructor() {
    suspend operator fun invoke(
        bugType: String,
        deviceDetails: String,
        logs: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val props = Properties().apply {
                this["mail.smtp.host"] = "smtp.gmail.com"
                this["mail.smtp.socketFactory.port"] = "465"
                this["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
                this["mail.smtp.auth"] = "true"
                this["mail.smtp.port"] = "465"
            }

            val session = Session.getDefaultInstance(props, object : Authenticator() {
                override fun getPasswordAuthentication() =
                    PasswordAuthentication("checkfirmhelpdesk@gmail.com", "vzmkrotliewaifaf")
            })

            val mail = MimeMessage(session).apply {
                setFrom(InternetAddress("checkfirmhelpdesk@gmail.com"))
                addRecipient(
                    Message.RecipientType.TO, InternetAddress("checkfirmhelpdesk@gmail.com")
                )
                subject = "[신고] $deviceDetails"
            }

            var message = "[오류 내용]<br>- $bugType"
            message += "<br><br>[유저 메시지]<br>"
            message += logs.ifBlank { "메시지 없음" }

            val messageBodyPart = MimeBodyPart()
            messageBodyPart.setText(message, "utf-8", "html")

            mail.setContent(MimeMultipart().apply {
                addBodyPart(messageBodyPart)
            })

            Transport.send(mail)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        } catch (e: Error) {
            Result.failure(Exception(e.message))
        }
    }
}
