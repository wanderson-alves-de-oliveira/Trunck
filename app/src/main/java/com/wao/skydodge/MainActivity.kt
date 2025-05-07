package com.wao.skydodge

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.FrameLayout
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.QueryProductDetailsParams

import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.wao.skydodge.ferramentas.BillingManager
import com.wao.skydodge.ferramentas.NotificationScheduler
import com.wao.skydodge.view.GameView


@Suppress("DEPRECATION")
class MainActivity : Activity() {
    private lateinit var gameView: GameView
    private lateinit var adView: AdView
    private lateinit var billingClient: BillingClient
    private val productIds = listOf("coins_1000", "coins_5000", "coins_10000",  "remove_ads")
    var listaPreco = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        ) //coloca em fullscreen
        this.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        NotificationScheduler.scheduleNotification(this)

        MobileAds.initialize(this@MainActivity) {}
       // listaPreco = mutableListOf<String>()
        setupBillingClient()

        val layout = FrameLayout(this)


        val gameParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )

        val billingManager = BillingManager(
            context = this,
            onCoinsPurchased = { qtd ->

                gameView.adicionarMoedas(qtd)
            },
            onRemoveAdsPurchased = {
                // Por exemplo, desativar banner ou rewarded ads
                gameView.removerAnuncios()
                layout.removeView(adView)
            }
        )

        gameView = GameView(this, billingManager)





        layout.addView(gameView, gameParams) // Adiciona o jogo
//
//        val adView = AdView(this)
//        adView.adUnitId = "ca-app-pub-1070048556704742/1261828358"
//        adView.setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, 360))
//        this.adView = adView
//
//        val adRequest = AdRequest.Builder().build()
//        adView.loadAd(adRequest)
//        val adParams = FrameLayout.LayoutParams(
//            FrameLayout.LayoutParams.MATCH_PARENT,
//            FrameLayout.LayoutParams.WRAP_CONTENT
//        ).apply {
//            gravity = android.view.Gravity.BOTTOM // Fixa o banner na parte inferior
//        }
//        adParams.bottomMargin = 0  // Alinhar ao rodapé
//
//        layout.addView(this.adView, adParams)
//
//
//
//
//        if (billingManager.foiComprado("remove_ads")) {
//            layout.removeView(adView)
//            gameView.removerAnuncios()
//        }





        setContentView(layout)
    }

    private fun setupBillingClient() {
        billingClient = BillingClient.newBuilder(this)
            .enablePendingPurchases()
            .setListener { _, _ -> }
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryProductDetails()
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.w("Billing", "Desconectado. Tentando reconectar...")
            }
        })
    }

    private fun queryProductDetails() {
        val products = productIds.map {
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(it)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        }

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(products)
            .build()

        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                runOnUiThread {
                    listaPreco.clear()

                    for (product in productDetailsList) {

                        val price = product.oneTimePurchaseOfferDetails?.formattedPrice ?: "Preço não disponível"


                        listaPreco.add(price)


                    }
        if(!listaPreco.isEmpty()){
            gameView.gameLoop.lojaWAO.listaPreco = listaPreco
            gameView.gameLoop.lojaWAO.precos()
        }
                }
            }else{

                for (i in 0 until 4) {

                    val price = "Preço não disponível"

                    listaPreco.add(price)


                }
            }
        }
    }
}