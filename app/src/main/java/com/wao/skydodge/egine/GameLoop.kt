package com.wao.skydodge.egine

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.RectF
import android.media.MediaPlayer
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.MotionEvent
import android.view.SurfaceHolder
import com.wao.skydodge.R
import com.wao.skydodge.db.BDSky
import com.wao.skydodge.db.Base
import com.wao.skydodge.ferramentas.Colisao
import com.wao.skydodge.view.BotaoM
import com.wao.skydodge.view.Fundo
import com.wao.skydodge.view.GameView
import com.wao.skydodge.view.MahjongTile
import com.wao.skydodge.view.MainView
import com.wao.skydodge.view.Selecao
import com.wao.skydodge.view.Venceu
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameLoop(
    private val surfaceHolder: SurfaceHolder,
    private val context: Context,
    private val gameView: GameView
) : Thread() {
    private var running = false
    private var isTouching = false
    private var lastTime = System.nanoTime()
    private val targetFps = 60
    private var toque = 0
    private var preload = 0

    private val optimalTime = 1_000_000_000 / targetFps
    private var isTouched = false


    private var avaliar3 = false
    private var gameState = GameState.MENU
    private var gameStateAUX = GameState.MENU
    private var tipodePremio: Int = 0
    private var efeitoSonoro: MediaPlayer = MediaPlayer.create(this.context, R.raw.dim)
    private var efeitoSonoro2: MediaPlayer = MediaPlayer.create(this.context, R.raw.finalyy)
    private val paint = Paint()
    private val display: DisplayMetrics = context.resources.displayMetrics
    private val h = display.heightPixels
    private val w = display.widthPixels
    private val hp = 0
    private val wp = 0
    var semanuncio = false
    private var ajustarY = true
    private var bloquerBT = false
    private var bloquerBT2 = false
    private var embaralhando = false
    var pontos = 0
    private var pontosCont = 0

    private val selecao = Selecao(context,w,h)

    private var venceu = false
    private var falhou = false
    private var carro = Carro(context)
    private var fundo = Fundo(context)
    private var gameouver = false
    var tutor = false

    private var fase = 0

    private var time1 = 0
    private var time2 = 0
    private var time3 = 0
    var index = 0
    private var cLocked = false

    private var carrinho = BitmapFactory.decodeResource(context.resources, R.drawable.carrinho)
    private var noadsP = BitmapFactory.decodeResource(context.resources, R.drawable.noads)

    private var coin = BitmapFactory.decodeResource(context.resources, R.drawable.moeda)
    private val coinP = Bitmap.createScaledBitmap(
        coin,
        ((w * 0.1f)).toInt(),
        ((w * 0.1f)).toInt(),
        false
    )


    var compraBT = MahjongTile(
        carrinho,
        this.context,
        (w * 0.45f),
        h * 0.75f,
        ((w * 1.0f) / 6).toInt(),
        ((w * 0.9f) / 6).toInt(),
        2,
        2000

    )

    var noADS = MahjongTile(
        noadsP,
        this.context,
        (w * 0.55f),
        h * 0.75f,
        ((w * 1.0f) / 6).toInt(),
        ((w * 0.9f) / 6).toInt(),
        2,
        2000

    )
    private var lampada = BitmapFactory.decodeResource(context.resources, R.drawable.lampada)
    private var ima = BitmapFactory.decodeResource(context.resources, R.drawable.ima)
    private var giro = BitmapFactory.decodeResource(context.resources, R.drawable.giro)
    private val b: Bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)

    private var main = MainView(this.context, (w * 1.1f).toInt(), (h * 1.1f).toInt())
    private var venceuP = Venceu(this.context, (w), (h), 0)
    var perdeuL = Venceu(this.context, (w), (h), 1)
    var lojaWAO = Loja(this.context, gameView, (w), (h))

    var semInternet = false

    private var credLuz = Venceu(this.context, (w), (h), 2)
    private var credIma = Venceu(this.context, (w), (h), 3)
    private var credSufle = Venceu(this.context, (w), (h), 4)
    private var premiar = false


    private var objX = credLuz
    private var timePress = 0
    private var creditoRecorsus = false

    //var b2: Bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    private val b3: Bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)

    private var pontuacaoNova = 0
    var score = 0
    private var limitar = 7
    private var valorminimo = 300

    private var luzP = 0
    private var imaP = 0
    private var sufleP = 0
    private val colisao = Colisao()
    private var ultimaFase = 0

    private var btm = BotaoM(
        this.context,
        ((this.w * 0.9 / 4) * (1)).toFloat(),
        (this.h * 0.6).toFloat(),
        (this.w * 0.6).toInt(),
        (this.h * 0.1).toInt(),
        0,
        ultimaFase.toString()
    )

    var walld: MutableList<Bitmap> = mutableListOf()
    var skyImages = mutableListOf<Bitmap>()
    private val botao1 = MahjongTile(
        lampada,
        this.context,
        (((w * 0.02f) + (w * 0.9f) / 6) * 1.5f),
        h * 0.83f,
        ((w * 0.8f) / 6).toInt(),
        ((w * 0.9f) / 6).toInt(),
        2,
        5000
    )
    private val botao2 = MahjongTile(
        ima,
        this.context,
        (((w * 0.02f) + (w * 0.9f) / 6) * 2.5f),
        h * 0.83f,
        ((w * 0.8f) / 6).toInt(),
        ((w * 0.9f) / 6).toInt(),
        2,
        2000

    )
    private val botao3 = MahjongTile(
        giro,
        this.context,
        (((w * 0.02f) + (w * 0.9f) / 6) * 3.5f),
        h * 0.83f,
        ((w * 0.8f) / 6).toInt(),
        ((w * 0.9f) / 6).toInt(),
        2,
        3000

    )
    private val ialimite: Int = 50
    private var timeValidarIA = ialimite

    fun startLoop() {
        running = true
        start()
    }

    fun stopLoop() {
        running = false
        join()
    }

    override fun run() {
        init()
        while (running) {
            val now = System.nanoTime()
            (now - lastTime) / 1_000_000_000.0

            // Convertendo para segundos
            lastTime = now
            try {
                update()

            } catch (e: Exception) {
                e.stackTrace
            }




            render()
            //  handleInput()

            val sleepTime = optimalTime - (System.nanoTime() - now)
            if (sleepTime > 0) {
                sleep(sleepTime / 1_000_000)
            }
        }
    }

    private var canvas: Canvas? = null
    private fun update() {


        //  this.canvas = null

//        if (lojaWAO.abrirLoja == false) {
//            gameState = gameStateAUX
//        }


        if (!gameouver) {
            fundo.update()


            carro.colisao = colisao


            carro.update(fundo)
            if(gameState==GameState.SELECAO) {
                selecao.update()
            }
        }


        draw()


    }



    private fun sleep() {
        try {
            Thread.sleep(17) // Aproximadamente 60 FPS
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun adsr() {
        gameView.showRewardedAd(
            onReward = {
                premiar = true
            },
            onAdClosed = {
                if (premiar) {
                    receberPremio()
                }
                gameView.recarregarRewardedAd()
            }
        )
    }

    private fun adsi() {
        finalizarFase()
        gameView.showInterstitialAd(

            onAdClosed = {

                gameView.recarregarIntersticialAd()

            }

        )
    }

    private fun receberPremio() {
        premiar = false

        when (tipodePremio) {
            0 -> reviver()
            1 -> luzP += 3
            2 -> imaP += 3
            3 -> sufleP += 3
            4 -> {
                score += 50

                venceuP.btmCoin.liberar = 0
            }
        }

        val bd = BDSky(context)
        // val base = bd.buscar()
        bd.atualizar(Base(fase, score.toLong(), luzP, imaP, sufleP))

    }

    private fun reviver() {
        perdeuL =
            Venceu(this.context, (w), (h), 1)
        perdeuL.btm.liberar = 0
    }

    private fun criditar(obj: Venceu, canvas: Canvas) {


        obj.pontos = score
        obj.fase = ultimaFase + 1
        obj.pontos = score

        obj.draw(canvas)


    }

    private fun posCredito(obj: Venceu) {
        val bd = BDSky(context)
        score -= valorminimo
        val base = bd.buscar()
        base.pontos = score.toLong()
        base.luz = luzP
        base.ima = imaP
        base.sufle = sufleP
        bd.atualizar(base)

        obj.btmCoin.liberar = 0
    }

    private fun finalizarFase() {

        fase++

        venceu = false
        venceuP = Venceu(this.context, (w), (h), 0)
        val bd = BDSky(context)
        val base = bd.buscar()
        bd.atualizar(Base(fase, score.toLong(), base.luz, base.ima, base.sufle))

        ultimaFase = fase

        popularTiles()


    }

    private fun draw() {


        try {

            if (timePress > 0) {
                timePress--

            }

            when (gameState) {
                GameState.MENU -> drawMenu(canvas)
                GameState.PLAYING -> drawGame(canvas)
                GameState.GAME_OVER -> drawGameOver(canvas)
                GameState.SHOP -> drawShop(canvas)
                GameState.SELECAO -> drawSelecao(canvas)
            }


        } catch (e: Exception) {
            // Handle any exceptions that occur during drawing
            e.printStackTrace()
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas)
            }
        }


    }

    private fun drawGame(canvas: Canvas?) {
         fundo.draw(canvas!!)
         carro.draw(canvas!!)

        if (gameouver) {
            paint.textSize = spToPx((this.w * 0.05f))
            canvas.drawText("FIM DE JOGO", 100f, 790f, paint)
        } else {

            paint.textSize = spToPx((this.w * 0.01f))
            canvas.drawText("Nivel ${(fundo.mountainsSpeed - 15).toInt()}", 100f, 290f, paint)
            canvas.drawText("Km: ${(fundo.distancia / 1000)}", 100f, 390f, paint)

        }

    }

    private fun drawGameOver(canvas: Canvas?) {
        TODO("Not yet implemented")
    }
    private fun drawSelecao(canvas: Canvas?) {
        selecao.draw(canvas!! )
    }

    private fun drawShop(canvas: Canvas?) {
        lojaWAO.semanuncio = semanuncio
        lojaWAO.draw(canvas!!)
    }

    private fun drawMenu(canvas: Canvas?) {

        if (ultimaFase == 0) {
            val bd = BDSky(context)
            val base = bd.buscar()
            ultimaFase = base.nivel
            fase = ultimaFase


        }
        btm.stt = "Nível $ultimaFase"

        if (canvas != null) {

            try {
                if (lojaWAO.atualizar) {
                    val bd = BDSky(context)
                    val base = bd.buscar()
                    luzP = base.luz
                    imaP = base.ima
                    sufleP = base.sufle
                    score = bd.buscar().pontos.toInt()
                    lojaWAO.atualizar = false

                }


                main.draw(this.canvas!!)

                if (preload >= 100) {

                    if (!semanuncio) {

                        noADS.draw(canvas!!)
                        compraBT.x = (w * 0.35f)
                    } else {
                        compraBT.x = (w * 0.45f)

                    }


                    compraBT.draw(canvas!!)
                    btm.draw(this.canvas!!)


                } else {
                    paint.color = Color.GREEN
                    canvas!!.drawRoundRect(
                        RectF(
                            (w * 0.25f),
                            h * 0.45f,
                            ((w * 0.45f) + (100 * (w * 0.003f))),
                            ((h * 0.5)).toFloat()
                        ), 60f, 60f, paint
                    )
                    paint.color = Color.MAGENTA
                    canvas!!.drawRoundRect(
                        RectF(
                            (w * 0.25f),
                            h * 0.45f,
                            ((w * 0.45f) + (preload * (w * 0.003f))),
                            ((h * 0.5)).toFloat()
                        ), 60f, 60f, paint
                    )

                    paint.textSize = spToPx((this.w * 0.014f))
                    paint.color = Color.WHITE
                    canvas!!.drawText(
                        "LOADING...",
                        (w * 0.36f),
                        h * 0.48f,
                        paint
                    )


                    preload += 20
                }

                if (btm.liberar > 3) {
                    index = 1
                    btm.liberar = 0
                    compraBT.y = h * 0.06f
                    compraBT.x = w * 0.78f
                    gameState = GameState.PLAYING
                    gameStateAUX = gameState
                }


                ///////////////////////////////////////////


            } catch (ew: Exception) {
                ew.stackTrace
            }


        }

    }

    private fun render() {


        canvas = surfaceHolder.lockCanvas()

    }

    private fun handleInput() {

    }

    private fun init() {
        carro.screenHeight = h
        carro.iniciarRodas()
        var nx: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.nuven)
        var n = Bitmap.createScaledBitmap(
            nx,
            (w).toInt(),
            (h).toInt(),
            false
        )


        var cx: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ceu)
        var c = Bitmap.createScaledBitmap(
            cx,
            (w).toInt(),
            (h).toInt(),
            false
        )
        var cxx: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.terra)
        var cxs = Bitmap.createScaledBitmap(
            cxx,
            (w).toInt(),

            (h).toInt(),
            false
        )
        fundo.backgroundClouds = n
        fundo.backgroundMountains = fundo.gerarBitmapOnduladoComTextura(context, w * 3, h, cxs)
        fundo.backgroundMountains2 = fundo.gerarBitmapOnduladoComTextura(context, w * 3, h, cxs)
        fundo.texturaBitmap = cxs
        fundo.backgroundSky = c
        fundo.mountainsX = 0f

        fundo.mountainsX2 = fundo.backgroundMountains2.width.toFloat() //+ 1800
    }

    private fun popularTiles() {
        avaliar3 = false
        ajustarY = true
        pontos = 0

        try {


            val canvasB = Canvas(b3)
            canvasB.drawColor(
                Color.TRANSPARENT,
                PorterDuff.Mode.CLEAR
            )
            paint.color = Color.BLACK
            paint.alpha = 150


            canvasB.drawRoundRect(
                RectF(
                    (this.w * 0.08).toFloat(),
                    ((this.h * 0.75).toFloat() - (this.w * 0.03).toFloat()),
                    (this.w * 0.93).toFloat(),
                    ((this.h * 0.75)).toFloat() + (((this.w * 0.9 / 8).toInt()) * 1.22f)
                ), 30f, 30f, paint
            )
            paint.color = Color.BLUE
            paint.alpha = 150

            canvasB.drawRoundRect(
                RectF(
                    (this.w * 0.09).toFloat(),
                    ((this.h * 0.75).toFloat() - (this.w * 0.02).toFloat()),
                    (this.w * 0.92).toFloat(),
                    ((this.h * 0.75).toFloat()) + (((this.w * 0.9 / 8).toInt()) * 1.15f)
                ), 30f, 30f, paint
            )
            paint.color = Color.GREEN
            paint.alpha = 150

            canvasB.drawRoundRect(
                RectF(
                    (this.w * 0.09).toFloat(),
                    ((this.h * 0.755).toFloat() - (this.w * 0.02).toFloat()),
                    (this.w * 0.92).toFloat(),
                    ((this.h * 0.755).toFloat()) + (((this.w * 0.9 / 8).toInt()) * 1.0f)
                ), 30f, 30f, paint
            )
            paint.color = Color.BLACK
            paint.alpha = 255

        } catch (ett: Exception) {
            ett.stackTrace
        }


        val bd = BDSky(context)
        ultimaFase = bd.buscar().nivel


        val base = bd.buscar()

        luzP = base.luz
        imaP = base.ima
        sufleP = base.sufle
        score = bd.buscar().pontos.toInt()

        fase = ultimaFase



        walld.shuffle()

    }


    private fun onTocarEfeito(i: Int) {
        val coroutineScope = CoroutineScope(Dispatchers.Default)

        coroutineScope.launch {

            if (i == 0) {
                if (!efeitoSonoro.isPlaying) {
                    efeitoSonoro.setVolume(0.2f, 0.2f)
                    efeitoSonoro.seekTo(0)
                    efeitoSonoro.start()
                } else {
                    efeitoSonoro.pause()
                    efeitoSonoro.setVolume(0.2f, 0.2f)
                    efeitoSonoro.seekTo(0)
                    efeitoSonoro.start()
                }
            } else if (i == 1) {
                if (!efeitoSonoro2.isPlaying) {
                    efeitoSonoro2.setVolume(0.2f, 0.2f)
                    efeitoSonoro2.seekTo(0)
                    efeitoSonoro2.start()
                } else {
                    efeitoSonoro2.pause()
                    efeitoSonoro2.setVolume(0.2f, 0.2f)
                    efeitoSonoro2.seekTo(0)
                    efeitoSonoro2.start()
                }
            }


        }
    }


    fun onTouchEvent(event: MotionEvent): Boolean {

        if (gameState == GameState.PLAYING) {

            if (event.action == MotionEvent.ACTION_DOWN && event.x>w*0.8f) {
                selecao.sair = false
                gameState = GameState.SELECAO
            }else{

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // Inicia a ação quando o toque começa
                        isTouching = true
                        carro.applyLift()  // Faz o avião subir quando pressionado
                        carro.parou = false
                        fundo.reduzindo = false
                        //carro.reduzindo = false

                        fundo.mountainsSpeed+=1f
                        return true
                    }

                    MotionEvent.ACTION_MOVE -> {
                        // Mantém a ação enquanto o dedo estiver se movendo na tela
                        if (isTouching) {
                            // player.applyLift()  // Continua fazendo o avião subir
                            carro.applyLift()
                            fundo.reduzindo = false
                            carro.rodaF.reduzindo = false
                            carro.rodaT.reduzindo = false
                            carro.parou = false
                            //  carro.reduzindo = false
                            fundo.mountainsSpeed+=1f
                        }
                        return true
                    }

                    MotionEvent.ACTION_UP -> {
                        if (gameouver) {
                            gameouver = false
                            fundo.distancia = 0

                            fundo.mountainsX = 0f

                            fundo.mountainsX2 = fundo.backgroundMountains2.width.toFloat() //+ 1800
                        }

                        fundo.reduzindo = true
                        carro.reduzindo = true
                        isTouching = false
                        return true
                    }

                    else -> return false
                }
            }



        } else {

            when (event.action) {
                MotionEvent.ACTION_DOWN -> handleTouch(event)
            }
        }
        return true
    }

    private fun handleTouch(event: MotionEvent) {
        when (gameState) {
            GameState.MENU -> {
                if (compraBT.containsTouch(
                        event.x,
                        event.y
                    )
                ) {
                    gameStateAUX = gameState
                    gameState = GameState.SHOP
                    lojaWAO.moedas = -2
                    lojaWAO.abrirLoja = true


                } else if (noADS.containsTouch(
                        event.x,
                        event.y
                    ) && !semanuncio
                ) {

                    gameView.comprar("remove_ads")


                }

                if (btm.containsTouch(
                        event.x,
                        event.y
                    )
                ) {

                    gameStateAUX = gameState
                    selecao.sair = false
                   gameState = GameState.SELECAO

                    // gameState = GameState.PLAYING

                }
            }

            GameState.GAME_OVER -> {

            }

            GameState.SHOP -> {

                lojaWAO.onTouchEvent(event)
            }   GameState.SELECAO -> {

                if(!selecao.sair) {
                    selecao.onTouchEvent(event)
                    if(selecao.sair) {
                        carro.bitmap = selecao.listaMonters[selecao.index]
                        carro.rodaF.bitmap = selecao.getRoda()
                        carro.rodaT.bitmap = selecao.getRoda()
                        gameState = GameState.PLAYING
                    }

                }else{
                    carro.bitmap=selecao.listaMonters[selecao.index]
                    carro.rodaF.bitmap=selecao.getRoda()
                    carro.rodaT.bitmap=selecao.getRoda()
                    gameState = GameState.PLAYING

                }


        }

            else -> {}
        }
    }


    private fun Float.toDp(resources: Resources): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            resources.displayMetrics
        )
    }


    private fun spToPx(sp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            context.resources.displayMetrics
        )
    }

}

enum class GameState {
    MENU, PLAYING, GAME_OVER, SHOP,SELECAO
}