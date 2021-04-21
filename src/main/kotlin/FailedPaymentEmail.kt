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
        val msgFmt2 = MessageFormat(mem.getString("subs"), ULocale.forLocale(lang))
        val msgFmt3 = MessageFormat(mem.getString("podpplur"), ULocale.forLocale(lang))
        val msgFmt4 = MessageFormat(mem.getString("s2"), ULocale.forLocale(lang))
        val hashMap4 = hashMapOf<String, Any>();
        hashMap4.put("num", data.items.size)
        hashMap4.put("period", mem.getString(data.subscriptionPack.billingPeriod.toString()))
        hashMap4.put("card", (data.cardDetails ?: mem.getString("s3")))
        hashMap4.put("items", data.items.joinToString(
            ", "
        ) { it.productName })
        val hashmap3 = hashMapOf<String, Any>();
        hashmap3.put("card", (data.cardDetails ?: mem.getString("s3")))
        hashmap3.put("num", data.items.sumBy { it.quantity })
        hashmap3.put("ref", data.subscriptionPack.subPackRef?.let { "#$it" }.orEmpty())
        hashmap3.put("period", (when (data.subscriptionPack.billingPeriod) {
            BillingPeriod.MONTHLY -> mem.getString("month")
            BillingPeriod.ANNUAL -> mem.getString("year")
            else -> mem.getString("otherp")
        })
        )
        val hasMap2 = hashMapOf<String, Any>();
        hasMap2.put("date", DateTimeFormatter.ofPattern("MMM dd, YYYY", lang).format(data.paymentDeadline))
        hasMap2.put("num", data.subscriptionPack.totalLicenses)

            body {
                p {
                    +mem.getString("s1");
                }
                p {
                    if (data.customerType == CustomerType.PERSONAL) {
                        + msgFmt4.format(hashMap4)
                    } else {
                        +msgFmt3.format(hashmap3)
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
                    val kek = msgFmt2.format(hasMap2)
                    val fpart = kek.substringBefore(mem.getString("manually"))
                    val spart = kek.substringAfter(mem.getString("manually"))
                    +fpart
                    a(href = "https://foo.bar/ex") {
                          +mem.getString("manually")
                        }
                    +spart
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
