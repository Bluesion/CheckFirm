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
    /**
     * @param bugTypeLabels Localized labels for each selected bug type. The use-case
     *   joins them into the email body so support reads "펌웨어 정보 오류" instead of
     *   raw "type_1" keys.
     */
    suspend operator fun invoke(
        bugTypeLabels: List<String>,
        logs: String,
    ): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
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
                    Message.RecipientType.TO,
                    InternetAddress("checkfirmhelpdesk@gmail.com"),
                )
                subject = "[신고]"
            }

            val errorList = bugTypeLabels.joinToString(separator = "<br>") { "- $it" }
            val message = buildString {
                append("[오류 내용]<br>")
                append(errorList.ifBlank { "- (none)" })
                append("<br><br>[유저 메시지]<br>")
                append(logs.ifBlank { "메시지 없음" })
            }

            val bodyPart = MimeBodyPart().apply { setText(message, "utf-8", "html") }
            mail.setContent(MimeMultipart().apply { addBodyPart(bodyPart) })

            Transport.send(mail)
        }
    }
}
