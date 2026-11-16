package com.example.audioguiaforeground_compose.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import com.example.audioguiaforeground_compose.MainActivity
import com.example.audioguiaforeground_compose.data.local.AppDatabase
import com.example.audioguiaforeground_compose.data.local.entity.UltimoEdificioEntity
import com.example.audioguiaforeground_compose.data.model.Edificio
import com.example.audioguiaforeground_compose.data.model.EdificiosData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Foreground Service que gestiona la reproducción real de audio con MediaPlayer,
 * emite notificaciones persistentes con acciones interactivas y persiste el último edificio en Room.
 */
class AudioGuiaService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private var mediaSession: MediaSessionCompat? = null
    private var audioFocusRequest: AudioFocusRequest? = null
    private var trackerJob: Job? = null
    private val serviceScope = CoroutineScope(Dispatchers.IO)

    companion object {
        const val ACCION_REPRODUCIR = "com.example.audioguiaforeground_compose.REPRODUCIR"
        const val ACCION_PAUSAR = "com.example.audioguiaforeground_compose.PAUSAR"
        const val ACCION_DETENER = "com.example.audioguiaforeground_compose.DETENER"
        const val ACCION_SIGUIENTE = "com.example.audioguiaforeground_compose.SIGUIENTE"
        const val ACCION_SELECCIONAR = "com.example.audioguiaforeground_compose.SELECCIONAR"
        const val EXTRA_EDIFICIO_ID = "extra_edificio_id"

        private const val ID_CANAL = "canal_audioguia_v2"
        private const val ID_NOTIFICACION = 1

        private val _playbackState = MutableStateFlow(AudioPlaybackState())
        val playbackState: StateFlow<AudioPlaybackState> = _playbackState.asStateFlow()
    }

    override fun onCreate() {
        super.onCreate()
        crearCanalNotificaciones()
        mediaSession = MediaSessionCompat(this, "CampusTourSession").apply {
            isActive = true
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACCION_REPRODUCIR -> {
                val edificioId = intent.getIntExtra(EXTRA_EDIFICIO_ID, _playbackState.value.edificioActual.id)
                iniciarOReanudarAudio(edificioId)
            }
            ACCION_PAUSAR -> {
                pausarAudio()
            }
            ACCION_DETENER -> {
                detenerAudio()
            }
            ACCION_SIGUIENTE -> {
                avanzarSiguienteEdificio()
            }
            ACCION_SELECCIONAR -> {
                val edificioId = intent.getIntExtra(EXTRA_EDIFICIO_ID, 1)
                iniciarAudioNuevo(edificioId)
            }
        }
        return START_NOT_STICKY
    }

    private fun iniciarOReanudarAudio(edificioId: Int) {
        val estadoActual = _playbackState.value
        val edificio = EdificiosData.obtenerPorId(edificioId)

        if (mediaPlayer != null && estadoActual.edificioActual.id == edificioId && estadoActual.estaPausado) {
            solicitarAudioFocus()
            mediaPlayer?.start()
            actualizarEstado(
                servicioActivo = true,
                estaReproduciendo = true,
                estaPausado = false,
                edificio = edificio
            )
            actualizarNotificacion(edificio, estaReproduciendo = true)
            iniciarMonitoreoProgreso()
        } else {
            iniciarAudioNuevo(edificioId)
        }
    }

    private fun iniciarAudioNuevo(edificioId: Int) {
        val edificio = EdificiosData.obtenerPorId(edificioId)

        liberarMediaPlayer()

        try {
            solicitarAudioFocus()

            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()

            val afd = resources.openRawResourceFd(edificio.audioResId)
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(audioAttributes)
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                afd.close()
                prepare()
                setVolume(1.0f, 1.0f)
                isLooping = false
                setOnCompletionListener {
                    actualizarEstado(
                        servicioActivo = true,
                        estaReproduciendo = false,
                        estaPausado = true,
                        edificio = edificio,
                        posicionMs = duration,
                        duracionMs = duration
                    )
                    actualizarNotificacion(edificio, estaReproduciendo = false)
                }
                start()
            }

            val duracion = mediaPlayer?.duration ?: 0
            actualizarEstado(
                servicioActivo = true,
                estaReproduciendo = true,
                estaPausado = false,
                edificio = edificio,
                posicionMs = 0,
                duracionMs = duracion
            )

            // Iniciar como Foreground Service con notificación
            val notificacion = construirNotificacion(edificio, estaReproduciendo = true)
            startForeground(ID_NOTIFICACION, notificacion)

            // Guardar último edificio reproducido en Room (Ejercicio 3)
            persistirUltimoEdificioEnRoom(edificio)

            iniciarMonitoreoProgreso()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun pausarAudio() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
        detenerMonitoreoProgreso()

        val edificio = _playbackState.value.edificioActual
        actualizarEstado(
            servicioActivo = true,
            estaReproduciendo = false,
            estaPausado = true,
            edificio = edificio
        )
        actualizarNotificacion(edificio, estaReproduciendo = false)
    }

    private fun detenerAudio() {
        detenerMonitoreoProgreso()
        liberarMediaPlayer()
        abandonarAudioFocus()

        val edificio = _playbackState.value.edificioActual
        actualizarEstado(
            servicioActivo = false,
            estaReproduciendo = false,
            estaPausado = false,
            edificio = edificio,
            posicionMs = 0,
            duracionMs = 0
        )

        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun avanzarSiguienteEdificio() {
        val siguiente = EdificiosData.obtenerSiguiente(_playbackState.value.edificioActual.id)
        iniciarAudioNuevo(siguiente.id)
    }

    private fun solicitarAudioFocus() {
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()

            val request = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(audioAttributes)
                .setAcceptsDelayedFocusGain(true)
                .setOnAudioFocusChangeListener { focusChange ->
                    when (focusChange) {
                        AudioManager.AUDIOFOCUS_LOSS -> pausarAudio()
                        AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                            if (mediaPlayer?.isPlaying == true) {
                                mediaPlayer?.pause()
                            }
                        }
                        AudioManager.AUDIOFOCUS_GAIN -> {
                            if (_playbackState.value.estaReproduciendo) {
                                mediaPlayer?.start()
                            }
                        }
                    }
                }
                .build()
            audioFocusRequest = request
            audioManager.requestAudioFocus(request)
        } else {
            @Suppress("DEPRECATION")
            audioManager.requestAudioFocus(
                { focusChange ->
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS) pausarAudio()
                },
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
            )
        }
    }

    private fun abandonarAudioFocus() {
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioFocusRequest?.let { audioManager.abandonAudioFocusRequest(it) }
            audioFocusRequest = null
        } else {
            @Suppress("DEPRECATION")
            audioManager.abandonAudioFocus(null)
        }
    }

    private fun persistirUltimoEdificioEnRoom(edificio: Edificio) {
        serviceScope.launch {
            try {
                val db = AppDatabase.getInstance(applicationContext)
                val entidad = UltimoEdificioEntity(
                    id = 1,
                    edificioId = edificio.id,
                    nombreEdificio = edificio.nombre,
                    categoria = edificio.categoria,
                    timestamp = System.currentTimeMillis()
                )
                db.ultimoEdificioDao().guardarUltimoEdificio(entidad)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun iniciarMonitoreoProgreso() {
        trackerJob?.cancel()
        trackerJob = serviceScope.launch {
            while (true) {
                val player = mediaPlayer
                if (player != null && player.isPlaying) {
                    val pos = player.currentPosition
                    val dur = player.duration
                    _playbackState.value = _playbackState.value.copy(
                        posicionMs = pos,
                        duracionMs = dur
                    )
                }
                delay(500)
            }
        }
    }

    private fun detenerMonitoreoProgreso() {
        trackerJob?.cancel()
        trackerJob = null
    }

    private fun liberarMediaPlayer() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }
        mediaPlayer = null
    }

    private fun actualizarEstado(
        servicioActivo: Boolean,
        estaReproduciendo: Boolean,
        estaPausado: Boolean,
        edificio: Edificio,
        posicionMs: Int? = null,
        duracionMs: Int? = null
    ) {
        val current = _playbackState.value
        _playbackState.value = current.copy(
            servicioActivo = servicioActivo,
            estaReproduciendo = estaReproduciendo,
            estaPausado = estaPausado,
            edificioActual = edificio,
            posicionMs = posicionMs ?: current.posicionMs,
            duracionMs = duracionMs ?: current.duracionMs
        )
    }

    private fun crearCanalNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                ID_CANAL,
                "Reproductor Multimedia",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Canal para el reproductor de narraciones de audio"
                setShowBadge(true)
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(canal)
        }
    }

    private fun actualizarNotificacion(edificio: Edificio, estaReproduciendo: Boolean) {
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(ID_NOTIFICACION, construirNotificacion(edificio, estaReproduciendo))
    }

    /**
     * Construye la notificación interactiva del Foreground Service con botones de acción (Ejercicio 2)
     * y el nombre del edificio en el título (Ejercicio 1).
     */
    private fun construirNotificacion(edificio: Edificio, estaReproduciendo: Boolean): Notification {
        val intentPrincipal = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntentPrincipal = PendingIntent.getActivity(
            this,
            0,
            intentPrincipal,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Acción Pausar o Reanudar
        val intentPlayPause = Intent(this, AudioGuiaService::class.java).apply {
            action = if (estaReproduciendo) ACCION_PAUSAR else ACCION_REPRODUCIR
        }
        val pendingIntentPlayPause = PendingIntent.getService(
            this,
            1,
            intentPlayPause,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val accionPlayPauseTexto = if (estaReproduciendo) "Pausar" else "Reanudar"
        val accionPlayPauseIcono = if (estaReproduciendo) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play

        // Acción Siguiente Edificio (Ejercicio 1)
        val intentSiguiente = Intent(this, AudioGuiaService::class.java).apply {
            action = ACCION_SIGUIENTE
        }
        val pendingIntentSiguiente = PendingIntent.getService(
            this,
            2,
            intentSiguiente,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Acción Detener (Ejercicio 2)
        val intentDetener = Intent(this, AudioGuiaService::class.java).apply {
            action = ACCION_DETENER
        }
        val pendingIntentDetener = PendingIntent.getService(
            this,
            3,
            intentDetener,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val estadoTexto = if (estaReproduciendo) "Reproduciendo" else "En pausa"

        return NotificationCompat.Builder(this, ID_CANAL)
            .setContentTitle(edificio.nombre)
            .setContentText("$estadoTexto • ${edificio.categoria}")
            .setSubText("Campus Tour")
            .setSmallIcon(if (estaReproduciendo) android.R.drawable.ic_media_play else android.R.drawable.ic_media_pause)
            .setContentIntent(pendingIntentPrincipal)
            .setOngoing(estaReproduciendo)
            .addAction(accionPlayPauseIcono, accionPlayPauseTexto, pendingIntentPlayPause)
            .addAction(android.R.drawable.ic_media_next, "Siguiente", pendingIntentSiguiente)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Detener", pendingIntentDetener)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession?.sessionToken)
                    .setShowActionsInCompactView(0, 1, 2)
            )
            .build()
    }

    override fun onDestroy() {
        detenerMonitoreoProgreso()
        liberarMediaPlayer()
        abandonarAudioFocus()
        mediaSession?.apply {
            isActive = false
            release()
        }
        mediaSession = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
