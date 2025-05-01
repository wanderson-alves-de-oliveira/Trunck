package com.wao.skydodge.ferramentas



import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.wao.skydodge.db.BDSky


class BillingManager(
    private val context: Context,
    private val onCoinsPurchased: (Int) -> Unit,
    private val onRemoveAdsPurchased: () -> Unit
) {
    private val billingClient: BillingClient = BillingClient.newBuilder(context)
        .enablePendingPurchases()
        .setListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (purchase in purchases) handlePurchase(purchase)
            }
        }.build()

    init {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                billingClient.queryPurchasesAsync(
                    QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP).build()
                ) { _, purchases -> purchases.forEach { handlePurchase(it) } }
            }
            override fun onBillingServiceDisconnected() {}
        })
    }

    fun launchPurchaseFlow(activity: android.app.Activity, productId: String) {
        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(productId)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && productDetailsList.isNotEmpty()) {
                val productDetails = productDetailsList[0]

                val productDetailsParams = BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(productDetails)
                    .build()

                val billingFlowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(listOf(productDetailsParams))
                    .build()

                billingClient.launchBillingFlow(activity, billingFlowParams)
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {

            when (purchase.products[0]) {
                "coins_1000" -> consumirCompra(purchase, 1000)
                "coins_5000" -> consumirCompra(purchase, 5000)
                "coins_10000" -> consumirCompra(purchase, 10000)
                "remove_ads" -> reconhecerCompraUnica(purchase)
            }


        }
    }

    private fun consumirCompra(purchase: Purchase, quantidade: Int) {
        val consumeParams = ConsumeParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        billingClient.consumeAsync(consumeParams) { billingResult, _ ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {

                onCoinsPurchased(quantidade)
            }
        }
    }

    private fun reconhecerCompraUnica(purchase: Purchase) {
        if (!purchase.isAcknowledged) {
            val acknowledgeParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
            billingClient.acknowledgePurchase(acknowledgeParams) { billingResult ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {

                    val bd = BDSky(context)
                    val base = bd.buscar()
                    base.pontos += 2500
                    bd.atualizar(base)
                    salvarCompra("remove_ads")
                    onRemoveAdsPurchased()
                }
            }
        } else {
            onRemoveAdsPurchased()
        }
    }

    private fun salvarCompra(produto: String) {
        context.getSharedPreferences("jogo", Context.MODE_PRIVATE)
            .edit().putBoolean(produto, true).apply()
    }

    fun foiComprado(produto: String): Boolean {
        return context.getSharedPreferences("jogo", Context.MODE_PRIVATE)
            .getBoolean(produto, false)
    }
}

