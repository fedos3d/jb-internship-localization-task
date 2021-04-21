import com.ibm.icu.text.MessageFormat
import com.ibm.icu.util.ULocale
import com.ibm.icu.util.UResourceBundle
import kotlinx.html.*
import java.time.format.DateTimeFormatter
import java.util.*

class FailedPaymentEmail(
    private val data: FailedPaymentData
) {
    fun buildContent(body: HTML, lang: Locale) = with(body) {
        val mem = UResourceBundle.getBundleInstance("Languages", ULocale.forLocale(lang));
        val msgFmt = MessageFormat(mem.getString("podp"),ULocale.forLocale(lang))
        val msgFmt2 = MessageFormat(mem.getString("subs"), ULocale.forLocale(lang))
        val msgFmt3 = MessageFormat(mem.getString("podpplur"), ULocale.forLocale(lang))
        val hashmap3 = hashMapOf<String, Any>();
        hashmap3.put("num", data.items.sumBy { it.quantity })
        val hashMap = hashMapOf<String, Any>()
        hashMap.put("num", data.items.size)
        hashMap.put("period", mem.getString(data.subscriptionPack.billingPeriod.toString()))
        val hasMap2 = hashMapOf<String, Any>();
        hasMap2.put("num", data.subscriptionPack.totalLicenses)
            body {
                p {
                    +mem.getString("s1");
                }
                p {
                    + mem.getString("s2")
                    + (data.cardDetails ?: mem.getString("s3"))
                    if (data.customerType == CustomerType.PERSONAL) {
                        +"${msgFmt.format(hashMap)} "//mem.getString("s4"))
                        + data.items.joinToString(
                            ", "
                        ) { it.productName }
                    } else {
                        +msgFmt3.format(hashmap3)
                        +"${mem.getString("s6")} ${
                            data.subscriptionPack.subPackRef?.let { "#$it" }.orEmpty()
                        } ${mem.getString("s7")} "
                        +(when (data.subscriptionPack.billingPeriod) {
                            BillingPeriod.MONTHLY -> mem.getString("month")
                            BillingPeriod.ANNUAL -> mem.getString("year")
                            else -> mem.getString("otherp")
                        } + ": ")
                        br()
                        data.items.forEach {
                            +"- ${it.quantity} x ${mem.getString(it.description.replace(" ", "_"))}";br()
                        }
                    }
                }

                if (data.cardProvider == CardProvider.PAY_PAL)
                    paypalFailedPaymentReasons(mem)
                else
                    creditCardFailedPaymentReasons(mem)

                p {
                    +(msgFmt2.format(hasMap2))
                    a(href = "https://foo.bar/ex") {
                        +mem.getString("manually")
                    }
                    +"${mem.getString("till")} ${DateTimeFormatter.ofPattern("MMM dd, YYYY", lang).format(data.paymentDeadline)}"
                }
                p {
                   +mem.getString("doublecheck")
                }
            }
    }
}

private fun FlowContent.creditCardFailedPaymentReasons(mem: UResourceBundle) {
    p {
        +mem.getString("Card1"); br()
        +mem.getString("Card2"); br()
        +mem.getString("Card3"); br()
        +mem.getString("Card4"); br()
    }
}

private fun FlowContent.paypalFailedPaymentReasons(mem: UResourceBundle) {
    p {
        +(mem.getString("PayPal0") +
                mem.getString("PayPal1") +
                mem.getString("PayPal2")); br()
        +mem.getString("PayPal3"); br()
        +mem.getString("PayPal4"); br()
        +mem.getString("PayPal5"); br()
        +mem.getString("PayPal6")
    }
}
