import kotlinx.html.*
import java.time.format.DateTimeFormatter
import java.util.*

enum class Languages {
    RU,
    EN,
    PL
}
class FailedPaymentEmail(
    private val data: FailedPaymentData
) {
    //localizations
    //RU
    private val descriptionRU = mapOf("commercial monthly subscription" to "коммерческая подписка на месяц", "commercial annual subscription" to "коммерческая годовая подписка")
    private val billingPeriodRU = mapOf(BillingPeriod.ANNUAL to "годовой", BillingPeriod.MONTHLY to "месячной", BillingPeriod.OTHER to "заданного периода")
    private val localizationRUGeneral = arrayOf("Спасибо, что остаетесь с JetBrains.",
            "К сожалению, мы не можем произвести оплату по карте ${data.cardDetails ?: "ваша карта"} по " + "вашей".hardPluralizeRU(data.items.sumBy { it.quantity }) + " ",
            "${billingPeriodRU[data.subscriptionPack.billingPeriod]} подписке ${
                 data.items.joinToString(
                ", "
                ) { it.productName }
            }.",
            "подписка".simplyPluralizeRU(data.items.sumBy { it.quantity }),
            " как часть Subscription Pack ${data.subscriptionPack.subPackRef?.let { "#$it" }.orEmpty()} для следующего ",
            "месяца",
            "года",
            "периода",
            "Для обеспечения бесперебойного доступа к ${data.subscriptionPack.pluralizeRU1()}, ",
            "пожалуйста перейдите по ссылке и обновите ${data.subscriptionPack.pluralizeRU2()}", " собственноручно",
            " до ${DateTimeFormatter.ofPattern("MMM dd, YYYY", Locale.forLanguageTag("RU")).format(data.paymentDeadline)}",
            "Вы можете перепроверить и попробовать вашу существующую платежную карту еще раз, использовать другую карту или выбрать другой способ оплаты."
    )
    private val localizationRUPayPal = arrayOf("Пожалуйста, убедитесь, что ваш PayPal аккаунт не закрыт или не удален. ",
            "Кредитная карта, подключенная к вашему счету PayPal, должна быть активной. ",
            "Частые причины неудачных расчетов по карте включают в себя:",
            "- Карта не подтверждена на вашем PayPal счете;",
            "- Детали карты (Номер, Дата окончания срока действия, CVC, Адрес для выставления счета) являются неполными или были введены неправильно;",
            "- Срок действия карты истек; или",
            "- Недостаточные средства или лимит оплаты по карте."
    )
    private val localizationRUCard = arrayOf("Частые причины неудачных платежей по кредитным картам включают в себя:",
            "- Срок действия карты истек, или срок ее действия был введен неправильно;",
            "- Недостаточные средства или лимит оплаты по карте; или",
            "- Карточка не предназначена для международных/оффшорных операций, либо банк-эмитент отклонил операцию."
    )

    //EN
    private val descriptionEN = mapOf("commercial monthly subscription" to "commercial monthly subscription", "commercial annual subscription" to "commercial annual subscription")
    private val billingPeriodEN = mapOf(BillingPeriod.ANNUAL to "annual", BillingPeriod.MONTHLY to "monthly", BillingPeriod.OTHER to "period")
    private val localizationENGeneral = arrayOf("Thank you for staying with JetBrains.",
            "Unfortunately, we were not able to charge ${data.cardDetails ?: "your card"} for your ",
            "${billingPeriodEN[data.subscriptionPack.billingPeriod]} subscription to ${
                data.items.joinToString(
                        ", "
                ) { it.productName }
            }.",
            "subscription".simplyPluralize(data.items.sumBy { it.quantity }),
            " as part of Subscription Pack ${
                data.subscriptionPack.subPackRef?.let { "#$it" }.orEmpty()
            } for the next ",
            "month",
            "year",
            "period",
            "To ensure uninterrupted access to your ${data.subscriptionPack.pluralize()}, ",
            "please follow the link and renew your ${data.subscriptionPack.pluralize()} ",
            "manually",
            " till ${DateTimeFormatter.ofPattern("MMM dd, YYYY", Locale.US).format(data.paymentDeadline)}",
            "You can double-check and try your existing payment card again, use another card, or choose a different payment method."
    )
    private val localizationENPayPal = arrayOf("Please make sure that your PayPal account is not closed or deleted. ",
            "The credit card connected to your PayPal account should be active. ",
            "Common reasons for failed card payments include:",
            "- The card is not confirmed in your PayPal account;",
            "- The card details (Number, Expiration date, CVC, Billing address) are incomplete or were entered incorrectly;",
            "- The card is expired; or",
            "- Insufficient funds or payment limit on the card."
    )
    private val localizationENCard = arrayOf("Common reasons for failed credit card payments include:",
            "- The card is expired, or the expiration date was entered incorrectly;",
            "- Insufficient funds or payment limit on the card; or",
            "- The card is not set up for international/overseas transactions, or the issuing bank has rejected the transaction."
    )

    //PL
    private val descriptionPL = mapOf("commercial monthly subscription" to "komercyjny abonament miesięczny", "commercial annual subscription" to "komercyjna prenumerata roczna")
    private val billingPeriodPL = mapOf(BillingPeriod.ANNUAL to "coroczny", BillingPeriod.MONTHLY to "miesięcznie", BillingPeriod.OTHER to "okres")
    private val localizationPLGeneral = arrayOf("Dziękujemy za pozostanie z JetBrains.",
            "Niestety, nie byliśmy w stanie pobrać opłaty ${data.cardDetails ?: "twoja karta"}  za Twój ",
            "${billingPeriodPL[data.subscriptionPack.billingPeriod]} subskrypcja na ${
                data.items.joinToString(
                        ", "
                ) { it.productName }
            }.",
            "subskrypcja".simplyPluralizePL(data.items.sumBy { it.quantity }),
            " jako część pakietu subskrypcyjnego ${
                data.subscriptionPack.subPackRef?.let { "#$it" }.orEmpty()
            } dla następnego ",
            "miesiąc",
            "rok",
            "okres",
            "Aby zapewnić nieprzerwany dostęp do ${data.subscriptionPack.pluralizePL()}, ",
            "proszę wejść na link i odnowić swoje ${data.subscriptionPack.pluralizePL()} ",
            "ręcznie",
            " do ${DateTimeFormatter.ofPattern("MMM dd, YYYY", Locale.forLanguageTag("PL")).format(data.paymentDeadline)}",
            "Możesz ponownie sprawdzić i wypróbować swoją dotychczasową kartę płatniczą, użyć innej karty lub wybrać inną metodę płatności.")
    private val localizationPLPayPal = arrayOf("Upewnij się, że Twoje konto PayPal nie zostało zamknięte lub usunięte. ",
            "Karta kredytowa podłączona do Twojego konta PayPal powinna być aktywna. ",
            "Do najczęstszych przyczyn nieudanych płatności kartą należą:",
            "- Karta nie jest potwierdzona na Twoim koncie PayPal;",
            "- Dane karty (Numer, Data ważności, CVC, Adres rozliczeniowy) są niekompletne lub zostały wprowadzone niepoprawnie;",
            "- Karta jest nieważna; lub",
            "- Niewystarczające środki lub limit płatniczy na karcie."
    )
    private val localizationPLCard = arrayOf("Do najczęstszych przyczyn nieudanych płatności kartą kredytową należą:",
            "- Karta jest nieważna lub data ważności została wprowadzona niepoprawnie;",
            "- niewystarczające środki lub limit płatności na karcie; lub",
            "- Karta nie jest przystosowana do transakcji międzynarodowych/ zagranicznych lub bank wydający kartę odrzucił transakcję.")

    //default localization
    private var localizationPayPal = localizationENPayPal
    private var localizationGeneral = localizationENCard
    private var localizationCard = localizationENCard
    private var description = descriptionEN

    fun buildContent(body: HTML, lang: Languages) = with(body) {
        when (lang) {
            Languages.RU -> {
                localizationPayPal = localizationRUPayPal
                localizationGeneral = localizationRUGeneral
                localizationCard = localizationRUCard
                description = descriptionRU
            }
            Languages.EN -> {
                localizationPayPal = localizationENPayPal
                localizationGeneral = localizationENGeneral
                localizationCard = localizationENCard
                description = descriptionEN
            }
            Languages.PL -> {
                localizationPayPal = localizationPLPayPal
                localizationCard = localizationPLCard
                localizationGeneral = localizationPLGeneral
                description = descriptionPL
            }
        }
            body {
                p {
                    +localizationGeneral[0]
                }
                p {
                    +localizationGeneral[1]
                    if (data.customerType == CustomerType.PERSONAL) {
                        +localizationGeneral[2]
                    } else {
                        +localizationGeneral[3]
                        +localizationGeneral[4]
                        +(when (data.subscriptionPack.billingPeriod) {
                            BillingPeriod.MONTHLY -> localizationGeneral[5]
                            BillingPeriod.ANNUAL -> localizationGeneral[6]
                            else -> localizationGeneral[7]
                        } + ": ")
                        br()
                        data.items.forEach {
                            +"- ${it.quantity} x ${description[it.description]}";br()
                        }
                    }
                }

                if (data.cardProvider == CardProvider.PAY_PAL)
                    paypalFailedPaymentReasons(localizationPayPal)
                else
                    creditCardFailedPaymentReasons(localizationCard)

                p {
                    +(localizationGeneral[8] +
                            localizationGeneral[9])
                    a(href = "https://foo.bar/ex") {
                        +localizationGeneral[10]
                    }
                    +localizationGeneral[11]
                }
                p {
                    +localizationGeneral[12]
                }
            }
    }
}

private fun FlowContent.creditCardFailedPaymentReasons(localizationCard: Array<String>) {
    p {
        +localizationCard[0]; br()
        +localizationCard[1]; br()
        +localizationCard[2]; br()
        +localizationCard[3]; br()
    }
}

private fun FlowContent.paypalFailedPaymentReasons(localizationPayPal: Array<String>) {
    p {
        +(localizationPayPal[0] +
                localizationPayPal[1] +
                localizationPayPal[2]); br()
        +localizationPayPal[3]; br()
        +localizationPayPal[4]; br()
        +localizationPayPal[5]; br()
        +localizationPayPal[6]
    }
}

//EN pluralization
private fun SubscriptionPack.pluralize(title: String = "subscription"): String {
    return title.simplyPluralize(this.totalLicenses)
}

private fun String.simplyPluralize(amount: Int): String {
    return when (amount) {
        1 -> this
        else -> "${this}s"
    }
}

//RU pluralization
private fun SubscriptionPack.pluralizeRU1(title: String = "подписке"): String {
    return title.simplyPluralizeRU(this.totalLicenses)
}
private fun SubscriptionPack.pluralizeRU2(title: String = "подписку"): String {
    return title.simplyPluralizeRU2(this.totalLicenses)
}
private fun String.simplyPluralizeRU2(amount: Int): String {
    return when (amount) {
        1 -> this
        else -> "подписки"
    }
}
private fun String.simplyPluralizeRU(amount: Int): String {
    return when (amount) {
        1 -> this
        else -> "подпискам"
    }
}
private fun String.hardPluralizeRU(amount: Int): String {
    return when (amount) {
        1 -> this
        else -> "вашим"
    }
}

//PL pluralization
private fun SubscriptionPack.pluralizePL(title: String = "subskrypcja"): String {
    return title.simplyPluralizePL(this.totalLicenses)
}
private fun String.simplyPluralizePL(amount: Int): String {
    return when (amount) {
        1 -> this
        else -> "subskrypcje"
    }
}
