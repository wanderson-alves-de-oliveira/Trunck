package com.wao.skydodge.egine

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.RectF
import android.media.MediaPlayer
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.MotionEvent
import android.view.SurfaceHolder
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.toArgb
import com.wao.skydodge.R
import com.wao.skydodge.db.BDSky
import com.wao.skydodge.db.Base
import com.wao.skydodge.ferramentas.Colisao
import com.wao.skydodge.pistas.TrackRenderer
import com.wao.skydodge.view.BotaoBitmap
import com.wao.skydodge.view.BotaoM
import com.wao.skydodge.view.Ceu
import com.wao.skydodge.view.Fundo
import com.wao.skydodge.view.GameView
import com.wao.skydodge.view.MahjongTile
import com.wao.skydodge.view.MainView
import com.wao.skydodge.view.Selecao
import com.wao.skydodge.view.Venceu
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.max
import kotlin.random.Random

class GameLoop(
    private val surfaceHolder: SurfaceHolder,
    private val context: Context,
    private val gameView: GameView
) : Thread() {
    private var running = false
    private var isTouching = false
    private var isJumping = false
    private var isAccelerating = false
    private var lastTime = System.nanoTime()
    private val targetFps = 60
    private var toque = 0
    private var preload = 0
    private val optimalTime = 1_000_000_000 / targetFps

    private val trackRenderer = TrackRenderer(context)
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

    var semanuncio = false
    private var ajustarY = true

    var pontos = 0

    private val selecao = Selecao(context, w, h)

    private var venceu = false
    private var carro = Carro(context)
    private var carroRival = CarroRival(context)
    private var carroRival1 = CarroRival(context)
    //  private var carroRival2 = CarroRival(context)


    private var fundo = Fundo(context)
    private var ceu = Ceu(context)
    private var gameouver = false
    var tutor = false

    private var fase = 0

    var index = 0
    private var cLocked = false
    val options = BitmapFactory.Options().apply {
        inPreferredConfig = Bitmap.Config.RGB_565
    }
    private var carrinho =
        BitmapFactory.decodeResource(context.resources, R.drawable.carrinho, options)
    private var noadsP = BitmapFactory.decodeResource(context.resources, R.drawable.noads, options)

    private var coin = BitmapFactory.decodeResource(context.resources, R.drawable.moeda, options)
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
    private var lampada =
        BitmapFactory.decodeResource(context.resources, R.drawable.lampada, options)
    private var ima = BitmapFactory.decodeResource(context.resources, R.drawable.ima, options)
    private var giro = BitmapFactory.decodeResource(context.resources, R.drawable.giro, options)
    private val b: Bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)

    private var main = MainView(this.context, (w * 1.1f).toInt(), (h * 1.1f).toInt())
    private var venceuP = Venceu(this.context, (w), (h), 0)
    var perdeuL = Venceu(this.context, (w), (h), 1)
    var lojaWAO = Loja(this.context, gameView, (w), (h))

    var semInternet = false

    private var credLuz = Venceu(this.context, (w), (h), 2)
    private val obstacles = mutableListOf<Obstacle>()
    private var obstacleTimer = 0L
    private val obstacleInterval = 200L
    private var premiar = false
    private val touches = mutableMapOf<Int, Pair<Float, Float>>()

    private var timePress = 0

    //var b2: Bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    private val b3: Bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)

    private var pontuacaoNova = 0
    var score = 0
    private var valorminimo = 300

    private var luzP = 0
    private var imaP = 0
    private var sufleP = 0
    private val colisao = Colisao()
    private val colisao2 = Colisao()
    private val colisao3 = Colisao()
    private val colisao4 = Colisao()
    private val colisao5 = Colisao()
    private var ultimaFase = 0
    private val corrotina: CoroutineScope = CoroutineScope(Dispatchers.Default)
    private var btm = BotaoM(
        this.context,
        ((this.w * 0.9 / 4) * (1)).toFloat(),
        (this.h * 0.6).toFloat(),
        (this.w * 0.6).toInt(),
        (this.h * 0.1).toInt(),
        0,
        ultimaFase.toString()
    )
    var acel: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pedal,options)
    var acelb: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pedalb,options)

    var oficina: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.oficina,options)
    var pulo: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pulo,options)
    var pulob: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pulob,options)


    var btmAcel = BotaoBitmap(
        this.context,
        ((this.w * 0.8)).toFloat(),
        (this.h * 0.76).toFloat(),
        (this.w * 0.15).toInt(),
        (this.w * 0.12).toInt(),
        acel
    )
    var btmOficina = BotaoBitmap(
        this.context,
        ((this.w * 0.05)).toFloat(),
        (this.h * 0.05).toFloat(),
        (this.w * 0.1).toInt(),
        (this.w * 0.05).toInt(),
        oficina
    )
    var btmPulo = BotaoBitmap(
        this.context,
        ((this.w * 0.05)).toFloat(),
        (this.h * 0.76).toFloat(),
        (this.w * 0.12).toInt(),
        (this.w * 0.10).toInt(),
        pulo
    )
    var walld: MutableList<Bitmap> = mutableListOf()


    private val ialimite: Int = 50

    fun startLoop() {
        running = true
        start()
    }

    fun stopLoop() {
        running = false
        join()
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
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

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    private fun update() {


        if (!gameouver) {
            if (isAccelerating) {
                isTouching = true
                carro.applyLift()
                carro.parou = false
                fundo.reduzindo = false
                carroRival.inicio = true
                carroRival.parou = false
                carroRival1.inicio = true
                carroRival1.parou = false
                fundo.mountainsSpeed += 2f
                carro.acelerando = true
            }
            if (isJumping && carro.rodaT.gravity==0f  ) {
                carro.rotacao = 0f
                carro.pulo()
            }


            carro.update(fundo)

            runBlocking {
                launch(Dispatchers.Default) {
                    updateOB()

                }
            }



            updateCameraOffset(carro.rodaT.y)
            fundo.update(trackRenderer)
            carroRival.update(fundo)
            carroRival1.update(fundo)
             if (fundo.mountainsSpeed > 1) {
                ceu.corremdo = true
            } else {
                ceu.corremdo = false
            }


            ceu.update()

            if (gameState == GameState.SELECAO) {
                selecao.update()
            }

        }




        draw()


    }


    private fun updateOB() {
        val novoX = carro.rodaF.x
        val novoY = carro.rodaF.y
        val rectf = RectF(
            (novoX),
            (novoY),
            (novoX + (carro.rodaF.largura).toInt()),
            (novoY + (carro.rodaF.altura.toInt()))
        )
        val scrollSpeed = fundo.mountainsSpeed
        obstacles.forEach { it.update(scrollSpeed) }
        obstacleTimer += 1
        var ob = checkCollision(rectf)
        if (ob.isNotEmpty() && ob[0].caiu) {

            ob.map {
                it.voar = true
                it.snowmanVelocityX =
                    if (scrollSpeed > 0) scrollSpeed * 0.8f else (scrollSpeed * -1) * 0.8f
                it.snowmanVY *= -0.5f

            }
            fundo.bateu = true

            when(ob[0].causa){
                0->{
                    fundo.mountainsSpeed = -20f
                    carro.rotacao -= 30
                }
                1->{
                    fundo.mountainsSpeed = -8f
                    carro.rotacao -= 30
                }
                2->{
                    fundo.mountainsSpeed = -12f
                    carro.rotacao -= 30
                }
                3-> {
                    fundo.mountainsSpeed += 20f
                    carro.rotacao += 30
                }
            }



        }

        var opp = obstacles.filter { it.x < -100 }

        if (opp.isNotEmpty()) {
            obstacles.removeAll(opp)
            addRandomObstacle(fundo, 0f)
        }

        if (obstacles.isNotEmpty()) {
            if (obstacles[0].caiu == false) {
                obstacles[0].y -= 15f
                obstacles[0].caiu = !(obstacles[0].verirficarColisao(fundo))


            }
        }


        var oppx = obstacles.filter { it.voar }

        if (oppx.isNotEmpty()) {
            oppx[0].giro += oppx[0].snowmanVelocityX
            oppx[0].sumindo = true



        }

    }

    fun addRandomObstacle(fundo: Fundo, x: Float) {


        var obstaclex = Obstacle(
            bitmap = pulo,
            x = fundo.backgroundMountains.width.toFloat() + x,
            y = 0f
        )
        obstaclex.bitmap = obstaclex.pegarObj(context, 0)

        while (!obstaclex.verirficarColisao(fundo)) {

            obstaclex.y += 150f
            //  obstaclex.girar()
        }

        obstaclex.chegou = true

        obstacles.add(obstaclex)

    }

    fun checkCollision(playerRect: RectF): MutableList<Obstacle> {
        var ob = obstacles.filter {
            obstacles.any { obstacle ->
                RectF(
                    obstacle.x,
                    obstacle.y,
                    obstacle.x + obstacle.width,
                    obstacle.y + obstacle.height
                )
                    .intersect(playerRect)
            }
        }

        return ob.toMutableList()
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

    var cameraOffsetY = 0f

    fun updateCameraOffset(carY: Float) {
        val baseY = h * 0.6f
        val targetOffsetY = max(0f, baseY - carY) * 0.5f
        cameraOffsetY += (targetOffsetY - cameraOffsetY) * 0.1f
    }

    private fun drawGame(cam: Canvas?) {
        // canvas!!.drawColor(androidx.compose.ui.graphics.Color.Black.toArgb())
        canvas = cam

        ceu.draw(canvas!!)



        canvas?.save()
        canvas?.translate(0f, cameraOffsetY)



        canvas?.let { fundo.draw(it) }
        // trackRenderer.draw(canvas)


        canvas?.let { carroRival.draw(it) }

        canvas?.let { carroRival1.draw(it) }

        // canvas?.let { carroRival2.draw(it) }

        canvas?.let { carro.draw(it) }


        obstacles.forEach { it.draw(canvas!!) }
        canvas?.restore()



        if (gameouver) {
            paint.textSize = spToPx((this.w * 0.05f))
            canvas?.drawText("FIM DE JOGO", 100f, 790f, paint)
        } else {

            paint.textSize = spToPx((this.w * 0.01f))
            canvas?.drawText(" ${(verificarPos()).toInt()}", 100f, 290f, paint)
            canvas?.drawText("Km: ${(fundo.distancia / 1000)}", 100f, 390f, paint)
            btmAcel.draw(canvas!!)
            btmOficina.draw(canvas!!)
            btmPulo.draw(canvas!!)
        }


        if (cLocked) {
            surfaceHolder.unlockCanvasAndPost(canvas)
            cLocked = false
        }

    }

    private fun verificarPos(): Int {
        var pos: MutableList<CarroRival> = mutableListOf()

        pos.add(carroRival)
        pos.add(carroRival1)
        //   pos.add(carroRival2)

        pos.sortedBy { it.rodaT.x }

        var posx = pos.filter { it.rodaT.x > carro.rodaT.x }

        var p = 0
        when (posx.size) {
            0 -> p = 1
            1 -> p = 2
            2 -> p = 3
            3 -> p = 4

        }
        return p
    }

    private fun drawGameOver(canvas: Canvas?) {
        TODO("Not yet implemented")
    }

    private fun drawSelecao(canvas: Canvas?) {
        selecao.draw(canvas!!)
        if (cLocked) {
            surfaceHolder.unlockCanvasAndPost(canvas)
            cLocked = false
        }
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
        if (cLocked) {
            surfaceHolder.unlockCanvasAndPost(canvas)
            cLocked = false
        }
    }

    private fun render() {

        if (!cLocked) {
            canvas = this.surfaceHolder.lockCanvas()
            cLocked = true
        }

    }

    private fun handleInput() {

    }

    private fun init() {
        carro.screenHeight = h
        carro.iniciarRodas()

        carroRival.screenHeight = h
        carroRival.iniciarRodas()

        carroRival1.screenHeight = h
        carroRival1.iniciarRodas()


        //  carroRival2.screenHeight = h
        //  carroRival2.iniciarRodas()


        carroRival.f = 1800f
        carroRival.t = 1650f

        carroRival1.f = 2800f
        carroRival1.t = 2650f

        //  carroRival2.f = 3800f
        //  carroRival2.t = 3650f


        carroRival.bitmap = selecao.listaMonters[2]
        carroRival1.bitmap = selecao.listaMonters[3]
        //  carroRival2.bitmap = selecao.listaMonters[4]

        carro.colisao = colisao
        carroRival.colisao = colisao2
        carroRival1.colisao = colisao3
        //   carroRival2.colisao = colisao4
        var nx: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.casasb, options)
        var n = Bitmap.createScaledBitmap(
            nx,
            (w).toInt(),
            (h).toInt(),
            false
        )


        var cx: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ceub, options)
        var c = Bitmap.createScaledBitmap(
            cx,
            (w).toInt(),
            (h).toInt(),
            false
        )
        var cxx: Bitmap =
            BitmapFactory.decodeResource(context.resources, R.drawable.bitmapb, options)
        var cxs = Bitmap.createScaledBitmap(
            cxx,
            (w).toInt(),

            (h).toInt(),
            false
        )
        trackRenderer.loadTrackSegments(((w * 1.5f).toInt()), h)

        ceu.backgroundClouds = n
        //  fundo.backgroundMountains = cxs

        fundo.backgroundMountains = trackRenderer.trackSegments[0]

        fundo.backgroundMountains2 = trackRenderer.trackSegments[1]

        fundo.texturaBitmap = cxs
        ceu.backgroundSky = c
        fundo.mountainsX = 0f

        fundo.mountainsX2 = fundo.mountainsX + (fundo.backgroundMountains.width).toFloat()//+ 1800

        carroRival.verificarPosiçãoPista(fundo)
        carroRival1.verificarPosiçãoPista(fundo)

        addRandomObstacle(fundo, 0f)


        // carroRival2.verificarPosiçãoPista(fundo)
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


    private val activePointers = mutableMapOf<Int, String>()
    fun onTouchEvent(event: MotionEvent): Boolean {


        if (gameState == GameState.PLAYING) {





            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                    val pointerIndex = event.actionIndex
                    val pointerId = event.getPointerId(pointerIndex)
                    val x = event.getX(pointerIndex)
                    val y = event.getY(pointerIndex)

                    if (btmOficina.containsTouch(
                            x,
                            y
                        )
                    ) {
                        selecao.sair = false
                        gameState = GameState.SELECAO

                    }


                    if (btmPulo.containsTouch(
                            x,
                            y
                        )
                    ){
                        activePointers[pointerId] = "left"
                        btmPulo.btm = btmPulo.trocarBtm(pulob)
                    }else if (btmAcel.containsTouch(
                             x,
                             y
                        )
                    ) {
                        activePointers[pointerId] = "right"
                        btmAcel.btm = btmAcel.trocarBtm(acelb)
                    }
                    updateActions()
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_CANCEL -> {
                    val pointerIndex = event.actionIndex
                    val pointerId = event.getPointerId(pointerIndex)
                    activePointers.remove(pointerId)
                    val x = event.getX(pointerIndex)
                    val y = event.getY(pointerIndex)
                    if (gameouver) {
                        gameouver = false
                        fundo.distancia = 0

                        fundo.mountainsX = 0f

                        fundo.mountainsX2 = 0f //+ 1800
                    }
                    if (btmPulo.containsTouch(
                            x,
                            y
                        )
                    ){
                        isJumping = false
                        btmPulo.btm = btmPulo.trocarBtm(pulo)
                    }

                    if (btmAcel.containsTouch(
                            x,
                            y
                        )
                    ){
                        isAccelerating = false
                        btmAcel.btm = btmAcel.trocarBtm(acel)

                        carro.acelerando = true
                        fundo.reduzindo = true
                        carro.reduzindo = true
                        isTouching = false
                    }


                    updateActions()
                }

                MotionEvent.ACTION_MOVE -> {
                    // Verifica todos os dedos ativos
                    for (i in 0 until event.pointerCount) {
                        val pointerId = event.getPointerId(i)
                        val x = event.getX(i)
                        if (x < w / 2f) {
                            activePointers[pointerId] = "left"
                        } else {
                            activePointers[pointerId] = "right"
                        }
                    }
                    updateActions()
                }
            }


        } else {

            when (event.action) {
                MotionEvent.ACTION_DOWN -> handleTouch(event)
            }
        }

        return true
    }

    private fun updateActions() {
        isJumping = activePointers.containsValue("left")
        isAccelerating = activePointers.containsValue("right")
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
            }

            GameState.SELECAO -> {

                if (!selecao.sair) {
                    selecao.onTouchEvent(event)
                    if (selecao.sair) {
                        carro.bitmap = selecao.listaMonters[selecao.index]
                        carro.rodaF.bitmap = selecao.getRoda()
                        carro.rodaT.bitmap = selecao.getRoda()
                        gameState = GameState.PLAYING
                    }

                } else {
                    carro.bitmap = selecao.listaMonters[selecao.index]
                    carro.rodaF.bitmap = selecao.getRoda()
                    carro.rodaT.bitmap = selecao.getRoda()
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
    MENU, PLAYING, GAME_OVER, SHOP, SELECAO
}